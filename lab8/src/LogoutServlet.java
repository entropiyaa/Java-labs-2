import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutServlet extends ChatServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = (String) request.getSession().getAttribute("name");
        if (name != null) {
            ChatUser aUser = activeUsers.get(name);
            // Если идентификатор сессии пользователя, вошедшего под
            // этим именем, совпадает с идентификатором сессии
            // пользователя, пытающегося выйти из чата
            // (т.е. выходит тот же, кто и входил)
            if (aUser.getSessionId().equals((String)request.getSession().getId())) {
                synchronized (activeUsers) {
                    activeUsers.remove(name);
                }
                request.getSession().setAttribute("name", null);
                response.addCookie(new Cookie("sessionId", null));
                response.sendRedirect(response.encodeRedirectURL("/lab8_war_exploded/login"));
            } else {
                response.sendRedirect(response.encodeRedirectURL("/lab8_war_exploded/view.html"));
            }
        } else {
            response.sendRedirect(response.encodeRedirectURL("/lab8_war_exploded/view.html"));
        }
    }
}
