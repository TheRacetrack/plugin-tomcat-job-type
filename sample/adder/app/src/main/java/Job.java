import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;
import java.util.logging.Logger;
import io.prometheus.metrics.core.metrics.Counter;

class NumbersPayload {
    public int numbers[];
}

@WebServlet(urlPatterns = {"/api/v1/perform", "/docs_input_example"})
public class Job extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger( Job.class.getName() );

    private static final Counter counter = Counter.builder().name("my_count_total")
                                                            .help("example counter")
                                                            .labelNames("status")
                                                            .register();

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

        this.counter.labelValues("ok").inc();
        this.counter.labelValues("ok").inc();
        this.counter.labelValues("error").inc();

        NumbersPayload payload = new Gson().fromJson(request.getReader(), NumbersPayload.class);

        int sum = 0;
        for (int number : payload.numbers) {
            sum += number;
        }
        response.getWriter().append(Integer.toString(sum));
    }

}
