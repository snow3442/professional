package client.uicomponents;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class ItemTree extends TreeView<GameItem> {
    //default constructor
    private String name;
    private TreeItem<GameItem> treeItem; //serves as the tree control
    private Category category;

    public ItemTree(GameView.GardenWarClient client, String name, Category category) {
        this.name = name;
        this.category = category;
        //default quantity is -1 for tree names
        treeItem = new TreeItem<GameItem>(new GameItem(name, -1));
        treeItem.setExpanded(true);
        //always make the treeItem expanded
        treeItem.addEventHandler(TreeItem.branchCollapsedEvent(),
                new EventHandler<TreeItem.TreeModificationEvent<String>>() {
                    @Override
                    public void handle(TreeItem.TreeModificationEvent<String> event) {
                        event.getTreeItem().setExpanded(true);
                    }
                });
        setRoot(treeItem);
        setCellFactory(e -> new CustomCell(client, category));
    }

    /**
     * returns the GameItem with name that matches searchText
     *
     * @param searchText
     * @return
     */
    public GameItem retrieveItem(String searchText) {
        for (TreeItem<GameItem> child : treeItem.getChildren()) {
            if (child.getValue().getName().equals(searchText)) {
                return child.getValue();
            }
        }
        return null;
    }

    /**
     * add gameItem to the treeItem
     *
     * @param gameItem
     */
    public void addItem(GameItem gameItem) {
        treeItem.getChildren().add(new TreeItem<GameItem>(gameItem));
    }

    /**
     * removes gameItem from treeItem
     *
     * @param itemName
     */
    public void removeItem(String itemName) {
        for (TreeItem<GameItem> child : treeItem.getChildren()) {
            if (((GameItem) child.getValue()).getName().equals(itemName)) {
                treeItem.getChildren().remove(child);
                break;
            }
        }
    }

    public String getName() {
        return name;
    }
}
