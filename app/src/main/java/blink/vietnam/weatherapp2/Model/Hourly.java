package blink.vietnam.weatherapp2.Model;

public class Hourly {
    String currentHour;
    String tempHourly;
    String iconHourly;

    public Hourly(String currentHour, String tempHourly, String iconHourly) {
        this.currentHour = currentHour;
        this.tempHourly = tempHourly;
        this.iconHourly = iconHourly;
    }

    public String getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(String currentHour) {
        this.currentHour = currentHour;
    }

    public String getTempHourly() {
        return tempHourly;
    }

    public void setTempHourly(String tempHourly) {
        this.tempHourly = tempHourly;
    }

    public String getIconHourly() {
        return iconHourly;
    }

    public void setIconHourly(String iconHourly) {
        this.iconHourly = iconHourly;
    }
}
