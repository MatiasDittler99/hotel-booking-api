package com.matias.dittler.hotelbooking.exception;

/**
 * Excepción personalizada para la aplicación Hotel Booking API.
 *
 * Se utiliza para representar errores específicos del negocio
 * o de la lógica de la aplicación que no están cubiertos por
 * excepciones estándar de Java.
 *
 * Hereda de RuntimeException para que sea una excepción no verificada,
 * es decir, no obliga a capturarla ni declararla en throws.
 */
public class OurException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo de la excepción.
     *
     * @param message Mensaje que describe el motivo de la excepción
     */
    public OurException(String message) {
        super(message);
    }

}
