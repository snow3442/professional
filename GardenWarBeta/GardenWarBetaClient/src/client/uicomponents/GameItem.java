package client.uicomponents;

import java.util.HashMap;
import java.util.Map;

public class GameItem {
    private String imgUrl;
    private String name;
    private int quantity;
    private String description;
    public static final Map<String, String> itemUrlMap;
    //Parameter Names to Parameter Description Map
    static {
        itemUrlMap = new HashMap<>();
        //seed items
        itemUrlMap.put("PATATO SEED", "../../images/gameicons/patatoseed.png");
        itemUrlMap.put("PUMPKIN SEED", "../../images/gameicons/pumpkinseed.png");
        itemUrlMap.put("BEANS SEED", "../../images/gameicons/beansseed.png");
        itemUrlMap.put("GOURD SEED", "../../images/gameicons/gourdseed.png");
        itemUrlMap.put("RADISH SEED", "../../images/gameicons/radishseed.png");
        itemUrlMap.put("SPINACH SEED", "../../images/gameicons/spinachseed.png");
        //plant items
        itemUrlMap.put("PATATO", "../../images/gameicons/patato.png");
        itemUrlMap.put("PUMPKIN", "../../images/gameicons/pumpkin.png");
        itemUrlMap.put("BEANS", "../../images/gameicons/beans.png");
        itemUrlMap.put("GOURD", "../../images/gameicons/gourd.png");
        itemUrlMap.put("RADISH", "../../images/gameicons/radish.png");
        itemUrlMap.put("SPINACH", "../../images/gameicons/spinach.png");
        //farm items
        itemUrlMap.put("FERTILIZER", "../../images/gameicons/fertilizer.png");
        itemUrlMap.put("PESTICIDE", "../../images/gameicons/pesticide.png");
        itemUrlMap.put("PLOWER", "../../images/gameicons/plower.png");
        itemUrlMap.put("COLLECT", "../../images/gameicons/collect.png");
        //disaster items
        itemUrlMap.put("PEST","../../images/gameicons/pest.png");
        itemUrlMap.put("HURRICANE", "../../images/gameicons/hurricane.png");
        itemUrlMap.put("DROUGHT", "../../images/gameicons/drought.png");
        itemUrlMap.put("FLOOD", "../../images/gameicons/flooding.png");
        itemUrlMap.put("WILDFIRE", "../../images/gameicons/wildfire.png");
        //problem items
        itemUrlMap.put("REBEL","../../images/gameicons/rebel.png");
        //policy items
        itemUrlMap.put("STARVATION", "../../images/gameicons/starvation.png");
        itemUrlMap.put("PLANT TREE","../../images/gameicons/planttree.png");
        itemUrlMap.put("GREEN ENERGY","../../images/gameicons/greenenergy.png");
        itemUrlMap.put("ACCEPT REFUGEES","../../images/gameicons/refugee.png");
        itemUrlMap.put("WAR","../../images/gameicons/war.png");
        itemUrlMap.put("NORMAL","../../images/backgrounds/ground.jpg");
        itemUrlMap.put("USED","../../images/gameicons/used.png");

        //miscellaneous

    }

    private static final Map<String, String> itemDescriptionMap;

