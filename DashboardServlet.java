import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    //request: Represents the client's request
    //response: Represents the servlet's response to the client
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // Retrieves the current session associated with the request.
    	HttpSession session = request.getSession(false); // Get the session, don't create a new one if it doesn't exist

    	//Checks if a session exists and if the session contains an attribute named "username"
        if (session != null && session.getAttribute("username") != null) {
  // Retrieves the value of the "username" attribute from the session and casts it to a String
            String user_name = (String) session.getAttribute("username");
            // Writes a welcome message to the response, including the username.
            response.getWriter().println("Welcome, " + user_name + "!");
            // Proceed with displaying the dashboard
        } else {
            // No session or session expired
            response.sendRedirect("login.jsp");
        }
    }
}
