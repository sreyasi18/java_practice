
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


/**
 * Servlet implementation class SetPassword
 */
@WebServlet("/SetPassword")
public class SetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetPassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user_name = request.getParameter("user_name");
        String password = request.getParameter("password");
        
        String jdbcURL = "jdbc:mysql://localhost:3306/bid";
        String dbUser = "root";
        String dbPassword = "";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);


            String sql = "UPDATE user SET password = ? WHERE user_name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, password);
            pstmt.setString(2, user_name);

            int updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                response.sendRedirect("login.jsp?message=Password successfully changed");
            } else {
                response.sendRedirect("password.jsp?user_name=" + user_name + "&error=User not found");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            response.sendRedirect("password.jsp?user_name=" + user_name + "&error=An error occurred");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
