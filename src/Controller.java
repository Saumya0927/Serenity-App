
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.*;


public class Controller {

    
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ComboBox<String> themeList;
    @FXML
    private BorderPane background;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label incorrectLogin;
    @FXML
    private Button registerButton;
    @FXML
    private CheckBox guest;
    public static boolean guestLogin;
    public static String currentUser;


    @FXML 
    public TextArea textArea;
    //jdbc connection variables
    private final String url = "jdbc:mysql://localhost:3306/serenity_db";
    private final String user = "root";
    private final String pass = "";
    
    public static int userid;
    public static int logincount;

    public void switchToLogin(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //This function is called when register account button is pressed.
    public void registerAccount(){
        // DO SOMETHING
        String SQL = "INSERT INTO users(userName,password) "
                + "VALUES(?,?)";
        String SQL1 = "INSERT INTO counter(id,loginCount) "
        + "VALUES(?,?)";
        long userid = 0;

        try (Connection connection = connect();
                PreparedStatement prepStatement = connection.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

                prepStatement.setString(1, username.getText());
                prepStatement.setString(2, password.getText());
                
                
            int affectedRows = prepStatement.executeUpdate();
            // check the affected rows 
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = prepStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        userid = rs.getLong(1);
                    
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        try (Connection connection = connect();
                PreparedStatement prepStatement = connection.prepareStatement(SQL1,
                Statement.RETURN_GENERATED_KEYS)) {

                prepStatement.setLong(1, userid);
                prepStatement.setInt(2, 0);
                
                
                prepStatement.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        
        // System.out.print(userid);
        return;
 
        
    }


    public void switchToMainMenu(ActionEvent event) throws IOException{
        if (checkLogin() || guestLogin){
            currentUser = username.getCharacters().toString();
            root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else{
            incorrectLogin.setOpacity(1f);
        }
        
    }


    public boolean checkLogin(){

        String dbUsername = "";
        String dbPassword = "";

        userid=0;

        try {
            Connection connect = connect();
        
        //preparedstatement is used to avoid sql injections
        PreparedStatement stmnt = (PreparedStatement) connect
        .prepareStatement("Select username, password, id from users where userName=? and password=?");
        stmnt.setString(1, username.getText());
        stmnt.setString(2, password.getText());
        ResultSet rs = stmnt.executeQuery();
        
        // Check Username and Password
        while (rs.next()) {
            dbUsername = rs.getString("username");
            dbPassword = rs.getString("password");
            userid = rs.getInt("id");
        }
       
        if (username.getText().equals(dbUsername) && password.getText().equals(dbPassword)) {
            return true;
        } else {
            return false;
        }

        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            return false;
        }  
    //     if  (username.getCharacters().toString().equals("username") && password.getCharacters().toString().equals("password")){
    //         return true;
    //     }
    //     return false;
    }

    public boolean enableGuest(){
        if (guest.isSelected()){
            username.setText("");
            username.setEditable(false);
            password.setText("");
            password.setEditable(false);
            registerButton.setDisable(true);
            return guestLogin = true;
        }
        registerButton.setDisable(false);
        username.setEditable(true);
        password.setEditable(true);
        return guestLogin = false;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

}
