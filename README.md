# UPN Arena — Sistema de Gestión de Torneos

Aplicación de escritorio desarrollada en **JavaFX** y **SQLite** para administrar torneos competitivos: registro de jugadores, conformación de escuadrones, historial de partidas y tabla de posiciones calculada automáticamente, con una interfaz oscura inspirada en *Gears of War*.

---

## Funcionalidades principales

* **Jugadores:** Registro, edición y eliminación de participantes con validación de campos y correo electrónico único.
* **Escuadrones (Equipos):** Creación de equipos y asignación de hasta **5 integrantes** disponibles (un jugador solo puede pertenecer a un escuadrón a la vez).
* **Partidas:** Registro y edición de enfrentamientos con marcador y fecha, impidiendo puntajes negativos o partidos de un equipo contra sí mismo.
* **Clasificación automática:** Cálculo en tiempo real de combates jugados, victorias (3 pts), empates (1 pt), derrotas (0 pts), bajas, muertes, balance y ordenamiento automático de posiciones.
* **Persistencia segura:** Almacenamiento local en `arena.db` mediante SQLite utilizando consultas parametrizadas (`PreparedStatement` con `?`) para prevenir inyección SQL y manejo de excepciones con `try/catch`.

---

## Tecnologías utilizadas

* **Lenguaje:** Java 25
* **Interfaz gráfica:** JavaFX 25 (FXML + CSS)
* **Gestor de proyecto:** Apache Maven
* **Base de datos:** SQLite (`sqlite-jdbc`)

---

## Estructura del proyecto (`co.edu.upn.arena`)

* `App.java`: Punto de entrada de la aplicación; inicializa las tablas en SQLite y carga los datos guardados.
* `Conexion.java`: Administra la conexión JDBC con `arena.db` y la creación de las tablas `jugador`, `equipo` y `partido`.
* `DatosTorneo.java`: Capa de acceso a datos que sincroniza las consultas SQL (`INSERT`, `UPDATE`, `DELETE`, `SELECT`) con las `ObservableList` de la interfaz.
* `Jugador.java`, `Equipo.java`, `Partido.java`: Clases modelo encapsuladas.
* `PrincipalController.java`, `JugadoresController.java`, `EquiposController.java`, `PartidosController.java`, `ClasificacionController.java`: Controladores de cada vista FXML.
* `estilos.css`: Hoja de estilos personalizada con temática táctica oscura.

---

## Cómo ejecutar el proyecto

1. Clonar este repositorio o abrir la carpeta del proyecto en **Apache NetBeans**.
2. Verificar la descarga de dependencias de Maven (`pom.xml`).
3. Ejecutar el proyecto con **`F6`** (*Run Project*) o desde terminal con:
   ```bash
   mvn clean javafx:run
