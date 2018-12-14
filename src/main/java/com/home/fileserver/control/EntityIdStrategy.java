package com.home.fileserver.control;

import org.neo4j.ogm.id.IdStrategy;

import java.util.UUID;

public class EntityIdStrategy implements IdStrategy {

    @Override
    public Object generateId(Object entity) {
        return UUID.randomUUID().toString();
    }
}
