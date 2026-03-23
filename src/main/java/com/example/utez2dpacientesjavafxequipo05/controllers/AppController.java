package com.example.demolistview.controllers;

import com.example.demolistview.services.PersonService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class AppController {
    @FXML
    private ListView<String> listView;
    @FXML
    private Label lblMsg;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtAge;
    @FXML
    private TextField searchBar;

    private final ObservableList<String> data = FXCollections.observableArrayList();

    private PersonService service = new PersonService();

    @FXML
    public void initialize(){ //Se ejecuta al inicio en cuanto se cargue el controller
        //Inicializar ListView

        loadFromFile();

        listView.getSelectionModel().selectedItemProperty().addListener( (obs,old,newValue)->{
                loadDataToForm(newValue);
                }

        );

        searchBar.textProperty().addListener((observable, oldValue, Search) -> {
            System.out.println("Text field changed from " + oldValue + " to " + Search);
            loadFromFileSearch(Search);

        });

        listView.setItems(data);
    }

    @FXML
    public void onAddPerson() throws IOException {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String age = txtAge.getText();

        try {
            service.addPerson(name,email,age);
            loadFromFile();
            lblMsg.setText("Datos cargados Exitosamente ");
            lblMsg.setStyle("-fx-text-fill: green");
            txtName.clear();
            txtEmail.clear();
            txtAge.clear();
        }catch (IOException e){
            lblMsg.setText("Hubo un error con el archivo");
            lblMsg.setStyle("-fx-text-fill: red");

        }catch (IllegalArgumentException ex){
            lblMsg.setText("Hubo un error con los datos:" + ex.getMessage());
            lblMsg.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    public void OnUpdate(){
        int index = listView.getSelectionModel().getSelectedIndex();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String age = txtAge.getText();

        try {
            service.updatePerson(index,name,email,age);
            lblMsg.setText("Actualizacion correcta");
            lblMsg.setStyle("-fx-text-fill: green");
            txtName.clear();
            txtEmail.clear();
            txtAge.clear();
            loadFromFile();
        } catch (IOException ex) {

            lblMsg.setText(ex.getMessage());
            lblMsg.setStyle("-fx-text-fill: red");
        } catch (IllegalArgumentException e){
            lblMsg.setText(e.getMessage());
            lblMsg.setStyle("-fx-text-fill: red");
        }


    }

    @FXML
    private void OnDelete(){
        int index = listView.getSelectionModel().getSelectedIndex();
        try {
            service.deletePerson(index);
            loadFromFile();
            lblMsg.setText("Persona eliminada exitosamente");
            lblMsg.setStyle("-fx-text-fill: green");
            txtName.clear();
            txtEmail.clear();
            txtAge.clear();
        } catch (IOException e) {
            lblMsg.setText(e.getMessage());
            lblMsg.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    private void loadFromFile(){
        try{
            List<String> items = service.loadDataForList();
            data.setAll(items);
            lblMsg.setText("Datos cargados Exitosamente ");
            lblMsg.setStyle("-fx-text-fill: green");
        }catch (IOException e){
            lblMsg.setText(e.getMessage());
            lblMsg.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    private void loadFromFileSearch(String search){
        try{
            List<String> items = service.loadDataForListSearch(search);
            data.setAll(items);

        }catch (IOException e){
            lblMsg.setText(e.getMessage());
            lblMsg.setStyle("-fx-text-fill: red");
        }
    }

    private void loadDataToForm(String item){

        String[] parts = item.split("-");
        txtName.setText(parts[0]);
        txtEmail.setText(parts[1]);
        txtAge.setText(parts[2]);

    }




}
