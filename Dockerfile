# ------------------------
# ETAPA DE BUILD
# ------------------------
FROM eclipse-temurin:21-jdk-alpine AS build
# Imagen base ligera con JDK 21 para compilar la aplicación

WORKDIR /app
# Directorio de trabajo dentro del contenedor

# Instalar utilidades necesarias
RUN apk add --no-cache dos2unix

# ------------------------
# Copiamos archivos necesarios para Maven Wrapper
# ------------------------
# Copia el pom.xml y el script mvnw (Maven Wrapper)
# Copia la carpeta .mvn que contiene configuración del wrapper
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# ------------------------
# Permiso de ejecución para mvnw (recomendado en Linux/Alpine)
# ------------------------
RUN dos2unix mvnw && chmod +x mvnw
# Asegura que mvnw se pueda ejecutar dentro del contenedor

# ------------------------
# Pre-descargamos dependencias para acelerar el build
# ------------------------
RUN ./mvnw -B -q -DskipTests dependency:go-offline
# Descarga todas las dependencias declaradas en pom.xml sin compilar aún

# ------------------------
# Copiamos todo el proyecto
# ------------------------
COPY src src
# Copia todos los archivos del proyecto al contenedor

# ------------------------
# Construimos el JAR final
# ------------------------
RUN ./mvnw clean package -DskipTests
# Construye el artefacto JAR, omitiendo tests para acelerar el build

# ------------------------
# IMAGEN FINAL
# ------------------------
FROM eclipse-temurin:21-jdk-alpine
# Imagen base ligera solo con JDK para correr la aplicación

WORKDIR /app
# Directorio donde se colocará el JAR

# ------------------------
# Copiamos el JAR construido en la etapa de build
# ------------------------
COPY --from=build /app/target/hotel-booking-api-0.0.1-SNAPSHOT.jar app.jar
# Copia el JAR generado en la imagen final

# ------------------------
# Variables de entorno por defecto (pueden sobrescribirse)
# ------------------------
# En produccion
ENV SPRING_PROFILES_ACTIVE=prod
# Perfil de Spring Boot por defecto
ENV PORT=8080
# Puerto por defecto de la aplicación

# ------------------------
# Puertos expuestos
# ------------------------
EXPOSE 8080
# Indica el puerto que usará el contenedor para networking

# ------------------------
# Comando de inicio
# ------------------------
ENTRYPOINT ["java","-jar","app.jar"]
# Ejecuta la aplicación Spring Boot al iniciar el contenedor
