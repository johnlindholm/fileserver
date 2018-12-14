package com.home.fileserver.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "Image")
public class Image extends Media {
}
