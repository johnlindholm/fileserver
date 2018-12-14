package com.home.fileserver.exception;

public class StorageDataNotFoundException extends RuntimeException {

    private String dataId;

    public StorageDataNotFoundException(String dataId) {
        super();
        this.dataId = dataId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}
