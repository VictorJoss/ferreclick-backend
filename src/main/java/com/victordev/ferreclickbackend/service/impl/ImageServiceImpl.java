package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.persistence.entity.Image;
import com.victordev.ferreclickbackend.persistence.repository.ImageRepository;
import com.victordev.ferreclickbackend.service.ICloudinaryService;
import com.victordev.ferreclickbackend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementación del servicio de imágenes que proporciona métodos para subir y borrar imágenes.
 */
@Service
public class ImageServiceImpl implements ImageService {

    /**
     * Servicio de Cloudinary.
     */
    @Autowired
    private ICloudinaryService cloudinaryService;
    /**
     * Repositorio de imágenes.
     */
    @Autowired
    private ImageRepository imageRepository;

    /**
     * Sube una imagen a Cloudinary.
     * @param file Archivo a subir.
     * @return URL de la imagen subida.
     */
    @Override
    public String uploadImage(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }
            Image image = new Image();
            image.setUrl(cloudinaryService.uploadFile(file, "ferreclick"));
            String url = image.getUrl();
            if(url == null) {
                throw new RuntimeException("Error creating image url");
            }
            imageRepository.save(image);
            return url;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading image");
        }
    }

    /**
     * Elimina una imagen de Cloudinary.
     * @param imageUrl URL de la imagen a eliminar.
     */
    public void deleteImage(String imageUrl){
        try {
            cloudinaryService.deleteFile(imageUrl, "ferreclick");
        } catch (Exception e) {
            throw new RuntimeException("Error deleting image", e);
        }
    }
}
