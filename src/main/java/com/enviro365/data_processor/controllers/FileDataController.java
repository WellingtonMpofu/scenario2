package com.enviro365.data_processor.controllers;

import com.enviro365.data_processor.models.FileData;
import com.enviro365.data_processor.services.FileDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class FileDataController {

    private static final Logger logger = LoggerFactory.getLogger(FileDataController.class);

    @Autowired
    private FileDataService fileDataService;

    @PostMapping("/upload")
    @Operation(
        summary = "Upload a file for processing",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "multipart/form-data",
                    schema = @Schema(type = "string", format = "binary")
            )
        )
    )
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("Received file upload request: {}", file.getOriginalFilename());
            FileData fileData = fileDataService.processFile(file);
            logger.info("File processed successfully: {}", fileData.getId());
            return ResponseEntity.ok(fileData);
        } catch (Exception e) {
            logger.error("Error processing file: {}", file.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }

    @GetMapping("/data/{id}")
    @Operation(summary = "Retrieve processed file data", description = "Retrieves the processed data for the file with the specified ID")
    public ResponseEntity<?> getFileData(@PathVariable Long id) {
        try {
            logger.info("Received request to retrieve data for file ID: {}", id);
            FileData fileData = fileDataService.getFileDataById(id);
            logger.info("Data retrieved successfully for file ID: {}", id);
            return ResponseEntity.ok(fileData);
        } catch (Exception e) {
            logger.error("Error retrieving data for file ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found with ID: " + id);
        }
    }
}
