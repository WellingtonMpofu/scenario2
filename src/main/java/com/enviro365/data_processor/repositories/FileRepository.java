package com.enviro365.data_processor.repositories;

import com.enviro365.data_processor.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository <File, Long> {
}
