package com.victordev.ferreclickbackend.service;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que proporciona métodos para contar las líneas de código de archivos de código fuente.
 */
@Service
public class FileService {

    /**
     * Cuenta las líneas de código de los archivos de código fuente en un directorio.
     * @param folderPath Ruta del director de código fuente.
     * @return Lista de objetos `FileStats` que contienen las estadísticas de los archivos de código fuente.
     * @throws FileNotFoundException Si la ruta del directorio no existe.
     */
    public List<FileStats> contarLineas(String folderPath) throws FileNotFoundException {
        File folder = new File(folderPath);
        List<FileStats> statsList = new ArrayList<>();
        recorrerDirectorios(folder, statsList);
        return statsList;
    }

    /**
     * Recorre los directorios de manera recursiva y cuenta las líneas de código de los archivos de código fuente.
     * @param folder Directorio a recorrer.
     * @param statsList Lista de objetos `FileStats` que contienen las estadísticas de los archivos de código fuente.
     * @throws FileNotFoundException Si la ruta del directorio no existe.
     */
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

    /**
     * Cuenta las líneas de código de un archivo de código fuente.
     * @param file Archivo de código fuente.
     * @return Objeto `FileStats` que contiene las estadísticas del archivo de código fuente.
     * @throws FileNotFoundException Si el archivo no existe.
     */
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


    /**
     * Cuenta las líneas de código de los archivos de código fuente en un directorio.
     * @param folderPath Ruta del directorio de código fuente.
     * @return Lista de objetos `FileStats` que contienen las estadísticas de los archivos de código fuente.
     * @throws FileNotFoundException Si la ruta del directorio no existe.
     */
    public List<FileStats> contarLineas4J(String folderPath) throws FileNotFoundException {
        File folder = new File(folderPath);
        List<FileStats> statsList = new ArrayList<>();
        recorrerDirectorios4J(folder, statsList);
        return statsList;
    }

    /**
     * Recorre los directorios de manera recursiva y cuenta las líneas de código de los archivos de código fuente.
     * @param folder Directorio a recorrer.
     * @param statsList Lista de objetos `FileStats` que contienen las estadísticas de los archivos de código fuente.
     * @throws FileNotFoundException Si la ruta del directorio no existe.
     */
    private void recorrerDirectorios4J(File folder, List<FileStats> statsList) throws FileNotFoundException {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        recorrerDirectorios4J(file, statsList);
                    } else if (file.isFile() && file.getName().endsWith(".java")) {
                        FileStats stats = contarLineasArchivo4J(file);
                        statsList.add(stats);
                    }
                }
            }
        }
    }

    /**
     * Cuenta las líneas de código de un archivo de código fuente.
     * @param file Archivo de código fuente.
     * @return Objeto `FileStats` que contiene las estadísticas del archivo de código fuente.
     * @throws FileNotFoundException Si el archivo no existe.
     */
    private FileStats contarLineasArchivo4J(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int totalLineas = 0;
        int lineasCodigo = 0;
        int lineasComentario = 0;
        boolean enComentarioBloque = false;

        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            totalLineas++;

            if (enComentarioBloque) {
                lineasComentario++;
                if (linea.endsWith("*/")) {
                    enComentarioBloque = false;
                }
            } else if (linea.startsWith("//")) {
                lineasComentario++;
            } else if (linea.startsWith("/*")) {
                lineasComentario++;
                if (!linea.endsWith("*/")) {
                    enComentarioBloque = true;
                }
            } else if (!linea.isEmpty()) {
                lineasCodigo++;
            }
        }

        scanner.close();
        return new FileStats(file.getName(), totalLineas, lineasCodigo, lineasComentario);
    }
    /**
     * Clase que contiene las estadísticas de un archivo de código fuente.
     */
    public static class FileStats {
        private String nombreArchivo;
        private int totalLineas;
        private int lineasCodigo;
        private int lineasComentario;

        /**
         * Constructor de la clase `FileStats`.
         * @param nombreArchivo Nombre del archivo.
         * @param totalLineas Número total de líneas.
         * @param lineasCodigo Número de líneas de código.
         * @param lineasComentario Número de líneas de comentario.
         */
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
