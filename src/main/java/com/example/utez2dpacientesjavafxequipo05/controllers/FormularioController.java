package com.example.utez2dpacientesjavafxequipo05.controllers;

import com.example.utez2dpacientesjavafxequipo05.models.Paciente;
import com.example.utez2dpacientesjavafxequipo05.services.PersonService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FormularioController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCurp;
    @FXML private TextField txtEdad;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtAlergias;
    @FXML private CheckBox  chkEstatus;
    @FXML private Label     lblError;

    private final PersonService service = new PersonService();
    private AppController appController;
    private Paciente pacienteEditar;   // null = modo alta
    private int indexEditar = -1;

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    // Si paciente != null, precarga el formulario para edición
    public void setPaciente(Paciente paciente) throws IOException {
        this.pacienteEditar = paciente;
        if (paciente != null) {
            txtNombre.setText(paciente.getNombre());
            txtCurp.setText(paciente.getCurp());
            txtEdad.setText(String.valueOf(paciente.getEdad()));
            txtTelefono.setText(paciente.getTelefono());
            txtAlergias.setText(paciente.getAlergias());
            chkEstatus.setSelected(paciente.isActivo());
            // Buscar índice real en el archivo
            List<Paciente> todos = service.loadDataForList();
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).getCurp().equalsIgnoreCase(paciente.getCurp())) {
                    indexEditar = i;
                    break;
                }
            }
        } else {
            chkEstatus.setSelected(true); 
        }
    }

    @FXML
    public void onGuardar() {
        String nombre   = txtNombre.getText().trim();
        String curp     = txtCurp.getText().trim();
        String edadStr  = txtEdad.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String alergias = txtAlergias.getText().trim();
        boolean activo  = chkEstatus.isSelected();

        try {
            int edad = Integer.parseInt(edadStr);
            Paciente p = new Paciente(curp, nombre, edad, telefono, alergias, activo);

            for (Paciente existente : service.loadDataForList()) {
                if (existente.getCurp().equalsIgnoreCase(curp) && (pacienteEditar == null || !existente.getCurp().equalsIgnoreCase(pacienteEditar.getCurp()))) {
                    throw new IllegalArgumentException("Ya existe un paciente con ese CURP");
                }
            }

            if (pacienteEditar == null) {
                service.addPerson(p);
            } else {
                service.updatePerson(indexEditar, nombre, curp, edad, telefono, alergias, activo);
            }

            if (appController != null) {
                appController.refreshTable();
            }
            cerrarVentana();

        } catch (NumberFormatException e) {
            lblError.setText("La edad debe ser un número");
        } catch (IllegalArgumentException e) {
            lblError.setText(e.getMessage());
        } catch (IOException e) {
            lblError.setText("Error de archivo: " + e.getMessage());
        }
    }

    @FXML
    public void onCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}
