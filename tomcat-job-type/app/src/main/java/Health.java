package com.racetrack.adder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(urlPatterns = {"/ready", "/live", "/health"})
public class Health extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Health() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getServletPath();

		if (path.equals("/ready")) {
			response.getWriter().append("Ready");
			response.setStatus(200);
		} else if (path.equals("/health")) {
			response.getWriter().append("Healthy");
			response.setStatus(200);
		} else if (path.equals("/live")) {
			response.getWriter().append('{"deployment_timestamp": 1654779364}'); // todo take from JOB_DEPLOYMENT_TIMESTAMP
			response.setStatus(200);
		} else {
			response.getWriter().append("Invalid endpoint");
			response.setStatus(404);
		}
	}
}
