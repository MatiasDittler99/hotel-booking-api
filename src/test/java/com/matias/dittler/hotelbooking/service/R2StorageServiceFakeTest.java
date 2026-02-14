package com.matias.dittler.hotelbooking.service;

import com.matias.dittler.hotelbooking.exception.OurException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para R2StorageServiceFake.
 *
 * Verifica que la subida de archivos simule correctamente la generación
 * de URL y que lance excepción en caso de archivo inválido.
 */
class R2StorageServiceFakeTest {

    // Instancia del servicio falso
    private final R2StorageServiceFake service = new R2StorageServiceFake();

    /**
     * Verifica que la subida de un archivo válido
     * devuelva una URL simulada correcta.
     */
    @Test
    void uploadImage_ReturnsFakeUrl() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "contenido de prueba".getBytes()
        );

        // Act
        String url = service.uploadImage(file);

        // Para debug: muestra la URL generada
        System.out.println(url); // https://fake-bucket.test/images/test.jpg

        // Assert
        assertNotNull(url);
        assertTrue(url.startsWith("https://fake-bucket.test/images/"));
        assertTrue(url.endsWith("test.jpg"));
    }

    /**
     * Verifica que se lance OurException cuando el archivo es nulo o vacío.
     */
    @Test
    void uploadImage_NullOrEmptyFile_ThrowsOurException() {
        MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);

        // Archivo nulo
        assertThrows(OurException.class, () -> service.uploadImage(null));
        // Archivo vacío
        assertThrows(OurException.class, () -> service.uploadImage(emptyFile));
    }
}
