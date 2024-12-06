package com.victordev.ferreclickbackend.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interfaz del servicio de Cloudinary que proporciona métodos para subir y borrar archivos.
 */
public interface ICloudinaryService {

    /**
     * Sube un archivo a Cloudinary.
     * @param file Archivo a subir.
     * @param folderName Nombre de la carpeta en la que se subirá el archivo.
     * @return URL del archivo subido.
     */
    String uploadFile(MultipartFile file, String folderName);

    /**
     * Elimina un archivo de Cloudinary.
     * @param publicId Identificador público del archivo.
     * @param folderName Nombre de la carpeta en la que se encuentra el archivo.
     */
    void deleteFile(String publicId, String folderName);
}
