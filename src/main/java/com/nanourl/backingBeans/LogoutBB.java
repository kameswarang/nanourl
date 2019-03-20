package com.nanourl.backingBeans;

import javax.inject.Named;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;

import java.io.IOException;

@Named("logoutBB") @RequestScoped
public class LogoutBB {
    private ExternalContext ctx;
    
    @Inject
    private HttpSession httpSession;
    
    @Inject
    private RestrictBB restrictBB;
    
    private String errorMessage;
    public String getErrorMessage() { return this.errorMessage; }
    
    @PostConstruct
    public void logout() {
        this.restrictBB.restrictForVisitor();
        this.ctx = FacesContext.getCurrentInstance().getExternalContext();
        //this.errorMessage = "Logging out...";
        this.httpSession.invalidate();
        
        try {
            this.ctx.redirect("home.faces");
        }
        catch(IOException e) {
            e.printStackTrace();
            this.errorMessage = "Unable to logout. Please try again after sometime.";
        }
    }
}