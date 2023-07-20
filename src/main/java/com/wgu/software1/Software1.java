package com.wgu.software1;

import com.wgu.software1.Data.Inventory;
import com.wgu.software1.Data.Outsourced;
import com.wgu.software1.Data.Part;
import com.wgu.software1.Data.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Software1 extends Application {
    private Stage previousStage;
    private static Part modifiedPart=null;
    private static Product product=null;

    public static Part getPart() {
        return modifiedPart;
    }

    public static void setPart(Part modifiedPart) {
        Software1.modifiedPart = modifiedPart;
    }

    public static Product getProduct() {
        return product;
    }

    public static void setProduct(Product product) {
        Software1.product = product;
    }

    /**
     * JavaFx entry point starts here
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Software1.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Software 1 project");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * java main function entry point
     * @param args
     */
    public static void main(String[] args) {

        launch();
    }
}