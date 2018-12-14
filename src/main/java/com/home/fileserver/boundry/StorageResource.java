package com.home.fileserver.boundry;

import com.home.fileserver.control.StorageService;
import com.home.fileserver.domain.Data;
import com.home.fileserver.domain.Image;
import com.home.fileserver.domain.Video;
import com.home.fileserver.exception.AuthorizationException;
import com.home.fileserver.exception.StorageDataNotFoundException;
import com.home.fileserver.exception.StorageImageNotFoundException;
import com.home.fileserver.exception.StorageVideoNotFoundException;
import com.home.fileserver.response.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@RestController
public class StorageResource {

    @Autowired
    private StorageService storageService;

    @Value("#{'${client.ids}'.split(',')}")
    private List<String> clientIds;

    @PostMapping("/api/v1/files")
    public ResponseEntity uploadFile(@RequestHeader("ClientID") String clientId,
                                     @RequestParam("file") MultipartFile file) {
        validateClientId(clientId);
        MediaType mediaType = MediaType.parseMediaType(file.getContentType());
        Data data;
        if (isImage(mediaType)) {
            data = storageService.storeImage(file);
        } else if (isVideo(mediaType)) {
            data = storageService.storeVideo(file);
        } else {
            data = storageService.storeData(file);
        }
        URI link = null;
        try {
            //TODO link should be complete
            link = new URL("http://localhost:8080" + data.getApiPath()).toURI();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return ResponseEntity.created(link).build();
    }

    @PatchMapping("/api/v1/files/data/{dataId}")
    public ResponseEntity<Data> patchDataMetadata(@RequestHeader("ClientID") String clientId, @PathVariable String dataId,
                                                  @RequestBody Data data) {
        validateClientId(clientId);
        data = storageService.updateData(dataId, data);
        return ResponseEntity.ok(data);
    }

    @PatchMapping("/api/v1/files/images/{imageId}")
    public ResponseEntity<Image> uploadImageMetadata(@RequestHeader("ClientID") String clientId,
                                                     @RequestBody Image image, @PathVariable String imageId) {
        validateClientId(clientId);
        image = storageService.updateImage(imageId, image);
        return ResponseEntity.ok(image);
    }

    @PatchMapping("/api/v1/files/videos/{videoId}")
    public ResponseEntity<Video> uploadVideoMetadata(@RequestHeader("ClientID") String clientId,
                                                     @RequestBody Video video,
                                                     @PathVariable String videoId) {
        validateClientId(clientId);
        video = storageService.updateVideo(videoId, video);
        return ResponseEntity.ok(video);
    }

    @GetMapping("/api/v1/files/data")
    public ResponseEntity<PageResponse<Data>> getAllData(@RequestHeader("ClientID") String clientId,
                                                         @RequestParam(required = false) Integer limit,
                                                         @RequestParam(required = false) Integer pageNumber) {
        validateClientId(clientId);
        Page<Data> page = storageService.getAllData(pageNumber != null ? pageNumber : 0, limit != null ? limit : 10);
        return ResponseEntity.ok(new PageResponse<>(page, "/api/v1/files/data"));
    }

    @GetMapping("/api/v1/files/images")
    public ResponseEntity<PageResponse<Image>> getAllImages(@RequestHeader("ClientID") String clientId,
                                                         @RequestParam(required = false) Integer limit,
                                                         @RequestParam(required = false) Integer pageNumber) {
        validateClientId(clientId);
        Page<Image> page = storageService.getAllImages(pageNumber != null ? pageNumber : 0, limit != null ? limit : 10);
        return ResponseEntity.ok(new PageResponse<>(page, "/api/v1/files/images"));
    }

    @GetMapping("/api/v1/files/videos")
    public ResponseEntity<PageResponse<Video>> getAllVideos(@RequestHeader("ClientID") String clientId,
                                                         @RequestParam(required = false) Integer limit,
                                                         @RequestParam(required = false) Integer pageNumber) {
        validateClientId(clientId);
        Page<Video> page = storageService.getAllVideos(pageNumber != null ? pageNumber : 0, limit != null ? limit : 10);
        return ResponseEntity.ok(new PageResponse<>(page, "/api/v1/files/videos"));
    }

    @GetMapping("/api/v1/files/data/{dataId}")
    public ResponseEntity<Data> getData(@RequestHeader("ClientID") String clientId, @PathVariable String dataId) {
        validateClientId(clientId);
        Data data = storageService.getData(dataId).orElseThrow(() -> new StorageDataNotFoundException(dataId));
        return ResponseEntity.ok(data);
    }

    @GetMapping("/api/v1/files/images/{imageId}")
    public ResponseEntity<Image> getImage(@RequestHeader("ClientID") String clientId, @PathVariable String imageId) {
        validateClientId(clientId);
        Image image = storageService.getImage(imageId).orElseThrow(() -> new StorageImageNotFoundException(imageId));
        return ResponseEntity.ok(image);
    }

    @GetMapping("/api/v1/files/video/{videoId}")
    public ResponseEntity<Video> getVideo(@RequestHeader("ClientID") String clientId, @PathVariable String videoId) {
        validateClientId(clientId);
        Video video = storageService.getVideo(videoId).orElseThrow(() -> new StorageVideoNotFoundException(videoId));
        return ResponseEntity.ok(video);
    }

    @GetMapping("/api/v1/files/info")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("info response");
    }

    private void validateClientId(String clientId) {
        if (!clientIds.contains(clientId)) {
            throw new AuthorizationException(clientId);
        }
    }

    private static boolean isImage(MediaType mediaType) {
        return mediaType.getType().equalsIgnoreCase("image");
    }

    private static boolean isVideo(MediaType mediaType) {
        return mediaType.getType().equalsIgnoreCase("video");
    }
}