    static{
        itemDescriptionMap = new HashMap<>();
        //seed items
        itemDescriptionMap.put("PATATO SEED", "Seeds for planting 1 unit of patato");
        itemDescriptionMap.put("PUMPKIN SEED", "Seeds for planting 1 unit of pumpkin");
        itemDescriptionMap.put("BEANS SEED", "Seeds for planting 1 unit of beans");
        itemDescriptionMap.put("GOURD SEED", "Seeds for planting 1 unit of gourd");
        itemDescriptionMap.put("RADISH SEED", "Seeds for planting 1 unit of radish");
        itemDescriptionMap.put("SPINACH SEED", "Seeds for planting 1 unit of spinach");
        //plant items
        itemDescriptionMap.put("PATATO", "Time to Grow:"+"\t"+"1"+
                "\n"+"Product:"+"\t"+"1 food unit"
                +"\n"+"Ability:" +"\t"+"Refunds the seeds");
        itemDescriptionMap.put("PUMPKIN", "Time to Grow:"+"\t"+"2"+
                "\n"+"Product:"+"\t"+"2 food unit"
                +"\n"+"Ability:" +"\t"+"Can be Sold for 2 Action Points");
        itemDescriptionMap.put("BEANS","Time to Grow"+"\t"+"1"+
                "\n"+"Product:"+"\t"+"1 food unit"
                +"\n"+"Ability:" +"\t"+"generates food for 2 turns");
        itemDescriptionMap.put("GOURD", "Time to Grow:"+"\t"+"2"+
                "\n"+"Product:"+"\t"+"2 food unit"
                +"\n"+"Ability:" +"\t"+"Immune to flood and droughts");
        itemDescriptionMap.put("RADISH","Time to Grow:"+"\t"+"0"+
                "\n"+"Product:"+"\t"+"1 food unit"
                +"\n"+"Ability:" +"\t"+"triples production if flooded");
        itemDescriptionMap.put("SPINACH", "Time to Grow:"+"\t"+"2"+
                "\n"+"Product:"+"\t"+"2 food unit"
                +"\n"+"Ability:" +"\t"+"grants 2 action point");
        //farm items
        itemDescriptionMap.put("FERTILIZER", "Cost:"+"\t"+"1pt"+ "\n"+
                "Can be used on any seed, if the see grows successfully into plant," +
                "the plant will generate 1 extra food unit");
        itemDescriptionMap.put("PESTICIDE","Cost:"+"\t"+"1pt"+ "\n"+
                "Kill the pests for 1 garden");
        itemDescriptionMap.put("PLOWER", "Cost:"+"\t"+"1pt"+"\n"+
                "Turning a used garden into a normal garden");
        itemDescriptionMap.put("COLLECT", "Cost:"+"\t"+"1pt"+"\n"+
                "Necessary Item to collect your fruit" );
        //disaster items
        itemDescriptionMap.put("HURRICANE",
                "randomly invades 2 adjacent gardens");
        itemDescriptionMap.put("DROUGHT",
                "randomly affects 2 gardens");
        itemDescriptionMap.put("FLOODING",
                "randomly affects 2 gardens");
        itemDescriptionMap.put("WILDFIRE",
                "randomly affects 2 gardens");
        itemDescriptionMap.put("STARVATION",
                "Cost:"+"\t"+"1pt"+ "\n"+
                "reduce people consumption to 0 for this turn, this will " +
                "result in deaths and causes permanent" +
                        "1 action point reduction");
        itemDescriptionMap.put("PLANT TREE",
                "Cost:"+"\t"+"1pt"+ "\n"+
                "plant a tree at a normal garden, all adjacent gardens" +
                "will not have flooding, hurricane nor droughts. ");
        itemDescriptionMap.put("GREEN ENERGY",
                "Cost:"+"\t"+"2pt"+ "\n"+
                "Adopt green energy policy, stop temperature increase.");
        itemDescriptionMap.put("ACCEPT REFUGEES",
                "Cost:"+"\t"+"2pt"+ "\n"+
                "Take in climate refugees into the country, permanently increase" +
                "consumption level by 1 and increase action points received per turn by 2");
        itemDescriptionMap.put("WAR",
                "Cost:"+"\t"+"3pt"+ "\n"+
                "halves both player food unit, consumption and income");
    }

    private static final Map<String, String> evolutionMap;
    static{
        evolutionMap = new HashMap<>();
        evolutionMap.put("PATATO SEED","PATATO");
        evolutionMap.put("GOURD SEED","GOURD");
        evolutionMap.put("BEANS SEED","BEANS");
        evolutionMap.put("RADISH SEED","RADISH");
        evolutionMap.put("SPINACH SEED","SPINACH");
        evolutionMap.put("PUMPKIN SEED","PUMPKIN");
    }
    public GameItem(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
        this.imgUrl = itemUrlMap.get(name);
        this.description = itemDescriptionMap.get(name);
    }

    public String getEvolutionName(String name){
        return evolutionMap.get(name);
    }
    public boolean isSeed(){
        return evolutionMap.containsKey(getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }
}
