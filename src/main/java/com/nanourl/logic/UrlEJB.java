package com.nanourl.logic;

import java.io.Serializable;
import java.io.File;

import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.ejb.EJB;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.annotation.Resource;
import javax.annotation.PostConstruct;

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
		surl.append("/nano?r=");
		
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
	
	public boolean createUrl(Url u, User user, String domain) {
		try {
			user.addUrl(u);
			
			em.persist(u);
			em.flush();
			//em.merge(user);
			
			u = (Url) em.createNamedQuery("findByUrl").setParameter("ourl", u.getOurl()).getSingleResult();
			
			u.setSurl(this.encodeUrl(u.getId(), domain));
			em.merge(u);
			em.flush();
			
			em.refresh(u);
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