package co.edu.upn.arena;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ClasificacionController {

    @FXML private TableView<FilaPosicion> tablaClasificacion;
    @FXML private TableColumn<FilaPosicion, Integer> colPos;
    @FXML private TableColumn<FilaPosicion, String> colEquipo;
    @FXML private TableColumn<FilaPosicion, Integer> colCombates;
    @FXML private TableColumn<FilaPosicion, Integer> colVictorias;
    @FXML private TableColumn<FilaPosicion, Integer> colEmpates;
    @FXML private TableColumn<FilaPosicion, Integer> colDerrotas;
    @FXML private TableColumn<FilaPosicion, Integer> colBajas;
    @FXML private TableColumn<FilaPosicion, Integer> colMuertes;
    @FXML private TableColumn<FilaPosicion, Integer> colBalance;
    @FXML private TableColumn<FilaPosicion, Integer> colPuntos;

    @FXML
    public void initialize() {
        colPos.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().posicion).asObject());
        colEquipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().nombreEquipo));
        colCombates.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().combates).asObject());
        colVictorias.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().victorias).asObject());
        colEmpates.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().empates).asObject());
        colDerrotas.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().derrotas).asObject());
        colBajas.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().bajas).asObject());
        colMuertes.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().muertes).asObject());
        colBalance.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().bajas - c.getValue().muertes).asObject());
        colPuntos.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().puntos).asObject());

        calcularTabla();
    }

    @FXML
    public void calcularTabla() {
        ObservableList<FilaPosicion> filas = FXCollections.observableArrayList();

        for (Equipo eq : DatosTorneo.equipos) {
            FilaPosicion fila = new FilaPosicion(eq.getNombre());

            for (Partido p : DatosTorneo.partidos) {
                if (p.getEquipoLocalId() == eq.getId()) {
                    fila.combates++;
                    fila.bajas += p.getPuntosLocal();
                    fila.muertes += p.getPuntosVisitante();
                    if (p.getPuntosLocal() > p.getPuntosVisitante()) {
                        fila.victorias++;
                        fila.puntos += 3;
                    } else if (p.getPuntosLocal() == p.getPuntosVisitante()) {
                        fila.empates++;
                        fila.puntos += 1;
                    } else {
                        fila.derrotas++;
                    }
                } else if (p.getEquipoVisitanteId() == eq.getId()) {
                    fila.combates++;
                    fila.bajas += p.getPuntosVisitante();
                    fila.muertes += p.getPuntosLocal();
                    if (p.getPuntosVisitante() > p.getPuntosLocal()) {
                        fila.victorias++;
                        fila.puntos += 3;
                    } else if (p.getPuntosVisitante() == p.getPuntosLocal()) {
                        fila.empates++;
                        fila.puntos += 1;
                    } else {
                        fila.derrotas++;
                    }
                }
            }
            filas.add(fila);
        }

        filas.sort((a, b) -> {
            if (b.puntos != a.puntos) {
                return Integer.compare(b.puntos, a.puntos);
            }
            int balanceA = a.bajas - a.muertes;
            int balanceB = b.bajas - b.muertes;
            if (balanceB != balanceA) {
                return Integer.compare(balanceB, balanceA);
            }
            return Integer.compare(b.bajas, a.bajas);
        });

        for (int i = 0; i < filas.size(); i++) {
            filas.get(i).posicion = i + 1;
        }

        tablaClasificacion.setItems(filas);
    }

    public static class FilaPosicion {
        int posicion;
        String nombreEquipo;
        int combates;
        int victorias;
        int empates;
        int derrotas;
        int bajas;
        int muertes;
        int puntos;

        public FilaPosicion(String nombreEquipo) {
            this.nombreEquipo = nombreEquipo;
        }
    }
}
