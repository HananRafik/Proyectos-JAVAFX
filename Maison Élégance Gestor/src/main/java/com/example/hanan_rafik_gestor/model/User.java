package com.example.hanan_rafik_gestor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User {

    private int id;
    private String nombre,apellido,correo,pass;
    private int id_perfil;


    public User(String nombre, String apellido, String correo, String pass, int id_perfil) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.pass = pass;
        this.id_perfil = id_perfil;
    }
}
