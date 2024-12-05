package com.victordev.ferreclickbackend.service;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    public List<FileStats> contarLineas(String folderPath) throws FileNotFoundException {
        File folder = new File(folderPath);
        List<FileStats> statsList = new ArrayList<>();
        recorrerDirectorios(folder, statsList);
        return statsList;
    }

    private void recorrerDirectorios(File folder, List<FileStats> statsList) throws FileNotFoundException {
        if (folder.isDirectory()) {
            // Obtener todos los archivos y subdirectorios
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Si es un subdirectorio, recorrerlo de manera recursiva
                        recorrerDirectorios(file, statsList);
                    } else if (file.isFile() && (file.getName().endsWith(".ts") || file.getName().endsWith(".java"))) {
                        // Si es un archivo de código, contar las líneas
                        FileStats stats = contarLineasArchivo(file);
                        statsList.add(stats);
                    }
                }
            }
        }
    }

    private FileStats contarLineasArchivo(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int totalLineas = 0;
        int lineasCodigo = 0;
        int lineasComentario = 0;

        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine();
            totalLineas++;
            if (linea.trim().startsWith("//")) {
                lineasComentario++;
            } else if (!linea.trim().isEmpty()) {
                lineasCodigo++;
            }
        }

        scanner.close();
        return new FileStats(file.getName(), totalLineas, lineasCodigo, lineasComentario);
    }

    public static class FileStats {
        private String nombreArchivo;
        private int totalLineas;
        private int lineasCodigo;
        private int lineasComentario;

        public FileStats(String nombreArchivo, int totalLineas, int lineasCodigo, int lineasComentario) {
            this.nombreArchivo = nombreArchivo;
            this.totalLineas = totalLineas;
            this.lineasCodigo = lineasCodigo;
            this.lineasComentario = lineasComentario;
        }

        public String getNombreArchivo() {
            return nombreArchivo;
        }

        public int getTotalLineas() {
            return totalLineas;
        }

        public int getLineasCodigo() {
            return lineasCodigo;
        }

        public int getLineasComentario() {
            return lineasComentario;
        }
    }
}
