package com.nanourl.backingBeans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.enterprise.context.RequestScoped;

import com.nanourl.persistence.User;

@Named("loginBB") @RequestScoped
public class LoginBB implements Serializable {
    @Inject
    private UserBB userBB;
    public UserBB getUserBB() { return this.userBB; };

    @Inject
    private HttpSession httpSession;    
    
    @Inject
    private RestrictBB restrictBB;
    
    private String status;
    public String getStatus() { return this.status; }
    public void setStatus(String s) { this.status = s; }
	
	@PostConstruct
	public void initialise() {
		this.restrictBB.restrictForUser();
		this.status = new String();
	}
	
	public String loginUser() {
	    User v = this.userBB.getUserByEmail();
	    if(v != null) {
		    if(v.verifyPassword(this.userBB.getUser()) == true) {
				this.httpSession.setAttribute("userLoggedIn", true);
				this.httpSession.setAttribute("currentUser", this.userBB.getUser().getEmail());
				this.status = "Login successful. Redirecting.";
				
		        return "home?faces-redirect=true";
		    }
		    else {
				this.httpSession.invalidate();
		        this.status = "Incorrect password. Try again.";
		    }
	    }
	    else {
	    	this.status = "Username does not exist. Try with a different username.";
	    }
        return "";
	}
}