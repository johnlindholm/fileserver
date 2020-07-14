package com.home.fileserver.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.home.fileserver.control.EntityIdStrategy;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import java.util.Date;

@NodeEntity(label = "File")
public class Data {

    @Id
    @GeneratedValue(strategy = EntityIdStrategy.class)
    private String id;

    private String internalStoragePath;

    private String apiPath;

    private String name;

    private String description;

    private Long size;

    private String mediaType;

    @DateLong
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date created;

    private String md5;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getInternalStoragePath() {
        return internalStoragePath;
    }

    public void setInternalStoragePath(String internalStoragePath) {
        this.internalStoragePath = internalStoragePath;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }
}
