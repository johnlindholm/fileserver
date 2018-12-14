package com.home.fileserver.control;

import com.home.fileserver.domain.Data;
import com.home.fileserver.domain.Image;
import com.home.fileserver.domain.Video;
import com.home.fileserver.exception.StorageDataNotFoundException;
import com.home.fileserver.exception.StorageImageNotFoundException;
import com.home.fileserver.exception.StorageVideoNotFoundException;
import com.home.fileserver.repository.DataRepository;
import com.home.fileserver.repository.ImageRepository;
import com.home.fileserver.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private FileTransformer fileTransformer;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private USBStorageService usbStorageService;

    private void mergeMetadata(Data data, Data newData) {
        nullSafeSet(s -> data.setMediaType(s), newData.getMediaType());
        nullSafeSet(s -> data.setName(s), newData.getName());
        nullSafeSet(s -> data.setDescription(s), newData.getDescription());
    }

    private void mergeImageMetadata(Image image, Image newImage) {
        mergeMetadata(image, newImage);
        nullSafeSet(s -> image.setTitle(s), newImage.getTitle());
        image.setDateTaken(newImage.getDateTaken());
        if (newImage.getLocation() != null) {
            image.setLocation(newImage.getLocation());
        }
    }

    private void mergeVideoMetadata(Video video, Video newVideo) {
        mergeMetadata(video, newVideo);
        nullSafeSet(s -> video.setResolution(s), newVideo.getResolution());
        video.setDuration(newVideo.getDuration() > 0 ? newVideo.getDuration() : 0);
    }

    private void nullSafeSet(Consumer<String> consumer, String newValue) {
        if (newValue != null) {
            consumer.accept(newValue);
        }
    }

    @Override
    public Data storeData(MultipartFile multipartFile) {
        File file = usbStorageService.store(multipartFile);
        Data data = fileTransformer.process(file);
        data.setName(multipartFile.getOriginalFilename());
        data.setMediaType(multipartFile.getContentType());
        data.setSize((int) multipartFile.getSize());
        data = dataRepository.save(data);
        data.setApiPath("/api/v1/files/" + data.getId());
        data = dataRepository.save(data);
        return data;
    }

    @Override
    public Video storeVideo(MultipartFile multipartFile) {
        File file = usbStorageService.store(multipartFile);
        Video video = (Video) fileTransformer.process(file);
        video.setName(multipartFile.getOriginalFilename());
        video.setMediaType(multipartFile.getContentType());
        video.setSize((int) multipartFile.getSize());
        video = videoRepository.save(video);
        video.setApiPath("/api/v1/files/videos/" + video.getId());
        video = videoRepository.save(video);
        return video;
    }

    @Override
    public Image storeImage(MultipartFile multipartFile) {
        File file = usbStorageService.store(multipartFile);
        Image image = (Image) fileTransformer.process(file);
        image.setName(multipartFile.getOriginalFilename());
        image.setMediaType(multipartFile.getContentType());
        image.setSize((int) multipartFile.getSize());
        image = imageRepository.save(image);
        image.setApiPath("/api/v1/files/images/" + image.getId());
        image = imageRepository.save(image);
        return image;
    }

    @Override
    public Optional<Video> getVideo(String id) {
        return videoRepository.findById(id);
    }

    @Override
    public Optional<Image> getImage(String id) {
        return imageRepository.findById(id);
    }

    @Override
    public Optional<Data> getData(String id) {
        return dataRepository.findById(id);
    }

    @Override
    public Page<Data> getAllData(int page, int pageSize) {
        return dataRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Page<Image> getAllImages(int page, int pageSize) {
        return imageRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Page<Video> getAllVideos(int page, int pageSize) {
        return videoRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public List<Data> search(String partOfName) {
        return null;
    }

    @Override
    public List<Image> searchImage(String partOfName) {
        return null;
    }

    @Override
    public List<Video> searchVideo(String partOfName) {
        return null;
    }

    @Override
    public Video updateVideo(String videoId, Video updatedVideo) {
        Video video = getVideo(videoId).orElseThrow(() -> new StorageVideoNotFoundException(videoId));
        mergeVideoMetadata(video, updatedVideo);
        videoRepository.save(video);
        return video;
    }

    @Override
    public Image updateImage(String imageId, Image updatedImage) {
        Image image = getImage(imageId).orElseThrow(() -> new StorageImageNotFoundException(imageId));
        mergeImageMetadata(image, updatedImage);
        imageRepository.save(image);
        return image;
    }

    @Override
    public Data updateData(String dataId, Data updatedData) {
        Data data = getData(dataId).orElseThrow(() -> new StorageDataNotFoundException(dataId));
        mergeMetadata(data, updatedData);
        dataRepository.save(data);
        return data;
    }
}
