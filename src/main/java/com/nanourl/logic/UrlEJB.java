package com.nanourl.logic;

import java.io.Serializable;
import java.io.File;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.ejb.EJB;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.annotation.Resource;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolationException;
import javax.validation.ConstraintViolation;

import com.nanourl.persistence.Url;
import com.nanourl.persistence.User;
import com.nanourl.logic.interfaces.UrlEJBLocal;

@Stateless
public class UrlEJB implements UrlEJBLocal, Serializable {
	@PersistenceContext(unitName="nanourlPU")
	private EntityManager em;

	@Resource
	private SessionContext ctx;
	
	private String urlHash;
	
	private int urlBase;

	@PostConstruct
	public void initialise() {
		this.urlHash = " abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		this.urlBase = urlHash.length();
	}

	public String encodeUrl(int id, String domain) {
		StringBuilder surl = new StringBuilder();
		surl.append(domain);
		surl.append("?r=");
		
		while(id > 0) {
			surl.append(this.urlHash.charAt(id % urlBase));
			id /= urlBase;
		}
		
		//System.out.println(surl.toString());
		return surl.toString();
	}	
	
	public String decodeUrl(String surl, User user) {

		int l = surl.length();
		int id = 0;
		
		for(int i = 0; i < l; i++) {
			int pos = urlHash.indexOf(surl.charAt(i));
			id += pos * Math.pow((double) urlBase, (double) i);
		}
		
		Url u = em.find(Url.class, id);
		u.addClick();
		em.merge(u);
		em.flush();
		
		return u.getOurl();
	}
	
	public boolean createUrl(User user, Url u, String domain) {
		try {
			user.addUrl(u);
			
			//Save to database
			em.persist(u);
			em.flush();
			
			//Generate short url
			u.setSurl(this.encodeUrl(u.getId(), domain));
			
			//Update
			em.merge(u);
			
			//Merge user(currently detached) so that the cache will have the latest version
			em.merge(user);

			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			ctx.setRollbackOnly();
		}
		
		return false;
	}
}