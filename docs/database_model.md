## Explicación del Modelo Entidad-Relacion

El sistema gestiona reservas de habitaciones para un único hotel.

### Entidades

- USER: Representa a los usuarios del sistema que realizan reservas.
- ROOM: Representa las habitaciones disponibles del hotel.
- BOOKING: Representa una reserva realizada por un usuario para una habitación. 

### Atributos

- Atributos de USER: user_id (PK), email, name, phone_number, password y role
- Atributos de ROOM: room_id (PK), room_type, room_price, room_photo_url y room_description
- Atributos de Booking: booking_id (PK), room_id (FK), user_id (FK), check_in_date, check_out_date, num_of_adults, num_of_childrens, booking_confirmation_code 

### Relaciones

- Un usuario puede realizar muchas reservas.
- Una habitación puede estar asociada a múltiples reservas.