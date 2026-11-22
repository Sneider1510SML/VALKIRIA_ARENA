package co.edu.upn.arena;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatosTorneo {
    public static final ObservableList<Jugador> jugadores = FXCollections.observableArrayList();
    public static final ObservableList<Equipo> equipos = FXCollections.observableArrayList();
    public static final ObservableList<Partido> partidos = FXCollections.observableArrayList();

    private static int siguienteJugadorId = 1;
    private static int siguienteEquipoId = 1;
    private static int siguientePartidoId = 1;

    public static int generarJugadorId() {
        return siguienteJugadorId++;
    }

    public static int generarEquipoId() {
        return siguienteEquipoId++;
    }

    public static int generarPartidoId() {
        return siguientePartidoId++;
    }

    public static String nombreEquipoPorId(int id) {
        if (id == 0) {
            return "Sin equipo";
        }
        for (Equipo e : equipos) {
            if (e.getId() == id) {
                return e.getNombre();
            }
        }
        return "Desconocido";
    }
}
