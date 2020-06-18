package client.uicomponents;

import common.PurchaseRequest;
import common.SellRequest;
import common.UseRequest;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class CustomCell extends TreeCell<GameItem> {
    private ImageView ivItem;
    private Label label;
    private Button btFirst;
    private Button btSecond;
    private Category category;
    private GridPane playerInfo;
    private Text txtQuantity;
    private GameView.GardenWarClient client;
    private final String BTUSESOUND = "src/audio/ingame/btuse.mp3";
    private final String BTSELLSOUND = "src/audio/ingame/btsell.mp3";
    private final String MENUCLICKSOUNDURL = "src/audio/click_hover/buttonclick.wav";

    public CustomCell(GameView.GardenWarClient client, Category category) {
        this.client = client;
        this.category = category;
    }

    @Override
    protected void updateItem(GameItem item, boolean empty) {
        super.updateItem(item, empty);
        // If the cell is empty we don't show anything.
        if (isEmpty()) {
            setGraphic(null);
            setText(null);

        } else {
            // We only show the custom cell if it is a leaf, meaning it has
            // no children.
            if (this.getTreeItem().isLeaf() && this.getTreeItem().getParent() != null) {
                playerInfo = new GridPane();
                playerInfo.setPrefSize(300, 20);
                ColumnConstraints column1 = new ColumnConstraints();
                column1.setPercentWidth(20);
                ColumnConstraints column2 = new ColumnConstraints();
                column2.setPercentWidth(40);
                ColumnConstraints column3 = new ColumnConstraints();
                column3.setPercentWidth(10);
                ColumnConstraints column4 = new ColumnConstraints();
                column4.setPercentWidth(15);
                ColumnConstraints column5 = new ColumnConstraints();
                column5.setPercentWidth(15);
                playerInfo.getColumnConstraints().addAll(column1, column2, column3, column4, column5);
                ivItem = new ImageView(new Image(getClass().getResourceAsStream(item.getImgUrl())));
                ivItem.setFitWidth(22);
                ivItem.setFitHeight(22);
                txtQuantity = new Text();
                if (category.equals(Category.MAIN)) {
                    txtQuantity.setText(String.valueOf(item.getQuantity()));
                } else if (category.equals(Category.FARMERCENTER)||
                        category.equals(Category.GOVERNMENT)) {
                    txtQuantity.setText(item.getQuantity() + "pt");
                } else if (category.equals(Category.WEATHER)) {
                    txtQuantity.setText(item.getQuantity() + "%");
                }
                txtQuantity.getStyleClass().add("txtResource");
                wireUpCategory(item);
                label = new Label(item.getName());
                label.getStyleClass().add("lblResource");
                GridPane.setHalignment(label, HPos.LEFT);
                GridPane.setHalignment(txtQuantity, HPos.LEFT);
                GridPane.setHalignment(btFirst, HPos.LEFT);
                GridPane.setHalignment(btSecond, HPos.LEFT);
                playerInfo.add(ivItem, 0, 0);
                playerInfo.add(label, 1, 0);
                playerInfo.add(txtQuantity, 2, 0);
                playerInfo.add(btFirst, 3, 0);
                playerInfo.add(btSecond, 4, 0);
                // We set the cellBox as the graphic of the cell.
                setGraphic(playerInfo);
                setText(null);
            } else {
                // If this is the root we just display the text.
                setGraphic(null);
                setText(item.getName());
            }
        }
    }

    private void wireUpCategory(GameItem item) {
        if (category.equals(Category.MAIN)) {
            btFirst = new Button("Use");
            btFirst.setPrefWidth(45);
            btFirst.getStyleClass().add("btUseSell");
            btFirst.setOnAction(e->{
                client.send(new UseRequest(item.getName()));
            });
            btSecond = new Button("Sell");
            btSecond.setPrefWidth(45);
            btSecond.getStyleClass().add("btUseSell");
            btSecond.setOnAction(e->{
                MediaControl.getInstance().playGameSound(BTSELLSOUND);
                client.send(new SellRequest(item.getName()));
            });
        } else if (category.equals(Category.FARMERCENTER)) {
            btFirst = new Button("Buy");
            btFirst.setPrefWidth(45);
            btFirst.getStyleClass().add("btUseSell");
            btFirst.setOnAction(e->{
                MediaControl.getInstance().playGameSound(BTUSESOUND);
                client.send(new PurchaseRequest(item.getName()));
            });
            btSecond = new Button("Info");
            btSecond.setPrefWidth(45);
            btSecond.getStyleClass().add("btUseSell");
            btSecond.setOnAction(e->{
                showInfo(item);
            });
        } else if (category.equals(Category.GOVERNMENT)) {
            btFirst = new Button("Buy");
            btFirst.setPrefWidth(45);
            btFirst.getStyleClass().add("btUseSell");
            btFirst.setOnAction(e->{
                MediaControl.getInstance().playGameSound(BTUSESOUND);
                client.send(new PurchaseRequest(item.getName()));
            });
            btSecond = new Button("info");
            btSecond.setPrefWidth(45);
            btSecond.getStyleClass().add("btUseSell");
            btSecond.setOnAction(e->{
                showInfo(item);
            });
        } else if (category.equals(Category.WEATHER)) {
            btFirst = new Button("Info");
            btFirst.setPrefWidth(45);
            btFirst.getStyleClass().add("btUseSell");
            btSecond = new Button();
            btSecond.setVisible(false);
            btSecond.setDisable(true);
            btFirst.setOnAction(e->{
                showInfo(item);
            });
        }

    }

    private void showInfo(GameItem item){
        MediaControl.getInstance().playGameSound(MENUCLICKSOUNDURL);
        ItemDescriptionPane itemDescriptionPane = new ItemDescriptionPane(item);
        itemDescriptionPane.show();
    }


}
