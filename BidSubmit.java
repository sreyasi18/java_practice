import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/BidSubmit")
public class BidSubmit extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        
        // Check if userId is null
        if (userId == null) {
            response.sendRedirect("login.jsp?error=User not logged in");
            return;
        }
        
        String jobId = request.getParameter("job_id");
        String bidAmountStr = request.getParameter("bid_amount");
        
        
        
        
        double bidAmount = 0.0;
        if (bidAmountStr != null && !bidAmountStr.isEmpty() && !bidAmountStr.equals("null")) {
            try {
                bidAmount = Double.valueOf(bidAmountStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect("job_details.jsp?job_id=" + jobId + "&error=Invalid bid amount");
                return;
            }
        } else {
            response.sendRedirect("job_details.jsp?job_id=" + jobId + "&error=Missing bid amount");
            return;
        }
        String jdbcURL = "jdbc:mysql://localhost:3306/bid";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "INSERT INTO job_bid(user_id, job_id, bid_amount) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, Integer.parseInt(jobId));
                pstmt.setDouble(3, bidAmount);
                int rowsInserted = pstmt.executeUpdate();

                if (rowsInserted > 0) {
//                    response.sendRedirect("FindJobServlet");
                    request.setAttribute("message", "Form submitted successfully!");
				    request.setAttribute("messageType", "success");
				    request.getRequestDispatcher("FindJobServlet").forward(request, response);
//				    response.sendRedirect("FindJobServlet");
				    
				    // Forward to another servlet or JSP
//				    RequestDispatcher dispatcher = request.getRequestDispatcher("FindJobServlet");
//				    dispatcher.forward(request, response);
                    
                } else {
                    response.sendRedirect("job_details.jsp?job_id=" + jobId + "&error=Bid submission failed");
                }
            }
        } catch (SQLException ex) {
			ex.printStackTrace();
			//response.sendRedirect("postJob.jsp?error=sql_exception");
			
			request.setAttribute("message", "Error in form submission due to some sql error.");
			request.setAttribute("messageType", "error");
			request.getRequestDispatcher("postJob.jsp").forward(request, response);
		} 
        
	}
}