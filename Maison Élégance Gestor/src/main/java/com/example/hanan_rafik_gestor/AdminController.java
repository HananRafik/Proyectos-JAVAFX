package com.example.hanan_rafik_gestor;

import com.example.hanan_rafik_gestor.dao.UsuarioDao;
import com.example.hanan_rafik_gestor.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import java.util.ResourceBundle;

public class AdminController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    private Button btnAniader;

    @FXML
    private Button btnEliminar;


    @FXML
    private Button btnSalir;

    @FXML
    private TableColumn<User, String> colApellido;

    @FXML
    private TableColumn<User, String> colCorreo;

    @FXML
    private TableColumn<User, Integer> colId;

    @FXML
    private TableColumn<User, Integer> colIdPerfil;

    @FXML
    private TableColumn<User, String> colNombre;

    @FXML
    private TableColumn<User, String> colpass;

    @FXML
    private TableView<User> tabUsuarios;


    private UsuarioDao usuarioDao;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initGUI();
        acciones();
        instancias();
        actualizarTabla();

    }

    private void acciones(){
        btnAniader.setOnAction(this);
        btnEliminar.setOnAction(this);
        btnSalir.setOnAction(this);


    }

    private void instancias(){
        usuarioDao = new UsuarioDao();


    }

    private void initGUI(){

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colpass.setCellValueFactory(new PropertyValueFactory<>("pass"));
        colIdPerfil.setCellValueFactory(new PropertyValueFactory<>("id_perfil"));



        Image imageAniadir = new Image(getClass().getResourceAsStream("aniader.png"));
        Image imageComprar = new Image(getClass().getResourceAsStream("eliminar.png"));
        Image imageSalir = new Image(getClass().getResourceAsStream("salir.png"));


        ImageView imageViewAniadir = new ImageView(imageAniadir);
        imageViewAniadir.setFitWidth(20);
        imageViewAniadir.setFitHeight(20);

        ImageView imageViewEliminar = new ImageView(imageComprar);
        imageViewEliminar.setFitWidth(20);
        imageViewEliminar.setFitHeight(20);

        ImageView imageViewSalir = new ImageView(imageSalir);
        imageViewSalir.setFitWidth(20);
        imageViewSalir.setFitHeight(20);


        btnAniader.setGraphic(imageViewAniadir);
        btnEliminar.setGraphic(imageViewEliminar);
        btnSalir.setGraphic(imageViewSalir);
    }


    @Override
    public void handle(ActionEvent event) {

        if (event.getSource() == btnAniader){
            aniaderUsuario();

        } else if (event.getSource()==btnEliminar) {
            eliminarUsuario();

        } else if (event.getSource() == btnSalir) {
            salir();

        }

    }










    private void eliminarUsuario(){
        //borrar el usuario que estas seleccionado en la tabla
        User usuarioSeleccionado = tabUsuarios.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado == null){
            mostrarAlerta("Error","Selecciona un usuario para eliminar");
        }

        try {
            // lo eliminamos de base de datos
            boolean eliminado = usuarioDao.eliminarUsuario(usuarioSeleccionado.getId());

            if (eliminado){
                mostrarAlerta("Eliminado","Usuario Eliminado correctamente");

                //actualizar base de datos
                actualizarTabla();
            }else {
                mostrarAlerta("Error","Error al eliminar el usaurio");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }






    private void aniaderUsuario(){
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Añadir Usuario");
        dialog.setHeaderText("Introduce los datos del nuevo usuario:");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);


        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre");
        TextField apellidoField = new TextField();
        apellidoField.setPromptText("Apellido");
        TextField correoField = new TextField();
        correoField.setPromptText("Correo");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Contraseña");
        TextField idPerfilField = new TextField();
        idPerfilField.setPromptText("ID Perfil");

        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(10));
        contenido.getChildren().addAll(
                new Label("Nombre:"), nombreField,
                new Label("Apellido:"), apellidoField,
                new Label("Correo:"), correoField,
                new Label("Contraseña:"), passField,
                new Label("ID Perfil:"), idPerfilField
        );

        dialog.getDialogPane().setContent(contenido);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    String nombre = nombreField.getText();
                    String apellido = apellidoField.getText();
                    String correo = correoField.getText();
                    String pass = passField.getText();
                    int idPerfil = Integer.parseInt(idPerfilField.getText());


                    User nuevoUsuario = new User(nombre, apellido, correo, pass, idPerfil);
                    boolean guardado = usuarioDao.insertarUsuario(nuevoUsuario);

                    if (guardado) {
                        mostrarAlerta("Éxito", "Usuario añadido correctamente.");
                        actualizarTabla();
                    } else {
                        mostrarAlerta("Error", "No se pudo añadir el usuario.");
                    }


                } catch (NumberFormatException e) {

                } catch (Exception e) {

                }
            }
            return null;
        });

        dialog.showAndWait();

    }

    private void actualizarTabla() {
        try {
            List<User> usuariosActualizados = usuarioDao.getUsuarios();


            ObservableList<User> listaUsuarios = FXCollections.observableArrayList(usuariosActualizados);


            tabUsuarios.getItems().clear();


            tabUsuarios.setItems(listaUsuarios);
        } catch (SQLException e) {

            mostrarAlerta("Error", "Error al actualizar la tabla: " + e.getMessage());
        }
    }



    private void mostrarAlerta(String titulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void salir(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene sceneAnterior = new Scene(loader.load(),900,700);
            Stage stageActual = (Stage) btnSalir.getScene().getWindow();
            stageActual.setScene(sceneAnterior);
            stageActual.setTitle("Iniciar Sesión");




        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
