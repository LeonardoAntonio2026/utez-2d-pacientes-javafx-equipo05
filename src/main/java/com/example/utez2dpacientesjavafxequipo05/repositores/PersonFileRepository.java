package com.example.utez2dpacientesjavafxequipo05.repositores;

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

    public void appendAllLines(List<String> lines) throws IOException{
        Files.write(filePath,lines,
                StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING);