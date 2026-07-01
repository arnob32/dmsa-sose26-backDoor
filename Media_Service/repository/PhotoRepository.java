package com.example.Media_Service.repository;

import com.example.Media_Service.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {

    List<Photo> findByWorkOrderId(String workOrderId);

    List<Photo> findByUploaderId(String uploaderId);

    List<Photo> findByActive(boolean active);
}
