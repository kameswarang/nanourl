package com.nanourl.backingBeans;

import javax.inject.Named;
import javax.inject.Inject;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;
import javax.annotation.PostConstruct;

import java.io.IOException;

@Named("restrictBB") @RequestScoped
public class RestrictBB {
    ExternalContext ctx;
    
    @Inject
    HttpSession httpSession;
    
    @PostConstruct
    public void initialise() {
        ctx = FacesContext.getCurrentInstance().getExternalContext();
    }
    
    public void restrictForUser() {
        if(httpSession.getAttribute("userLoggedIn") != null) {
            try {
                ctx.redirect("home.faces");
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void restrictForVisitor() {
        if(httpSession.getAttribute("userLoggedIn") == null) {
            try {
                ctx.redirect("login.faces");
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}