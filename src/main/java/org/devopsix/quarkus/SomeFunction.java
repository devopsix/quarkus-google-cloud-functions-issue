package org.devopsix.quarkus;

import com.google.cloud.functions.CloudEventsFunction;
import io.cloudevents.CloudEvent;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SomeFunction implements CloudEventsFunction {

    private static final Logger log = Logger.getLogger(SomeFunction.class);

    @Override
    public void accept(CloudEvent event) throws Exception {
        log.infof("Received event: %s", event);
    }
}
