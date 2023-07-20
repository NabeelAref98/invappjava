package com.wgu.software1.Controllers;

import com.wgu.software1.Data.Inventory;
import com.wgu.software1.Data.Part;
import com.wgu.software1.Data.Product;
import com.wgu.software1.Software1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 *
 * @author Nabeel Aref
 */
public class ModifyProductController {



    @FXML
    TextField txtId;
   @FXML
    TextField txtName;
   @FXML
    TextField txtInv;
   @FXML
    TextField txtPrice;
   @FXML
    TextField txtMax;
   @FXML
    TextField txtMin;

    @FXML
    TableView<Part> tbAllParts;
    @FXML
    TableView<Part> tbCurrentParts;
    @FXML
    Label lblError;
    ObservableList<Part> allParts = FXCollections.observableArrayList();
    @FXML
    TextField txtSearch;
    Product product;

    /**
     * intializes the tables in the form and loads the product and its associated parts
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

        tbAllParts.getColumns().add(columnID);
        tbAllParts.getColumns().add(columnName);
        tbAllParts.getColumns().add(columnPrice);
        tbAllParts.getColumns().add(columnInv);

        TableColumn<Part,Integer> columnIDCurrent = new TableColumn<Part,Integer>("Part ID");
        columnIDCurrent.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Part,String> columnNameCurrent = new TableColumn<Part,String>("Part Name");
        columnNameCurrent.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Part,Double> columnPriceCurrent = new TableColumn<Part,Double>("Part Price");
        columnPriceCurrent.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Part,Integer> columnInvCurrent = new TableColumn<Part,Integer>("Part Inventory");
        columnInvCurrent.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tbCurrentParts.getColumns().add(columnIDCurrent);
        tbCurrentParts.getColumns().add(columnNameCurrent);
        tbCurrentParts.getColumns().add(columnPriceCurrent);
        tbCurrentParts.getColumns().add(columnInvCurrent);

        for (Part part : Inventory.getAllParts()) {
            tbAllParts.getItems().add(part);

        }
        if(Software1.getProduct() == null) return;
        product = Software1.getProduct();
        Integer id = product.getId();
        txtId.setText(id.toString());
        Integer inv = product.getStock();
        txtInv.setText(inv.toString());
        Integer max = product.getMax();
        txtMax.setText(max.toString());
        Integer min = product.getMin();
        txtMin.setText(min.toString());
        Double price = product.getPrice();
        txtPrice.setText(price.toString());
        txtName.setText(product.getName());
        allParts = product.getAllAssociatedParts();

        if(allParts.size()>0) {
            for (Part part : allParts) {
                tbCurrentParts.getItems().add(part);

            }
        }
    }

    /**
     * to search parts in the table in the form the table to search for all parts
     * @param event
     */
    @FXML
    protected void onSearchPart(InputEvent event)
    {
        boolean isSearchEmpty = true;
        if(checkInteger( txtSearch.getText()))
        {
            Integer id = Integer.parseInt( txtSearch.getText());
            tbAllParts.getItems().clear();
            for (Part part : Inventory.getAllParts()) {
                if(part.getId() == id)
                {
                    tbAllParts.getItems().add(part);
                    isSearchEmpty = false;
                }

            }
        }
        else if( txtSearch.getText().equals("")) {
            isSearchEmpty = false;
            tbAllParts.getItems().clear();
            for (Part part : Inventory.getAllParts()) {
                tbAllParts.getItems().add(part);
            }
        }
        else {
            tbAllParts.getItems().clear();
            for (Part part : Inventory.getAllParts()) {
                if (part.getName().contains( txtSearch.getText())) {
                    tbAllParts.getItems().add(part);
                    isSearchEmpty = false;
                }
            }
        }
        if(isSearchEmpty )
        {
            lblError.setText("No results found.");
            lblError.setTextFill(new Color(1,0,0,1));
        }
        else {
            lblError.setText("Parts");
            lblError.setTextFill(new Color(0,0,0,1));

        }
    }

