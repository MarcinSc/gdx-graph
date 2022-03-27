package com.gempukku.libgdx.graph.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.component.AnchorComponent;
import com.gempukku.libgdx.graph.component.PhysicsComponent;
import com.gempukku.libgdx.graph.component.PositionComponent;
import com.gempukku.libgdx.graph.component.SizeComponent;
import com.gempukku.libgdx.graph.entity.SensorData;
import com.gempukku.libgdx.graph.entity.def.SensorDef;
import com.gempukku.libgdx.graph.system.sensor.SensorContactListener;

public class PhysicsSystem extends EntitySystem implements Disposable, EntityListener {
    public static final float PIXELS_TO_METERS = 100f;

    public static final short SENSOR = 0x1;
    public static final short CHARACTER = 0x1 << 1;
    public static final short ENVIRONMENT = 0x1 << 2;
    public static final short INTERACTIVE = 0x1 << 3;

    private final World world;
    private final ObjectMap<String, SensorContactListener> sensorContactListeners = new ObjectMap<>();
    private final ObjectMap<String, Short> categoryBits = new ObjectMap<>();
    private ImmutableArray<Entity> physicsEntities;

    public PhysicsSystem(int priority, float gravity) {
        super(priority);
        world = new World(new Vector2(0, gravity), true);
        world.setContactListener(
                new ContactListener() {
                    @Override
                    public void beginContact(Contact contact) {
                        contactBegan(contact);
                    }

                    @Override
                    public void endContact(Contact contact) {
                        contactEnded(contact);
                    }

                    @Override
                    public void preSolve(Contact contact, Manifold oldManifold) {

                    }

                    @Override
                    public void postSolve(Contact contact, ContactImpulse impulse) {

                    }
                }
        );
        categoryBits.put("Sensor", SENSOR);
        categoryBits.put("Character", CHARACTER);
        categoryBits.put("Environment", ENVIRONMENT);
        categoryBits.put("Interactive", INTERACTIVE);
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.all(PhysicsComponent.class).get();
        engine.addEntityListener(family, this);
        physicsEntities = engine.getEntitiesFor(family);
    }

    @Override
    public void entityAdded(Entity entity) {
        PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
        String bodyType = physicsComponent.getType();
        Body body = null;
        if (bodyType.equals("dynamic")) {
            body = createDynamicBody(entity, physicsComponent.getColliderAnchor(), physicsComponent.getColliderScale(),
                    physicsComponent.getCategory(), physicsComponent.getMask());
        } else if (bodyType.equals("static")) {
            body = createStaticBody(entity, physicsComponent.getColliderAnchor(), physicsComponent.getColliderScale(),
                    physicsComponent.getCategory(), physicsComponent.getMask());
        }

        SensorDef[] sensors = physicsComponent.getSensors();
        if (sensors != null) {
            for (SensorDef sensor : sensors) {
                physicsComponent.addSensor(createSensor(entity, body, sensor.getType(), sensor.getAnchor(), sensor.getScale(), sensor.getMask()));
            }
        }

        physicsComponent.setBody(body);
    }

    @Override
    public void entityRemoved(Entity entity) {
        PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
        world.destroyBody(physicsComponent.getBody());
    }

    public void addSensorContactListener(String type, SensorContactListener sensorContactListener) {
        sensorContactListeners.put(type, sensorContactListener);
    }

    public void removeSensorContactListener(String type) {
        sensorContactListeners.remove(type);
    }

    public World getWorld() {
        return world;
    }

    private Body createDynamicBody(Entity entity, Vector2 colliderAnchor, Vector2 colliderScale,
                                   String[] category, String[] mask) {
        PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        SizeComponent sizeComponent = entity.getComponent(SizeComponent.class);
        AnchorComponent anchorComponent = entity.getComponent(AnchorComponent.class);
        Vector3 position = positionComponent.getPosition(new Vector3());
        Vector2 size = sizeComponent.getSize(new Vector2());
        Vector2 anchor = anchorComponent.getAnchor(new Vector2());

        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;
        bodyDef.position.set(position.x, position.y).scl(1 / PIXELS_TO_METERS, 1 / PIXELS_TO_METERS);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x * colliderScale.x / 2 / PIXELS_TO_METERS, size.y * colliderScale.y / 2 / PIXELS_TO_METERS, new Vector2(size.x * (anchor.x - colliderAnchor.x) / PIXELS_TO_METERS, size.y * (anchor.y - colliderAnchor.y) / PIXELS_TO_METERS), 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.filter.categoryBits = getBits(category);
        fixtureDef.filter.maskBits = getBits(mask);

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(entity);

        shape.dispose();

        return body;
    }

