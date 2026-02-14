package com.matias.dittler.hotelbooking.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

/**
 * Clase utilitaria para generar y validar tokens JWT.
 * Se usa para la autenticación de usuarios en la API.
 */
@Service
public class JWTUtils {

    // Tiempo de expiración del token en milisegundos: 7 días
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

    // Clave secreta para firmar los tokens
    private final SecretKey Key;

    /**
     * Constructor que inicializa la clave secreta.
     * Se convierte una cadena Base64 en un SecretKey para HmacSHA256.
     */
    public JWTUtils() {
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    /**
     * Genera un token JWT a partir de un UserDetails.
     * @param userDetails Información del usuario
     * @return Token JWT firmado
     */
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())           // Usuario
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiración
                .signWith(Key)                                   // Firma con la clave
                .compact();                                      // Compactar a String
    }

    /**
     * Extrae el username (subject) de un token.
     * @param token Token JWT
     * @return Nombre de usuario contenido en el token
     */
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Método genérico para extraer claims de un token.
     * @param token Token JWT
     * @param claimsTFunction Función que indica qué claim extraer
     * @param <T> Tipo del claim
     * @return Valor del claim
     */
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        // parseSignedClaims() es parte de la nueva API de JWT 0.12.x
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    /**
     * Valida que el token pertenezca al usuario y que no esté expirado.
     * @param token Token JWT
     * @param userDetails Información del usuario
     * @return true si el token es válido
     */
    public boolean isValidToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Comprueba si el token ya expiró.
     * @param token Token JWT
     * @return true si el token está vencido
     */
    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}
