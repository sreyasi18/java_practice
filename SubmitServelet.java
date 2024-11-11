import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SubmitServelet")
public class SubmitServelet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

            String sql = "SELECT * FROM user WHERE user_name = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user_name);
            pstmt.setString(2, password);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                // Set session attribute
                HttpSession bid_session = request.getSession();
                bid_session.setAttribute("is_loggedin", 1);
                bid_session.setAttribute("user_id", result.getInt("id"));
               System.out.println(result.getInt("id"));
                bid_session.setMaxInactiveInterval(60 * 30);

                // Set cookie
                String encodedUserName = URLEncoder.encode(user_name, "UTF-8");
                Cookie userCookie = new Cookie("user_name", encodedUserName);
                userCookie.setMaxAge(60 * 60 * 24); // 1 day
                response.addCookie(userCookie);

                response.sendRedirect("dashboard.jsp");
            } else {
                // Set cookie
                String errorMessage = "Invalid_credentials";
                String encodedErrorMessage = URLEncoder.encode(errorMessage, "UTF-8");
                Cookie errorCookie = new Cookie("error", encodedErrorMessage);
                errorCookie.setMaxAge(10); // 10 seconds
                response.addCookie(errorCookie); 

                response.sendRedirect("login.jsp");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            response.sendRedirect("login.jsp");
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
