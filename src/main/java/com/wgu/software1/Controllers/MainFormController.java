package com.wgu.software1.Controllers;

import com.wgu.software1.Data.Inventory;
import com.wgu.software1.Data.Part;
import com.wgu.software1.Data.Product;
import com.wgu.software1.Software1;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.lang.Object;
import java.util.List;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainFormController {
    @FXML
    Label lblPartsError;
    @FXML
    Label lblProductsError;
    @FXML
    private TableView<Part> tbParts;
    @FXML
    private TableView<Product> tbProducts;
    @FXML
    private TextField txtSearchPart;
    @FXML
    private TextField txtSearchProducts;
/**
 * FUTURE ENHANCEMENT
 * We should add themes for this application and make sure the application saves the part and products
 * to a file or database.
 */

    /**
     * goes to the add part screen
     * @param event
     */
    @FXML
    protected void onAddPartClick(MouseEvent event) {
        SwitchScreen("addPart",event);
    }

    /**
     * goes to the modify part screen
     * @param event
     */
    @FXML
    protected void onModifyPartClick(MouseEvent event) {
        if(tbParts.getSelectionModel().isEmpty()) {
            ShowMessageBox("error","Be sure to select a Part", Alert.AlertType.ERROR);
            return;
        }
        Software1.setPart(tbParts.getSelectionModel().getSelectedItem());
        SwitchScreen("ModifyPart",event);
    }

    /**
     * this funnction goes to the add product screen
     * @param event
     */
    @FXML
    protected void onAddProductClick(MouseEvent event) {
        SwitchScreen("addProduct",event);
    }

    /**
     * this funnction goes to the modify product screen
     * @param event
     */
    @FXML
    protected void onModifyProductClick(MouseEvent event) {
        if(tbProducts.getSelectionModel().isEmpty()) {
            ShowMessageBox("error","Be sure to select a Product", Alert.AlertType.ERROR);
            return;
        }
        Software1.setProduct(tbProducts.getSelectionModel().getSelectedItem());
        SwitchScreen("ModifyProduct",event);

    }

    /**
     * this funnction closes the application
     */
    @FXML
    void closeApplication()
    {
        Platform.exit();
        System.exit(0);
    }

    /**
     * this funnction deletes the part if not contained in a product
     */
    @FXML void deletePart()
    {
        if(tbParts.getSelectionModel().isEmpty()) {
            ShowMessageBox("error","Be sure to select a Part", Alert.AlertType.ERROR);
            return;
        }
        Part deletedPart = tbParts.getSelectionModel().getSelectedItem();

        for (Product product :Inventory.getAllProducts()) {
            if(product.getAllAssociatedParts().size()<1)continue;
            for (Part searchedPart:product.getAllAssociatedParts()) {
                if(deletedPart.getId() == searchedPart.getId())
                {
                    ShowMessageBox("error","That part is in the "+product.getName()+" product be sure to delete it there first.", Alert.AlertType.ERROR);
                    return;
                }
            }
        }
        /**
         * RUNTIME ERROR
         * when the yes button was not deleting after clicking yes in this alert dialog
         * we changed the old line from if (type == ButtonType.OK) { to  (type == okButton) {
         * the type is the acutual button in java it wouldnt throw an error if two different objects are
         * compared using == because it would check their location in memory rather than the actual object
         * in this case type a object was compared to a Enumerator which is not the same which caused the
         * application not to delete the part.
         */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("delete this part?");
        alert.setContentText("Are you sure you want to delete this part?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);

        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                Inventory.deletePart(deletedPart);

            } else if (type == noButton) {

            }
        });

        tbParts.getItems().clear();
        for (Part part : Inventory.getAllParts()) {
            tbParts.getItems().add(part);
        }
    }

    /**
     * this funnction deletes a product if selected
     */
    @FXML void deleteProduct()
    {
        if(tbProducts.getSelectionModel().isEmpty()) {
            ShowMessageBox("error","Be sure to select a Product", Alert.AlertType.ERROR);
            return;
        }
        if(tbProducts.getSelectionModel().getSelectedItem().getAllAssociatedParts().size() > 0) {
            ShowMessageBox("error","Be sure the product has no associated parts, try removing the associated part by modifying it.", Alert.AlertType.ERROR);
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("delete this product?");
        alert.setContentText("Are you sure you want to delete this product?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                Inventory.deleteProduct(tbProducts.getSelectionModel().getSelectedItem());
            } else if (type == noButton) {

            }
        });


        tbProducts.getItems().clear();
        for (Product product : Inventory.getAllProducts()) {
            tbProducts.getItems().add(product);
        }
    }

    /**
     * this funnction searches the parts in the table
     * @param event
     */
    @FXML
    protected void onSearchPart(InputEvent event)
    {
        boolean isSearchEmpty = true;
        if(checkInteger(txtSearchPart.getText()))
        {
            Integer id = Integer.parseInt(txtSearchPart.getText());
            tbParts.getItems().clear();
            for (Part part : Inventory.getAllParts()) {
                if(part.getId() == id)
                {
                    tbParts.getItems().add(part);
                    isSearchEmpty = false;
                }

            }
        }
        else if(txtSearchPart.getText().equals("")) {
            isSearchEmpty = false;
            tbParts.getItems().clear();
            for (Part part : Inventory.getAllParts()) {
                tbParts.getItems().add(part);
            }
        }
        else {
            tbParts.getItems().clear();
            for (Part part : Inventory.getAllParts()) {
                if (part.getName().contains(txtSearchPart.getText())) {
                    tbParts.getItems().add(part);
                    isSearchEmpty = false;
                }
            }
        }
        if(isSearchEmpty )
        {
            lblPartsError.setText("No results found.");
            lblPartsError.setTextFill(new Color(1,0,0,1));
        }
        else {
            lblPartsError.setText("Parts");
            lblPartsError.setTextFill(new Color(0,0,0,1));

        }

    }

    /**
     * this function searches the products in the table
     * @param event
     */
    @FXML
    void onSearchProduct(InputEvent event)
    {
        boolean isSearchEmpty = true;
        if(checkInteger(txtSearchProducts.getText()))
        {
            Integer id = Integer.parseInt(txtSearchProducts.getText());
            tbProducts.getItems().clear();
            for (Product product : Inventory.getAllProducts()) {
                if(product.getId() == id)
                {
                    tbProducts.getItems().add(product);
                    isSearchEmpty = false;
                }

            }
        }
        else if(txtSearchProducts.getText().equals("")) {
            isSearchEmpty = false;
            tbProducts.getItems().clear();
            for (Product product : Inventory.getAllProducts()) {
                tbProducts.getItems().add(product);
            }
        }
        else {
            tbProducts.getItems().clear();
            for (Product product : Inventory.getAllProducts()) {
                if (product.getName().contains(txtSearchProducts.getText())) {
                    tbProducts.getItems().add(product);
                    isSearchEmpty = false;

                }
            }
        }
        if(isSearchEmpty )
        {
            lblProductsError.setText("No results found.");
            lblProductsError.setTextFill(new Color(1,0,0,1));
        }
        else {
            lblProductsError.setText("Products");
            lblProductsError.setTextFill(new Color(0,0,0,1));

        }

    }

    /**
     * this function checks if text can be a integer
     * @param text
     * @return
     */
    private boolean checkInteger(String text)
    {
        try
        {
            Integer.parseInt(text);
            return  true;
        }
        catch (Exception e)
        {
            return  false;
        }
    }

    /**
     * this functions starts when the main screen opens it loads all the tables
     */
    public void initialize()
    {
        TableColumn<Part,Integer> columnID = new TableColumn<Part,Integer>("Part ID");
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Part,String> columnName = new TableColumn<Part,String>("Part Name");
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Part,Double> columnPrice = new TableColumn<Part,Double>("Part Price");
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Part,Integer> columnInv = new TableColumn<Part,Integer>("Part Inventory");
        columnInv.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tbParts.getColumns().add(columnID);
        tbParts.getColumns().add(columnName);
        tbParts.getColumns().add(columnPrice);
        tbParts.getColumns().add(columnInv);

        for (Part part : Inventory.getAllParts()) {
            tbParts.getItems().add(part);

        }

        TableColumn<Product,Integer> columnIDProduct = new TableColumn<Product,Integer>("Product ID");
        columnIDProduct.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Product,String> columnNameProduct = new TableColumn<Product,String>("Product Name");
        columnNameProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product,Double> columnPriceProduct = new TableColumn<Product,Double>("Product Price");
        columnPriceProduct.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Product,Integer> columnInvProduct = new TableColumn<Product,Integer>("Product Inventory");
        columnInvProduct.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tbProducts.getColumns().add(columnIDProduct);
        tbProducts.getColumns().add(columnNameProduct);
        tbProducts.getColumns().add(columnPriceProduct);
        tbProducts.getColumns().add(columnInvProduct);

        for (Product product : Inventory.getAllProducts()) {
            tbProducts.getItems().add(product);

        }

    }

    /**
     * this function goes to a different screen
     * @param screen
     * @param event
     */
    void SwitchScreen(String screen, MouseEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/wgu/software1/"+screen+".fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.load();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * this function shows a message box using the specified parameters
     * @param title
     * @param Message
     * @param type
     */
    void ShowMessageBox(String title, String Message, Alert.AlertType type)
    {
        Alert alert = new Alert(type);
        alert.setTitle("error");
        alert.setHeaderText(title);
        alert.setContentText(Message);
        alert.setResizable(true);
alert.getDialogPane().setMinWidth(600);

        alert.show();
    }

}