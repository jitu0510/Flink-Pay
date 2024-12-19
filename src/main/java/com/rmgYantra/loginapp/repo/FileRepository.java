package com.rmgYantra.loginapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rmgYantra.loginapp.model.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}