    private Body createStaticBody(Entity entity, Vector2 colliderAnchor, Vector2 colliderScale,
                                  String[] category, String[] mask) {
        PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        SizeComponent sizeComponent = entity.getComponent(SizeComponent.class);
        AnchorComponent anchorComponent = entity.getComponent(AnchorComponent.class);
        Vector3 position = positionComponent.getPosition(new Vector3());
        Vector2 size = sizeComponent.getSize(new Vector2());
        Vector2 anchor = anchorComponent.getAnchor(new Vector2());

        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x, position.y).scl(1 / PIXELS_TO_METERS, 1 / PIXELS_TO_METERS);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x * colliderScale.x / 2 / PIXELS_TO_METERS, size.y * colliderScale.y / 2 / PIXELS_TO_METERS, new Vector2(size.x * (anchor.x - colliderAnchor.x) / PIXELS_TO_METERS, size.y * (anchor.y - colliderAnchor.y) / PIXELS_TO_METERS), 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = getBits(category);
        fixtureDef.filter.maskBits = getBits(mask);

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(entity);

        shape.dispose();

        return body;
    }

    private short getBits(String... categories) {
        short result = 0;
        if (categories != null) {
            for (String category : categories) {
                result |= categoryBits.get(category);
            }
        }
        return result;
    }

    @Override
    public void update(float delta) {
        world.step(delta, 6, 2);

        for (Entity physicsEntity : physicsEntities) {
            PhysicsComponent physicsComponent = physicsEntity.getComponent(PhysicsComponent.class);
            Body body = physicsComponent.getBody();
            if (body.getType() == BodyDef.BodyType.DynamicBody) {
                PositionComponent position = physicsEntity.getComponent(PositionComponent.class);
                position.setPosition(body.getPosition().x * PhysicsSystem.PIXELS_TO_METERS, body.getPosition().y * PhysicsSystem.PIXELS_TO_METERS, position.getZ());
            }
        }
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    private SensorData createSensor(Entity entity, Body body, String type, Vector2 sensorAnchor, Vector2 sensorScale, String[] mask) {
        SizeComponent sizeComponent = entity.getComponent(SizeComponent.class);
        AnchorComponent anchorComponent = entity.getComponent(AnchorComponent.class);
        Vector2 size = sizeComponent.getSize(new Vector2());
        Vector2 anchor = anchorComponent.getAnchor(new Vector2());

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x * sensorScale.x / 2 / PIXELS_TO_METERS, size.y * sensorScale.y / 2 / PIXELS_TO_METERS, new Vector2(size.x * (anchor.x - sensorAnchor.x) / PIXELS_TO_METERS, size.y * (anchor.y - sensorAnchor.y) / PIXELS_TO_METERS), 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = SENSOR;
        fixtureDef.filter.maskBits = getBits(mask);

        Fixture fixture = body.createFixture(fixtureDef);
        SensorData sensorData = new SensorData(type);
        fixture.setUserData(sensorData);

        return sensorData;
    }

    private void contactBegan(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        processContactBegun(fixtureB, fixtureA);
        processContactBegun(fixtureA, fixtureB);
    }

    private void processContactBegun(Fixture fixture1, Fixture fixture2) {
        if (fixture2.getUserData() != null && fixture2.isSensor()) {
            SensorData sensorData = (SensorData) fixture2.getUserData();
            String type = sensorData.getType();
            SensorContactListener sensorContactListener = sensorContactListeners.get(type);
            if (sensorContactListener != null) {
                sensorContactListener.contactBegun(sensorData, fixture1);
            }
        }
    }

    private void contactEnded(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        processContactEnded(fixtureB, fixtureA);
        processContactEnded(fixtureA, fixtureB);
    }

    private void processContactEnded(Fixture fixture1, Fixture fixture2) {
        if (fixture2.getUserData() != null && fixture2.isSensor()) {
            SensorData sensorData = (SensorData) fixture2.getUserData();
            String type = sensorData.getType();
            SensorContactListener sensorContactListener = sensorContactListeners.get(type);
            if (sensorContactListener != null) {
                sensorContactListener.contactEnded(sensorData, fixture1);
            }
        }
    }
}
