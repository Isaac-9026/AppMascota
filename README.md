# AppMascotas

Aplicación Android para la gestión de mascotas. Permite registrar, listar,
editar y eliminar mascotas consumiendo un Web Service.

> El Web Service se encuentra en un repositorio separado:
> [WS_Mascotas](https://github.com/Isaac-9026/WS_Mascotas)

## Documentación

- [Proceso de editar mascota](Documentación%20del%20proceso%20EDITAR.pdf)

## Requisitos previos

- Tener el Web Service corriendo antes de usar la app
- Actualizar la IP del servidor en `Registrar.java` y `ListarCustom.java`:
```java
private final String URL = "http://TU_IP:3000/mascotas/";
```

## Funcionalidades

- Registrar una nueva mascota
- Listar mascotas con RecyclerView
- Editar mascota mediante AlertDialog
- Eliminar mascota con confirmación

## Estructura
```
com.example.appmascotas
├── MainActivity.java       # Pantalla principal
├── Registrar.java          # Formulario de registro
├── Listar.java             # Lista simple
├── ListarCustom.java       # Lista con RecyclerView + Editar y Eliminar
├── MascotaAdapter.java     # Adapter del RecyclerView
└── Mascota.java            # Modelo de datos
```

## Dependencias

- Volley — peticiones HTTP
- MinSDK: 30 (Android 11)
