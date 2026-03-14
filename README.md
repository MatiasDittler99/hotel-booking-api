# 🏨 hotel-booking-api
API REST para un sistema de la gestion de reservas de habitaciones en un hotel.

Este backend permite registrar usuarios, consultar habitaciones disponibles, crear reservas y administrar la información del sistema.

Desarrollada con Java y Spring Boot, con seguridad, persistencia de datos, testing automatizado, documentada y con despliegue.

El proyecto está diseñado siguiendo buenas prácticas de desarrollo backend y arquitectura modular.

---

## 🛠 Tecnologias utilizadas

- Java 21
- Spring Boot 3
- Spring Security
- Spring Web
- Spring Data JPA
- Hibernate
- Validation
- Lombock
- Spring Boot DevTools
- JSON web token (jjwt-jackson, jjwt-api y jjwt-impl)
- Software amazon aws sdk v2 (sdk para poder trabajar con cloudflare)
- Cloudflare (servicio de almacenamiento en la nube)
- PostgreSQL
- H2 Database (testing)
- Maven
- Spring Security Test
- JUnit 5 (testing)
- Mockito (testing)
- Swagger / OpenAPI
- Postman (pruebas url)
- Render (deploy)
- Docker (contenedores)
- Java Dotenv

---

## 📦 Funcionalidades y Caracterpisticas principales

- Registro y autenticación de usuarios
- Autenticación con **JWT**
- Consulta de habitaciones disponibles
- Sistema de creación y gestion de reservas
- Sistema de roles de usuario  
- API REST estructurada  
- Validación de datos 
- Subida de imágenes a Cloudflare
- Documentación automática con Swagger

## 🏗 Arquitectura del proyecto

El backend sigue una arquitectura en capas (Layered Architecture):

Controller Layer
↓
Service Layer
↓
Repository Layer
↓
Database

---

## 🗂 Estructura del proyecto del Backend

repo github
└── hotel-booking-api
    ├─ .mvn\wrapper
    ├─ docs
    ├─ src
    |  ├─ main
    |  |  ├─ java/com/matias/dittler/hotelbooking
    |  |  |  ├─ config # Configuracion de Swagger
    |  |  |  ├─ controller # Controladores Rest
    |  |  |  ├─ dto # Objetos de transferencia de datos
    |  |  |  ├─ entity # Entidades del sistema
    |  |  |  ├─ exception # Excepciones para errores del sistema
    |  |  |  ├─ repository # Acceso a base de datos
    |  |  |  ├─ security # Configuración de autenticación, cors y JWT
    |  |  |  ├─ service # Lógica de negocio
    |  |  |  |  ├─ implementation # Implementaciones del sistema
    |  |  |  |  └── intefac # Interaces del sistema
    |  |  |  └──  utils # Archivos utiles del sistema
    |  |  └── resources
    |  |      ├─ static
    |  |      ├─ templates
    |  |      └── application.yml
    |  └── test
    |      └──  java/com/matias/dittler/hotelbooking
    |           ├─ controller
    |           ├─ dto
    |           ├─ entity
    |           ├─ repository
    |           ├─ security
    |           ├─ service
    |           |  └── implementation
    |           └── utils
    ├─ target
    ├─ .dockerignore
    ├─ .env.local
    ├─ .env.prod
    ├─ .gitatributes
    ├─ .gitignore
    ├─ docker-compose.local.yml
    ├─ docker-compose.prod.yml
    ├─ Dockerfile
    ├─ HELP.md
    ├─ LICENSE
    ├─ mvnw
    ├─ mvnw.cmd
    ├─ pom.xml
    └── README.md

---

# 📂 Entidades principales

### 👤 Usuario

Representa un usuario del sistema.

Campos principales:

- id
- name
- email
- phoneNumber
- password
- role
- bookings

---

### 🏨 Habitación

Representa una habitación del hotel.

Campos principales:

- id
- roomType
- roomPrice
- roomPhotoUrl
- roomDescription
- bookings

---

### 📅 Reserva

Representa una reserva realizada por un usuario.

Campos principales:

- id
- checkInDate
- checkOutDate
- numOfAdults
- numOfChildren
- totalNumOfGuest
- bookingConfirmationCode
- user
- room

---

# 🔐 Autenticación

La API utiliza **JWT (JSON Web Token)** para la autenticación.

Flujo:

1. Usuario se registra
2. Usuario inicia sesión
3. El servidor devuelve un **token JWT**
4. El cliente envía el token en cada request

Header requerido:

Authorization: Bearer TOKEN_JWT

---

# 📡 Endpoints principales

## Autenticación

### Registro de usuario

POST

/auth/register

Body:

{
    "email": "usuario_prueba@user.com",
    "password": "user",
    "phoneNumber": "9876543210",
    "name": "Usuario Prueba",
    "role": "USER"
}

