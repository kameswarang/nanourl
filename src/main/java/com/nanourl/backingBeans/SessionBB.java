package com.nanourl.backingBeans;

import com.nanourl.persistence.User;
import com.nanourl.logic.interfaces.UserEJBLocal;

import javax.inject.Named;
import javax.inject.Inject;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import java.io.Serializable;

@Named("sessionBB") @RequestScoped
public class SessionBB implements Serializable {
    @Inject
    HttpSession httpSession;
    
    @EJB
    UserEJBLocal userEJB;
    
    private User currentUser;
    public User getCurrentUser() { return this.currentUser; }

    @PostConstruct
    public void fetchCurrentUserFromDB() {
        String currentEmail = (String) httpSession.getAttribute("currentUser");
        this.currentUser = userEJB.getUserByEmail(currentEmail);
        //System.out.println(this.currentUser.getUrls().size());
    }
}