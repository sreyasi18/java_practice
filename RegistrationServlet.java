import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RegistrationServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String user_type = request.getParameter("user_type");
        String user_name = request.getParameter("user_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String is_active = request.getParameter("is_active");

        String jdbcURL = "jdbc:mysql://localhost:3306/bid";
        String dbUser = "root";
        String dbPassword = "";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            String sql = "INSERT INTO user (name, user_type, user_name, email, password, is_active) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, user_type);
            pstmt.setString(3, user_name);
            pstmt.setString(4, email);
            pstmt.setString(5, password);
            pstmt.setString(6, is_active);

            System.out.println(pstmt);
            
            pstmt.executeUpdate();
            
            // Set session attribute
            response.sendRedirect("success.jsp");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("KKKKKKKKKKKKKKK");
            response.sendRedirect("login.jsp");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("jjjjjjjjjjjjjjj");

            response.sendRedirect("registration.jsp");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}

