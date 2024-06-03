package com.enviro365.data_processor.repositories;

import com.enviro365.data_processor.models.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDataRepository extends JpaRepository <FileData, Long> {
}
