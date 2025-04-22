package com.example.hanan_rafik_gestor.dao;

import com.example.hanan_rafik_gestor.database.DBConnection;
import com.example.hanan_rafik_gestor.model.Productos;
import com.example.hanan_rafik_gestor.model.User;
import com.example.hanan_rafik_gestor.utils.ShemaDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

   private  Connection connection;
   private Statement statement;
   private PreparedStatement preparedStatement;
   private ResultSet resultSet;



   //para conectar a la base de datos
   public UsuarioDao(){
       connection = new DBConnection().getConnection();
   }


   //agregar usaurio a base de datos
    public boolean insertarUsuario(User usuario) throws SQLException {

        String query = "INSERT INTO %s (%s,%s,%s,%s,%s) VALUES (?,?,?,?,?)";

        String queryFormateado = String.format(query,
                ShemaDB.TAB_name,
                ShemaDB.COL_nombre,ShemaDB.Col_apellido, ShemaDB.Col_correo, ShemaDB.Col_pass, ShemaDB.Col_idperfil);

        preparedStatement = connection.prepareStatement(queryFormateado);
        preparedStatement.setString(1,usuario.getNombre());
        preparedStatement.setString(2,usuario.getApellido());
        preparedStatement.setString(3,usuario.getCorreo());
        preparedStatement.setString(4,usuario.getPass());
        preparedStatement.setInt(5,usuario.getId_perfil());


        //aqui donde se guarda el usuario
        int resultado = preparedStatement.executeUpdate();

        //hacemos un retur setodo salio bien
        return resultado > 0;
    }


    //para buscar el usuario en la base de datos con el correo y contrasena
    public User buscarUsuario(String correo, String pass) throws SQLException {

       //contsultamos se el usauario coincida con correo y contrasena
        String query = String.format(
                "SELECT * FROM %s WHERE %s = ? AND %s = ?",
                ShemaDB.TAB_name,
                ShemaDB.Col_correo,
                ShemaDB.Col_pass
        );

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, correo);
        preparedStatement.setString(2, pass);

        //obtenimos los resultados
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return new User(
                    resultSet.getInt(ShemaDB.COL_id),
                    resultSet.getString(ShemaDB.COL_nombre),
                    resultSet.getString(ShemaDB.Col_apellido),
                    resultSet.getString(ShemaDB.Col_correo),
                    resultSet.getString(ShemaDB.Col_pass),
                    resultSet.getInt(ShemaDB.Col_idperfil)
            );
        }

        return null;
    }


    //filtrar usuauarios por apellido
    public List<User> filtrarUsuario(String apellido) throws SQLException {
       String query = String.format("SELECT * FROM %s = ?",
               ShemaDB.TAB_name, ShemaDB.Col_apellido);



       preparedStatement = connection.prepareStatement(query);
       preparedStatement.setString(1, apellido);

       resultSet = preparedStatement.executeQuery();

       //lista donde guardaremostodos los usuarios encontrados
        List<User> usuarios = new ArrayList<>();


        //recorremos los resultado
        while (resultSet.next()){
           usuarios.add(new User(
                   resultSet.getInt(ShemaDB.COL_id),
                   resultSet.getString(ShemaDB.COL_nombre),
                   resultSet.getString(ShemaDB.Col_apellido),
                   resultSet.getString(ShemaDB.Col_correo),
                   resultSet.getString(ShemaDB.Col_pass),
                   resultSet.getInt(ShemaDB.Col_idperfil)
           ));

       }
        //retorna la lista de usuarios
       return usuarios;
    }



    //obtener tosdos los usuarios de la base de datos
    public List<User> getUsuarios() throws SQLException {


        String query = String.format("SELECT * FROM %s",ShemaDB.TAB_name);
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();


        List<User> usuarios = new ArrayList<>();

        while (resultSet.next()){
            usuarios.add(new User(
                    resultSet.getInt(ShemaDB.COL_id),
                    resultSet.getString(ShemaDB.COL_nombre),
                    resultSet.getString(ShemaDB.Col_apellido),
                    resultSet.getString(ShemaDB.Col_correo),
                    resultSet.getString(ShemaDB.Col_pass),
                    resultSet.getInt(ShemaDB.Col_idperfil)

            ));

        }

        return usuarios;

    }

    //eliminar Usuario por id
    public boolean eliminarUsuario(int id) throws SQLException {
        String query = String.format(
                "DELETE FROM %s WHERE %s = ?",
                ShemaDB.TAB_name,
                ShemaDB.COL_id
        );
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,id);

        //ejetamsos y verificamos si borro algo
        return preparedStatement.executeUpdate() > 0 ;
    }





}

