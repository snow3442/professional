package client.uicomponents;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class DescriptionArea extends Pane {

    private Text txtDescription = new Text();
    private Category category;
    private final String WEATHERDESCRITPION = "Global temperature rises by 1C per year. Each 1C raise will" +
            "increase chances of all disaster events. When temperature reaches 22C, all disasters will" +
            "have 100% chances of occurring! ";
    private final String GOVDESCRIPTION = "Here, you can purchase policy items to help better survive!";
    private final String INFOFARMER = "Farmer Center"+"\n\t"+ "Purchase farming tools and seeds";
    private final String INFOWEATHER = "Weather Report"+"\n\t"+ "View the probabilities of extreme weather events. ";
    private final String INFOGOV = "Government"+"\n\t"+"Purchase policy item to manage longer survival.";

    public DescriptionArea(Category category){
        this.category = category;
        txtDescription.getStyleClass().add("descriptionText");
        txtDescription.layoutXProperty().bind(layoutXProperty().add(20));
        txtDescription.layoutYProperty().bind(layoutYProperty().add(20));
        txtDescription.wrappingWidthProperty().bind(prefWidthProperty().subtract(20));
        getChildren().add(txtDescription);
        if(category.equals(Category.INFOFARMER)){
            txtDescription.setText(INFOFARMER);
        }
        else if(category.equals(Category.INFOGOV)){
            txtDescription.setText(INFOGOV);
        }
        else if(category.equals(Category.INFOWEATHER)){
            txtDescription.setText(INFOWEATHER);
        }
        else if(category.equals(Category.GOVERNMENT)){
            txtDescription.setText(GOVDESCRIPTION);
        }
        else if(category.equals(Category.WEATHER)){
            txtDescription.setText(WEATHERDESCRITPION);
        }

    }




}
