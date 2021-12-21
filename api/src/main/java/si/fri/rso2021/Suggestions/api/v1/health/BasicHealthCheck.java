package si.fri.rso2021.Suggestions.api.v1.health;


import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import si.fri.rso2021.Suggestions.services.v1.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Liveness
@ApplicationScoped
public class BasicHealthCheck implements HealthCheck {

    @Inject
    private RestProperties restProperties;

    @Override
    public HealthCheckResponse call() {
        if (restProperties.getBroken()) {
            return HealthCheckResponse.down(BasicHealthCheck.class.getSimpleName());
        }
        else {
            return HealthCheckResponse.up(BasicHealthCheck.class.getSimpleName());
        }
    }
}
