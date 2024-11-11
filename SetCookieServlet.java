import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SetCookieServlet")
public class SetCookieServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie userCookie = new Cookie("username", "JohnDoe");
        userCookie.setMaxAge(60 * 60 * 24); // 1 day
        response.addCookie(userCookie);
        response.sendRedirect("index.jsp");
    }
}
