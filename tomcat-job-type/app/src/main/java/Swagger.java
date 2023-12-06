import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(urlPatterns = {"/swagger"})
public class Swagger extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String swagger_template;
	private String swagger;

	public Swagger() {
		super();
		this.swagger_template = """
                                openapi: 3.0.0
                                info:
                                  title: Job
                                  description: JOB_NAME Job wrapped in a Tomcat server
                                  version: JOB_VERSION
                                servers:
                                  - url: /pub/job/JOB_NAME/JOB_VERSION
                                    description: proxy prefixed path
                                  - url: /
                                    description: root base path
                                paths:
                                  /health:
                                    get:
                                      tags:
                                        - root
                                      summary: ' Health'
                                      description: Report current aggregated application status
                                      operationId: _health_health_get
                                      responses:
                                        '200':
                                          description: Successful Response
                                          content:
                                            application/json:
                                              schema: {}
                                  /live:
                                    get:
                                      tags:
                                        - root
                                      summary: ' Live'
                                      description: 'Report application liveness: whether it has started (but might not be ready yet)'
                                      operationId: _live_live_get
                                      responses:
                                        '200':
                                          description: Successful Response
                                          content:
                                            application/json:
                                              schema: {}
                                  /ready:
                                    get:
                                      tags:
                                        - root
                                      summary: ' Ready'
                                      description: 'Report application readiness: whether it''s available for accepting traffic'
                                      operationId: _ready_ready_get
                                      responses:
                                        '200':
                                          description: Successful Response
                                          content:
                                            application/json:
                                              schema: {}
                                  /metrics:
                                    get:
                                      tags:
                                        - root
                                      summary: ' Metrics Endpoint'
                                      description: List current Prometheus metrics
                                      operationId: _metrics_endpoint_metrics_get
                                      responses:
                                        '200':
                                          description: Successful Response
                                          content:
                                            application/json:
                                              schema: {}
                                  /api/v1/perform:
                                    post:
                                      tags:
                                        - API
                                      summary: Call main action
                                      description: 'Call main action'
                                      operationId: _perform_endpoint_api_v1_perform_post
                                      requestBody:
                                        content:
                                          application/json:
                                            schema:
                                              type: object
                                              title: Payload
                                              default:
                                                {numbers: [40, 2]}
                                      responses:
                                        '200':
                                          description: Successful Response
                                          content:
                                            application/json:
                                              schema: {}
                                        '422':
                                          description: Validation Error
                                          content:
                                            application/json:
                                              schema:
                                                $ref: '#/components/schemas/HTTPValidationError'

                                components:
                                  schemas:
                                    HTTPValidationError:
                                      properties:
                                        detail:
                                          items:
                                            $ref: '#/components/schemas/ValidationError'
                                          type: array
                                          title: Detail
                                      type: object
                                      title: HTTPValidationError
                                    ValidationError:
                                      properties:
                                        loc:
                                          items:
                                            anyOf:
                                              - type: string
                                              - type: integer
                                          type: array
                                          title: Location
                                        msg:
                                          type: string
                                          title: Message
                                        type:
                                          type: string
                                          title: Error Type
                                      type: object
                                      required:
                                        - loc
                                        - msg
                                        - type
                                      title: ValidationError
                                  securitySchemes:
                                    racetrackAuth:
                                      type: apiKey
                                      in: header
                                      name: X-Racetrack-Auth
                                security:
                                  - racetrackAuth: []
                                """;
        String JobName = System.getenv("JOB_NAME");
        String JobVersion = System.getenv("JOB_VERSION");

        this.swagger = this.swagger_template.replaceAll("JOB_NAME", JobName);
        this.swagger = this.swagger.replaceAll("JOB_VERSION", JobVersion);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getServletPath();

		if (path.equals("/swagger")) {
			response.getWriter().append(this.swagger);
			response.setStatus(200);
		} else {
			response.getWriter().append("Invalid endpoint");
			response.setStatus(404);
		}
	}
}
