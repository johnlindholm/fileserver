package com.home.fileserver.exception;

public class StorageImageNotFoundException extends RuntimeException {

    private String imageId;

    public StorageImageNotFoundException(String imageId) {
        super();
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
