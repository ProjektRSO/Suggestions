package si.fri.rso2021.Suggestions.services.v1.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ConfigBundle("rest-properties")
@ApplicationScoped
public class RestProperties {

    @ConfigValue(watch = true)
    private Boolean maintenanceMode;

    @ConfigValue(watch = true)
    private String customersurl;

    @ConfigValue(watch = true)
    private String apikey;

    private Boolean broken;

    public Boolean getMaintenanceMode() {
        return this.maintenanceMode;
    }

    public void setMaintenanceMode(final Boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public Boolean getBroken() {
        return broken;
    }

    public void setBroken(final Boolean broken) {
        this.broken = broken;
    }

    public String getCustomersurl() { return customersurl; }

    public void setCustomersurl(String customersurl) { this.customersurl = customersurl;}

    public String getApikey() {return apikey;}

    public void setApikey(String apikey) {this.apikey = apikey;}
}
