package com.example.hanan_rafik_gestor;

import com.example.hanan_rafik_gestor.dao.UsuarioDao;
import com.example.hanan_rafik_gestor.model.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SigninController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    private Button btnRegistrar,btnVolver;
    @FXML
    private TextArea textoNombre,textoApellido,textoCorreo,textoPass;
    @FXML
    private ImageView imagenLogo;
    @FXML
    private RadioButton btnAdmin,btnUsuario;
    private ToggleGroup grupoRadios;
    private UsuarioDao usuarioDao;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instancias();
        initGUI();
        acciones();
    }



    private void instancias(){
    grupoRadios = new ToggleGroup();
    usuarioDao = new UsuarioDao();
    }

    private void acciones(){
        btnRegistrar.setOnAction(this);
        btnVolver.setOnAction(this);

    }

    private void initGUI(){
        grupoRadios.getToggles().addAll(btnAdmin,btnUsuario);
      imagenLogo.setImage(new Image(getClass().getResourceAsStream("maisonelegancelogo.png")));
    }

    @Override
    public void handle(ActionEvent event) {

        if (event.getSource() == btnVolver){
            volver();

        } else if (event.getSource() == btnRegistrar) {
            try {
                registrarUsuario();
            } catch (SQLException e) {
                mostrarAlerta("Error al registrar","No se ha podido guardar los datos en base de datos");
            }

        }
    }

    private void registrarUsuario() throws SQLException {

        // verificar que no hay cambos vacios
        if (textoNombre.getText().isEmpty() || textoApellido.getText().isEmpty() ||
                textoCorreo.getText().isEmpty() || textoPass.getText().isEmpty() ||
                grupoRadios.getSelectedToggle() == null){
            mostrarAlerta("Campos vacios","Completa los campos");
            return;

        }

        int idPerfil = grupoRadios.getSelectedToggle() == btnAdmin? 1 :2;

        //creamos un usuario nuevo
        User nuevoUsuario = new User(
                textoNombre.getText(),
                textoApellido.getText(),
                textoCorreo.getText(),
                textoPass.getText(),
                idPerfil
        );

        // intentamos guardar el usuario en base de datos
        boolean resultado = usuarioDao.insertarUsuario(nuevoUsuario);

        if (!resultado){
            mostrarAlerta("Registro exitoso","El usuario se ha resgistrado correctamente");
            limpiar();

        }else {
            mostrarAlerta("Error al registrarse","Un problema al guardar el usuario");
        }


    }

    private void limpiar(){
        textoNombre.clear();
        textoCorreo.clear();
        textoApellido.clear();
        textoPass.clear();
        grupoRadios.selectToggle(null);
    }

    private void mostrarAlerta(String titulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void volver(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene sceneAnterior = new Scene(loader.load(),900,700);
            Stage stageActual = (Stage) btnVolver.getScene().getWindow();
            stageActual.setScene(sceneAnterior);
            stageActual.setTitle("Iniciar Sesión");




        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
