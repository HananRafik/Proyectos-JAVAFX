package com.example.hanan_rafik_gestor.database;

import com.example.hanan_rafik_gestor.utils.ShemaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;


    public Connection getConnection (){
        if (connection ==null){
            createConnection();
        }
        return connection;
    }

    private void createConnection(){
        String  urlSring = "jdbc:mysql://%s:%d/%s";
        try {
            connection = DriverManager.getConnection(String.format(urlSring,"localhost",3307, ShemaDB.DB_name),"root","");
            System.out.println("Connexion establicida correctamente");
        } catch (SQLException e) {
            System.out.println("Error al conectarse con mysql");
        }

    }






}
