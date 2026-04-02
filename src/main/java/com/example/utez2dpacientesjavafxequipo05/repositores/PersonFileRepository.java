package com.example.utez2dpacientesjavafxequipo05.repositores;

import com.example.utez2dpacientesjavafxequipo05.models.Paciente;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class PersonFileRepository {
    private final Path filePath = Paths.get("data", "persons.csv");

    private void ensureFileExist() throws IOException {
        if(Files.notExists(filePath)){
            Files.createFile(filePath);
        }
    }

    public List<String> readAllLines() throws IOException {
        ensureFileExist();
        return Files.readAllLines(filePath, StandardCharsets.UTF_8);
    }

    public void appendNewLine(String line) throws IOException {

        boolean hasContent = Files.exists(filePath) && Files.size(filePath) > 0;

        String text = hasContent ? System.lineSeparator() + line : line;

        Files.writeString(filePath, text,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }


    public void appendAllLines(List<Paciente> pacientes) throws IOException {
        Path path = filePath;
        //Se utiliza BufferedWriter para escribir por cada elemento de pacientes. https://www.w3schools.com/JAVA/java_bufferedwriter.asp
        try (BufferedWriter writer = Files.newBufferedWriter(path,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE)) {
            for (Paciente p : pacientes) {
                String nameNoComa  = p.getNombre().replace(",", "");
                String curpNoComa  = p.getCurp().replace(",", "");
                String alergiasNoComa = p.getAlergias().replace(",", "");
                writer.write(nameNoComa + "," + curpNoComa + "," + p.getEdad()
                        + "," + p.getTelefono() + "," + alergiasNoComa + ","
                        + (p.isActivo() ? "Activo" : "Inactivo"));
                writer.newLine();
            }
        }
    }


}