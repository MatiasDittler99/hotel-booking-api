# hotel-booking-api
API REST para un sistema de reservas de hotel desarrollada con Java y Spring Boot, con seguridad, persistencia de datos, testing automatizado, documentada y con despliegue.

---

## 🛠 Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Validation
- Lombock
- Spring Boot DevTools
- JSON web token (jjwt-jackson, jjwt-api y jjwt-impl)
- Software amazon aws sdk v2
- PostgreSQL
- Maven
- JUnit 5 (agregar dependencia)
- Mockito (agregar dependencia)
- Swagger / OpenAPI (agregar dependencia)
- Postman
- Render
- Docker

---

## 🗂 Estructura del proyecto del Backend

hotel-booking
├─ .mvn\wrapper
├─ docs
├─ src
├─ target
├─ .gitatributes
├─ .gitignore
├─ HELP.md
├─ LICENSE
├─ mvnw
├─ mvnw.cmd
├─ pom.xml
└── README.md

---

## 📄 Documentación de la API

La API está documentada y se puede probar de las siguientes formas:

- **Swagger / OpenAPI / README.md**:
    - Visualizar endpoints disponibles
    - Probar peticiones desde el navegador
    - Consultar modelos y respuestas
    - Lectura del archivo README.md para obtener toda la informacion del proyecto

- **Postman**  
  - Se incluye una colección de Postman (`docs/postman_collection.json`) que permite importar todos los endpoints y probarlos fácilmente desde la aplicación.  
  - Para usarla:
    1. Abrir Postman.
    2. Importar el archivo `docs/postman_collection.json`.
    3. Configurar la URL base si es necesario.

---

## Base de datos

- El modelo entidad–relación se encuentra documentado en la carpeta `/docs`.
- Visualizacion del diagrama ENTIDAD-RELACIÓN: ![ER Diagram](docs/er-diagram.png)
- Explicación del diagrama ENTIDAD-RELACIÓN: ![EXPLANATION OF THE ER DIAGRAM](docs/database_model.md)
- La base de datos utilizada para este proyecto es una base de datos relacional con PostgreSQL utilizando la herramienta grafica pgAdmin

---

## Servicio cloud MinIO en Fly.io

--

## 🧪 Testing

- Tests unitarios implementados con **JUnit 5**
- Uso de **Mockito** para simulación de dependencias cuando es necesario

---

## 🚀 Despliegue

La aplicación se encuentra desplegada en **Render**.

---



