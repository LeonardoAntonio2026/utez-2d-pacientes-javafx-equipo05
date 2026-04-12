module com.example.utez2dpacientesjavafxequipo05 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jdi;
    requires java.desktop;


    opens com.example.utez2dpacientesjavafxequipo05 to javafx.fxml;
    opens com.example.utez2dpacientesjavafxequipo05.controllers to javafx.fxml;
    opens com.example.utez2dpacientesjavafxequipo05.repositores to javafx.fxml;
    opens com.example.utez2dpacientesjavafxequipo05.services to javafx.fxml;
    exports com.example.utez2dpacientesjavafxequipo05;
    exports com.example.utez2dpacientesjavafxequipo05.controllers;
    exports com.example.utez2dpacientesjavafxequipo05.services;
    exports com.example.utez2dpacientesjavafxequipo05.repositores;


}