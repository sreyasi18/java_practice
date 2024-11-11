import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AllJobServlet
 */
@WebServlet("/AllJobServlet")
public class AllJobServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllJobServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("is_loggedin") == null || (int) session.getAttribute("is_loggedin") != 1) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Map<String, Object>> jobList = fetchJobListFromDatabase();

        session.setAttribute("job_list", jobList);

        request.getRequestDispatcher("job_list.jsp").forward(request, response);
    }

    private List<Map<String, Object>> fetchJobListFromDatabase() {
        String jdbcURL = "jdbc:mysql://localhost:3306/bid";
        String dbUser = "root";
        String dbPassword = "";
        List<Map<String, Object>> jobList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "SELECT id, title, description, created_at, status, final_bid_amount FROM job";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet result = pstmt.executeQuery()) {

                while (result.next()) {
                    Map<String, Object> job = new HashMap<>();
                    job.put("id", result.getInt("id"));
                    job.put("title", result.getString("title"));
                    job.put("description", result.getString("description"));
                    job.put("created_at", result.getTimestamp("created_at"));
                    job.put("status", result.getString("status"));
                    job.put("final_bid_amount", result.getDouble("final_bid_amount"));
                    jobList.add(job);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return jobList;
    }
}