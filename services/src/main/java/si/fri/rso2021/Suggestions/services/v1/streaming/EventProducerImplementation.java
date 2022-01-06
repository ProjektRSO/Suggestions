package si.fri.rso2021.Suggestions.services.v1.streaming;

import com.kumuluz.ee.streaming.common.annotations.StreamProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@ApplicationScoped
public class EventProducerImplementation {

    private static final Logger log = Logger.getLogger(EventProducerImplementation.class.getName());

    private static final String TOPIC_NAME = "xb9xnuao-suggestions";

    @Inject
    @StreamProducer
    private Producer producer;

    public Response produceMessage(String id, String customer) {

        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("customer", customer);
        obj.put("status", "unprocessed");

        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, id, obj.toString());

        producer.send(record,
                (metadata, e) -> {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        log.info("The offset of the produced message record is: " + metadata.offset());
                    }
                });

        return Response.ok().build();

    }
}