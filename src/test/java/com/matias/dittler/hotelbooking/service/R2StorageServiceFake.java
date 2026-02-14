package com.matias.dittler.hotelbooking.service;

import com.matias.dittler.hotelbooking.exception.OurException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio falso para pruebas que simula la subida de imágenes a R2.
 *
 * Permite testear servicios que dependen de la subida de archivos
 * sin necesidad de conexión real a un bucket o almacenamiento en la nube.
 */
public class R2StorageServiceFake {

    /**
     * Simula la subida de un archivo y devuelve una URL falsa.
     *
     * @param file archivo a "subir"
     * @return URL simulada del archivo
     * @throws OurException si el archivo es nulo o está vacío
     */
    public String uploadImage(MultipartFile file) {
        // Valida que el archivo no sea nulo ni vacío
        if (file == null || file.isEmpty()) {
            throw new OurException("Archivo inválido");
        }

        // Retorna una URL simulada usando el nombre original del archivo
        return "https://fake-bucket.test/images/" + file.getOriginalFilename();
    }
}
