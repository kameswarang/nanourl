package com.nanourl.backingBeans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.enterprise.context.RequestScoped;

import com.nanourl.logic.exceptions.EmailAlreadyInUseException;

@Named("signupBB") @RequestScoped
public class SignupBB implements Serializable {
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
	
	public String signupUser() {
		try {
			if(this.userBB.createUser()) {
				this.httpSession.setAttribute("userLoggedIn", true);
				this.httpSession.setAttribute("currentUser", this.userBB.getUser().getEmail());
    			this.status = "Signup successful. Redirecting.";

				return "home?faces-redirect=true";
			}
			else {
				this.httpSession.invalidate();
				this.status = "Problem with signup. Try later.";
				
				return null;
			}
		}
		catch(EmailAlreadyInUseException e) {
			this.httpSession.invalidate();
			this.status = "Email is already in use. Use a different email.";
		}
		return null;
	}
}