package com.example.utez2dpacientesjavafxequipo05.services;

import com.example.utez2dpacientesjavafxequipo05.models.Paciente;
import com.example.utez2dpacientesjavafxequipo05.repositores.PersonFileRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersonService {

    private final PersonFileRepository repo = new PersonFileRepository();

    public List<Paciente> loadDataForList() throws IOException {
        return getAllCleanLines();
    }

    public List<Paciente> loadDataForListSearch(String search) throws IOException {
        List<Paciente> result = new ArrayList<>();
        for (Paciente paciente : getAllCleanLines()) {
            if (paciente.getCurp().contains(search) || paciente.getNombre().contains(search))
                result.add(paciente);
        }
        return result;
    }

    public void addPerson(Paciente paciente) throws IOException {
        validatePerson(paciente);


        for (Paciente p : getAllCleanLines()) {
            if (p.getCurp().equalsIgnoreCase(paciente.getCurp()))
                throw new IllegalArgumentException("Ya existe un paciente con ese CURP");
        }

        String nameNoComa     = paciente.getNombre().replace(",", "");
        String curpNoComa     = paciente.getCurp().replace(",", "");
        String alergiasNoComa = paciente.getAlergias().replace(",", "");

        repo.appendNewLine(nameNoComa + "," + curpNoComa + "," + paciente.getEdad()
                + "," + paciente.getTelefono() + "," + alergiasNoComa + ","
                + (paciente.isActivo() ? "Activo" : "Inactivo"));
    }

    public void updatePerson(int index, String name, String curp, int edad,
                             String telefono, String alergias, boolean estatus) throws IOException {
        if (index < 0) throw new IllegalArgumentException("El índice recibido es inválido");

        validatePerson(new Paciente(curp, name, edad, telefono, alergias, estatus));

        List<Paciente> lines = getAllCleanLines();
        Paciente pacienteEditar = lines.get(index);
        pacienteEditar.setNombre(name);
        pacienteEditar.setCurp(curp);
        pacienteEditar.setEdad(edad);
        pacienteEditar.setTelefono(telefono);
        pacienteEditar.setAlergias(alergias);
        pacienteEditar.setActivo(estatus);
        lines.set(index, pacienteEditar);
        repo.appendAllLines(lines);
    }

    public void changeEstatus(int index) throws IOException {
        List<Paciente> lines = getAllCleanLines();
        if (index < 0 || index >= lines.size())
            throw new IllegalArgumentException("Índice inválido");
        Paciente p = lines.get(index);
        p.setActivo(!p.isActivo());
        repo.appendAllLines(lines);
    }

    public void deletePerson(int index) throws IOException {
        List<Paciente> lines = getAllCleanLines();
        if (index < 0 || index >= lines.size())
            throw new IllegalArgumentException("Índice inválido");
        lines.get(index).setActivo(false);
        repo.appendAllLines(lines);
    }

    public int countTotal() throws IOException       { return getAllCleanLines().size(); }
    public long countActivos() throws IOException    { return getAllCleanLines().stream().filter(Paciente::isActivo).count(); }
    public long countInactivos() throws IOException  { return getAllCleanLines().stream().filter(p -> !p.isActivo()).count(); }

    private List<Paciente> getAllCleanLines() throws IOException {
        List<String> lines = repo.readAllLines();
        List<Paciente> cleanLines = new ArrayList<>();
        for (String line : lines) {
            if (line == null || line.isBlank()) continue;
            String[] parts = line.split(",", -1);
            if (parts.length < 6) continue;
            String name     = parts[0].trim();
            String curp     = parts[1].trim();
            int edad        = Integer.parseInt(parts[2].trim());
            String telefono = parts[3].trim();
            String alergias = parts[4].trim();
            boolean estatus = parts[5].trim().equalsIgnoreCase("Activo");
            cleanLines.add(new Paciente(curp, name, edad, telefono, alergias, estatus));
        }
        return cleanLines;
    }

    public void validatePerson(Paciente paciente) {
        String name = paciente.getNombre() == null ? "" : paciente.getNombre().trim();
        if (name.isBlank() || name.length() < 5)
            throw new IllegalArgumentException("El nombre debe tener al menos 5 caracteres");

        String curp = paciente.getCurp() == null ? "" : paciente.getCurp().trim();
        if (curp.isBlank())
            throw new IllegalArgumentException("El CURP no puede estar vacío");

        int edad = paciente.getEdad();
        if (edad < 0 || edad > 120)
            throw new IllegalArgumentException("La edad debe estar entre 0 y 120");

        String tel = paciente.getTelefono() == null ? "" : paciente.getTelefono().trim();
        if (!tel.matches("\\d{10,}"))
            throw new IllegalArgumentException("El teléfono debe contener solo dígitos y mínimo 10");
    }
}
