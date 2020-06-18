package client.uicomponents;

import client.animations.BackGroundAnimation;
import common.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameView extends Stage {
    private String username;
    private String ip;
    private final double WIDTH = 1200;
    private final double HEIGHT = 680;
    private final int NUM_OF_ROWS = 8;
    private final int NUM_OF_COLS = 8;
    private final String BACKURL = "../../images/backgrounds/background.jpg";
    private final Pane mainFrame;
    private GridPane gameBoard;
    private VBox rightPane;
    private final PlayerPane playerPane1 = new PlayerPane(1); //controls player1 stats
    private final PlayerPane playerPane2 = new PlayerPane(2); //controls player2 stats
    private final ActionPaneButton btFarmer = new ActionPaneButton("FARMER");
    private final ActionPaneButton btGov = new ActionPaneButton("GOV");
    private final ActionPaneButton btInfo = new ActionPaneButton("INFO");
    private final ActionPaneButton btWeather = new ActionPaneButton("WEATHER");
    private final ActionPaneButton btEndTurn = new ActionPaneButton("ENDTURN");
    private ItemTree tvSeedsItems;
    private ItemTree tvFarmerItems;
    private ItemTree tvPolicyItems;
    private SliderPane sliderPane;
    private static final HashMap<Integer, String> cssMap;
    private ArrayList<GameSquare> squares = new ArrayList<>();
    //connection variables
    private GardenWarClient gardenWarClient;
    //Audio
    private final String INGAMEMUSICURL = "src/audio/backmusic/ingame.mp3";
    private final String WILDFIREMUSICURL = "src/audio/ingame/wildfire.mp3";
    private final String HURRICANEMUSICURL = "src/audio/ingame/hurricane.wav";
    private final String FLOODMUSICURL = "src/audio/ingame/flood.mp3";
    private final String PESTMUSCIURL = "src/audio/ingame/pest.wav";
    private final String DROUGHTMUSICURL = "src/audio/ingame/drought.wav";
    private final String GRETASPEECH = "src/audio/ingame/gretaspeech.mp3";
    private final String CLIMATECHANGEBACKMUSIC = "src/audio/backmusic/climatechange.mp3";

    static {
        cssMap = new HashMap<>();
        cssMap.put(0, "gamesquare");
        cssMap.put(1, "square1");
        cssMap.put(2, "square2");
    }

    public GameView(String username, String ip) {
        this.username = username;
        this.ip = ip;
        new Thread(() -> connect(username, ip)).start();
        mainFrame = new Pane();
        mainFrame.setPrefSize(WIDTH, HEIGHT);
        createContent();
        Scene gameScene = new Scene(mainFrame);
        gameScene.getStylesheets().add("css/game.css");
        setScene(gameScene);
        setResizable(false);
        show();
        MediaControl.getInstance().pause();
        //comment out the next line if you don't want background music in game
        MediaControl.getInstance().play(INGAMEMUSICURL);
        new BackGroundAnimation(gameBoard);
    }

    private void createContent() {
        addBackground();
        addGameBoard();
        addRightPane();
    }

    private void addBackground() {
        Image imgBackground = new Image(getClass().getResourceAsStream(BACKURL));
        ImageView ivBackground = new ImageView(imgBackground);
        ivBackground.fitWidthProperty().bind(mainFrame.prefWidthProperty());
        ivBackground.fitHeightProperty().bind(mainFrame.prefHeightProperty());
        mainFrame.getChildren().add(ivBackground);
    }

    private void addGameBoard() {
        gameBoard = new GridPane();
        gameBoard.setPrefSize(640, 640);
        gameBoard.setLayoutX(45);
        gameBoard.setLayoutY(20);
        gameBoard.setHgap(2);
        gameBoard.setVgap(2);
        gameBoard.getStyleClass().add("gameBoard");
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_ROWS; j++) {
                GameSquare gs = new GameSquare(gardenWarClient);
                gs.setPrefSize((640 - (NUM_OF_ROWS - 1) * 2) / NUM_OF_ROWS, (640 - (NUM_OF_COLS - 1) * 2) / NUM_OF_COLS);
                gs.setLayoutX(j * gs.getPrefWidth());
                gs.setLayoutY(i * gs.getPrefHeight());
                gameBoard.add(gs, j, i);
                wireUpSquares(gs);
            }
        }
        mainFrame.getChildren().add(gameBoard);
    }

    private void wireUpSquares(GameSquare gs) {
        gs.setOnMouseClicked(e -> {
            gardenWarClient.send(new SquareClickRequest(GridPane.getRowIndex(gs), GridPane.getColumnIndex(gs)));
        });

    }


    private void addRightPane() {
        //create right pane
        rightPane = new VBox();
        rightPane.setLayoutX(705);
        rightPane.setLayoutY(20);
        rightPane.setPrefSize(475, 640);
        rightPane.getStyleClass().add("rightPane");
        //create player box that contains two players
        HBox playerBox = new HBox();
        playerBox.setPrefSize(475, 150);
        //embed both playerPanes
        playerBox.getChildren().addAll(playerPane1, playerPane2);
        rightPane.getChildren().add(playerBox);
        //add actionPane
        Pane actionPane = new Pane();
        actionPane.getStyleClass().add("actionPane");
        actionPane.setPrefSize(470, 480);
        actionPane.setLayoutX(0);
        actionPane.setLayoutY(160);
        //build a hbox for all buttons
        HBox buttonBox = new HBox(35);
        buttonBox.setPrefSize(435, 60);
        buttonBox.setLayoutX(20);
        buttonBox.setLayoutY(20);
        //building components fo the box
        ArrayList<ActionPaneButton> actionButtonsList = new ArrayList<>();
        actionButtonsList.add(btFarmer);
        actionButtonsList.add(btGov);
        actionButtonsList.add(btWeather);
        actionButtonsList.add(btInfo);
        actionButtonsList.add(btEndTurn);
        for (ActionPaneButton apb : actionButtonsList) {
            apb.setPrefSize(50, 50);
        }
        // putting all together
        buttonBox.getChildren().addAll(btFarmer, btGov, btWeather, btInfo, btEndTurn);
        buttonBox.setPadding(new Insets(0, 20, 0, 20));
        actionPane.getChildren().add(buttonBox);
        //add the line
        Line line = new Line();
        line.setStartX(5);
        line.setStartY(85);
        line.setEndX(465);
        line.setEndY(85);
        line.setStrokeWidth(1);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(1, Color.GOLDENROD));
        actionPane.getChildren().add(line);
        //add stackcontainer
        StackPane resourceContainer = new StackPane();
        resourceContainer.setPrefSize(435, 360);
        resourceContainer.setLayoutX(20);
        resourceContainer.setLayoutY(100);
        //add the resourceInfoPane
        VBox resourceInfoPane = new VBox();
        resourceInfoPane.getStyleClass().add("resourceInfoPane");
        resourceInfoPane.setPrefSize(435, 360);
        tvSeedsItems = new ItemTree(gardenWarClient, "Seeds Items", Category.MAIN);
        tvSeedsItems.getStyleClass().add("seedsTree");
        tvFarmerItems = new ItemTree(gardenWarClient, "Farmer Items", Category.MAIN);
        tvFarmerItems.getStyleClass().add("farmerTree");
        tvPolicyItems = new ItemTree(gardenWarClient, "Policy Items", Category.MAIN);
        tvPolicyItems.getStyleClass().add("policyTree");
        resourceInfoPane.getChildren().addAll(tvSeedsItems, tvFarmerItems, tvPolicyItems);
        //end of resourceInfoPane
        sliderPane = new SliderPane(gardenWarClient);
        sliderPane.prefWidthProperty().bind(resourceInfoPane.prefWidthProperty());
        sliderPane.prefHeightProperty().bind(resourceInfoPane.prefHeightProperty());
        sliderPane.setLayoutX(0);
        sliderPane.setLayoutY(0);
        sliderPane.setTranslateX(500);
        wireupUI();
        resourceContainer.getChildren().addAll(resourceInfoPane, sliderPane);
        actionPane.getChildren().add(resourceContainer);
        //add the actionPane
        rightPane.getChildren().add(actionPane);
        mainFrame.getChildren().add(rightPane);

    }

    /**
     * inject all change listeners into the ui
     */
    private void wireupUI() {
        btFarmer.setOnMouseClicked(e -> {
            sliderPane.build(Category.FARMERCENTER);
            slideIn(sliderPane);
        });

        btWeather.setOnMouseClicked(e -> {
            sliderPane.build(Category.WEATHER);
            slideIn(sliderPane);
        });

        btGov.setOnMouseClicked(e -> {
            sliderPane.build(Category.GOVERNMENT);
            slideIn(sliderPane);
        });
        btInfo.setOnMouseClicked(e -> {
            sliderPane.build(Category.INFORMATION);
            slideIn(sliderPane);
        });

        btEndTurn.setOnMouseClicked(e -> {
            gardenWarClient.send(new EndTurnRequest());
        });

        sliderPane.getBgBack().setOnMouseClicked(e -> {
            slideBack(sliderPane);
        });

        rightPane.setOnMouseClicked(e -> {
            gardenWarClient.send("UNSELECTALL");
        });
    }


    private void slideIn(SliderPane sliderPane) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), sliderPane);
        tt.setToX(0);
        tt.play();
        sliderPane.setCache(true);
        sliderPane.setCacheHint(CacheHint.SPEED);
    }

    private void slideBack(SliderPane sliderPane) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), sliderPane);
        tt.setToX(500);
        tt.play();
        sliderPane.setCache(true);
        sliderPane.setCacheHint(CacheHint.SPEED);
    }

    //function to retrieve a Node of a particular row and column index
    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    private void connect(String username, String ip) {
        //GardenWarClient c;
        try {
            gardenWarClient = new GardenWarClient(ip, 8000);
            Platform.runLater(() -> {
                gardenWarClient.send(new LogInMessage(username));
            });
        } catch (Exception ex) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Sorry, could not connect to \n"
                                + ip + " on port 8000." + "\n" + "Shutting down");
                alert.showAndWait();
                System.exit(0);
            });
        }
    }

    private void doQuit() {
        close();
    }

    public class GardenWarClient extends Client {

        /**
         * Connect to a PokerHub at a specified hostname and port number.
         */
        public GardenWarClient(String hubHostName, int hubPort) throws IOException {
            super(hubHostName, hubPort);
        }

        /**
         * This method is called when a message from the hub is received
         * by this client.  If the message is of type PokerGameState,
         * then the newState() method in the PokerWindow class is called
         * to handle the change in the state of the game.  If the message
         * is of type String, it represents a message that is to be
         * displayed to the user; the string is displayed as
         * messageFromServer.  If the message is of type PokerCard[],
         * then it is the opponent's hand.  That hand is sent when the
         * game has ended and the player gets to see the opponent's hand.
         * <p>Note that this method is called from a separate thread, not
         * from the GUI event thread.  In order to avoid synchronization
         * issues, this method uses Platform.runLater() to carry
         * out its task in the GUI event thread.
         */
        protected void messageReceived(final Object message) {
            Platform.runLater(() -> {
                if (message instanceof LogInUpdate) {
                    ArrayList<String> nameList = ((LogInUpdate) message).getUserNames();
                    if (nameList.size() == 1) {
                        playerPane1.getTxtUserName().setText(nameList.get(0));
                    } else if (nameList.size() == 2) {
                        playerPane1.getTxtUserName().setText(nameList.get(0));
                        playerPane2.getTxtUserName().setText(nameList.get(1));
                    }
                } else if(message instanceof EndGameSignal) {
                    EndGameSignal endGameSignal = (EndGameSignal) message;
                    String msg = endGameSignal.getMessage();
                    ItemDescriptionPane itemDescriptionPane = new ItemDescriptionPane(msg);
                    itemDescriptionPane.show();
                    itemDescriptionPane.setOnCloseRequest(e->{
                        close();
                    });

                } else if (message instanceof EventUpdate) {
                    EventUpdate update = (EventUpdate) message;
                    String msg = update.getMessage();
                    ItemDescriptionPane itemDescriptionPane = new ItemDescriptionPane(msg);
                    itemDescriptionPane.show();
                } else if (message instanceof TurnChangeUpdate) {
                    TurnChangeUpdate update = (TurnChangeUpdate) message;
                    int playerID = update.getCurrentID();
                    getPlayerPane(playerID).setEffect(new Glow(1.5));
                    getPlayerPane(3 - playerID).setEffect(null);
                } else if (message instanceof FoodUpdate) {
                    FoodUpdate update = (FoodUpdate) message;
                    int playerID = update.getPlayerID();
                    int x = update.getX();
                    int y = update.getY();
                    int increment = update.getIncrement();
                    animateFoodUpdate(playerID, x, y, increment);
                } else if (message instanceof StatsUpdate) {
                    StatsUpdate update = (StatsUpdate) message;
                    int id = update.getId();
                    String name = update.getStatItem();
                    int newQuant = update.getNewQuant();
                    updateStats(id, newQuant, name);
                } else if (message instanceof PurchaseUpdate) {
                    PurchaseUpdate update = (PurchaseUpdate) message;
                    int quantity = update.getNewQuant();
                    String name = update.getPurchasedItem();
                    updatePurchase(false, quantity, name);
                } else if (message instanceof SellUpdate) {
                    SellUpdate update = (SellUpdate) message;
                    int quantity = update.getQuantity();
                    String name = update.getItemName();
                    updatePurchase(true, quantity, name);
                } else if (message instanceof SelectionUpdate) {
                    SelectionUpdate update = (SelectionUpdate) message;
                    int playerID = update.getPlayerID();
                    HashSet<Integer[]> coords = update.getCoords();
                    lightUpForSelection(playerID, coords);
                } else if (message instanceof PlantInfoUpdate) {
                    PlantInfoUpdate update = (PlantInfoUpdate) message;
                    int playerID = update.getPlayerID();
                    int x = update.getX();
                    int y = update.getY();
                    int totalTurn = update.getTotalTurn();
                    int turnsAchieved = update.getTurnsAchieved();
                    UpdatePlantInfoOnSquare(playerID, x, y, totalTurn, turnsAchieved);
                } else if (message instanceof ImageUpdate) {
                    ImageUpdate update = (ImageUpdate) message;
                    String name = update.getItemName();
                    int playerID = update.getPlayerID();
                    int x = update.getX();
                    int y = update.getY();
                    String url = GameItem.itemUrlMap.get(name);
                    AnimationStrategy animationStrategy = update.getAnimationStrategy();
                    animateImageUpdate(playerID, animationStrategy, x, y, url);

                } else if (message instanceof OwnerShipUpdate) {
                    OwnerShipUpdate update = (OwnerShipUpdate) message;
                    int playerID = update.getPlayerID();
                    int x = update.getX();
                    int y = update.getY();
                    //setting borders to differentiate sides
                    getNodeByRowColumnIndex(x, y, gameBoard).getStyleClass().add(cssMap.get(playerID));
                } else if (message instanceof String) {
                    String s = (String) message;
                    if (s.equals("UNSELECTALL")) {
                        cancelAllEffects();
                    }
                } else if (message instanceof WeatherUpdate) {
                    WeatherUpdate update = (WeatherUpdate) message;
                    int temperature = update.getTemperature();
                    int hurricane = update.getHurricane();
                    int wildfire = update.getWildfire();
                    int flood = update.getFlood();
                    int drought = update.getDrought();
                    int pest = update.getPest();
                    renderSliderPane(temperature, hurricane, wildfire, flood, drought, pest);
                }
            });
        }

        /**
         * animate image update according to player side as well as the animation strategy requested
         *
         * @param id
         * @param animationStrategy
         */
        private void animateImageUpdate(int id, AnimationStrategy animationStrategy, int x, int y, String url) {
            Platform.runLater(() -> {
                Color color1 = Color.color(0.65, 0.05, 0.43, 1);
                Color color2 = Color.color(0.07, 0.76, 0.17, 1);
                Color final1 = Color.color(1, 0.00, 0.43, 1);
                Color final2 = Color.color(0.07, 1, 0.17, 1);
                Color[] colors = {color1, color2};
                Color[] finalColors = {final1, final2};
                //get rid of the maturity text if image changed
                if (!animationStrategy.equals(AnimationStrategy.PLANTEVOLVE) &&
                        !animationStrategy.equals(AnimationStrategy.FERTILIZER) &&
                        !animationStrategy.equals(AnimationStrategy.TREEPROTECTED)) {
                    ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).getTxtMaturity()
                            .setText("");
                }
                //if it is not about plant evolving, get rid of the fertilizer effect
                if (!animationStrategy.equals(AnimationStrategy.PLANTEVOLVE)) {
                    ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).getIvItem().setEffect(null);
                }
                //plants getting tree protection
                if (animationStrategy.equals(AnimationStrategy.TREEPROTECTED)) {
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setRadius(10.0);
                    dropShadow.setOffsetX(3.0);
                    dropShadow.setOffsetY(3.0);
                    dropShadow.setColor(Color.GREEN);
                    ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).getIvItem().setEffect(dropShadow);
                }
                //animate plant evolutions
                if (animationStrategy.equals(AnimationStrategy.PLANTEVOLVE) ||
                        animationStrategy.equals(AnimationStrategy.COLLECT)) {
                    Glow initGlow = new Glow(0.1);
                    Light.Distant light = new Light.Distant();
                    light.setAzimuth(45.0);
                    light.setElevation(45.0);
                    light.setColor(colors[id - 1]);
                    Lighting initLighting = new Lighting();
                    initLighting.setLight(light);
                    initGlow.setInput(initLighting);
                    getNodeByRowColumnIndex(x, y, gameBoard).setEffect(initGlow);
                    Timeline timeline = new Timeline();
                    timeline.getKeyFrames().setAll(
                            new KeyFrame(Duration.ZERO,
                                    new KeyValue(light.colorProperty(), light.getColor()),
                                    new KeyValue(initGlow.levelProperty(), initGlow.getLevel())
                            ),
                            new KeyFrame(Duration.seconds(1.0),
                                    new KeyValue(light.colorProperty(), finalColors[id - 1]),
                                    new KeyValue(initGlow.levelProperty(), 1.5)
                            )
                    );
                    timeline.setAutoReverse(false);
                    timeline.setCycleCount(1);
                    timeline.play();
                    timeline.setOnFinished(e -> {
                        getNodeByRowColumnIndex(x, y, gameBoard).setEffect(null);
                        ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).addLayerImage(url);
                        getNodeByRowColumnIndex(x, y, gameBoard).getStyleClass().clear();
                        if (animationStrategy.equals(AnimationStrategy.PLANTEVOLVE)) {
                            (getNodeByRowColumnIndex(x, y, gameBoard)).getStyleClass().add("ivPlant" + id);
                        } else {
                            (getNodeByRowColumnIndex(x, y, gameBoard)).getStyleClass().add("gamesquare");
                        }
                    });
                } else if (animationStrategy.equals(AnimationStrategy.PESTICIDE)) {
                    Group group = new Group();
                    group.setManaged(false);
                    double animationWidth = ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).getPrefWidth();
                    double animationHeight = ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).getPrefHeight();
                    double initX = getNodeByRowColumnIndex(x, y, gameBoard).getLayoutX() + animationWidth / 2;
                    double initY = getNodeByRowColumnIndex(x, y, gameBoard).getLayoutY() + ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).getPrefHeight();
                    //build smokes
                    ArrayList<Circle> smokes = new ArrayList<>();
                    for (int i = 0; i < 50; i++) {
                        Circle smoke = new Circle(initX + animationWidth * Math.random(),
                                initY + animationHeight * Math.random(), 10);
                        smoke.setManaged(false);
                        smoke.setFill(Color.WHITESMOKE);
                        smoke.setEffect(new GaussianBlur(200));
                        smoke.setCache(true);
                        smoke.setCacheHint(CacheHint.SPEED);
                        smokes.add(smoke);
                        group.getChildren().add(smoke);
                        mainFrame.getChildren().add(smoke);
                    }
                    mainFrame.getChildren().add(group);
                    // build parallel transition
                    ParallelTransition pt = new ParallelTransition();
                    for (Circle circle : smokes) {
                        Path path = new Path();
                        path.setVisible(false);
                        path.getElements().add(new MoveTo(circle.getCenterX(), circle.getCenterY()));
                        path.getElements().add(new LineTo(
                                initX + animationWidth * Math.random(),
                                circle.getCenterY() - animationHeight * Math.random()));
                        PathTransition pathTransition = new PathTransition();
                        pathTransition.setDuration(Duration.millis(1000));
                        pathTransition.setNode(circle);
                        pathTransition.setPath(path);
                        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        pathTransition.setCycleCount(1);
                        pt.getChildren().add(pathTransition);
                    }
                    pt.play();
                    pt.setOnFinished(e -> {
                        for (Circle smoke : smokes) {
                            smoke.setVisible(false);
                        }
                        getNodeByRowColumnIndex(x, y, gameBoard).setEffect(null);
                        ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).addLayerImage(url);
                    });
                } else if (animationStrategy.equals(AnimationStrategy.FERTILIZER)) {
                    //DO NOT CHANGED IMAGE
                    GameSquare s = ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard));
                    Glow glow = new Glow(1);
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setColor(Color.GOLD);
                    glow.setInput(dropShadow);
                    s.getIvItem().setEffect(glow);
                } else if (animationStrategy.equals(AnimationStrategy.HURRICANE)) {
                    MediaControl.getInstance().playGameSound(HURRICANEMUSICURL);
                    ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).addLayerImage(url);
                    getNodeByRowColumnIndex(x, y, gameBoard).getStyleClass().clear();
                    (getNodeByRowColumnIndex(x, y, gameBoard)).getStyleClass().add("gamesquare");

                } else if (animationStrategy.equals(AnimationStrategy.PEST)) {
                    MediaControl.getInstance().playGameSound(PESTMUSCIURL);
                    ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).addLayerImage(url);
                    getNodeByRowColumnIndex(x, y, gameBoard).getStyleClass().clear();
                    (getNodeByRowColumnIndex(x, y, gameBoard)).getStyleClass().add("gamesquare");
                } else if (animationStrategy.equals(AnimationStrategy.DROUGHT)) {
                    MediaControl.getInstance().playGameSound(DROUGHTMUSICURL);
                    ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).addLayerImage(url);
                    getNodeByRowColumnIndex(x, y, gameBoard).getStyleClass().clear();
                    (getNodeByRowColumnIndex(x, y, gameBoard)).getStyleClass().add("gamesquare");
                } else if (animationStrategy.equals(AnimationStrategy.FLOOD)) {
                    MediaControl.getInstance().playGameSound(FLOODMUSICURL);
                    ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).addLayerImage(url);
                    getNodeByRowColumnIndex(x, y, gameBoard).getStyleClass().clear();
                    (getNodeByRowColumnIndex(x, y, gameBoard)).getStyleClass().add("gamesquare");
                } else if (animationStrategy.equals(AnimationStrategy.WILDFIRE)) {
                    MediaControl.getInstance().playGameSound(WILDFIREMUSICURL);
                    ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard)).addLayerImage(url);
                    getNodeByRowColumnIndex(x, y, gameBoard).getStyleClass().clear();
                    (getNodeByRowColumnIndex(x, y, gameBoard)).getStyleClass().add("gamesquare");
                } else if (animationStrategy.equals(AnimationStrategy.PLOWER)) {

                    ParallelTransition pt = new ParallelTransition();
                    ArrayList<Circle> particles = new ArrayList<>();
                    GameSquare square = ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard));
                    double originalOpacity = square.getIvItem().getOpacity();
                    double xOffSet = square.getLayoutX();
                    double yOffSet = square.getLayoutY();
                    double imgWidth = square.getIvItem().getFitWidth();
                    Image snapshot = square.getIvItem().snapshot(null, null);
                    for (int j = 0; j < snapshot.getHeight(); j = j + 3) {
                        for (int i = 0; i < snapshot.getWidth(); i = i + 3) {
                            Circle circle = new Circle(xOffSet + i + 1, yOffSet + j + 1.5, 1);
                            circle.setFill(snapshot.getPixelReader().getColor(x, y));
                            particles.add(circle);
                            circle.setManaged(false);
                            gameBoard.getChildren().add(circle);
                        }
                    }
                    //animations
                    FadeTransition ft = new FadeTransition(Duration.millis(1000), square.getIvItem());
                    ft.setFromValue(0.0);
                    ft.setToValue(1);
                    ft.setAutoReverse(true);
                    ft.setCycleCount(2);
                    for (Circle circle : particles) {
                        Path path = new Path();
                        path.setVisible(false);
                        path.getElements().add(new MoveTo(circle.getCenterX(), circle.getCenterY()));
                        path.getElements().add(new LineTo(xOffSet + imgWidth * Math.random(), 0));
                        PathTransition pathTransition = new PathTransition();
                        pathTransition.setDuration(Duration.millis(3000));
                        pathTransition.setNode(circle);
                        pathTransition.setPath(path);
                        pathTransition.setCycleCount(1);
                        pt.getChildren().add(pathTransition);
                    }
                    pt.getChildren().add(ft);
                    pt.play();

                    pt.setOnFinished(e -> {
                        for (Circle circle : particles) {
                            gameBoard.getChildren().remove(circle);
                        }
                        square.setEffect(null);
                        square.addLayerImage(url);
                        //give the original opacity to the imageview
                        square.getIvItem().setOpacity(originalOpacity);
                    });

                }

            });

        }

        private void cancelAllEffects() {
            for (int i = 0; i < NUM_OF_ROWS; i++) {
                for (int j = 0; j < NUM_OF_COLS; j++) {
                    getNodeByRowColumnIndex(i, j, gameBoard).setEffect(null);
                }
            }
        }

        /**
         * gets the player pane of the corresponding player with playerID = id
         *
         * @param id
         * @return
         */
        private PlayerPane getPlayerPane(int id) {
            if (id == 1) {
                return playerPane1;
            } else {
                return playerPane2;
            }
        }

        /**
         * light up for selection
         *
         * @param playerID
         * @param coords
         */
        private void lightUpForSelection(int playerID, HashSet<Integer[]> coords) {
            for (Integer[] ints : coords) {
                getNodeByRowColumnIndex(ints[0], ints[1], gameBoard).
                        setEffect(getPlayerPane(playerID).getLightUp());
            }

        }

        /**
         * visually update stats
         *
         * @param id
         * @param newQuant
         * @param itemName
         */
        private void updateStats(int id, int newQuant, String itemName) {

            if (itemName.equals("food")) {
                boolean increased = (newQuant - Integer.parseInt(getPlayerPane(id).getTxtFood().getText()) > 0);
                animateStatsChange(newQuant, getPlayerPane(id).getTxtFood(), increased);
            } else if (itemName.equals("consumption")) {
                boolean increased = (newQuant - Integer.parseInt(getPlayerPane(id).getTxtConsumption().getText()) > 0);
                animateStatsChange(newQuant, getPlayerPane(id).getTxtConsumption(), increased);

            } else if (itemName.equals("actionpoints")) {
                boolean increased = (newQuant - Integer.parseInt(getPlayerPane(id).getTxtActionPoints().getText()) > 0);
                animateStatsChange(newQuant, getPlayerPane(id).getTxtActionPoints(), increased);
            } else if (itemName.equals("earning")) {
                boolean increased = (newQuant - Integer.parseInt(getPlayerPane(id).getTxtEarning().getText()) > 0);
                animateStatsChange(newQuant, getPlayerPane(id).getTxtEarning(), increased);
            }
        }

        /**
         * animate any stats update inside player pane
         *
         * @param newQuant  {int} the new quantity to be put into the player pane
         * @param text      {Text} the Text component to conduct the fill transition animation
         * @param increased {boolean} indicates whether it was an increase to determine the
         *                  color of the animation
         */
        private void animateStatsChange(int newQuant, Text text, boolean increased) {
            Color color;
            if (increased) {
                color = Color.GREEN;
            } else {
                color = Color.RED;
            }
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0), new KeyValue(text.fillProperty(), text.getFill())),
                    new KeyFrame(Duration.seconds(1.2), new KeyValue(text.fillProperty(), color))
            );
            timeline.setCycleCount(1);
            timeline.play();
            timeline.setOnFinished(e -> {
                Platform.runLater(() -> {
                    text.setText(String.valueOf(newQuant));
                    text.getStyleClass().add("txtPlayerInfo");
                });

            });
        }

        private void updatePurchase(boolean sell, int quantity, String name) {
            if (name.equals("PATATO SEED") || name.equals("PUMPKIN SEED") || name.equals("BEANS SEED")
                    || name.equals("GOURD SEED") || name.equals("RADISH SEED") || name.equals("SPINACH SEED")) {
                if (!sell) {
                    if (quantity == 1) {
                        tvSeedsItems.addItem(new GameItem(name, 1));
                    } else {
                        tvSeedsItems.removeItem(name);
                        tvSeedsItems.addItem(new GameItem(name, quantity));
                    }
                } else {
                    if (quantity == 0) {
                        tvSeedsItems.removeItem(name);
                    } else {
                        tvSeedsItems.removeItem(name);
                        tvSeedsItems.addItem(new GameItem(name, quantity));
                    }
                }
            } else if (name.equals("FERTILIZER") || name.equals("PESTICIDE") || name.equals("PLOWER") || name.equals("COLLECT")) {
                if (!sell) {
                    if (quantity == 1) {
                        tvFarmerItems.addItem(new GameItem(name, 1));
                    } else {
                        tvFarmerItems.removeItem(name);
                        tvFarmerItems.addItem(new GameItem(name, quantity));
                    }
                } else {
                    if (quantity == 0) {
                        tvFarmerItems.removeItem(name);
                    } else {
                        tvFarmerItems.removeItem(name);
                        tvFarmerItems.addItem(new GameItem(name, quantity));
                    }
                }
            } else if (name.equals("STARVATION") || name.equals("PLANT TREE") || name.equals("GREEN ENERGY")
                    || name.equals("ACCEPT REFUGEES") || name.equals("WAR")) {
                System.out.println("GOV item sell received");
                if (!sell) {
                    if (quantity == 1) {
                        tvPolicyItems.addItem(new GameItem(name, 1));
                    } else {
                        tvPolicyItems.removeItem(name);
                        tvPolicyItems.addItem(new GameItem(name, quantity));
                    }
                } else {
                    if (quantity == 0) {
                        tvPolicyItems.removeItem(name);
                    } else {
                        tvPolicyItems.removeItem(name);
                        tvPolicyItems.addItem(new GameItem(name, quantity));
                    }
                }
            }
        }

        /**
         * builds the animation for food increment update
         *
         * @param id        {int} plant owner id
         * @param x         {int} plant x coordinate
         * @param y         {int} plant y coordinate
         * @param increment {int} food gained through collection
         */
        private void animateFoodUpdate(int id, int x, int y, int increment) {
            Text txtAnim = new Text("+" + increment);
            txtAnim.setFont(Font.font("Verdana", FontPosture.REGULAR, 30));
            txtAnim.setFill(Color.GREEN);
            GameSquare gameSquare = (GameSquare) getNodeByRowColumnIndex(x, y, gameBoard);
            txtAnim.setLayoutX((gameSquare.getLayoutX() + 30));
            txtAnim.setLayoutY((gameSquare.getLayoutY() + 40));
            //set the position
            txtAnim.setManaged(false);
            gameBoard.getChildren().add(txtAnim);
            double currentYpos = txtAnim.getLayoutY();
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().setAll(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(txtAnim.layoutYProperty(), currentYpos)
                    ),
                    new KeyFrame(Duration.millis(3000),
                            new KeyValue(txtAnim.layoutYProperty(), currentYpos - 60)
                    )
            );
            timeline.setAutoReverse(false);
            timeline.setCycleCount(1);
            timeline.play();
            timeline.setOnFinished(e -> {
                gameBoard.getChildren().remove(txtAnim);
            });

        }

        private void renderSliderPane(int temperature, int hurricane, int wildfire, int flood, int drought, int pest) {
            sliderPane.getTxtTemperature().setText(temperature + "C");
            sliderPane.getGiHurri().setQuantity(hurricane);
            sliderPane.getGiWildfire().setQuantity(wildfire);
            sliderPane.getGiFlood().setQuantity(flood);
            sliderPane.getGiDrought().setQuantity(drought);
            sliderPane.getGiPest().setQuantity(pest);
            if (temperature == 22) {
                Platform.runLater(() -> {
                    MediaControl.getInstance().playGameSound(GRETASPEECH);
                    MediaControl.getInstance().pause();
                    MediaControl.getInstance().play(CLIMATECHANGEBACKMUSIC);
                });

            }
        }

        /**
         * reflect the plant information on the layer pane of the corresponding square
         *
         * @param playerId {int} plant's owner's id
         * @param x        {int} plant's square x coordinate
         * @param y        {int} plant's square y coordinate
         * @param total    {int} total turns for plant to go from seed to mature
         * @param achieved {int} number of turns the seed has waited for
         */
        private void UpdatePlantInfoOnSquare(int playerId, int x, int y, int total, int achieved) {
            Platform.runLater(() -> {
                if (achieved == total) {
                    ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard))
                            .getTxtMaturity().setText("Mat");
                } else {
                    ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard))
                            .getTxtMaturity().setText(achieved + "/" + total);
                }
                ((GameSquare) getNodeByRowColumnIndex(x, y, gameBoard))
                        .getTxtMaturity().getStyleClass().add("txtMaturity" + playerId);
            });

        }

    }

}
