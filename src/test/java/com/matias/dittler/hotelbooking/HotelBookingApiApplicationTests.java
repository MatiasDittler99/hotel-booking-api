package com.matias.dittler.hotelbooking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.matias.dittler.hotelbooking.service.R2StorageService;

/**
 * Test básico de integración para la aplicación Hotel Booking API.
 * 
 * Objetivo:
 * - Verificar que el contexto de Spring Boot se cargue correctamente sin errores.
 * - Asegurar que todas las configuraciones, beans y dependencias estén inicializados.
 * 
 * Notas:
 * - Se utiliza un perfil "test" para no afectar la configuración de producción.
 * - Se utiliza @MockBean para evitar la inicialización real del servicio R2StorageService.
 */
@ActiveProfiles("test")
@SpringBootTest
class HotelBookingApiApplicationTests {

    /**
     * Se simula (mock) el servicio de almacenamiento R2StorageService.
     * Esto evita que el test intente conectarse a recursos externos o reales.
     */
	@MockBean
    private R2StorageService r2StorageService;

    /**
     * Test principal que verifica la carga del contexto de Spring Boot.
     * 
     * Si el contexto no se carga correctamente, este test falla automáticamente.
     * No es necesario agregar código adicional.
     */
    @Test
    void contextLoads() {
        // Test vacío: solo verifica que Spring Boot se inicialice sin excepciones.
    }
}
