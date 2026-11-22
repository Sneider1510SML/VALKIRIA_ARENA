package co.edu.upn.arena;

import java.time.LocalDate;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PartidosController {

    @FXML private ComboBox<Equipo> cmbLocal;
    @FXML private ComboBox<Equipo> cmbVisitante;
    @FXML private TextField txtPuntosLocal;
    @FXML private TextField txtPuntosVisitante;
    @FXML private DatePicker dpFecha;

    @FXML private TableView<Partido> tablaPartidos;
    @FXML private TableColumn<Partido, Integer> colId;
    @FXML private TableColumn<Partido, String> colLocal;
    @FXML private TableColumn<Partido, String> colMarcador;
    @FXML private TableColumn<Partido, String> colVisitante;
    @FXML private TableColumn<Partido, String> colFecha;

    private Partido seleccionado = null;

    @FXML
    public void initialize() {
        cmbLocal.setItems(DatosTorneo.equipos);
        cmbVisitante.setItems(DatosTorneo.equipos);
        dpFecha.setValue(LocalDate.now());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLocal.setCellValueFactory(celda ->
                new SimpleStringProperty(DatosTorneo.nombreEquipoPorId(celda.getValue().getEquipoLocalId())));
        colMarcador.setCellValueFactory(celda -> {
            Partido p = celda.getValue();
            return new SimpleStringProperty(p.getPuntosLocal() + " - " + p.getPuntosVisitante());
        });
        colVisitante.setCellValueFactory(celda ->
                new SimpleStringProperty(DatosTorneo.nombreEquipoPorId(celda.getValue().getEquipoVisitanteId())));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        tablaPartidos.setItems(DatosTorneo.partidos);

        tablaPartidos.getSelectionModel().selectedItemProperty().addListener((obs, ant, nuevo) -> {
            seleccionado = nuevo;
            if (nuevo != null) {
                txtPuntosLocal.setText(String.valueOf(nuevo.getPuntosLocal()));
                txtPuntosVisitante.setText(String.valueOf(nuevo.getPuntosVisitante()));
                dpFecha.setValue(LocalDate.parse(nuevo.getFecha()));
                seleccionarEquipoEnCombo(cmbLocal, nuevo.getEquipoLocalId());
                seleccionarEquipoEnCombo(cmbVisitante, nuevo.getEquipoVisitanteId());
            }
        });
    }

    @FXML
    private void guardar() {
        Equipo local = cmbLocal.getValue();
        Equipo visitante = cmbVisitante.getValue();
        String strPtsLocal = txtPuntosLocal.getText().trim();
        String strPtsVisitante = txtPuntosVisitante.getText().trim();
        LocalDate fecha = dpFecha.getValue();

        if (local == null || visitante == null || strPtsLocal.isEmpty() || strPtsVisitante.isEmpty() || fecha == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Selecciona ambos escuadrones, los puntos y la fecha.");
            return;
        }

        if (local.getId() == visitante.getId()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Equipos iguales", "Un escuadrón no puede enfrentarse contra sí mismo.");
            return;
        }

        int ptsLocal;
        int ptsVisitante;
        try {
            ptsLocal = Integer.parseInt(strPtsLocal);
            ptsVisitante = Integer.parseInt(strPtsVisitante);
            if (ptsLocal < 0 || ptsVisitante < 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Puntaje inválido", "Los puntos no pueden ser negativos.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Formato inválido", "Los puntos deben ser números enteros.");
            return;
        }

        if (seleccionado == null) {
            int nuevoId = DatosTorneo.generarPartidoId();
            DatosTorneo.partidos.add(new Partido(nuevoId, local.getId(), visitante.getId(), ptsLocal, ptsVisitante, fecha.toString()));
            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso", "Partida registrada");
        } else {
            seleccionado.setPuntosLocal(ptsLocal);
            seleccionado.setPuntosVisitante(ptsVisitante);
            seleccionado.setFecha(fecha.toString());
            tablaPartidos.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Actualización exitosa", "Partida actualizada");
        }
        limpiar();
    }

    @FXML
    private void eliminar() {
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin selección", "Selecciona una partida de la tabla para eliminarla.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Eliminar la partida #" + seleccionado.getId() + "?");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            DatosTorneo.partidos.remove(seleccionado);
            limpiar();
        }
    }

    @FXML
    private void limpiar() {
        seleccionado = null;
        cmbLocal.getSelectionModel().clearSelection();
        cmbVisitante.getSelectionModel().clearSelection();
        txtPuntosLocal.clear();
        txtPuntosVisitante.clear();
        dpFecha.setValue(LocalDate.now());
        tablaPartidos.getSelectionModel().clearSelection();
    }

    private void seleccionarEquipoEnCombo(ComboBox<Equipo> combo, int equipoId) {
        for (Equipo e : DatosTorneo.equipos) {
            if (e.getId() == equipoId) {
                combo.setValue(e);
                return;
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}