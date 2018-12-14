package com.home.fileserver.exception;

public class StorageVideoNotFoundException extends RuntimeException {

    private String videoId;

    public StorageVideoNotFoundException(String videoId) {
        super();
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
