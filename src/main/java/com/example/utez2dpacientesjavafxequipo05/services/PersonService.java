package com.example.demolistview.services;

import com.example.demolistview.repositores.PersonFileRepository;
import com.sun.jdi.connect.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersonService {

    private PersonFileRepository repo = new PersonFileRepository();

    public List<String> loadDataForList() throws IOException {
        List<String> lines = repo.readAllLines();
        List<String> result = new ArrayList<>();
        for (String line : lines){
            if (line==null || line.isBlank())continue;

            String[] parts = line.split(",", -1);
            String name = parts[0].trim();
            String correo = parts[1].trim();
            String edad = parts[2].trim();

            result.add(name+"-"+correo+"-"+edad);
        }
        return result;

    }

    public List<String> loadDataForListSearch(String search) throws IOException {
        List<String> lines = repo.readAllLines();
        List<String> result = new ArrayList<>();
        for (String line : lines){
            if (line==null || line.isBlank())continue;


            String[] parts = line.split(",", -1);
            String correo = parts[1].trim();
            if (!correo.contains(search)) continue;
            String name = parts[0].trim();

            String edad = parts[2].trim();

            result.add(name+"-"+correo+"-"+edad);
        }
        return result;

    }

    public void addPerson(String name, String email, String age) throws IOException {
        validatePerson(name,email,age);
        String nameNoComa = name.replace(",","");
        String emailNoComa = email.replace(",","");
        repo.appendNewLine(nameNoComa+","+emailNoComa+","+age);
    }

    public void updatePerson(int index, String name, String email, String age) throws IOException {
        List<String> lines = getAllCleanLines();
        if(index == -1){
            throw new IllegalArgumentException("El indice recibido es invalido");

        }
        lines.set(index,name+","+email+","+ age);
        repo.appendAllLines(lines);
    }

    private List<String> getAllCleanLines() throws IOException{
        List<String> lines = repo.readAllLines();
        List<String> cleanLines = new ArrayList<>();
        for (String line : lines){
            if(line!=null && !line.isBlank()){
                cleanLines.add(line);
            }
        }

        return cleanLines;
    }
    public void validatePerson(String name, String email, String age){
        if(name.isBlank() || name.length() < 3){
            throw new IllegalArgumentException("El nombre no cumple con los estanderes");
        }

        String em = (email==null) ? "" : email.trim();
        if(em.isBlank() || !em.contains("@") || !em.contains(".")){
            throw new IllegalArgumentException("El correo es incorrecto");
        }

        String a = (age==null) ? "" : age.trim();
        if(a.isBlank() || !a.matches("\\d+")){
            throw new IllegalArgumentException("La edad debe ser un valor numerico");
        }
        int edadNum = Integer.parseInt(a);
        if(edadNum < 18){
            throw new IllegalArgumentException("Solo se aceptan mayores de edad (>=18)");
        }
    }

    public void deletePerson(int index) throws IOException {
        List<String> lines = getAllCleanLines();
        lines.remove(index);
        repo.appendAllLines(lines);

    }
}
