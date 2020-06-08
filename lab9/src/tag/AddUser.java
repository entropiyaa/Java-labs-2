package tag;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import entity.User;
import entity.UserList;
import entity.UserList.UserExistsException;
import helper.UserListHelper;

public class AddUser extends SimpleTagSupport {

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void doTag() {
        String errorMessage = null;
        UserList userList = (UserList)getJspContext().getAttribute("users", PageContext.APPLICATION_SCOPE);
        if (user.getLogin() == null || "".equals(user.getLogin())) {
            errorMessage = "Login cannot be empty!";
        } else {
            if (user.getName() == null || "".equals(user.getName())) {
                errorMessage = "Username cannot be empty!";
            }
        }
        if (errorMessage == null) {
            try {
                userList.addUser(user);
                UserListHelper.saveUserList(userList);
            } catch (UserExistsException e) {
                errorMessage = "User with this login already exists!";
            }
        }

        getJspContext().setAttribute("errorMessage", errorMessage,
                PageContext.SESSION_SCOPE);
    }
}
