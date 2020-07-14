package com.home.fileserver.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "Video")
public class Video extends Media {

    private Long duration;
    private String resolution;
    private String tags;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
