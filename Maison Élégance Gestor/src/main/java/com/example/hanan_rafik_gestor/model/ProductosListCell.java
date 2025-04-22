package com.example.hanan_rafik_gestor.model;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ProductosListCell extends ListCell<Productos> {
    private HBox hbox = new HBox();
    private ImageView imageView = new ImageView();
    private Text text = new Text();

    public ProductosListCell() {
        hbox.getChildren().addAll(imageView, text);
        setGraphic(hbox);
    }

    @Override
    protected void updateItem(Productos item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {

            try {
                Image image = new Image(item.getImage_link(), true);
                imageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Error cargando la imagen: " + e.getMessage());
                imageView.setImage(null);
            }

            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            text.setText(item.getName() + " - " + item.getPrice() + " " + item.getDescription());

            setGraphic(hbox);
        }
    }
}
