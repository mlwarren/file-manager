package com.mlwarren.web.filemanager.repository;

import com.mlwarren.web.filemanager.entity.ManagedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagedFileRepository extends JpaRepository<ManagedFile, Integer> {
}
