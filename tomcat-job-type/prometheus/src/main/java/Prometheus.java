import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import java.util.logging.Logger;


public class Prometheus extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger( Prometheus.class.getName() );

    public Prometheus() {
        super();
        LOGGER.info("Registering JVM Prometheus metrics");
        JvmMetrics.builder().register();

    }

}
