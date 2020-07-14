package com.home.fileserver.control;

import com.home.fileserver.domain.Data;
import com.home.fileserver.domain.Image;
import com.home.fileserver.domain.Video;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StorageService {

    Data storeData(MultipartFile multipartFile, Date createdDate);

    Video storeVideo(MultipartFile multipartFile, Date createdDate);

    Image storeImage(MultipartFile multipartFile, Date createdDate);

    Optional<Video> getVideo(String id);

    Optional<Image> getImage(String id);

    Optional<Data> getData(String id);

    Page<Data> getAllData(int page, int pageSize);

    Page<Image> getAllImages(int page, int pageSize);

    Page<Video> getAllVideos(int page, int pageSize);

    List<Data> search(String partOfName);

    List<Image> searchImage(String partOfName);

    List<Video> searchVideo(String partOfName);

    Video updateVideo(String videoId, Video video);

    Image updateImage(String imageId, Image image);

    Data updateData(String dataId, Data data);
}
