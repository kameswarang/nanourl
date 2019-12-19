package com.nanourl.logic.interfaces;

import java.io.File;

import javax.ejb.Local;

import com.nanourl.persistence.Url;
import com.nanourl.persistence.User;

@Local
public interface UrlEJBLocal {
    public String encodeUrl(int id, String domain);
    public String decodeUrl(String surl, User user);
	public boolean createUrl(User user, Url u, String domain);
}