package com.wgu.software1.Controllers;

import com.wgu.software1.Data.InHouse;
import com.wgu.software1.Data.Inventory;
import com.wgu.software1.Data.Outsourced;
import com.wgu.software1.Data.Part;
import com.wgu.software1.Software1;
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
public class ModifyPartController {

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
    private Part part;

    private boolean inHouseIsChoosen;
    /**
     * loads as inhouse is choosen first rather than outsourced, this functions starts at first and loads the part class in the form
     */
    public void initialize()
    {
        part = Software1.getPart();
        if(part==null) return;
        if(InHouse.class == part.getClass())
            this.inHouseIsChoosen = true;
        if(Outsourced.class == part.getClass())
            this.inHouseIsChoosen=false;

        Integer id = part.getId();
        txtId.setText(id.toString());
        Integer inv = part.getStock();
        txtInv.setText(inv.toString());
        Integer max = part.getMax();
        txtMax.setText(max.toString());
        Integer min = part.getMin();
        txtMin.setText(min.toString());
        Double price = part.getPrice();
        txtPrice.setText(price.toString());
        txtName.setText(part.getName());
        if(inHouseIsChoosen)
        {
            InHouse inHousePart = (InHouse)part;
            Integer machineId = inHousePart.getMachineId();
            txtIdentifier.setText(machineId.toString());
            radioInHouse.setSelected(true);
        }
        else
        {
            Outsourced outSourcePart = (Outsourced)part;

            txtIdentifier.setText(outSourcePart.getCompanyName());
            radioInHouse.setSelected(false);
            radioOutSourced.setSelected(true);

        }

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
     * updates the part if all the data is verified in this function and the max is greater than min
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
        id = part.getId();
        name = txtName.getText();
        price = Double.parseDouble(txtPrice.getText());
        inv = Integer.parseInt(txtInv.getText());
        max = Integer.parseInt(txtMax.getText());
        min = Integer.parseInt(txtMin.getText());
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
            Inventory.updatePart(part.getId(),part);
        } else {

             companyName = txtIdentifier.getText();
            Part part = new Outsourced(id, name, price, inv, min , max,  companyName);
            Inventory.updatePart(part.getId(),part);
        }
        Software1.setPart(null);
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