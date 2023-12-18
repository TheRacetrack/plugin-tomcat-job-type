import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.Instant;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import io.prometheus.metrics.core.metrics.Gauge;

public class Valve extends ValveBase {
    private static final Logger logger = Logger.getLogger(Valve.class.getName());

    private static final Gauge lastCallTimestamp = Gauge.builder().name("last_call_timestamp").register();

    public void invoke(Request request, Response response) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = request.getRequest();

        String path = request.getServletPath();

        if (path.contains("/api/v1/perform")) {
            logger.info("Valve handling perform metrics");
            long currentTimestampSeconds = Instant.now().getEpochSecond();
            this.lastCallTimestamp.set(currentTimestampSeconds);
        }

        super.getNext().invoke(request, response);
    }
}
