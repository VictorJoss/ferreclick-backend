package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.exceptions.files.DeleteImageException;
import com.victordev.ferreclickbackend.exceptions.files.ImageUploadException;
import com.victordev.ferreclickbackend.exceptions.files.InvalidImageException;
import com.victordev.ferreclickbackend.persistence.entity.Image;
import com.victordev.ferreclickbackend.persistence.repository.ImageRepository;
import com.victordev.ferreclickbackend.service.ICloudinaryService;
import com.victordev.ferreclickbackend.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Implementación del servicio de imágenes que proporciona métodos para subir y borrar imágenes.
 */
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    /**
     * Servicio de Cloudinary.
     */
    private final ICloudinaryService cloudinaryService;
    /**
     * Repositorio de imágenes.
     */
    private final ImageRepository imageRepository;

    @Value("${cloudinary.folder}")
    private String CLOUDINARY_FOLDER;

    /**
     * Sube una imagen a Cloudinary.
     * @param file Archivo a subir.
     * @return URL de la imagen subida.
     */
    @Override
    public String uploadImage(MultipartFile file) {

        String contentType = Optional.ofNullable(file.getContentType()).orElse("");
        if (file.isEmpty() || !contentType.startsWith("image/")) {
            throw new InvalidImageException("The file is empty or not an image");
        }

        String url = cloudinaryService.uploadFile(file, CLOUDINARY_FOLDER);
        if (url == null) {
            throw new ImageUploadException("Error creating image URL");
        }

        Image image = new Image();
        image.setUrl(url);
        imageRepository.save(image);

        return url;
    }

    /**
     * Elimina una imagen de Cloudinary.
     * @param imageUrl URL de la imagen a eliminar.
     */
    @Override
    public void deleteImage(String imageUrl) {
        try {
            cloudinaryService.deleteFile(imageUrl, CLOUDINARY_FOLDER);
        } catch (Exception e) {
            throw new DeleteImageException("Error deleting image: " + e.getMessage());
        }
    }
}
