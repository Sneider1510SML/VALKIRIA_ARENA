package co.edu.upn.arena;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EquiposController {

    @FXML private TextField txtNombreEquipo;
    @FXML private TextField txtJuego;
    @FXML private TableView<Equipo> tablaEquipos;
    @FXML private TableColumn<Equipo, Integer> colId;
    @FXML private TableColumn<Equipo, String> colNombre;
    @FXML private TableColumn<Equipo, String> colJuego;

    @FXML private Label lblEquipoActivo;
    @FXML private ComboBox<Jugador> cmbJugadoresLibres;
    @FXML private ListView<Jugador> listaIntegrantes;

    private Equipo equipoSeleccionado = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colJuego.setCellValueFactory(new PropertyValueFactory<>("juego"));

        tablaEquipos.setItems(DatosTorneo.equipos);

        tablaEquipos.getSelectionModel().selectedItemProperty().addListener((obs, ant, nuevo) -> {
            equipoSeleccionado = nuevo;
            if (nuevo != null) {
                txtNombreEquipo.setText(nuevo.getNombre());
                txtJuego.setText(nuevo.getJuego());
                lblEquipoActivo.setText("Escuadrón: " + nuevo.getNombre());
            } else {
                lblEquipoActivo.setText("Selecciona un escuadrón en la tabla");
            }
            actualizarListasIntegrantes();
        });

        actualizarListasIntegrantes();
    }

    @FXML
    private void guardarEquipo() {
        String nombre = txtNombreEquipo.getText().trim();
        String juego = txtJuego.getText().trim();

        if (nombre.isEmpty() || juego.isEmpty()) {
            mostrarAlerta("Campos incompletos", "Ingresa el nombre del escuadrón y el juego.");
            return;
        }

        if (equipoSeleccionado == null) {
            int nuevoId = DatosTorneo.generarEquipoId();
            DatosTorneo.equipos.add(new Equipo(nuevoId, nombre, juego));
        } else {
            equipoSeleccionado.setNombre(nombre);
            equipoSeleccionado.setJuego(juego);
            tablaEquipos.refresh();
        }
        limpiar();
    }

    @FXML
    private void eliminarEquipo() {
        if (equipoSeleccionado == null) {
            mostrarAlerta("Sin selección", "Selecciona un escuadrón de la tabla para eliminarlo.");
            return;
        }
        int idEliminado = equipoSeleccionado.getId();
        for (Jugador j : DatosTorneo.jugadores) {
            if (j.getEquipoId() == idEliminado) {
                j.setEquipoId(0);
            }
        }
        DatosTorneo.equipos.remove(equipoSeleccionado);
        limpiar();
    }

    @FXML
    private void agregarJugador() {
        if (equipoSeleccionado == null) {
            mostrarAlerta("Sin escuadrón", "Primero selecciona un escuadrón en la tabla.");
            return;
        }
        Jugador elegido = cmbJugadoresLibres.getValue();
        if (elegido == null) {
            mostrarAlerta("Sin soldado", "Selecciona un soldado disponible en la lista desplegable.");
            return;
        }
        elegido.setEquipoId(equipoSeleccionado.getId());
        actualizarListasIntegrantes();
    }

    @FXML
    private void quitarJugador() {
        Jugador integrante = listaIntegrantes.getSelectionModel().getSelectedItem();
        if (integrante == null) {
            mostrarAlerta("Sin selección", "Selecciona un integrante de la lista para retirarlo.");
            return;
        }
        integrante.setEquipoId(0);
        actualizarListasIntegrantes();
    }

    @FXML
    private void limpiar() {
        equipoSeleccionado = null;
        txtNombreEquipo.clear();
        txtJuego.clear();
        tablaEquipos.getSelectionModel().clearSelection();
        actualizarListasIntegrantes();
    }

    private void actualizarListasIntegrantes() {
        ObservableList<Jugador> libres = FXCollections.observableArrayList();
        ObservableList<Jugador> miembros = FXCollections.observableArrayList();

        for (Jugador j : DatosTorneo.jugadores) {
            if (j.getEquipoId() == 0) {
                libres.add(j);
            } else if (equipoSeleccionado != null && j.getEquipoId() == equipoSeleccionado.getId()) {
                miembros.add(j);
            }
        }
        cmbJugadoresLibres.setItems(libres);
        listaIntegrantes.setItems(miembros);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}