---

### Login

POST

/auth/login

Body:

{
    "email": "usuario_prueba@user.com",
    "password": "user"
}

Al realizar el login, la API devuelve un token de autenticación.

Ejemplo de respuesta:

{
  "token": "jwt_token"
}

Este token debe copiarse y enviarse en las siguientes solicitudes a los endpoints protegidos.

Para hacerlo, se debe agregar en el header de la request:

Authorization: Bearer jwt_token

En herramientas como Swagger o Postman, se debe colocar el token en la sección **Authorization → Bearer Token**.

---

# 🏨 Habitaciones

### Añadir habitación

POST

/rooms/add

Body:

La solicitud debe enviarse en formato **form-data**.

Ejemplo de parámetros:

| Key | Tipo | Descripción |
|-----|------|-------------|
| photo | file | Imagen de la habitación |
| roomType | text | Tipo de habitación |
| roomPrice | text | Precio por noche |
| roomDescription | text | Descripción de la habitación |

---

### Obtener todas las habitaciones

GET

/rooms/all

---

### Obtener todas las habitaciones por tipo

GET

/rooms/types

---

### Obtener habitacion por id

GET

/rooms/room-by-id/{roomId}

---

### Obtener todas las habitaciones disponibles

GET

/rooms/all-avaible-rooms

---

### Obtener todas las habitaciones disponibles por fecha y tipo

/rooms/available-rooms-by-date-and-type?checkInDate=2026-06-07&checkOutDate=2026-07-09&roomType=Queen

Params:

Ejemplo de parámetros:

| Key | Descripción |
|-----|-------------|
| checkInDate | 2026-06-07 |
| checkOutDate | 2026-07-09 |
| roomType | Habitación Individual |

---

### Actualizar una habitacion por id

PUT

/rooms/update/{roomId}

Body:

La solicitud debe enviarse en formato **form-data**.

Ejemplo de parámetros:

| Key | Tipo | Descripción |
|-----|------|-------------|
| photo | file | Imagen de la habitación |
| roomType | text | Tipo de habitación |
| roomPrice | text | Precio por noche |
| roomDescription | text | Descripción de la habitación |

---

### Eliminar habitación por id

DELETE

/rooms/delete/{roomId}

---

# 📅 Reservas

### Reservar una habitación

POST

/bookings/book-room/{roomId}/{userId}

Body:

{
    "checkInDate":"2026-02-12",
    "checkOutDate":"2026-06-05",
    "numOfAdults":"3",
    "numOfChildren":"2"
}

---

### Obtener todas las reservas

GET

/bookings/all

---

### Obtener reserva por codigo de confirmación

GET

/bookings/get-by-confirmation-code/{confirmationCode}

---

### Cancelar una reserva por id

DELETE

/bookings/cancel/{bookingId}

---

### Obtener todos los usuarios

GET

/users/all

---

### Obtener usuario por id

GET

/users/get-by-id/{userId}

---

### Eliminar usuario por id

DELETE

/users/delete/{userId}

---

### Obtener informacion del perfil del usuario logueado

GET

/users/get-logged-in-profile-info

---

### Obtener reservas de usuario por id de usuario

GET

/get-user-bookings/{userId}

---

### Controlador de salud del servidor del backend

GET

/

---

## Base de datos

- El modelo entidad–relación se encuentra documentado en la carpeta `/docs`.
- Visualizacion del diagrama ENTIDAD-RELACIÓN: ![ER Diagram](docs/er-diagram.png)
- Explicación del diagrama ENTIDAD-RELACIÓN: ![EXPLANATION OF THE ER DIAGRAM](docs/database_model.md)
- La base de datos utilizada para este proyecto es una base de datos relacional con PostgreSQL utilizando la herramienta grafica pgAdmin (local) y Supabase (deploy)

---

## Servicio cloud con Cloudflare

- Se utiliza el servico de almacenamiento en la nube de Cloudflare para guardar las imagenes de las habitaciones del hotel

--

# ⚙️ Instalación del proyecto

## 1️⃣ Clonar el repositorio

git clone https://github.com/MatiasDittler99/hotel-booking-api.git

---

## 2️⃣ Entrar al proyecto

cd hotel-booking-api

---

## 3️⃣ 3️⃣ Configurar base de datos

El proyecto utiliza PostgreSQL como base de datos.

Antes de ejecutar la aplicación, debes configurar las variables de entorno necesarias para la conexión.

Variables de entorno

Ejemplo para desarrollo local:

POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_DB=hotel_booking_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
Configuración en Spring Boot

El backend utiliza estas variables en el archivo application.yml:

spring:
  datasource:
    url: jdbc:postgresql://${POSTGRES_HOST:localhost}:${POSTGRES_PORT:5432}/${POSTGRES_DB:hotel_booking_db}
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}
    driver-class-name: org.postgresql.Driver

