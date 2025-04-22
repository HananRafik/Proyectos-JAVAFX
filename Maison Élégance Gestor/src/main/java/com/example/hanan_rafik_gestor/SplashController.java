package com.example.hanan_rafik_gestor;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML
    private ImageView imageSplash;
    @FXML
    private javafx.scene.text.Text textologo;
    @FXML
    private Button btnComenzar;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initGUI();

        Task<Void> task = new Task<Void>() {


            @Override
            protected Void call() throws Exception {
                for (int i = 0; i<100; i++){
                    updateProgress(i,100);
                    Thread.sleep(50);
                }


               /* Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Stage actual = (Stage) imageSplash.getScene().getWindow();




                        Stage nuevo = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));

                        try {
                            Scene scene = new Scene(loader.load(), 800, 600);
                            nuevo.setTitle("Maison Élégance");
                            nuevo.setScene(scene);
                            nuevo.show();
                            actual.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                    }
                });*/

                return null;
            }
        };


        imageSplash.opacityProperty().bind(task.progressProperty());
        textologo.opacityProperty().bind(task.progressProperty());
        btnComenzar.opacityProperty().bind(task.progressProperty());



       new Thread(task).start();


        btnComenzar.setOnAction(event -> {
            Stage stageActual = (Stage) btnComenzar.getScene().getWindow();
            Stage stageNuevo = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));


            try {
                Scene scene = new Scene(loader.load(),900,700);
                stageNuevo.setTitle("Maison Élégance");
                stageNuevo.setScene(scene);
                stageNuevo.show();
                stageActual.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }

    private void initGUI(){
        imageSplash.setImage(new Image(getClass().getResourceAsStream("maisonelegancelogo.png")));
    }
}
