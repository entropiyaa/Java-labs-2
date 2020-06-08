package tag;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import entity.Ad;
import entity.AdList;
import entity.User;
import helper.AdListHelper;

public class DeleteAd extends SimpleTagSupport {

    private Ad ad;

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public void doTag() {
        String errorMessage = null;
        AdList adList = (AdList)getJspContext().getAttribute("ads", PageContext.APPLICATION_SCOPE);
        User currentUser = (User)getJspContext().getAttribute("authUser", PageContext.SESSION_SCOPE);
        if (currentUser == null || (ad.getId() > 0 && ad.getAuthorId() != currentUser.getId())) {
            errorMessage = "You are trying to change a message that you do not have access rights to!";
        }
        if (errorMessage == null) {
            adList.deleteAd(ad);
            AdListHelper.saveAdList(adList);
        }
        getJspContext().setAttribute("errorMessage", errorMessage, PageContext.SESSION_SCOPE);
    }
}
