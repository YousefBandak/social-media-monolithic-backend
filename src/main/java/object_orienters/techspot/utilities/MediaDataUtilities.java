package object_orienters.techspot.utilities;

import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.postTypes.DataTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Service
public class MediaDataUtilities {

    FileStorageService fileStorageService;
    DataTypeRepository dataTypeRepository;

    @Autowired
    public MediaDataUtilities(FileStorageService fileStorageService, DataTypeRepository dataTypeRepository) {
        this.fileStorageService = fileStorageService;
        this.dataTypeRepository = dataTypeRepository;
    }

    public DataType handleAddFile(MultipartFile file) {
        DataType pic = new DataType();
        String fileName = fileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/media_uploads/")
                .path(fileName)
                .toUriString();
        pic.setType(file.getContentType());
        pic.setFileName(fileName);
        pic.setFileUrl(fileDownloadUri);
        return pic;
    }

    public void handleAddMediaData(List<MultipartFile> files, List<DataType> allMedia) {
        files.stream().forEach((file) -> {
            DataType media = new DataType();
            String fileName = fileStorageService.storeFile(file);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/media_uploads/")
                    .path(fileName)
                    .toUriString();
            media.setType(file.getContentType());
            media.setFileName(fileName);
            media.setFileUrl(fileDownloadUri);
            allMedia.add(media);
        });
    }

    public void handleDeleteMediaData(ReactableContent post) {
        post.getMediaData().stream().forEach(media -> {
            fileStorageService.deleteFile(media.getFileName());
            dataTypeRepository.delete(media);
        });

    }
}
