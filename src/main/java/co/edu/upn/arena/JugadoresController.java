package co.edu.upn.arena;

import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class JugadoresController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtAlias;
    @FXML private TextField txtCorreo;

    @FXML private TableView<Jugador> tablaJugadores;
    @FXML private TableColumn<Jugador, Integer> colId;
    @FXML private TableColumn<Jugador, String> colNombre;
    @FXML private TableColumn<Jugador, String> colAlias;
    @FXML private TableColumn<Jugador, String> colCorreo;
    @FXML private TableColumn<Jugador, String> colEquipo;

    private Jugador seleccionado = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colEquipo.setCellValueFactory(celda -> {
            int idEq = celda.getValue().getEquipoId();
            return new SimpleStringProperty(DatosTorneo.nombreEquipoPorId(idEq));
        });

        tablaJugadores.setItems(DatosTorneo.jugadores);

        tablaJugadores.getSelectionModel().selectedItemProperty().addListener((obs, ant, nuevo) -> {
            seleccionado = nuevo;
            if (nuevo != null) {
                txtNombre.setText(nuevo.getNombre());
                txtAlias.setText(nuevo.getAlias());
                txtCorreo.setText(nuevo.getCorreo());
            }
        });
    }

    @FXML
    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String alias = txtAlias.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombre.isEmpty() || alias.isEmpty() || correo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Completa nombre, alias y correo");
            return;
        }

        if (!correo.contains("@")) {
            mostrarAlerta(Alert.AlertType.WARNING, "Correo inválido", "El correo debe tener @");
            return;
        }

        if (seleccionado == null) {
            int nuevoId = DatosTorneo.generarJugadorId();
            DatosTorneo.jugadores.add(new Jugador(nuevoId, nombre, alias, correo, 0));
            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso", "Jugador registrado");
        } else {
            seleccionado.setNombre(nombre);
            seleccionado.setAlias(alias);
            seleccionado.setCorreo(correo);
            tablaJugadores.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Actualización exitosa", "Jugador actualizado");
        }
        limpiar();
    }

    @FXML
    private void eliminar() {
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin selección", "Selecciona un jugador de la tabla para eliminarlo.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Eliminar al soldado " + seleccionado.getAlias() + "?");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            DatosTorneo.jugadores.remove(seleccionado);
            limpiar();
        }
    }

    @FXML
    private void limpiar() {
        seleccionado = null;
        txtNombre.clear();
        txtAlias.clear();
        txtCorreo.clear();
        tablaJugadores.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}