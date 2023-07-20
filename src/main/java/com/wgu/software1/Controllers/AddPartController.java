package com.wgu.software1.Controllers;

import com.wgu.software1.Data.InHouse;
import com.wgu.software1.Data.Inventory;
import com.wgu.software1.Data.Outsourced;
import com.wgu.software1.Data.Part;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
/**
 *
 * @author Nabeel Aref
 */
public class AddPartController {

    @FXML
    RadioButton radioInHouse;
    @FXML
    RadioButton radioOutSourced;
    @FXML
    Label identifierLabel;
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
    TextField txtIdentifier;

    private boolean inHouseIsChoosen;

    /**
     * loads as inhouse is choosen first rather than outsourced, this functions starts at first
     */
    public void initialize()
    {
        this.inHouseIsChoosen = true;
    }

    /**
     * this function changes the radio button to the inHouse radio button and unselects the outsourced radio button
     */
    @FXML
    void ChangeToInHouseForm()
    {
        radioOutSourced.setSelected(false);
        identifierLabel.setText("Machine ID");
        inHouseIsChoosen = true;
    }

    /**
     * this funnction changes the radioInhouse to unselected
     */
    @FXML
    void ChangeToOutSourcedForm()
    {
        radioInHouse.setSelected(false);
        identifierLabel.setText("Company Name");
        inHouseIsChoosen = false;
    }

    /**
     * saves a new part if all the data is verified in this function and the max is greater than min
     * @param event
     */
    @FXML
    void saveNewPart(MouseEvent event)
    {
        int id;
        String name;
        int inv;
        double price;
        int max;
        int min;
        int  machineId;
        String  companyName;

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
        if((!checkInteger(txtIdentifier.getText())|| checkEmpty(txtIdentifier.getText())) && inHouseIsChoosen)
        {
            ShowMessageBox("Error in validation","Make sure the correct information is in the Machine ID box, the value should be a number", Alert.AlertType.ERROR);
            return;
        }
        if(checkEmpty(txtIdentifier.getText()) && !inHouseIsChoosen)
        {
            ShowMessageBox("Error in validation","Make sure the correct information is in the company name box, the company name should be in that box", Alert.AlertType.ERROR);
            return;
        }
        id = getIdMaxNumber();
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
        if (inHouseIsChoosen) {

             machineId = Integer.parseInt(txtIdentifier.getText());
            Part part =new InHouse(id, name, price, inv, min , max, machineId) ;
            Inventory.addPart(part);
        } else {

             companyName = txtIdentifier.getText();
            Part part = new Outsourced(id, name, price, inv, min , max,  companyName);
            Inventory.addPart(part);
        }
        SwitchtoMainMenu(event);
        return;

    }

    /**
     * gets a new id number by using the size of the allParts observableList
     * @return
     */
    int getIdMaxNumber()
    {
        return Inventory.getAllParts().size();
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