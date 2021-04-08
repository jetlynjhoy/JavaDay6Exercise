/**
 * Sample Skeleton for 'sample.fxml' Controller Class
 */

package sample;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Controller {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="tfAgentId"
    private TextField tfAgentId; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgtFirstName"
    private TextField tfAgtFirstName; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgtMiddleInitial"
    private TextField tfAgtMiddleInitial; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgtLastName"
    private TextField tfAgtLastName; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgtBusPhone"
    private TextField tfAgtBusPhone; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgtEmail"
    private TextField tfAgtEmail; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgtPosition"
    private TextField tfAgtPosition; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgencyId"
    private TextField tfAgencyId; // Value injected by FXMLLoader

    @FXML // fx:id="cbAgents"
    private ComboBox<Agent> cbAgents; // Value injected by FXMLLoader

    @FXML // fx:id="btnSave"
    private Button btnSave; // Value injected by FXMLLoader

    @FXML // fx:id="btnEdit"
    private Button btnEdit; // Value injected by FXMLLoader

    @FXML
    void handlebtnEdit(ActionEvent event) {
        btnEdit.setDisable(true);
        btnSave.setDisable(false);
        modifyText();
    }

    @FXML
    void handlebtnSave(ActionEvent event) {
        btnEdit.setDisable(false);
        btnSave.setDisable(true);
        readOnlyText();
    }

    public void readOnlyText()
    {
        tfAgtFirstName.setEditable(false);
        tfAgtMiddleInitial.setEditable(false);
        tfAgtLastName.setEditable(false);
        tfAgtBusPhone.setEditable(false);
        tfAgtEmail.setEditable(false);
        tfAgtPosition.setEditable(false);
        tfAgencyId.setEditable(false);
    }

    public void modifyText()
    {
        tfAgtFirstName.setEditable(true);
        tfAgtMiddleInitial.setEditable(true);
        tfAgtLastName.setEditable(true);
        tfAgtBusPhone.setEditable(true);
        tfAgtEmail.setEditable(true);
        tfAgtPosition.setEditable(true);
        tfAgencyId.setEditable(true);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert tfAgentId != null : "fx:id=\"tfAgentId\" was not injected: check your FXML file 'sample.fxml'.";
        assert tfAgtFirstName != null : "fx:id=\"tfAgtFirstName\" was not injected: check your FXML file 'sample.fxml'.";
        assert tfAgtMiddleInitial != null : "fx:id=\"tfAgtMiddleInitial\" was not injected: check your FXML file 'sample.fxml'.";
        assert tfAgtLastName != null : "fx:id=\"tfAgtLastName\" was not injected: check your FXML file 'sample.fxml'.";
        assert tfAgtBusPhone != null : "fx:id=\"tfAgtBusPhone\" was not injected: check your FXML file 'sample.fxml'.";
        assert tfAgtEmail != null : "fx:id=\"tfAgtEmail\" was not injected: check your FXML file 'sample.fxml'.";
        assert tfAgtPosition != null : "fx:id=\"tfAgtPosition\" was not injected: check your FXML file 'sample.fxml'.";
        assert tfAgencyId != null : "fx:id=\"tfAgencyId\" was not injected: check your FXML file 'sample.fxml'.";
        assert cbAgents != null : "fx:id=\"cbAgents\" was not injected: check your FXML file 'sample.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'sample.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'sample.fxml'.";

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from agents");
            ObservableList<Agent> list = FXCollections.observableArrayList();

            while (rs.next())
            {
                list.add(new Agent(rs.getInt("AgentId"), rs.getString("AgtFirstName"),
                        rs.getString("AgtMiddleInitial"), rs.getString("AgtLastName"),
                        rs.getString("AgtBusPhone"), rs.getString("AgtEmail"),
                        rs.getString("AgtPosition"), rs.getInt("AgencyId")));
            }
            cbAgents.setItems(list);
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        cbAgents.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Agent>() {
            @Override
            public void changed(ObservableValue<? extends Agent> observableValue, Agent agent, Agent t1) {
                tfAgentId.setText(t1.getAgentId() + "");
                tfAgtFirstName.setText(t1.getAgtFirstName());
                tfAgtMiddleInitial.setText(t1.getAgtMiddleInitial());
                tfAgtLastName.setText(t1.getAgtLastName());
                tfAgtBusPhone.setText(t1.getAgtBusPhone());
                tfAgtEmail.setText(t1.getAgtEmail());
                tfAgtPosition.setText(t1.getAgtPosition());
                tfAgencyId.setText(t1.getAgencyId() + "");
            }
        });

        btnSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String sql = "UPDATE `agents` SET `AgtFirstName`=?,`AgtMiddleInitial`=?,"
                           + "`AgtLastName`=?,`AgtBusPhone`=?,`AgtEmail`=?,`AgtPosition`=?,"
                           +"`AgencyId`=? WHERE AgentId=?";

                Connection conn = getConnection();
                try {
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, tfAgtFirstName.getText());
                    stmt.setString(2, tfAgtMiddleInitial.getText());
                    stmt.setString(3, tfAgtLastName.getText());
                    stmt.setString(4, tfAgtBusPhone.getText());
                    stmt.setString(5, tfAgtEmail.getText());
                    stmt.setString(6, tfAgtPosition.getText());
                    stmt.setInt(7, Integer.parseInt(tfAgencyId.getText()));
                    stmt.setInt(8, Integer.parseInt(tfAgentId.getText()));
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0)
                    {
                        System.out.println("Update worked");
                    }
                    else
                    {
                        System.out.println("Update failed");
                    }
                    conn.close();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    private Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/travelexperts", "root", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }
}

