package tag;

import java.util.Calendar;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import entity.Ad;
import entity.AdList;
import entity.User;
import helper.AdListHelper;

public class UpdateAd extends SimpleTagSupport {

    private Ad ad;

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public void doTag() {
        String errorMessage = null;
        AdList adList = (AdList)getJspContext().getAttribute("ads", PageContext.APPLICATION_SCOPE);
        User currentUser = (User)getJspContext().getAttribute("authUser", PageContext.SESSION_SCOPE);
        if (ad.getSubject() == null || "".equals(ad.getSubject())) {
            errorMessage = "The title cannot be empty!";
        }
        else {
            if (currentUser == null || (ad.getId() > 0 && ad.getAuthorId() != currentUser.getId())) {
                errorMessage = "You are trying to change a message that you do not have access rights to!";
            }
        }

        if (errorMessage == null) {
            ad.setLastModified(Calendar.getInstance().getTimeInMillis());
            if (ad.getId() == 0) {
                adList.addAd(currentUser, ad);
            }
            else {
                adList.updateAd(ad);
            }
            AdListHelper.saveAdList(adList);
        }
        getJspContext().setAttribute("errorMessage", errorMessage, PageContext.SESSION_SCOPE);
    }
}
