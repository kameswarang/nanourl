package com.nanourl.backingBeans;

import com.nanourl.persistence.Url;
import com.nanourl.persistence.User;

import com.nanourl.logic.interfaces.UrlEJBLocal;

import javax.inject.Named;
import javax.inject.Inject;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ConstraintViolation;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.Writer;
import java.util.Set;

@Named("urlBB") @RequestScoped
public class UrlBB {
    @EJB
    private UrlEJBLocal urlEJB;
    
    @Inject
    private HttpServletRequest req;
	
	@Inject
	private SessionBB sessionBB;
	
	@Inject
	private RestrictBB restrictBB;
	
    private FacesContext fCtx;
    private ExternalContext eCtx;
    
    private User currentUser;
    private String surl;

    private Url newUrl;
    public Url getNewUrl() { return this.newUrl; }
    
    private String shortenRequestStatus;
    public String getShortenRequestStatus() { return this.shortenRequestStatus; }

    @PostConstruct
    public void initialise() {
        //Safety method
        this.restrictBB.restrictForVisitor();
        
        this.fCtx = FacesContext.getCurrentInstance();
        this.eCtx = fCtx.getExternalContext();
        this.currentUser = this.sessionBB.getCurrentUser();

        //Look for surl and redirect
        this.surl = req.getParameter("r");
        if(this.surl != null) {
            String ourl = this.urlEJB.decodeUrl(surl, this.currentUser);
            try {
                this.eCtx.redirect(ourl);
                return;
            }
            catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
        //Business as usual
        this.newUrl = new Url();

        this.shortenRequestStatus = "";

    }
    
    public String shortenUrl() {
        String domain = req.getServerName() + ":" + req.getServerPort();
        
        Url newShortUrl = new Url();
        newShortUrl.setOurl(newUrl.getOurl());
        //System.out.println(this.currentUser.getUrls().size());
        //newShortUrl.setUser(this.currentUser);
        //System.out.println(this.currentUser.getUrls().size());


        if(this.urlEJB.createUrl(this.currentUser, newShortUrl, domain) == false) {
            //put error message in page
            shortenRequestStatus = "Error occured. Try again later.";
        }
        else {
            //Success
            //Copy short url to newUrl so that result can be displayed
            this.newUrl.setSurl(newShortUrl.getSurl());
            
            //Request succeeded, update reset status message
            shortenRequestStatus = "";
        }
        return null;
    }
    
    public String downloadCsv() {
        eCtx.responseReset();
        eCtx.setResponseContentType("text/csv");
        eCtx.setResponseHeader("Content-disposition", "attachment; filename=allURLs.csv");
        
        Writer out = null;
        StringBuilder output = new StringBuilder();

        try {
            out = eCtx.getResponseOutputWriter();
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        
        output.append("Original URL,Created,Short URL,All Clicks\n");
        for(Url u : this.currentUser.getUrls()) {
            output.append(u.getOurl()); output.append(",");
            output.append(u.getCreated()); output.append(",");
            output.append(u.getSurl()); output.append(",");
            output.append(u.getClicks()); output.append(",");
            output.append("\n");
        }
        
        try {
            out.write(output.toString());
            out.flush();
            out.close();
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        
        fCtx.responseComplete();
        
        return null;
    }
}