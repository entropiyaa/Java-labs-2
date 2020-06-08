package tag;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import entity.User;
import entity.UserList;

public class Login extends SimpleTagSupport {

    private String login;
    private String password;

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void doTag() {
        String errorMessage = null;
        UserList userList = (UserList)getJspContext().getAttribute("users", PageContext.APPLICATION_SCOPE);
        if (login == null || "".equals(login)) {
            errorMessage = "Login cannot be empty!";
        } else {
            User user = userList.findUser(login);
            if (user == null || !user.getPassword().equals(password)) {
                errorMessage = "Such a user does not exist or the specified login / password combination is incorrect!";
            } else {
                getJspContext().setAttribute("authUser", user, PageContext.SESSION_SCOPE);
            }
        }
        getJspContext().setAttribute("errorMessage", errorMessage, PageContext.SESSION_SCOPE);
    }
}
