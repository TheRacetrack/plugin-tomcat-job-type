import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;
import java.util.logging.Logger;
import io.prometheus.metrics.core.metrics.Counter;
import javax.naming.InitialContext;
import javax.naming.Context;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import javax.naming.NamingException;

class NumbersPayload {
    public int numbers[];
}

@WebServlet(urlPatterns = {"/api/v1/perform", "/docs_input_example"})
public class Job extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger( Job.class.getName() );

    private static Counter counter = null;

    static {
        try {
            Context envCtx = (Context) new InitialContext().lookup("java:comp/env");
            PrometheusRegistry defaultReg = (PrometheusRegistry) envCtx.lookup("bean/PrometheusRegistry");

            counter = Counter.builder().name("my_count_total")
                .help("example counter")
                .labelNames("status").register((PrometheusRegistry)defaultReg);
        } catch (NamingException e) {
            e.printStackTrace();
            // Provide a default counter instance or handle the situation appropriately
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.equals("/docs_input_example")) {
            // Return example input values for this model
            response.getWriter().append("{\"numbers\": [40, 2]}");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        LOGGER.severe("Logging an SEVERE-level message");
        LOGGER.info("Logging an INFO-level message");
        LOGGER.warning("Logging an WARNING-level message");
        // With default logging config, levels below WARNING don't print

        if (this.counter != null) {
            this.counter.labelValues("ok").inc();
            this.counter.labelValues("ok").inc();
            this.counter.labelValues("error").inc();
        }

        NumbersPayload payload = new Gson().fromJson(request.getReader(), NumbersPayload.class);

        int sum = 0;
        for (int number : payload.numbers) {
            sum += number;
        }
        response.getWriter().append(Integer.toString(sum));
    }

}
