package client.uicomponents;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPane extends PopUpWindow {
    private static final int DEFAULT_PORT = 8000;
    private Label message;
    private TextField userNameInput;
    private TextField connectPortInput;
    private TextField serverIpInput;
    private Pattern pattern;
    private Matcher matcher;
    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public LoginPane() {
        super();
    }

    @Override
    public void setSize() {
        setWidth(350);
        setHeight(250);
    }

    @Override
    public void render() {
        Button btConnect = new Button("CONNECT");
        btConnect.setDefaultButton(true);
        Button btExit = new Button("CANCEL");
        btExit.setCancelButton(true);
        message = new Label("Garden War Log In");
        message.getStyleClass().add("lblTitleLogin");
        userNameInput = new TextField();
        userNameInput.setPrefWidth(150);
        connectPortInput = new TextField("" + DEFAULT_PORT);
        connectPortInput.setPrefWidth(65);
        Label lblUserName = new Label("Create Username: ");
        lblUserName.getStyleClass().add("lblLogin");
        Label connectPortLabel = new Label("Port Number: ");
        connectPortLabel.getStyleClass().add("lblLogin");
        Label lblIP = new Label("Server IP: ");
        lblIP.getStyleClass().add("lblLogin");
        serverIpInput = new TextField();
        serverIpInput.setPrefWidth(200);
        HBox row1 = new HBox(lblUserName, userNameInput);
        row1.setSpacing(15);
        HBox row2 = new HBox(connectPortLabel, connectPortInput);
        row2.setSpacing(15);
        HBox row3 = new HBox(lblIP, serverIpInput);
        row3.setSpacing(15);
        VBox inputs = new VBox(20);
        inputs.getStyleClass().add("inputsLogin");
        //set layouts
        HBox bottom = new HBox(20, btConnect, btExit);
        btConnect.setPrefWidth(100);
        btConnect.getStyleClass().add("button-dark-blue");
        btExit.setPrefWidth(100);
        btExit.getStyleClass().add("button-dark-blue");
        bottom.setPadding(new Insets(10, 0, 0, 0));
        inputs.getChildren().addAll(message, row1, row2, row3, bottom);
        //end of setting layouts
        addNode(inputs);
        btConnect.setOnAction(e -> {
            connectAttempt();
        });
        btExit.setOnAction(e -> {
            close();
        });
    }

    /**
     * form validation for connection inputs
     */

    private void connectAttempt(){
        int port = 8000;
        String username = "";
        String ip = "";
        try{
            username = userNameInput.getText().trim();
            if(username.length()>12|| username.length()==0){
                throw new InvalidUserNameException();
            }
        } catch (InvalidUserNameException ex){
            errorMessage("User name must be between 0 and 12 characters");
            userNameInput.selectAll();
            userNameInput.requestFocus();
            return;
        }

        try{
            pattern = Pattern.compile(IPADDRESS_PATTERN);
            ip = serverIpInput.getText().trim();
            if(!validate(ip)){
                throw new InvalidIPAddressException();
            }
        } catch(InvalidIPAddressException exIp){
            errorMessage("IP address must follow the format\n \"255.255.255.255\"");
            serverIpInput.selectAll();
            serverIpInput.requestFocus();
        }
        //end of field checking
        //establish the connection when all fields are checked
        connectToServer(username,ip);
    }

    private void connectToServer(String username, String ip){
        GameView gameview = new GameView(username, ip);
        close();
    }
    /**
     * pop out an alert to show the error message to the client
     *
     * @param message
     */
    private void errorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }


    /**
     * Validate ip address with regular expression
     *
     * @param ip ip address for validation
     * @return true valid ip address, false invalid ip address
     */
    public boolean validate(final String ip) {
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * exception handling for incorrect username input
     */
    private class InvalidUserNameException extends Exception{

    }

    /**
     * exception handling for incorrect ip address
     */
    private class InvalidIPAddressException extends Exception{

    }



}
