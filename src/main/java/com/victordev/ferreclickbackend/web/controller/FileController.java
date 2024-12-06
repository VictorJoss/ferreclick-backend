package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Controlador REST que maneja las peticiones relacionadas con archivos.
 */
@CrossOrigin(origins = "http://localhost:4200")  // Ajusta el puerto según tu aplicación Angular
@RestController
@RequestMapping("/api")
public class FileController {

    /**
     * Servicio de archivos.
     */
    @Autowired
    private FileService fileService;

    /**
     * Ruta del directorio de archivos.
     */
    @Value("${my.java.folder.path}")
    private String myJavaFolderPath;
    @Value("${my.front.folder.path}")
    private String myFrontFolderPath;

    /**
     * Cuenta las líneas de los archivos de un directorio.
     * @return Lista de objetos `FileStats` que representan las estadísticas de los archivos.
     */
    @GetMapping("/contar-lineas")
    public List<FileService.FileStats> contarLineas() throws FileNotFoundException {
        return fileService.contarLineas(myFrontFolderPath);
    }

    /**
     * Cuenta las líneas de los archivos de un directorio.
     * @return Lista de objetos `FileStats` que representan las estadísticas de los archivos.
     */
    @GetMapping("/contar-lineas-java")
    public List<FileService.FileStats> contarLineasJava() throws FileNotFoundException {
        return fileService.contarLineas4J(myJavaFolderPath);
    }
}
