package com.example.Media_Service.controller;

import com.example.Media_Service.model.FileType;
import com.example.Media_Service.model.Photo;
import com.example.Media_Service.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class MediaController {

    @Autowired
    private MediaService mediaService;

    // ===================== UI =====================

    @GetMapping("/")
    public String home() {
        return "redirect:/photos";
    }

    @GetMapping("/photos")
    public String listPhotos(Model model) {
        model.addAttribute("photos", mediaService.getAllPhotos());
        return "photos";
    }

    @GetMapping("/photos/upload")
    public String uploadForm() {
        return "upload";
    }

    @PostMapping("/photos/upload")
    public String uploadPhoto(@RequestParam String workOrderId,
                              @RequestParam String uploaderId,
                              @RequestParam String fileName,
                              @RequestParam String fileUrl,
                              @RequestParam String fileType,
                              @RequestParam Long fileSize,
                              @RequestParam(required = false) String description,
                              Model model) {
        try {
            mediaService.uploadPhoto(workOrderId, uploaderId, fileName, fileUrl,
                    FileType.valueOf(fileType.toUpperCase()), fileSize, description);
            return "redirect:/photos?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "upload";
        }
    }

    @GetMapping("/photos/{id}")
    public String photoDetail(@PathVariable String id, Model model) {
        model.addAttribute("photo", mediaService.getPhotoById(id));
        return "detail";
    }

    @PostMapping("/photos/{id}/remove")
    public String removePhoto(@PathVariable String id) {
        mediaService.removePhoto(id);
        return "redirect:/photos";
    }

    @PostMapping("/photos/{id}/delete")
    public String deletePhoto(@PathVariable String id) {
        mediaService.deletePhoto(id);
        return "redirect:/photos";
    }

    // ===================== REST API =====================

    @GetMapping("/api/photos")
    @ResponseBody
    public List<Map<String, Object>> getAllPhotos() {
        return mediaService.getAllPhotos().stream().map(this::toDto).toList();
    }

    @GetMapping("/api/photos/{id}")
    @ResponseBody
    public Map<String, Object> getPhotoById(@PathVariable String id) {
        return toDto(mediaService.getPhotoById(id));
    }

    @GetMapping("/api/photos/workorder/{workOrderId}")
    @ResponseBody
    public List<Map<String, Object>> getByWorkOrder(@PathVariable String workOrderId) {
        return mediaService.getPhotosByWorkOrder(workOrderId).stream()
                .map(this::toDto).toList();
    }

    @PostMapping("/api/photos")
    @ResponseBody
    public Map<String, Object> createPhoto(@RequestBody Map<String, Object> body) {
        Photo photo = mediaService.uploadPhoto(
                (String) body.get("workOrderId"),
                (String) body.get("uploaderId"),
                (String) body.get("fileName"),
                (String) body.get("fileUrl"),
                FileType.valueOf(((String) body.get("fileType")).toUpperCase()),
                ((Number) body.get("fileSize")).longValue(),
                (String) body.getOrDefault("description", "")
        );
        return toDto(photo);
    }

    private Map<String, Object> toDto(Photo p) {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("photoId", p.getPhotoId());
        map.put("workOrderId", p.getWorkOrderId());
        map.put("uploaderId", p.getUploaderId());
        map.put("fileName", p.getFileName());
        map.put("fileUrl", p.getFileUrl());
        map.put("fileType", p.getFileType() != null ? p.getFileType().name() : null);
        map.put("fileSize", p.getFileSize());
        map.put("active", p.isActive());
        return map;
    }
}
