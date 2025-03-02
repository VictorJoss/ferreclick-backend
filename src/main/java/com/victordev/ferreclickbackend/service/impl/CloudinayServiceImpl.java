package com.victordev.ferreclickbackend.service.impl;

import com.cloudinary.Cloudinary;
import com.victordev.ferreclickbackend.exceptions.cloudinary.CloudinaryException;
import com.victordev.ferreclickbackend.service.ICloudinaryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementación del servicio de Cloudinary que proporciona métodos para subir y eliminar archivos.
 */
@Service
public class CloudinayServiceImpl implements ICloudinaryService {

    /**
     * Instancia de Cloudinary.
     */
    @Resource
    private Cloudinary cloudinary;

    /**
     * Sube un archivo a Cloudinary.
     * @param file Archivo a subir.
     * @param folderName Nombre de la carpeta en la que se guardará el archivo.
     * @return URL del archivo subido.
     */
    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        try{
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), getOptions(folderName));
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);

        }catch (IOException e){
            throw new CloudinaryException("Error uploading file to Cloudinary", e);
        }
    }

    /**
     * Elimina un archivo de Cloudinary.
     * @param urlFile URL del archivo a eliminar.
     * @param folderName Nombre de la carpeta en la que se encuentra el archivo.
     */
    @Override
    public void deleteFile(String urlFile, String folderName) {
        try {
            String publicId = extractPublicId(urlFile);
            Map result = cloudinary.uploader().destroy(publicId, getOptions(folderName));

            if (!"ok".equals(result.get("result"))) {
                throw new CloudinaryException("Failed to delete file from Cloudinary. Response: " + result);
            }
        } catch (IOException e) {
            throw new CloudinaryException("Error deleting file from Cloudinary", e);
        }
    }

    /**
     * Obtiene las opciones para subir o eliminar un archivo.
     * @param folderName Nombre de la carpeta en la que se guardará o se encuentra el archivo.
     * @return Opciones para subir o eliminar un archivo.
     */
    private Map<String, Object> getOptions(String folderName){
        Map<String, Object> options = new HashMap<>();
        options.put("folder", folderName);
        return options;
    }

    /**
     * Extrae el public ID de un URL de Cloudinary.
     * @param url URL del archivo en Cloudinary.
     * @return Public ID del archivo.
     */
    private String extractPublicId(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("The URL cannot be null or empty");
        }

        String expectedPrefix = "/upload/";
        int startIndex = url.indexOf(expectedPrefix);

        if (startIndex == -1) {
            throw new IllegalArgumentException("The provided URL does not contain '/upload/'");
        }

        startIndex += expectedPrefix.length();
        int folderEndIndex = url.indexOf('/', startIndex);

        if (folderEndIndex == -1) {
            throw new IllegalArgumentException("The provided URL does not have a valid format");
        }

        int endIndex = url.indexOf('?', folderEndIndex + 1);
        if (endIndex == -1) {
            endIndex = url.length();
        }

        String publicId = url.substring(folderEndIndex + 1, endIndex);

        if (publicId.isEmpty()) {
            throw new IllegalArgumentException("Could not extract a valid public ID from the URL");
        }

        return publicId;
    }
}
