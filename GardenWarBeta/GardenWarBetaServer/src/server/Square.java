package server;

public class Square {
    private int xCoor;
    private int yCoor;
    private Plant plant = null;
    private Usability usability = Usability.NORMAL;
    private boolean selected = false;

    public Square(){
    }

    public int getxCoor() {
        return xCoor;
    }

    public void setxCoor(int xCoor) {
        this.xCoor = xCoor;
    }

    public int getyCoor() {
        return yCoor;
    }

    public void setyCoor(int yCoor) {
        this.yCoor = yCoor;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Usability getUsability() {
        return usability;
    }

    public void setUsability(Usability usability) {
        this.usability = usability;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public enum Usability{
        NORMAL, USED, FLOODED, BURNED, HURRICANED, PESTED, DROUGHTED, TREE, TREEPROTECTED
    }
}
