package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")  // Ajusta el puerto según tu aplicación Angular
@RestController
@RequestMapping("/api")
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping("/contar-lineas")
    public List<FileService.FileStats> contarLineas() throws FileNotFoundException {
        // Cambia esta ruta al directorio que desees
        String folderPath = "D:/Documents/Yo/ProyectosVisualStudio/ProyectoIngSoftware/ferreclick-front/src";  // Ruta de ejemplo para una aplicación Spring Boot
        return fileService.contarLineas(folderPath);
    }
}
