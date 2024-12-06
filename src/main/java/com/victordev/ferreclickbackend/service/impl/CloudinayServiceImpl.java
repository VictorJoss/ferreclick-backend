package com.victordev.ferreclickbackend.service.impl;

import com.cloudinary.Cloudinary;
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
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Elimina un archivo de Cloudinary.
     * @param urlFile URL del archivo a eliminar.
     * @param folderName Nombre de la carpeta en la que se encuentra el archivo.
     */
    @Override
    public void deleteFile(String urlFile, String folderName){
        try {
            String publicId = extractPublicId(urlFile);
            cloudinary.uploader().destroy(publicId, getOptions(folderName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene las opciones para subir o eliminar un archivo.
     * @param folderName Nombre de la carpeta en la que se guardará o se encuentra el archivo.
     * @return Opciones para subir o eliminar un archivo.
     */
    private HashMap<Object, Object> getOptions(String folderName){
        HashMap<Object, Object> options = new HashMap<>();
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
            throw new IllegalArgumentException("El URL no puede ser nulo o vacío");
        }

        String expectedPrefix = "/upload/";
        int startIndex = url.indexOf(expectedPrefix);

        if (startIndex == -1) {
            throw new IllegalArgumentException("El URL proporcionado no contiene '/upload/'");
        }

        startIndex += expectedPrefix.length();

        int folderEndIndex = url.indexOf('/', startIndex);
        if (folderEndIndex == -1) {
            throw new IllegalArgumentException("El URL proporcionado no tiene un formato válido");
        }

        int endIndex = url.indexOf('?', folderEndIndex + 1);
        if (endIndex == -1) {
            endIndex = url.length();
        }

        String publicId = url.substring(folderEndIndex + 1, endIndex);

        if (publicId.isEmpty()) {
            throw new IllegalArgumentException("No se pudo extraer un public ID válido del URL");
        }

        return publicId;
    }
}
