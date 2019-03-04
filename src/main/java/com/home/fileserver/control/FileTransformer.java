package com.home.fileserver.control;

import com.home.fileserver.domain.Data;
import com.home.fileserver.domain.Image;
import com.home.fileserver.domain.Video;
import com.home.fileserver.exception.InitException;
import com.home.fileserver.exception.StorageFileIOException;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

@Component
public class FileTransformer {

    private final static Logger logger = LoggerFactory.getLogger(FileTransformer.class);
    private TikaConfig mediaTypeDetector;

    @PostConstruct
    public void init() {
        try {
            mediaTypeDetector = new TikaConfig();
        } catch (TikaException | IOException e) {
            throw new InitException("Unable to create TikaConfig", e);
        }
    }

    public MediaType getMediaType(File file, byte[] bytes) throws IOException {
        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
        return mediaTypeDetector.getDetector().detect(new ByteArrayInputStream(bytes), metadata);
    }

    public Data process(File file) {
        byte[] bytes;
        try {
            bytes = IOUtils.toByteArray(new FileInputStream(file));
        } catch (IOException e) {
            throw new StorageFileIOException("Unable to read file bytes", e);
        }
        MediaType mediaType = null;
        try {
            mediaType = getMediaType(file, bytes);
        } catch (IOException e) {
            logger.warn("Unable to parse media type for file: {}", file);
        }
        Data data;
        if (mediaType != null) {
            if (mediaType.getType().equalsIgnoreCase("image")) {
                data = new Image();
            } else if (mediaType.getType().equalsIgnoreCase("video")) {
                data = new Video();
            } else {
                data = new Data();
            }
            data.setMediaType(mediaType.toString());
        } else {
            data = new Data();
        }
        String md5 = DigestUtils.md5DigestAsHex(bytes);
        data.setInternalStoragePath(file.getAbsolutePath());
        data.setMd5(md5);
        data.setName(file.getName());
        data.setCreated(new Date());
        data.setSize(bytes.length);
        return data;
    }
}
