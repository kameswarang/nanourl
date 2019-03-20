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

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.Writer;

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

    @PostConstruct
    public void initialise() {
        this.restrictBB.restrictForVisitor();
        this.fCtx = FacesContext.getCurrentInstance();
        this.eCtx = fCtx.getExternalContext();
        this.currentUser = this.sessionBB.getCurrentUser();
        this.surl = req.getParameter("r");
        this.newUrl = new Url();

        //Look for surl and redirect
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
    }
    
    public String shortenUrl() {
        String domain = req.getServerName() + ":" + req.getServerPort();
        
        this.urlEJB.createUrl(this.newUrl, this.currentUser, domain);
        System.out.println(this.newUrl.getSurl());
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