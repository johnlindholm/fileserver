package com.home.fileserver.repository;

import com.home.fileserver.domain.Media;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface MediaRepository<T extends Media> extends Neo4jRepository<T, String> {

}
