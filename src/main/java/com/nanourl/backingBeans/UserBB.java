package com.nanourl.backingBeans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.servlet.http.HttpSession;
import javax.enterprise.context.RequestScoped;

import com.nanourl.persistence.User;
import com.nanourl.logic.interfaces.UserEJBLocal;
import com.nanourl.logic.exceptions.EmailAlreadyInUseException;

@Named("userBB") @RequestScoped
public class UserBB implements Serializable {
	@EJB
	private UserEJBLocal userEJB;
	
	private User user;
	public User getUser() { return this.user; }
	
	@PostConstruct
	public void initialise() {
		this.user = new User();
	}
	
	public boolean createUser() throws EmailAlreadyInUseException {
		try {
			return this.userEJB.createUser(this.user);
		}
		catch(EmailAlreadyInUseException e) {
			throw e;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public User getUserByEmail() {
		return this.userEJB.getUserByEmail(this.user.getEmail());
	}

}