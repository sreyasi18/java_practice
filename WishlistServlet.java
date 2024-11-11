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

@WebServlet("/WishlistServlet")
public class WishlistServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public WishlistServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer isLoggedIn = (Integer) session.getAttribute("is_loggedin");
        if (isLoggedIn == null || isLoggedIn != 1) {
            response.sendRedirect("login.jsp");
            return;
        }

        String selectedDate = request.getParameter("selectedDate");
        if (selectedDate == null || selectedDate.isEmpty()) {
            response.sendRedirect("wishlist.jsp");
            return;
        }

        List<Map<String, Object>> availableJobs = fetchJobsUntilDate(selectedDate);

        request.setAttribute("available_jobs", availableJobs);
        request.getRequestDispatcher("wishlist.jsp").forward(request, response);
    }

    private List<Map<String, Object>> fetchJobsUntilDate(String date) {
        String jdbcURL = "jdbc:mysql://localhost:3306/bid";
        String dbUser = "root";
        String dbPassword = "";
        List<Map<String, Object>> jobList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "SELECT id, job_title, description, created_at, status, final_bid_amount FROM job WHERE created_at <= ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, date);
                try (ResultSet result = pstmt.executeQuery()) {
                    while (result.next()) {
                        Map<String, Object> job = new HashMap<>();
                        job.put("id", result.getInt("id"));
                        job.put("job_title", result.getString("job_title"));
                        job.put("description", result.getString("description"));
                        job.put("created_at", result.getTimestamp("created_at"));
                        job.put("status", result.getString("status"));
                        job.put("final_bid_amount", result.getDouble("final_bid_amount"));
                        jobList.add(job);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return jobList;
    }
}
