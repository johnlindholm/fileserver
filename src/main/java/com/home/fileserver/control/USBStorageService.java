package com.home.fileserver.control;

import com.home.fileserver.exception.USBStorageException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class USBStorageService {

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
    }

    public File store(MultipartFile multipartFile) {
        try {
            File file = new File(storageDirectory, multipartFile.getOriginalFilename());
            while (file.exists()) {
                int i = 1;
                String filename = multipartFile.getOriginalFilename();
                if (filename.indexOf(".") > 0) {
                    filename = filename.substring(0, filename.indexOf(".")) + i +
                            filename.substring(filename.indexOf("."));
                } else {
                    filename = filename + i;
                }
                file = new File(storageDirectory, filename);
            }
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            throw new USBStorageException(e.getMessage(), e);
        }
    }
}
