package si.fri.rso2021.Suggestions.services.v1.dtos;

import javax.enterprise.context.RequestScoped;
import java.util.Date;

@RequestScoped
public class SuggestionsDTO implements java.io.Serializable {

    private String id;
    private String location;
    private String weather;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeather() {
        return weather;
    }
}
