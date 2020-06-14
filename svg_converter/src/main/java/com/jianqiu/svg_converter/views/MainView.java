package com.jianqiu.svg_converter.views;

import com.jianqiu.svg_converter.controllers.WindowsController;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

//WindowService is a singleton
@Getter
@Setter
public class MainView {
    private BorderPane root;
    private ImageView imageView;
    private MenuItem convertWithPotrace = new MenuItem ("Convert");
    // Singleton constructor
    public MainView(Stage stage) {
        initGUI(stage);
    }

    /**
     * initialize the main window graphic user interface
     * from a given stage
     *
     * @param stage {Stage}
     */
    public void initGUI(Stage stage) {
        root = new BorderPane();
        MenuBar menuBar = new MenuBar(
                createFileMenu(stage));
        root.setTop(menuBar);
        root.setCenter(buildContent());
        Scene scene = new Scene(root, 800, 600, Color.GRAY);
        stage.setScene(scene);
        stage.setTitle("Java SVG Converter App");
        setupDragNDrop(stage);
        stage.show();
    }

    /**
     * This method Graphical assembles menuItems to FileMenu
     *
     * @return fileMenu
     */
    private Menu createFileMenu(Stage stage) {
        Menu fileMenu = new Menu("File");
        MenuItem fileItem = new MenuItem("Choose File");
        wireImageLoader(fileItem, stage);
        fileItem.setMnemonicParsing(true);
        //setting short cut as ctrl+O
        fileItem.setAccelerator(new KeyCodeCombination(KeyCode.O,
                KeyCombination.SHORTCUT_DOWN));
        //Convert Item
        convertWithPotrace.setDisable(true);
        wireUpConvertPotrace(convertWithPotrace, stage);
        //Clear Item
        MenuItem clearItem = new MenuItem("Clear");
        wireUpClearItem(clearItem, stage);
        //Exit Item
        MenuItem exitItem = new MenuItem("Quit");
        exitItem.setMnemonicParsing(true);
        //setting shortcut as ctrl+Q
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.Q,
                KeyCombination.SHORTCUT_DOWN));
        // exiting
        exitItem.setOnAction(actionEvent -> Platform.exit());
        //Add all menu items to the menu
        fileMenu.getItems().addAll(fileItem,convertWithPotrace, clearItem, exitItem);
        return fileMenu;
    }

    /**
     * Builds the GUI of the Window service that allow user to choose
     * image file with a file chooser as well as allowing user to
     * directly drag and drop into the main window
     *
     * @return {AnchorPane} The JavaFX Node that contains the main content of the GUI
     */
    private AnchorPane buildContent() {
        AnchorPane mainContentPane = new AnchorPane();
        imageView = new ImageView();
        AnchorPane.setTopAnchor(imageView, 0.0);
        AnchorPane.setLeftAnchor(imageView, 0.0);
        mainContentPane.setPrefSize(800, 600);
        imageView.fitWidthProperty().bind(mainContentPane.prefWidthProperty());
        imageView.fitHeightProperty().bind(mainContentPane.prefHeightProperty());
        mainContentPane.getChildren().add(imageView);
        return mainContentPane;
    }

    /**
     * Wires up a Filechooser to a menuitem such that when the MenuItem object
     * gets clicked, the filechooser will pop up
     *
     * @param menuItem {MenuItem} the MenuItem to wireup listener
     * @param stage    {Stage} The host Stage of the pop up file chooser
     */
    private void wireImageLoader(MenuItem menuItem, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images",
                        "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.gif"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("GIF", "*.gif")
        );
        menuItem.setOnAction(actionEvt -> {
            List<File> list = fileChooser.showOpenMultipleDialog(stage);
            if (list != null) {
                for (File file : list) {
                    //openFile(file);
                    try {
                        String url = file.toURI().toURL().toString();
                        if (isValidImageFile(url)) {
                            imageView.setImage(new Image(url, 300, 300, false, false));
                            convertWithPotrace.setDisable(false);
                        }
                    } catch (MalformedURLException e) {
                        System.out.println("User has chosen an invalid file extension");
                    }
                }

            }
        });
    }

    /**
     * Wires up a new Potrace conversion service panel with ConvertWithPotrace menu item
     * such that when the MenuItem object gets clicked, the new window will pop up
     *
     * @param menuItem {MenuItem} the MenuItem to wireup listener
     * @param stage    {Stage} The host Stage of new Stage
     */
    private void wireUpConvertPotrace(MenuItem menuItem, Stage stage){
        menuItem.setOnAction(e->{
            WindowsController.getInstance().createColorSelectionWindow(imageView.getImage());
        });

    }

    /**
     * Wires up the clear menu item such that when the user clicks on it
     * Image of selection disappears and user can see a clean UI again.
     *
     * @param menuItem {MenuItem} the MenuItem to wireup listener
     * @param stage    {Stage} The host Stage of the pop up file chooser
     */
    private void wireUpClearItem(MenuItem menuItem, Stage stage) {
        menuItem.setOnAction(e -> {
            imageView.setImage(null);
        });
    }

    /**
     * check whether user has picked an image file
     *
     * @param url {String} path to the user chosen image
     * @return {boolean} false if the file extension is not an image file extention
     */
    private boolean isValidImageFile(String url) {
        List<String> imgTypes = Arrays.asList(".jpg", ".jpeg",
                ".png", ".gif", ".bmp");
        return imgTypes.stream()
                .anyMatch(t -> url.toLowerCase().endsWith(t));
    }


    /**
     * Sets up the drag and drop capability for files to be dragged and
     * dropped onto the scene.
     */
    private void setupDragNDrop(Stage stage) {
        Scene scene = stage.getScene();
        // Dragging over surface
	     /*
	     If there are valid image files being dragged over the scene’s surface.
	     Set the transfer mode to (TransferMode.LINK);
	     otherwise, ignore or consume the event
	     */
        scene.setOnDragOver((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()
                    || isValidImageFile(db.getUrl())) {
                event.acceptTransferModes(TransferMode.LINK);
            } else {
                event.consume();
            }
            event.consume();
        });

        // Dropping over surface
	     /*
	        When the user drops the file, the file is already verified
	        by setOnDragOver, so we just set the image view to the
	        image that user has dropped over
	     */
        scene.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            db.getFiles().forEach(file -> {
                try {
                    String url = file.toURI().toURL().toString();
                    if (isValidImageFile(url)) {
                        imageView.setImage(new Image(url, 300, 300, false, false));
                    }
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
            });
            event.setDropCompleted(true);
            event.consume();
            //Checking file path for testing purpose
            db.getFiles().forEach(file -> System.out.println("file path: " + file.getPath()));
        });
    }
}