package com.nanourl.logic.interfaces;

import javax.ejb.Local;

import com.nanourl.persistence.User;
import com.nanourl.logic.exceptions.EmailAlreadyInUseException;

@Local
public interface UserEJBLocal {
	public boolean createUser(User u) throws EmailAlreadyInUseException;
    public User getUserByEmail(String email);
}