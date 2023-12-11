import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import java.util.logging.Logger;


@WebServlet(urlPatterns = {"/test"}) // without this, Servlet is not loaded
public class Prometheus extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger( Prometheus.class.getName() );

    public Prometheus() {
        super();

        LOGGER.info("Registering prometheus metrics");

        JvmMetrics.builder().register(); // initialize the out-of-the-box JVM metrics

        Counter counter = Counter.builder()
                .name("my_count_total")
                .help("example counter")
                .labelNames("status")
                .register();

        counter.labelValues("ok").inc();
        counter.labelValues("ok").inc();
        counter.labelValues("error").inc();
    }

}
