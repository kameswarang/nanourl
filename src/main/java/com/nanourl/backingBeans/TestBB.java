package com.nanourl.backingBeans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.nanourl.persistence.User;
import com.nanourl.backingBeans.SessionBB;

@Named("testMB")
public class TestBB {
    public void testing() {
        System.out.println("Nano: Testing...");
    }
}