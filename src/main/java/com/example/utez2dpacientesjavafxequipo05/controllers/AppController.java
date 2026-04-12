package com.example.utez2dpacientesjavafxequipo05.controllers;

import com.example.utez2dpacientesjavafxequipo05.models.Paciente;
import com.example.utez2dpacientesjavafxequipo05.services.PersonService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AppController {

    @FXML private TableView<Paciente> tablePacientes;
    @FXML private TableColumn<Paciente, String>  colNombre;
    @FXML private TableColumn<Paciente, String>  colCurp;
    @FXML private TableColumn<Paciente, Integer> colEdad;
    @FXML private TableColumn<Paciente, String>  colTelefono;
    @FXML private TableColumn<Paciente, String>  colAlergias;
    @FXML private TableColumn<Paciente, String>  colEstatus;
    @FXML private Label lblMsg;
    @FXML private Label lblResumen;
    @FXML private TextField searchBar;

    private final ObservableList<Paciente> data = FXCollections.observableArrayList();
    private final PersonService service = new PersonService();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory( cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getNombre()));
        colCurp.setCellValueFactory( cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCurp()));
        colEdad.setCellValueFactory( cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEdad()).asObject());
        colTelefono.setCellValueFactory( cellData ->new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getTelefono()));
        colAlergias.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getAlergias()));
        colEstatus.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().isActivo() ? "Activo" : "Inactivo"));

        tablePacientes.setItems(data);

        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isBlank()) {
                loadFromFile();
            } else {
                loadFromFileSearch(newVal.trim());
            }
        });

        loadFromFile();
    }

    @FXML
    public void onAddPerson() {
        abrirFormulario(null);
    }

    @FXML
    public void onUpdate() {
        Paciente seleccionado = tablePacientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Selecciona un paciente para editar");
            return;
        }
        abrirFormulario(seleccionado);
    }

    @FXML
    public void onChangeEstatus() {
        int index = tablePacientes.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            mostrarError("Selecciona un paciente para cambiar su estatus");
            return;
        }
        try {
            service.changeEstatus(index);
            loadFromFile();
            mostrarExito("Estatus actualizado");
        } catch (IOException | IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void onDelete() {
        int index = tablePacientes.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            mostrarError("Selecciona un paciente para eliminar");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar inactivación");
        confirm.setHeaderText("¿Estás seguro de inactivar este paciente?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            service.deletePerson(index);
            loadFromFile();
            mostrarExito("Paciente inactivado exitosamente");
        } catch (IOException | IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void loadFromFile() {
        try {
            List<Paciente> items = service.loadDataForList();
            data.setAll(items);
            actualizarResumen();
            mostrarExito("Datos cargados exitosamente");
        } catch (IOException e) {
            mostrarError(e.getMessage());
        }
    }

    public void refreshTable() {
        loadFromFile();
    }



    private void loadFromFileSearch(String search) {
        try {
            List<Paciente> items = service.loadDataForListSearch(search);
            data.setAll(items);
        } catch (IOException e) {
            mostrarError(e.getMessage());
        }
    }

    private void actualizarResumen() {
        try {
            int total       = service.countTotal();
            long activos    = service.countActivos();
            long inactivos  = service.countInactivos();
            lblResumen.setText("Total: " + total + " | Activos: " + activos + " | Inactivos: " + inactivos);
        } catch (IOException e) {
            lblResumen.setText("Error al calcular resumen");
        }
    }

    private void abrirFormulario(Paciente paciente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/utez2dpacientesjavafxequipo05/views/formulario.fxml"));
            Parent root = loader.load();

            FormularioController formCtrl = loader.getController();
            formCtrl.setAppController(this);
            formCtrl.setPaciente(paciente); // null = nuevo, objeto = editar

            Stage stage = new Stage();
            stage.setTitle(paciente == null ? "Nuevo Paciente" : "Editar Paciente");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            mostrarError("No se pudo abrir el formulario: " + e.getMessage());
        }
    }

    private void mostrarExito(String msg) {
        lblMsg.setText(msg);
        lblMsg.setStyle("-fx-text-fill: green");
    }

    private void mostrarError(String msg) {
        lblMsg.setText(msg);
        lblMsg.setStyle("-fx-text-fill: red");
    }
}
