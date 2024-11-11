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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class PostJobServlet
 */
@WebServlet("/PostJobServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50) // 50MB
public class PostJobServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PostJobServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String jdbcURL = "jdbc:mysql://localhost:3306/bid";
		String dbUser = "root";
		String dbPassword = "";

		PreparedStatement pstmt = null;

		String jobId = request.getParameter("job_id");
		String status = request.getParameter("status");

		Map<String, Object> job = new HashMap<>();
		Connection conn = null;
		ResultSet rs = null;

		if (jobId != null) {

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bid", "root", "");

				if (status != null) {
										
					String query = "UPDATE job SET status=? WHERE id = ?";
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, status);
					pstmt.setInt(2, Integer.parseInt(jobId));
					int rowsUpdated = pstmt.executeUpdate();
					System.out.println("Rows updated: " + rowsUpdated);

					if (rowsUpdated > 0) {
						response.sendRedirect("JobList_Servlet");
					} else {
						response.sendRedirect("PostJobServlet?job_id=" + jobId + "&error=update_failed");
					}

				} else {
					String query = "SELECT job_title, description, duration, timeline, job_ends_before, max_cost, min_cost, bid_end_on, final_bid_amount, location FROM job WHERE id = ?";
					pstmt = conn.prepareStatement(query);
					pstmt.setInt(1, Integer.parseInt(jobId));
					rs = pstmt.executeQuery();

					List<Map<String, Object>> jobList = new ArrayList<>();

					if (rs.next()) {
						job.put("job_title", rs.getString("job_title"));
						job.put("description", rs.getString("description"));
						job.put("duration", rs.getString("duration"));
						job.put("timeline", rs.getString("timeline"));
						job.put("job_ends_before", rs.getString("job_ends_before"));
						job.put("max_cost", rs.getString("max_cost"));
						job.put("min_cost", rs.getString("min_cost"));
						job.put("bid_end_on", rs.getString("bid_end_on"));
						job.put("final_bid_amount", rs.getString("final_bid_amount"));
						job.put("location", rs.getString("location"));

						jobList.add(job);
					}
					request.setAttribute("jobList", jobList);
					request.getRequestDispatcher("postJob.jsp").forward(request, response);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
				response.sendRedirect("postJob.jsp?");
			} finally {
				if (rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if (pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);// Retrieves the current session without creating a new one

		String jobId = request.getParameter("job_id");
		String jobTitle = request.getParameter("job_title");
		String description = request.getParameter("description");
		String duration = request.getParameter("duration");
		String timeline = request.getParameter("timeline");
		String jobEndsBefore = request.getParameter("job_ends_before");
		String maxCost = request.getParameter("max_cost");
		String minCost = request.getParameter("min_cost");
		String bidEndOn = request.getParameter("bid_end_on");
		String final_bid_amount = request.getParameter("final_bid_amount");

		String location = request.getParameter("location");

		String jdbcURL = "jdbc:mysql://localhost:3306/bid";
		String dbUser = "root";
		String dbPassword = "";

		Connection conn = null;
		PreparedStatement pstmt = null;

		Integer created_by = (Integer) session.getAttribute("user_id");

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
			System.out.println("Database connection established.");

			String sql;
			// Checks if job_id is present for update operation.
			if (jobId != null && !jobId.isEmpty()) {
				// Update existing job
				sql = "UPDATE job SET job_title = ?, description = ?, duration = ?, timeline = ?, job_ends_before = ?, max_cost = ?, min_cost = ?, bid_end_on = ?, final_bid_amount = ?, location = ? WHERE id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, jobTitle);
				pstmt.setString(2, description);
				pstmt.setString(3, duration);
				pstmt.setString(4, timeline);
				pstmt.setString(5, jobEndsBefore);
				pstmt.setString(6, maxCost);
				pstmt.setString(7, minCost);
				pstmt.setString(8, bidEndOn);
				pstmt.setString(9, final_bid_amount);
				pstmt.setString(10, location);
				pstmt.setInt(11, Integer.parseInt(jobId));

				int rowsUpdated = pstmt.executeUpdate();
				System.out.println("Rows updated: " + rowsUpdated);

				if (rowsUpdated > 0) {
					response.sendRedirect("JobList_Servlet");
				} else {
					response.sendRedirect("PostJobServlet?job_id=" + jobId + "&error=insert_failed");
				}
			} else {

				// If job_id is not present, perform insert operation
				// Insert new job
				sql = "INSERT INTO job (created_by, job_title, description, duration, timeline, job_ends_before, max_cost, min_cost, bid_end_on, final_bid_amount, location) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, created_by);
				pstmt.setString(2, jobTitle);
				pstmt.setString(3, description);
				pstmt.setString(4, duration);
				pstmt.setString(5, timeline);
				pstmt.setString(6, jobEndsBefore);
				pstmt.setString(7, maxCost);
				pstmt.setString(8, minCost);
				pstmt.setString(9, bidEndOn);
				pstmt.setString(10, final_bid_amount);
				pstmt.setString(11, location);

				int rowsInserted = pstmt.executeUpdate();
				System.out.println("Rows inserted: " + rowsInserted);

				if (rowsInserted > 0) {
					request.setAttribute("message", "Form submitted successfully!");
				    request.setAttribute("messageType", "success");
				    request.getRequestDispatcher("postJob.jsp").forward(request, response);
				} else {
					 request.setAttribute("message", "Error in form submission.");
					 request.setAttribute("messageType", "error");
					 request.getRequestDispatcher("postJob.jsp").forward(request, response);
				}
			}

			// Save the uploaded picture
//            String fileName = extractFileName(picturePart);
//            String savePath = "C:\\path_to_save_directory" + File.separator + fileName; // Update this path
//            picturePart.write(savePath);
//            pstmt.setString(9, savePath);

		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			response.sendRedirect("postJob.jsp?error=class_not_found");
		} catch (SQLException ex) {
			ex.printStackTrace();
			//response.sendRedirect("postJob.jsp?error=sql_exception");
			
			request.setAttribute("message", "Error in form submission due to some sql error.");
			request.setAttribute("messageType", "error");
			request.getRequestDispatcher("postJob.jsp").forward(request, response);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
				response.sendRedirect("postJob.jsp?error=close_failed");
			}
		}
	}

//    private String extractFileName(Part part) {
//        String contentDisp = part.getHeader("content-disposition");
//        String[] items = contentDisp.split(";");
//        for (String s : items) {
//            if (s.trim().startsWith("filename")) {
//                return s.substring(s.indexOf("=") + 2, s.length() - 1);
//            }
//        }
//        return "";
//    }
}
