package com.home.fileserver.repository;

import com.home.fileserver.domain.Data;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DataRepository extends Neo4jRepository<Data, String> {
}
