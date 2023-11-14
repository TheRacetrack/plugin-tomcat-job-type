# Tomcat job type

Set `lang: tomcat:latest` in your `job.yaml` manifest file in order to use this type of job.

## Job standards
Let's assume you already have your code in a repository at `Adder.java`:
```java
public class Adder {
    public float addTwo(x: float, y: float) {
        return x + y;
    }
}
```

Now you need to make a few adjustments to adhere to job standards.
Basically, you need to embed your code into an entrypoint class with `perform` method.
Create `AddingJob.java`:
```java
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddingJob extends HttpServlet {

	public AddingJob() {
	    super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        x = request.getReader().Read("x")
        y = request.getReader().Read("y")
        response.getWriter().append("Adding result: " + (x+y));
	}
```

This method will be called by Racetrack when calling `/api/v1/perform` endpoint on your job.

Then in manifest, point to this file in `jobtype_extra.entrypoint_path`.
