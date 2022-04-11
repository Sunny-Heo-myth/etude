package com.chatboard.etude.service.file;

import com.chatboard.etude.exception.FileUploadFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
// Save uploaded files in local.
public class LocalFileService implements FileService{

    @Value("${upload.image.location}")  // /Users/sunny/IdeaProjects/etude/fileStorage
    private String location;

    @PostConstruct
    void postConstruct() {
        File directory = new File(location);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    @Override
    public void upload(MultipartFile file, String filename) {
        try {
            // Filename will always be unique.
            file.transferTo(new File(location + filename));
        }
        catch (IOException e) {
            throw new FileUploadFailureException(e);
        }
    }

    @Override
    public void delete(String filename) {
        new File(location + filename).delete();
    }
}
