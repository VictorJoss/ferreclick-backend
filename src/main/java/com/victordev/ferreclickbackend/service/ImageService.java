package com.victordev.ferreclickbackend.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interfaz del servicio de imágenes que proporciona métodos para subir y borrar imágenes.
 */
public interface ImageService {

    /**
     * Sube una imagen.
     * @param file Archivo de la imagen.
     * @return URL de la imagen subida.
     */
    String uploadImage(MultipartFile file);

    /**
     * Elimina una imagen.
     * @param imageUrl URL de la imagen.
     */
    void deleteImage(String imageUrl);
}
