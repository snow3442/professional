package common;

import java.io.Serializable;

public class WeatherUpdate implements Serializable {
    private int temperature;
    private int wildfire;
    private int hurricane;
    private int pest;
    private int drought;
    private int flood;

    public WeatherUpdate(int temperature, int wildfire, int hurricane, int pest, int drought, int flood){
        this.temperature = temperature;
        this.wildfire = wildfire;
        this.hurricane = hurricane;
        this.pest = pest;
        this.drought = drought;
        this.flood = flood;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getWildfire() {
        return wildfire;
    }

    public int getHurricane() {
        return hurricane;
    }

    public int getPest() {
        return pest;
    }

    public int getDrought() {
        return drought;
    }

    public int getFlood() {
        return flood;
    }
}
