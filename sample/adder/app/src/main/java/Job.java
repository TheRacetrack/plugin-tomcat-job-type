import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;

class NumbersPayload {
    public int numbers[];
}

@WebServlet(urlPatterns = {"/api/v1/perform", "/docs_input_example"})
public class Job extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.equals("/docs_input_example")) {
            // Return example input values for this model
            response.getWriter().append("{\"numbers\": [40, 2]}");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        NumbersPayload payload = new Gson().fromJson(request.getReader(), NumbersPayload.class);

        int sum = 0;
        for (int number : payload.numbers) {
            sum += number;
        }
        response.getWriter().append(Integer.toString(sum));
    }

}
