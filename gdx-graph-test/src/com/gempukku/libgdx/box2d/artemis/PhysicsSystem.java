package com.gempukku.libgdx.box2d.artemis;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorContactListener;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorData;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorDef;
import com.gempukku.libgdx.box2d.artemis.shape.FixtureShapeHandler;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;

import static com.badlogic.gdx.math.Matrix4.M03;
import static com.badlogic.gdx.math.Matrix4.M13;

public class PhysicsSystem extends EntitySystem {
    private TransformSystem transformSystem;

    private World box2DWorld;
    private ObjectMap<String, SensorContactListener> sensorContactListeners = new ObjectMap<>();
    private ObjectMap<String, FixtureShapeHandler> shapeHandlers = new ObjectMap<>();
    private ObjectMap<String, Short> categoryBits = new ObjectMap<>();
    private Vector2 gravity;
    private float pixelsToMeters;

    private Matrix4 tmpMatrix4 = new Matrix4();
    private Vector3 tmpVector3 = new Vector3();

    public PhysicsSystem(Vector2 gravity, float pixelsToMeters) {
        super(Aspect.all(PhysicsComponent.class));
        this.gravity = gravity;
        this.pixelsToMeters = pixelsToMeters;
    }

    public void addCategory(String category, short value) {
        categoryBits.put(category, value);
    }

    public void addShapeHandler(String type, FixtureShapeHandler fixtureShapeHandler) {
        shapeHandlers.put(type, fixtureShapeHandler);
    }

    public void addSensorContactListener(String type, SensorContactListener sensorContactListener) {
        sensorContactListeners.put(type, sensorContactListener);
    }

    public void removeSensorContactListener(String type) {
        sensorContactListeners.remove(type);
    }

    @Override
    protected void initialize() {
        box2DWorld = new World(gravity, true);
        box2DWorld.setContactListener(
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
                });
    }

    @Override
    public void inserted(Entity entity) {
        PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
        Matrix4 resolvedTransform = transformSystem.getResolvedTransform(entity);

        Body body = createBody(entity, physicsComponent, resolvedTransform);

        SensorDef[] sensors = physicsComponent.getSensors();
        if (sensors != null) {
            for (SensorDef sensor : sensors) {
                physicsComponent.addSensor(createSensor(entity, body, sensor.getType(), sensor.getCategory(),
                        resolvedTransform,
                        sensor.getShape(), sensor.getShapeData(), sensor.getMask()));
            }
        }

        physicsComponent.setBody(body);
    }

    @Override
    public void removed(Entity entity) {
        PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
        box2DWorld.destroyBody(physicsComponent.getBody());
    }

    public World getBox2DWorld() {
        return box2DWorld;
    }

    private Body createBody(Entity entity, PhysicsComponent physicsComponent, Matrix4 transform) {
        Vector3 translation = transform.getTranslation(tmpVector3);

        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = physicsComponent.isFixedRotation();
        bodyDef.type = getBox2DBodyType(physicsComponent.getType());
        bodyDef.bullet = physicsComponent.isBullet();
        bodyDef.position.set(translation.x, translation.y).scl(1 / pixelsToMeters, 1 / pixelsToMeters);

        Body body = box2DWorld.createBody(bodyDef);

        FixtureShapeHandler fixtureShapeHandler = shapeHandlers.get(physicsComponent.getShape());
        Shape shape = fixtureShapeHandler.createShape(entity, physicsComponent.getShapeData(), transform, pixelsToMeters);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.filter.categoryBits = getBits(physicsComponent.getCategory());
        fixtureDef.filter.maskBits = getBits(physicsComponent.getMask());

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(entity);

        shape.dispose();

        return body;
    }

    private BodyDef.BodyType getBox2DBodyType(PhysicsComponent.BodyType type) {
        if (type == PhysicsComponent.BodyType.Dynamic)
            return BodyDef.BodyType.DynamicBody;
        else if (type == PhysicsComponent.BodyType.Static)
            return BodyDef.BodyType.StaticBody;
        else
            return BodyDef.BodyType.KinematicBody;
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
    protected void processSystem() {
        box2DWorld.step(world.getDelta(), 6, 2);

        Bag<Entity> physicsEntities = getEntities();
        for (int i = 0, size = physicsEntities.size(); i < size; i++) {
            Entity physicsEntity = physicsEntities.get(i);
            PhysicsComponent physicsComponent = physicsEntity.getComponent(PhysicsComponent.class);
            Body body = physicsComponent.getBody();
            if (body.getType() == BodyDef.BodyType.DynamicBody) {
                tmpMatrix4.set(transformSystem.getResolvedTransform(physicsEntity));
                Vector2 bodyPosition = body.getPosition();
                float positionX = bodyPosition.x * pixelsToMeters;
                float positionY = bodyPosition.y * pixelsToMeters;
                if (tmpMatrix4.val[M03] != positionX || tmpMatrix4.val[M13] != positionY) {
                    tmpMatrix4.val[M03] = positionX;
                    tmpMatrix4.val[M13] = positionY;
                    transformSystem.setEffectiveTransform(physicsEntity, tmpMatrix4);
                }
            }
        }
    }

    @Override
    public void dispose() {
        box2DWorld.dispose();
    }

    private SensorData createSensor(Entity entity, Body body, String type, String category,
                                    Matrix4 resolvedTransform,
                                    String shapeType, ObjectMap<String, String> shapeData, String[] mask) {
        FixtureShapeHandler fixtureShapeHandler = shapeHandlers.get(shapeType);
        Shape shape = fixtureShapeHandler.createShape(entity, shapeData, resolvedTransform, pixelsToMeters);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = getBits(category);
        fixtureDef.filter.maskBits = getBits(mask);

        Fixture fixture = body.createFixture(fixtureDef);
        SensorData sensorData = new SensorData(type);
        fixture.setUserData(sensorData);

        shape.dispose();

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
