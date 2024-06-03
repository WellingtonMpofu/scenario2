package com.enviro365.data_processor.services;

import com.enviro365.data_processor.exceptions.FileProcessingException;
import com.enviro365.data_processor.models.File;
import com.enviro365.data_processor.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    private FileRepository fileDataRepository;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    public File processFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileProcessingException("File is empty");
        }

        if (!file.getContentType().equals("text/plain")) {
            throw new FileProcessingException("Invalid file format. Only text files are allowed.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileProcessingException("File size exceeds the maximum allowed limit of 5 MB.");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String content = br.lines().collect(Collectors.joining("\n"));
            content = content.replace("\n", ". ");
            File fileData = new File();
            fileData.setFileName(file.getOriginalFilename());
            fileData.setProcessedData(content);
            return fileDataRepository.save(fileData);
        } catch (IOException e) {
            throw new FileProcessingException("Error reading file: " + e.getMessage());
        }
    }

    public File getFileDataById(Long id) {
        return fileDataRepository.findById(id).orElse(null);
    }
}
