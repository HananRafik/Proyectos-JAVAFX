package com.example.hanan_rafik_gestor;

import com.example.hanan_rafik_gestor.dao.UsuarioDao;
import com.example.hanan_rafik_gestor.model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class LoginController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    private RadioButton btnAdmin,btnInvitado,btnUsuario;

    @FXML
    private Button btnIniciarSession,btnRegistrar;

    @FXML
    private TextArea textoPass,textoCorreo;
    @FXML
    private ImageView imagenLogo;
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

    private void initGUI(){

        grupoRadios.getToggles().addAll(btnInvitado,btnAdmin,btnUsuario);
        imagenLogo.setImage(new Image(getClass().getResourceAsStream("maisonelegancelogo.png")));



    }

    private void acciones(){
        btnIniciarSession.setOnAction(this);
        btnRegistrar.setOnAction(this);

        // cuando cambiamos el radio, ajustamos el texto
        grupoRadios.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (((RadioButton) newValue).getText().equalsIgnoreCase("Usuario")){
                    textoCorreo.setEditable(true);
                    textoPass.setEditable(true);
                } else if (((RadioButton) newValue).getText().equalsIgnoreCase("Administrador")) {
                    textoPass.setEditable(true);
                    textoCorreo.setEditable(true);

                }else {
                    textoCorreo.setEditable(false);
                    textoPass.setEditable(false);
                }
            }
        });


    }



    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == btnRegistrar) {
            ventanaRegistrar();

        } else if (event.getSource() == btnIniciarSession) {
            iniciarSession();
        }



    }

    private void iniciarSession(){

        // si selecciona invitado abrimos la vista invitado
        if (btnInvitado.isSelected()){
            invitado();
            return;
        }

        if(textoCorreo.getText().isEmpty() || textoPass.getText().isEmpty()){
            mostrarAlerta("Error al iniciar sesion","completa los datos");
            return;
        }



        try {

            String correo = textoCorreo.getText();
            String pass = textoPass.getText();

            //buscar usuario en base de bados
            User usuario = usuarioDao.buscarUsuario(correo,pass);


            if (usuario != null){
                mostrarAlerta("Sesión iniciada"," Bienvenido " +usuario.getNombre());
                otrosPerfiles(usuario);
            }else {
            mostrarAlerta("Error de inicio sesión","Usuario no encontrado");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error de base de datos","Error al verificar el usuario ");
        }
    }

    private void invitado(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("invitado-view.fxml"));
        try {
            Scene sceneNueva = new Scene(loader.load(), 900, 700);
            Stage stageActual = (Stage) btnIniciarSession.getScene().getWindow();
            stageActual.setScene(sceneNueva);
            stageActual.setTitle("Invitado");

        } catch (IOException e) {
            mostrarAlerta("Error","Fallo al cargar la vista de invitado");
        }
    }

    private void otrosPerfiles(User usuario) {
        FXMLLoader loader;
        if (usuario.getId_perfil() == 1){
            loader = new FXMLLoader(getClass().getResource("admin-view.fxml"));

        } else{
            loader = new FXMLLoader(getClass().getResource("user-view.fxml"));

        }

        try {
            Scene sceneNueva = new Scene(loader.load(),900,700);
            Stage stageActual = (Stage) btnIniciarSession.getScene().getWindow();
            stageActual.setScene(sceneNueva);
            stageActual.setTitle("Maison Élégance");

        } catch (IOException e) {
            mostrarAlerta("Error","Error al cargar la siguiente vista");
        }


    }

    private void mostrarAlerta(String titulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void ventanaRegistrar(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("singin-view.fxml"));
            Scene sceneRegistro = new Scene(loader.load(),900,700);

            Stage stageActul = (Stage) btnRegistrar.getScene().getWindow();
            Stage stageRegisto= new Stage();
            stageRegisto.setTitle("Registrar");
            stageRegisto.setScene(sceneRegistro);
            stageRegisto.show();
            stageActul.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
