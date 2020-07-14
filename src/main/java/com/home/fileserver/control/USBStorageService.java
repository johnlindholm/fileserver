package com.home.fileserver.control;

import com.home.fileserver.exception.USBStorageException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import javax.annotation.PostConstruct;

@Service
public class USBStorageService {

    private static Logger logger = LoggerFactory.getLogger(USBStorageService.class);

    @Value("${storeage.usb.mountpoint}")
    private File usbMountpoint;

    @Value("${storage.usb.root}")
    private String storageDirectoryRootPath;

    @Value("${storage.directory.fallback}")
    private String storageDirectoryPathFallback;

    private File storageDirectory;

    @PostConstruct
    public void init() {
        if (usbMountpoint == null || !usbMountpoint.exists()) {
            //USB not connected
            storageDirectory = new File(storageDirectoryPathFallback);
            try {
                FileUtils.forceMkdir(storageDirectory);
            } catch (IOException e) {
                throw new USBStorageException(e.getMessage(), e);
            }
        } else {
            storageDirectory = new File(usbMountpoint, storageDirectoryRootPath);
            try {
                FileUtils.forceMkdir(storageDirectory);
            } catch (IOException e) {
                throw new USBStorageException(e.getMessage(), e);
            }
        }
        logger.debug("Storage directory is: {}", storageDirectory.getAbsolutePath());
    }

    public File store(MultipartFile multipartFile, Date createdDate) {
        try {
            File file = new File(storageDirectory, multipartFile.getOriginalFilename());
            if (file.exists()) {
                logger.debug("File already exists: " + file.getAbsolutePath());
            } else {
                multipartFile.transferTo(file);
                Files.setLastModifiedTime(file.toPath(), FileTime.fromMillis(createdDate.getTime()));
                logger.debug("Storing file: {}", file.getAbsolutePath());
            }
            return file;
        } catch (IOException e) {
            throw new USBStorageException(e.getMessage(), e);
        }
    }
}
