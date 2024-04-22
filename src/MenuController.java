

import java.io.File;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import javafx.util.Duration;

import java.sql.*;

public class MenuController {


    final ObservableList<String> THEMES = FXCollections.observableArrayList("Dark","Light");
    final ObservableList<String> UNIVERSITIES = FXCollections.observableArrayList("Wilfrid Laurier University", "University of Waterloo");
    Timer timer = new Timer();
    @FXML
    private ComboBox<String> universityOptions;
    @FXML
    private ComboBox<String> themeList;
    @FXML
    private AnchorPane background;
    @FXML
    private TabPane fullBackground;
    @FXML
    private Label welcomeMsg;
    
    @FXML
    private Label instrcText;
    @FXML
    private Button submitQ;
    //Question 1 ---------------------
    @FXML
    private Label question1;
    @FXML
    private RadioButton q1o1;
    @FXML
    private RadioButton q1o2;
    @FXML
    private RadioButton q1o3;
    //Question 2 ----------------------
    @FXML
    private Label question2;
    @FXML
    private RadioButton q2o1;
    @FXML
    private RadioButton q2o2;
    @FXML
    private RadioButton q2o3;
    //Question 3 ----------------------
    @FXML
    private Label question3;
    @FXML
    private RadioButton q3o1;
    @FXML
    private RadioButton q3o2;
    @FXML
    private RadioButton q3o3;
    //Question 4 ----------------------
    @FXML
    private Label question4;
    @FXML
    private RadioButton q4o1;
    @FXML
    private RadioButton q4o2;
    @FXML
    private RadioButton q4o3;
    //Question 5 ----------------------
    @FXML
    private Label question5;
    @FXML
    private RadioButton q5o1;
    @FXML
    private RadioButton q5o2;
    @FXML
    private RadioButton q5o3;
    //Question 6 ----------------------
    @FXML
    private Label question6;
    @FXML
    private RadioButton q6o1;
    @FXML
    private RadioButton q6o2;
    @FXML
    private RadioButton q6o3;
    //Toggle groups ------------------
    @FXML
    private ToggleGroup question1group;
    @FXML
    private ToggleGroup question2group;
    @FXML
    private ToggleGroup question3group;
    @FXML
    private ToggleGroup question4group;
    @FXML
    private ToggleGroup question5group;
    //Array of objects (buttons, labels, togglegroups, etc...)
    public ToggleGroup[] questionGroups;
    public Button[] allButtons;
    public RadioButton[] allRadioButtons;
    public Label[] allTexts;
    //Breathe Menu Objects
    @FXML
    private Button breatheButton;
    @FXML
    private Label breatheInstrc;
    @FXML
    private Button stopBreathing;
    @FXML
    private Label countdown;
    //Resources menu
    @FXML
    private Label resourcesText;
    //Journaling menu
    @FXML public TextField journalText;
    //Music Menu
    private Button playButton1;
    private Button playButton2;
    private Button playButton3;
    private Button stopMusicButton;

    MediaPlayer mediaPlayer;

    

    //jdbc connection variables
    private final String url = "jdbc:mysql://localhost:3306/serenity_db";
    private final String user = "root";
    private final String pass = "";


    @FXML
    public void initialize() {
        themeList.setItems(THEMES);
        themeList.setValue("Light");
        universityOptions.setItems(UNIVERSITIES);
        if (!Controller.guestLogin){

            
            int count=0;

            try {
                Connection connect = connect();
            
            //preparedstatement is used to avoid sql injections
            PreparedStatement stmnt = (PreparedStatement) connect
            .prepareStatement("Select loginCount from counter where id=?");
                
            stmnt.setInt(1, Controller.userid);
            ResultSet rs = stmnt.executeQuery();
            
            // Check Username and Password
            while (rs.next()) {
                count=rs.getInt("loginCount");
            }
            welcomeMsg.setText("Welcome back, "+Controller.currentUser+"! You have logged in "+count+" times.");
            count++;
                PreparedStatement stmnt1 = (PreparedStatement) connect
                .prepareStatement("UPDATE counter SET loginCount=? WHERE id=?");
                stmnt1.setInt(1, count);
                stmnt1.setInt(2, Controller.userid);
                stmnt1.executeUpdate();
            
            } catch (SQLException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                
            }  
            

        }else{
            welcomeMsg.setText("Welcome back!");
        }
        
        initializeGroups();
        initializeRadioButtons();
        initializeTexts();
        
        
    }

