package com.example.hanan_rafik_gestor;

import com.example.hanan_rafik_gestor.model.Productos;
import com.example.hanan_rafik_gestor.model.ProductosListCell;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InvitadoController implements Initializable{

    @FXML
    private TextField TextoFiltrar;

    @FXML
    private Button btnFiltrar, btnVolver;

    @FXML
    private ListView<Productos> listaProductos;

    private List<Productos> productos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        acciones();
        instancias();
        intGUI();
        cargarDatos();
    }

    private void acciones() {
        btnFiltrar.setOnAction(event -> filtrarProductos());
        btnVolver.setOnAction(event -> volver());
    }

    private void instancias() {
        productos = new ArrayList<>();
    }

    private void intGUI() {

            listaProductos.getItems().clear();
            listaProductos.setCellFactory(lv -> new ProductosListCell());

        Image imageVolver = new Image(getClass().getResourceAsStream("salir.png"));
        Image imageFiltrar = new Image(getClass().getResourceAsStream("filtrar.png"));

        ImageView imageViewVolver = new ImageView(imageVolver);
        imageViewVolver.setFitWidth(20);
        imageViewVolver.setFitHeight(20);

        ImageView imageViewFiltrar = new ImageView(imageFiltrar);
        imageViewFiltrar.setFitWidth(20);
        imageViewFiltrar.setFitHeight(20);

        btnVolver.setGraphic(imageViewVolver);
        btnFiltrar.setGraphic(imageViewFiltrar);



    }

    private void cargarDatos() {
        String apiUrl = "https://makeup-api.herokuapp.com/api/v1/products.json?brand=dior";


        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");


                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                Gson gson = new Gson();
                Type listType = new TypeToken<List<Productos>>() {}.getType();
                productos = gson.fromJson(response.toString(), listType);


                Platform.runLater(() -> {
                    listaProductos.getItems().addAll(productos);
                    System.out.println("Productos cargados: " + productos.size());
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void filtrarProductos() {
        String textoFiltro = TextoFiltrar.getText().toLowerCase();


        List<Productos> productosFiltrados = productos.stream()
                .filter(producto -> producto.getName().toLowerCase().contains(textoFiltro))
                .toList();


        listaProductos.getItems().clear();
        listaProductos.getItems().addAll(productosFiltrados);


        productosFiltrados.forEach(producto -> System.out.println("Producto filtrado: " + producto));
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
