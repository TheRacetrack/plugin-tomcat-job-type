# Tomcat job type

Set `lang: tomcat:latest` in your `job.yaml` manifest file in order to use this type of job.

Under the hood it is using gradle with `war` plugin, which will build your code to `app/build/libs/app.war` file.
This file will be hosted by the Tomcat at appropriate endpoints.

The best way to create your own job, is to copy the `sample` folder and start making changes.

## Job standards
Let's assume you already have your code in a repository at `Adder.java`:
```java
public class Adder {
    public float addTwo(x: float, y: float) {
        sum = x + y
        return sum
    }
}
```

Now you need to make a few adjustments to adhere to job standards.
Basically, you need to embed your code into an entrypoint class inheriting from `HttpServlet`, with `doPost` method:

```java
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

class NumbersPayload {
    public int x;
    public int y;
}

public class Job extends HttpServlet {

	public Job() {
	    super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
	
		NumbersPayload payload = new Gson().fromJson(request.getReader(), NumbersPayload.class);
		sum = payload.x + payload.y
        response.getWriter().append(Integer.toString(sum));
	}
```

This method will be called by Racetrack when calling `/api/v1/perform` endpoint on your job. You can also support GET
methods by implementing `doGet`:

```java
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getServletPath();

		if (path.equals("/foo")) {
			response.getWriter().append("Bar");
			response.addHeader("content-type", "text/plain");
			response.setStatus(200);
		}
	}
```

By default, you are expected to name the class `Job`, and the file as `Job.java`. 
If you need a different one, adjust this section in `build.gradle.kts`:
```
application {
    // Define the main class for the application.
    mainClass.set("Job")
}
```


For details, take a look at `sample/adder`.

## Swagger UI

When you access the root url of a job (ie. by clicking on it in Racetrack Dashboard), you will be presented with
Swagger UI, which simplifies calling of various urls.

Unfortunately currently Swagger doesn't detect available urls, and its hardcoded on base ones like /health,
and /api/v1/perform.