    //Inits groups of radio buttons and allocates into an array, so we can search this array later when the user attempts to answer all/some/none
    public void initializeGroups(){
        questionGroups = new ToggleGroup[5]; 
        questionGroups[0] = question1group;
        questionGroups[1] = question2group;
        questionGroups[2] = question3group;
        questionGroups[3] = question4group;
        questionGroups[4] = question5group;
    }
    public void initializeButtons(){
        allButtons = new Button[1];
        allButtons[0] = submitQ;
    }
    public void initializeRadioButtons(){
        allRadioButtons = new RadioButton[18];
        allRadioButtons[0] = q1o1;
        allRadioButtons[1] = q1o2;
        allRadioButtons[2] = q1o3;
        allRadioButtons[3] = q2o1;
        allRadioButtons[4] = q2o2;
        allRadioButtons[5] = q2o3;
        allRadioButtons[6] = q3o1;
        allRadioButtons[7] = q3o2;
        allRadioButtons[8] = q3o3;
        allRadioButtons[9] = q4o1;
        allRadioButtons[10] = q4o2;
        allRadioButtons[11] = q4o3;
        allRadioButtons[12] = q5o1;
        allRadioButtons[13] = q5o2;
        allRadioButtons[14] = q5o3;
        allRadioButtons[15] = q6o1;
        allRadioButtons[16] = q6o2;
        allRadioButtons[17] = q6o3;

    }
    public void initializeTexts(){
        allTexts = new Label[8];
        allTexts[0] = welcomeMsg;
        allTexts[1] = instrcText;
        allTexts[2] = question1;
        allTexts[3] = question2;
        allTexts[4] = question3;
        allTexts[5] = question4;
        allTexts[6] = question5;
        allTexts[7] = question6;

    }

    //Disables the button once the user has entered answers to all the questions
    public void submitButton(){
        boolean allAnswered = false;
        for (int i = 0; i < questionGroups.length; i++) {
            if (questionGroups[i].getSelectedToggle() != null){
                allAnswered = true;
            }else{
                allAnswered = false;
                break;
            }
        }

        if (allAnswered){
            submitQ.setDisable(true);
        }else{
            System.out.println("User hasn't put an answer!");
        }
        
    }


    public void changeTheme(){
        String theme = themeList.getValue();
         switch (theme){
            case "Dark":
                fullBackground.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
                for (int i = 0; i < allRadioButtons.length; i++) {
                    allRadioButtons[i].setTextFill(Color.WHITE);
                }
                for (int i = 0; i < allTexts.length; i++) {
                    allTexts[i].setTextFill(Color.WHITE);
                }
                break;
            case "Light":
                fullBackground.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                for (int i = 0; i < allRadioButtons.length; i++) {
                    allRadioButtons[i].setTextFill(Color.BLACK);
                }
                for (int i = 0; i < allTexts.length; i++) {
                    allTexts[i].setTextFill(Color.BLACK);
                }
                break;
            }
    }

    public void play1(){
        playAudio("calm.mp3");
    }
    public void play2(){
        playAudio("birds.mp3");
    }
    public void play3(){
        playAudio("meditation.mp3");
    }

    public void playAudio(String musicName){
        String musicFile = "music/"+musicName;     // For example
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public void getTime(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp.toString());
    }

    public void timeBreathe() throws InterruptedException{
        String musicFile = "music/breathe.mp3";     // For example
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        }); 
        timer = new Timer();
        breatheButton.setVisible(false);
        breatheInstrc.setVisible(true);
        stopBreathing.setVisible(true);
        countdown.setVisible(true);
        TimerTask task = new TimerTask(){
            int seconds = 1;
            Integer counter = 1;
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (seconds >= 1 && seconds <= 4){
                        breatheInstrc.setText("Breathe in...");
                    }else if(seconds >= 5 && seconds <= 8){
                        breatheInstrc.setText("Hold...");
                    }else if(seconds >= 9 && seconds <= 12){
                        breatheInstrc.setText("Breathe out...");
                    }else if(seconds >= 13 && seconds <= 16){
                        breatheInstrc.setText("Hold...");
                    }
                    countdown.setText(counter.toString());
                seconds++;
                counter++;
                if (seconds > 16){
                    seconds = 1;
                }
                if(counter > 4){
                    counter = 1;
                }
            });
            }
        };
        //Makes timer run every 4 seconds to constantly cycle through the 3 stages of breathing until user presses "Stop"
        timer.scheduleAtFixedRate(task, 0, 1000);
            
        
    }

    public void stopBreathing(){
        mediaPlayer.setAutoPlay(false);
        mediaPlayer.stop();
        breatheButton.setVisible(true);
        breatheInstrc.setVisible(false);
        stopBreathing.setVisible(false);
        countdown.setVisible(false);
        
        timer.cancel();
    }

    public void stopMusic(){
        mediaPlayer.stop();
    }

    public void saveJournal(){
        //TO DO

        String SQL = "INSERT INTO journals(id,entry) "
                + "VALUES(?,?)";

        long userid = 0; 

        try (Connection conn = connect();
                PreparedStatement prepStatement = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {
            
            prepStatement.setInt(1, Controller.userid);
            prepStatement.setString(2, ""+journalText.getText());
            // 

            int rows = prepStatement.executeUpdate();
            
            if (rows > 0) {
               
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
        //disable the textfield or button after pressed, and a popup message that its been saved
       


    }

    public void findResource(){
        if(universityOptions.getValue().equals("Wilfrid Laurier University")){
            resourcesText.setText("Student Wellness Centre \n E: wellness@wlu.ca \n T: 519.884.0710 \n F: 519.340.1403");
        }else{
            resourcesText.setText("Campus Wellness \n E: hsforms@uwaterloo.ca \n Health Services at 519-888-4096 \n Counselling Services at 519-888-4567 ext. 42655 \n");
        }
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}
