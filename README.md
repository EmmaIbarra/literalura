# 📚 Buscador de Libros con Gutendex API

## Índice
1. [Descripción](#descripción)
2. [Funcionalidades](#funcionalidades)
3. [Cómo usarlo](#cómo-usarlo)
4. [Requisitos previos](#requisitos-previos)
5. [Ejemplo de uso](#ejemplo-de-uso)

## Descripción
El **Buscador de Libros** es una aplicación interactiva desarrollada en Java que utiliza la API de [Gutendex](https://gutendex.com/) para buscar libros por nombre y gestionar información de libros y autores en una base de datos local. 

Con esta herramienta, puedes:
- Encontrar libros de interés con filtros personalizados.
- Guardar libros y sus autores en una base de datos para futuras consultas.
- Realizar búsquedas avanzadas por idioma, año, y más.

Este proyecto es ideal para quienes desean aprender más sobre APIs, bases de datos, y estructuración de aplicaciones con Java.

## Funcionalidades
- **Buscar libros por nombre**: Conecta a la API de Gutendex y devuelve resultados relevantes.
- **Gestión de base de datos local**:
  - Guarda libros y autores de forma automática.
  - Lista de libros guardados, ordenados alfabéticamente.
  - Lista de autores guardados, también ordenados alfabéticamente.
- **Filtros avanzados**:
  - Consulta de autores vivos en un año específico.
  - Filtrado de libros por idioma (español e inglés).
- **Validación**: Verifica si un libro o autor ya existe antes de añadirlo, evitando duplicados.

## Cómo usarlo
Sigue estos pasos para comenzar a usar la aplicación:

1. **Clona este repositorio**:
   ```bash
   git clone https://github.com/tu_usuario/tu_repositorio.git

2. **Configura la base de datos**:
    - Crea una base de datos PostgreSQL.
    - Ajusta las credenciales de conexión en el archivo de configuración **(application.properties)**.

3. **Instala las dependencias necesarias**:
    - Java 17.
    - Maven (o usa el wrapper incluido con ./mvnw).
    - PostgreSQL.
  
4. **Ejecuta el proyecto**:
```bash
./mvnw spring-boot:run
```
## Requisitos previos
  - Tener instalado Java 17 o una versión superior.
  - Tener configurado PostgreSQL como base de datos.
  - Acceso a Internet para interactuar con la API de Gutendex.

## Ejemplo de uso
Cuando ejecutes la aplicación, se mostrará el siguiente menú en la terminal:

![image](https://github.com/user-attachments/assets/c0938b51-203b-46ba-bffe-cb8ebcd3ea1c)

### Ejemplo: Buscar un libro
1. Elige la opción 1.
2. Ingresa el nombre del libro que deseas buscar:
```
¿Qué libro deseas buscar?
Don Quijote
```
3. Si el libro existe en la API de Gutendex, se mostrará la información correspondiente y se guardará en la base de datos.

### Ejemplo: Listar libros guardados
1. Selecciona la opción 2.
2. La aplicación mostrará una lista de los libros guardados, ordenados alfabéticamente.

![image](https://github.com/user-attachments/assets/b1be2ba6-d689-4c40-a8ae-efa987e694c4)



