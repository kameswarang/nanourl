package com.nanourl.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.validator.constraints.URL;

@Entity
@Table(name="Urls")
@NamedQuery(name="findByUrl",
    query="SELECT u FROM Url u WHERE u.ourl = :ourl AND u.surl = NULL")
public class Url implements Serializable {
    @Id @GeneratedValue
    private int id;
    public int getId() { return this.id; }
    public void setId(int i) { this.id = i; }
    
    @Column(nullable=false)
    @URL(message="Not a valid URL")
    @NotNull(message="Enter an URL")
    private String ourl;
    public String getOurl() { return this.ourl; }
    public void setOurl(String o) { this.ourl = o;}
    
    @Column(nullable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created;
    public Timestamp getCreated() { return this.created; }
    
    @Column
    private String surl;
    public String getSurl() { return this.surl; }
    public void setSurl(String s) { this.surl = s; }
    
    public String getPrettyOurl() {
        int i = this.ourl.indexOf("/", 8);
        return this.ourl.substring(0, i+1);
    }
    
    @Column(nullable=false)
    private int clicks;
    public int getClicks() { return this.clicks; }
    public void setClicks(int c) { this.clicks = c; }
    public void addClick() { this.clicks++; }
    
    @ManyToOne
    private User user;
    public User getUser() { return this.user; }
    public void setUser(User u) { this.user = u; }
}