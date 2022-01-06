package si.fri.rso2021.Suggestions.api.v1.resources;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso2021.Suggestions.services.v1.config.RestProperties;
//import si.fri.rso2021.Suggestions.services.v1.streaming.EventProducerImplementation;
import si.fri.rso2021.Suggestions.models.v1.objects.Customers;
import org.eclipse.microprofile.metrics.annotation.Metered;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import si.fri.rso2021.Suggestions.services.v1.streaming.EventProducerImplementation;

@Log
@ApplicationScoped
@Path("/suggestions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST")
public class SuggestionsResources {

    private Logger log = Logger.getLogger(SuggestionsResources.class.getName());

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;

    @Inject
    private EventProducerImplementation eventProducer;

    //@Inject
    //private EventProducerImplementation eventProducer;

    @Metered(name = "SuggestionsListRequest")
    private List<Customers> makeListRequest(String type, String urlparam) throws IOException {
        String dburl = restProperties.getCustomersurl();
        log.info("STARTING " + type + " REQUEST " + dburl);
        URL url = new URL(dburl + urlparam);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        InputStream responseStream = con.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        List<Customers> customers = mapper.readValue(responseStream, new TypeReference<>(){});
        return customers;
    }

    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Fallback(fallbackMethod = "makeObjectRequestFallback")
    @Timed(name="SuggestionsObjectRequest")
    @Metered(name = "SuggestionsObjectRequest")
    private String makeObjectRequest(String type, String urlparam) throws IOException {
        String dburl = restProperties.getCustomersurl();
        log.info("STARTING FIRST " + type + " REQUEST " + dburl + " " + urlparam);
        URL url = new URL(dburl + urlparam);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        InputStream responseStream = con.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Customers customer = mapper.readValue(responseStream, Customers.class);
        log.info(customer.getPostcode()+", "+ customer.getTown());
        return customer.getPostcode()+","+ customer.getTown();
    }

    public String makeObjectRequestFallback(String type, String urlparam){return null;};


    @Metered(name = "SuggestionsLocationRequest")
    private String [] makeLocationRequest(String type, String urlparam) throws IOException {
        String brokenKey = restProperties.getApikey();
        String dburl = "https://atlas.microsoft.com/search/address/json?&subscription-key="+ brokenKey +"Q&api-version=1.0&language=en-US&query="+urlparam;
        log.info("STARTING SECOND" + type + "REQUEST " + dburl);
        URL url = new URL(dburl + urlparam);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        String [] content = new String[2];
        int count = 0;
        int pos = 0;
        while ((inputLine = in.readLine()) != null) {
            count = inputLine.indexOf("position");
            log.info(String.valueOf(count));
            log.info(String.valueOf(inputLine.length()));
            inputLine = inputLine.substring(count, count+40);
            log.info(inputLine);
            log.info(String.valueOf(inputLine.length()));
            if(count > 0){
                if(inputLine.contains("lat")){
                    count = inputLine.indexOf("lat");
                    log.info(String.valueOf(count));
                    content[0] = inputLine.substring(count+5, count+13);
                }
                if(inputLine.contains("lon")){
                    count = inputLine.indexOf("lon");
                    log.info(String.valueOf(count));
                    content[1] = inputLine.substring(count+5, count+13);
                }
            }

        }
        in.close();
        log.info(content[0] +", " + content[1]);

        return content;
    }

    @Metered(name = "SuggestionsWeatherRequest")
    private String makeWeatherRequest(String type, String [] urlparam) throws IOException {
        String dburl = "https://atlas.microsoft.com/weather/indices/daily/json?api-version=1.0&query="+urlparam[0]+","+urlparam[1]+"&subscription-key=Aq5NNU-8Bhp2XOoXGtR4lX8sNGiXGKcrD4jnK2UqPDQ";
        log.info("STARTING SECOND" + type + "REQUEST " + dburl);
        URL url = new URL(dburl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        String [] content = new String[2];
        int count = 0;
        String out = "";
        while ((inputLine = in.readLine()) != null) {
            count = inputLine.indexOf("Outdoor Activity Forecast");
            if(inputLine.substring(count,count+125).contains("Fair") || inputLine.substring(count,count+125).contains("Good")){
                out = "OK for outdoor work";
            }else{
                out = "BAD for outdoor work";
            }

        }
        log.info(out);
        in.close();
        return out;
    }


    @GET
    @Path("/{id}")
    public Response getCustomerLocationByID(@PathParam("id") Integer id) throws IOException {

        String c = makeObjectRequest("GET", String.format("/%d", id));
        String [] coordinates = makeLocationRequest("GET", c);
        String weather = makeWeatherRequest("GET", coordinates);

        eventProducer.produceMessage(String.valueOf(id), c);

        if (c == "") {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(weather).build();
    }

}
