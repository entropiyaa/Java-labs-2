import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MessageListServlet extends ChatServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        // Получить доступ к потоку вывода HTTP-ответа
        PrintWriter pw = response.getWriter();
        // Записть в поток HTML-разметку страницы
        pw.println("<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/><meta http-equiv='refresh' content='10'></head>");
        pw.println("<body>");
        for (int i = messages.size() - 1; i >= 0; i--) {
            ChatMessage aMessage = messages.get(i);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            Date date = new Date(aMessage.getTimestamp());
            pw.println("<div><strong>" + aMessage.getAuthor().getName() + "</strong>: " + aMessage.getMessage() + "       " + "<i>" + "(" + sdf.format(date) + ")" +"</i>" + "</div>");
        }
        pw.println("</body></html>");
    }
}
