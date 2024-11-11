import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/JobDetailsServlet")
public class JobDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public JobDetailsServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jobId = request.getParameter("job_id");

        if (jobId == null || jobId.isEmpty()) {
            response.sendRedirect("FindJobServlet");
            return;
        }

        Map<String, Object> jobDetails = fetchJobDetails(Integer.parseInt(jobId));
        request.setAttribute("job_details", jobDetails);
        request.getRequestDispatcher("job_details.jsp").forward(request, response);
    }

    private Map<String, Object> fetchJobDetails(int jobId) {
        String jdbcURL = "jdbc:mysql://localhost:3306/bid";
        String dbUser = "root";
        String dbPassword = "";
        Map<String, Object> jobDetails = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "SELECT id,job_title, description, created_at, status, final_bid_amount FROM job WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, jobId);
                try (ResultSet result = pstmt.executeQuery()) {
                    if (result.next()) {
                    	jobDetails.put("job_id", result.getString("id"));
                        jobDetails.put("job_title", result.getString("job_title"));
                        jobDetails.put("description", result.getString("description"));
                        jobDetails.put("created_at", result.getTimestamp("created_at"));
                        jobDetails.put("status", result.getString("status"));
                        jobDetails.put("final_bid_amount", result.getDouble("final_bid_amount"));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return jobDetails;
    }
}
