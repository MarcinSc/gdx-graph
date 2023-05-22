package com.gempukku.libgdx.graph.util.storage;

public interface MultiPartMesh<T, U> {
    U addPart(T part);

    boolean containsPart(U partReference);

    U updatePart(T part, U partReference);

    void removePart(U partReference);

    boolean canStore(T part);

    boolean isEmpty();
}