    /**
     * adds parts from the all parts table to the current table
     */
    @FXML void addPart()
    {
        if(tbAllParts.getSelectionModel().isEmpty()) {
            ShowMessageBox("error","Be sure to select a Part", Alert.AlertType.ERROR);
            return;
        }
        allParts.add(tbAllParts.getSelectionModel().getSelectedItem());
        tbCurrentParts.getItems().clear();
        for (Part part : allParts) {
            tbCurrentParts.getItems().add(part);
            }

    }

    /**
     * removes the part from the table on the bottom of the form
     */
    @FXML
    void removePart()
    {
        if(tbCurrentParts.getSelectionModel().isEmpty()) {
            ShowMessageBox("error","Be sure to select a Part", Alert.AlertType.ERROR);
            return;
        }
        allParts.remove(tbCurrentParts.getSelectionModel().getSelectedItem());
        tbCurrentParts.getItems().clear();
        for (Part part : allParts) {
            tbCurrentParts.getItems().add(part);
        }

    }

    /**
     * updates the product if it validates all the textboxes information
     * @param event
     */
    @FXML
    void saveNewProduct(MouseEvent event)
    {
        int id;
        String name;
        int inv;
        double price;
        int max;
        int min;

        if(checkEmpty(txtName.getText()))
        {
            ShowMessageBox("Error in validation","Make sure the correct information is in the name box, your name should be in that box", Alert.AlertType.ERROR);
            return;
        }
        if(!checkInteger(txtInv.getText()) || checkEmpty(txtInv.getText()))
        {
            ShowMessageBox("Error in validation","Make sure the correct information is in the inventory box, the value should be a number", Alert.AlertType.ERROR);
            return;
        }
        if(!checkDouble(txtPrice.getText())|| checkEmpty(txtPrice.getText()))
        {
            ShowMessageBox("Error in validation","Make sure the correct information is in the price box, the value should be a number", Alert.AlertType.ERROR);
            return;
        }
        if(!checkInteger(txtMax.getText())|| checkEmpty(txtMax.getText()))
        {
            ShowMessageBox("Error in validation","Make sure the correct information is in the max box, the value should be a number", Alert.AlertType.ERROR);
            return;
        }
        if(!checkInteger(txtMin.getText())|| checkEmpty(txtMin.getText()))
        {
            ShowMessageBox("Error in validation","Make sure the correct information is in the min box, the value should be a number", Alert.AlertType.ERROR);
            return;
        }

        id = product.getId();
        name = txtName.getText();
        price = Double.parseDouble(txtPrice.getText());
        max = Integer.parseInt(txtMax.getText());
        min = Integer.parseInt(txtMin.getText());
        inv = Integer.parseInt(txtInv.getText());
        if(max <0 || min < 0 || min>max)
        {
            ShowMessageBox("Error in validation","Make sure max is greater than the min and both of them greater than 0.", Alert.AlertType.ERROR);
            return;
        }

        if(inv > max || inv < min)
        {
            ShowMessageBox("Error in validation","Make sure the inventory amount is not smaller than the min or greater than the max amount.", Alert.AlertType.ERROR);
            return;
        }



        Product product =new Product(id, name, price, inv, min , max) ;
        for (Part newParts : allParts) {
            product.addAssociatedPart(newParts);
        }
            Inventory.updateProduct(product.getId(), product);
        Software1.setProduct(null);
        SwitchtoMainMenu(event);
        return;

    }

    /**
     * checks if text given is a integer
     * @param text
     * @return
     */
    boolean checkInteger(String text)
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
     * checks if the text given is text is a double
     * @param text
     * @return
     */
    boolean checkDouble(String text)
    {
        try
        {
            Double.parseDouble(text);
            return  true;
        }
        catch (Exception e)
        {
            return  false;
        }
    }
    /**
     * checks if string is empty
     * @param text
     * @return
     */
    boolean checkEmpty(String text)
    {
        return (text=="");
    }
    /**
     * goes to main screen in the application
     * @param event
     */
    @FXML
    void SwitchtoMainMenu( MouseEvent event)
    {
        try {
           FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/wgu/software1/main.fxml"));
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
     * Shows a messagebox given the information of title, message and type
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