package com.home.fileserver.exception;

public class StorageMetadataNotFoundException extends RuntimeException {

    private String metadataId;

    public StorageMetadataNotFoundException(String metadataId) {
        super();
        this.metadataId = metadataId;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }
}
