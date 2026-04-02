package com.example.utez2dpacientesjavafxequipo05.models;

public class Paciente {
    private String curp;
    private String nombre;
    private int edad;
    private String telefono;
    private String alergias;
    private boolean activo;

    public Paciente(String curp, String nombre, int edad, String telefono, String alergias, boolean activo) {
        this.curp = curp;
        this.nombre = nombre;
        this.edad = edad;
        this.telefono = telefono;
        this.alergias = alergias;
        this.activo = activo;
    }

    // Getters y Setters
    public String getCurp() { return curp; }
    public void setCurp(String curp) { this.curp = curp; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String toCSV() {
        return curp + "," + nombre + "," + edad + "," + telefono + "," + alergias + "," + (activo ? "1" : "0");
    }

    public static Paciente fromCSV(String line) {
        String[] parts = line.split(",", -1);
        return new Paciente(
                parts[0].trim(),
                parts[1].trim(),
                Integer.parseInt(parts[2].trim()),
                parts[3].trim(),
                parts[4].trim(),
                parts[5].trim().equals("1")
        );
    }
}