package com.nanourl.logic;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.ejb.EJB;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.annotation.Resource;

import com.nanourl.persistence.User;
import com.nanourl.logic.interfaces.UserEJBLocal;
import com.nanourl.logic.exceptions.EmailAlreadyInUseException;

@Stateless
public class UserEJB implements UserEJBLocal, Serializable {
	@PersistenceContext(unitName="nanourlPU")
	private EntityManager em;

	@Resource
	private SessionContext ctx;
	

	public boolean createUser(User u) throws EmailAlreadyInUseException {
		User exU = em.find(u.getClass(), u.getEmail());
		if(exU != null) {
			throw new EmailAlreadyInUseException();
		}
		
		em.persist(u);
		em.flush();
		em.merge(u);
		
		return true;
	}
	
	public User getUserByEmail(String email) {
		if(email != null) {
		    try {
				return em.find(User.class, email);
		    }
		    catch(Exception e) {
		        e.printStackTrace();
		        ctx.setRollbackOnly();
		    }
		}
	    return null;
    }
}