Esto permite que la aplicación funcione tanto en desarrollo local como en producción, cambiando únicamente las variables de entorno.

---

## 4️⃣ Instalar y limpiar el proyecto

mvn clean install

## 4️⃣ Ejecutar el proyecto

Ejecución del proyecto en produccion:

mvn spring-boot:run "-Dspring-boot.run.profiles=prod"

Ejecución del proyecto en produccion:

mvn spring-boot:run "-Dspring-boot.run.profiles=local"

---

## 📄 Documentación de la API

La API está documentada y se puede probar de las siguientes formas:

- **Swagger / OpenAPI / README.md**:
    - Visualizar endpoints disponibles
    - Probar peticiones desde el navegador
    - Consultar modelos y respuestas
    - Lectura del archivo README.md para obtener toda la informacion del proyecto

- **Postman**  
  - Se incluyen dos colecciónes de Postman (`docs/postman_collection.json`) que permite importar todos los endpoints y probarlos fácilmente desde la aplicación.  
  - Para usarla:
    1. Abrir Postman.
    2. Importar el archivo `docs/Hotel Booking Api Local.postman_collection.json` o `docs/Hotel Booking Api Render.postman_collection.json`.
    3. Configurar la URL base si es necesario.

---

## 🧪 Testing

- Tests unitarios implementados con **JUnit 5** y **Spring Security Test**
- Uso de **Mockito** para simulación de dependencias cuando es necesario
- Para ejecutar los tests:

mvn test "-Dspring.profiles.active=test"

---

# 🐳 Ejecutar Docker en local

- La aplicacion se encuentra dockerizada tiene los archivos de docker compose y docker file

## 1️⃣ Crear archivo .env

Docker-compose usa variables, así que deberías tener un .env en la raíz:

POSTGRES_DB=hotel_booking_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

SPRING_PROFILES_ACTIVE=local
PORT=8080

R2_ENDPOINT=your_endpoint
R2_ACCESS_KEY=your_key
R2_SECRET_KEY=your_secret
R2_BUCKET=your_bucket
R2_REGION=auto
R2_PUBLIC_URL=your_public_url

Docker Compose lo carga automáticamente.

## 2️⃣ Levantar contenedores

docker compose -f docker-compose.local.yml up -d --build

--build es útil porque reconstruye la API si cambiaste código.

## 3️⃣ Ver contenedores corriendo

docker ps

Deberías ver algo como:

hotel_booking_postgres
hotel_booking_api

## 4️⃣ Ver logs (muy útil)

docker compose -f docker-compose.local.yml logs -f

## 5️⃣ Apagar contenedores

docker compose -f docker-compose.local.yml down

🌐 Acceso

Cuando esté levantado:

API:

http://localhost:8080

Swagger:

http://localhost:8080/swagger-ui.html

PostgreSQL:

localhost:5432

---

# 🐳 Ejecutar Docker en produccion

- La aplicación puede ejecutarse en modo producción usando Docker Compose.

## 1️⃣ Configurar variables de entorno

Crear un archivo .env en la raíz del proyecto con las siguientes variables:

SPRING_DATASOURCE_URL=jdbc:postgresql://your-host:5432/your_database
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password

R2_ENDPOINT=your_endpoint
R2_ACCESS_KEY=your_access_key
R2_SECRET_KEY=your_secret_key
R2_BUCKET=your_bucket
R2_REGION=auto
R2_PUBLIC_URL=your_public_url

Estas variables permiten configurar la conexión a la base de datos y el almacenamiento sin exponer credenciales en el repositorio.

## 2️⃣ Construir y levantar los contenedores

Ejecutar el siguiente comando:

docker compose -f docker-compose.prod.yml up -d --build

Esto iniciará:

La API Spring Boot en modo producción

Conectada a la base de datos configurada

Con almacenamiento de archivos en Cloudflare R2

## 3️⃣ Acceder a la aplicación

Una vez iniciada, la API estará disponible en:

http://localhost:8080

Documentación de la API (Swagger):

http://localhost:8080/swagger-ui.html

---

## 🚀 Despliegue y desarrollo

- La aplicación se encuentra desplegada en **Render**.
URL_DEPLOY: https://hotel-booking-api-aoih.onrender.com 
URL_LOCAL: http://localhost:8080

- La aplicación puede desplegarse en diferentes plataformas 

---

# 📝 Nota

- Ya hay datos previos cargados en la api para probar desde el deploy

---

# 🤝 Contribuciones

Las contribuciones son bienvenidas.

Pasos:

1. Fork del repositorio
2. Crear rama nueva
3. Commit de cambios
4. Pull Request

---

# 📄 Licencia

Este proyecto está bajo licencia MIT.

---

# 👨‍💻 Autor

**Matías Dittler**

GitHub  
https://github.com/MatiasDittler99

---