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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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

public class UserController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    private TextField TextoFiltrar;

    @FXML
    private Button btnFiltrar;

    @FXML
    private ListView<Productos> listaProductos;



    @FXML
    private Button btnAniader;

    @FXML
    private Button btnComprar;



    @FXML
    private Button btnSalir;

    @FXML
    private TextArea editCarrito;


    @FXML
    private BorderPane panelCarrito;

    @FXML
    private Label txtPrecio;

    private List<Productos> productos;

    private double totalCarrito = 0.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        acciones();
        instancias();
        intGUI();
        cargarDatos();
    }

    private void acciones() {
        btnFiltrar.setOnAction(event -> filtrarProductos());
        btnComprar.setOnAction(this);
        btnSalir.setOnAction(this);
        btnAniader.setOnAction(this);

    }

    private void instancias() {
        productos = new ArrayList<>();
    }

    private void intGUI() {

        listaProductos.getItems().clear();
        listaProductos.setCellFactory(lv -> new ProductosListCell());

        Image imageAniadir = new Image(getClass().getResourceAsStream("aniader.png"));
        Image imageComprar = new Image(getClass().getResourceAsStream("comprar.png"));
        Image imageSalir = new Image(getClass().getResourceAsStream("salir.png"));
        Image imageFiltrar = new Image(getClass().getResourceAsStream("filtrar.png"));


        ImageView imageViewAniadir = new ImageView(imageAniadir);
        imageViewAniadir.setFitWidth(20);
        imageViewAniadir.setFitHeight(20);

        ImageView imageViewComprar = new ImageView(imageComprar);
        imageViewComprar.setFitWidth(20);
        imageViewComprar.setFitHeight(20);

        ImageView imageViewSalir = new ImageView(imageSalir);
        imageViewSalir.setFitWidth(20);
        imageViewSalir.setFitHeight(20);

        ImageView imageViewFiltrar = new ImageView(imageFiltrar);
        imageViewFiltrar.setFitWidth(20);
        imageViewFiltrar.setFitHeight(20);


        btnAniader.setGraphic(imageViewAniadir);
        btnComprar.setGraphic(imageViewComprar);
        btnSalir.setGraphic(imageViewSalir);
        btnFiltrar.setGraphic(imageViewFiltrar);





    }




    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == btnAniader) {
            anadirAlCarrito();

        } else if (event.getSource() == btnComprar) {
            compraRealizada();
        } else if (event.getSource() == btnSalir) {
            volver();
        }
    }


    private void cargarDatos() {
        String apiUrl = "https://makeup-api.herokuapp.com/api/v1/products.json?brand=dior";

        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // Leer la respuesta
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Productos>>() {
                }.getType();
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


    private void anadirAlCarrito() {
        Productos productoSeleccionado = listaProductos.getSelectionModel().getSelectedItem();

        if (productoSeleccionado != null) {
            String productoTexto = productoSeleccionado.getName() +  productoSeleccionado.getPrice() + " € \n";
            editCarrito.appendText(productoTexto);


            totalCarrito += productoSeleccionado.getPrice();


            txtPrecio.setText(String.format("%.2f €" , totalCarrito));
        }
    }

    private void compraRealizada() {
        // Mostrar alerta cuando se haga la compra
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Compra Realizada");
        alert.setHeaderText(null);
        alert.setContentText("Compra realizada con éxito. precio total es: " + totalCarrito +"€");
        alert.showAndWait();


        editCarrito.clear();


        totalCarrito = 0.0;


        txtPrecio.setText("$0.00");
    }

    private void volver(){

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