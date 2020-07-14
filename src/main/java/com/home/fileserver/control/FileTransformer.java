package com.home.fileserver.control;

import com.home.fileserver.domain.Data;
import com.home.fileserver.domain.Image;
import com.home.fileserver.domain.Video;
import com.home.fileserver.exception.InitException;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import javax.annotation.PostConstruct;

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

    public Data process(File file) {
        MediaType mediaType = null;
        try {
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
            mediaType = mediaTypeDetector.getDetector().detect(is, metadata);
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
        String md5 = null;
        try {
            md5 = DigestUtils.md5DigestAsHex(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        data.setInternalStoragePath(file.getAbsolutePath());
        data.setMd5(md5);
        data.setName(file.getName());
        data.setCreated(new Date());
        data.setSize(file.length());
        return data;
    }
}
