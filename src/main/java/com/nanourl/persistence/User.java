package com.nanourl.persistence;

import java.io.Serializable;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.PrePersist;
import javax.persistence.PostLoad;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;

@Entity
@Table(name="Users")
public class User implements Serializable {
	@Column(nullable=false)
	@Size(max=50, message="Cannot be longer that 50 characters")
	@NotNull(message="Enter a name")
	private String fname;
	public String getFname() { return this.fname; }
	public void setFname(String fname) { this.fname = fname; }

	@Column(nullable=false)
	@NotNull(message="Enter a name")
	@Size(max=50, message="Cannot be longer than 50 characters")
	private String lname;
	public String getLname() { return this.lname; }
	public void setLname(String lname) { this.lname = lname; }

	@Id @Column(nullable=false)
	@NotNull(message="Enter an email address")
	@Pattern(regexp="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
	message="Not a valid email address")
	private String email;
	public String getEmail() { return this.email; }
	public void setEmail(String email) { this.email = email; }

	@Transient
	@NotNull(message="Enter a password")
	@Size(min=5, message="Must be alteast 5 characters")
	private char[] password;
	public char[] getPassword() { return this.password; }
	public void setPassword(char[] password) { this.password = password; }

	@Column(nullable=false)
	private byte[] salt;
	public byte[] getSalt() { return this.salt; }
	public void setSalt(byte[] s) { this.salt = s; }

	@Column(nullable=false)
	private byte[] hash;
	public byte[] getHash() { return this.hash; }

	@OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Url> urls;
	public List<Url> getUrls() { return this.urls; }
	public void addUrl(Url u) {
		if(u.getUser() == this) {
			this.urls.add(u);
			return;
		}
		u.setUser(this);
	}

	public User() {
	}

	public String getName() {
		return this.fname + " " + this.lname;
	}
	
	@PrePersist
	public void setHashFromPassword() {
		if(this.hash != null) {
			return;
		}
		
		String algorithm = System.getenv("PASS_ALGO");
		SecretKeyFactory skFactory = null;

		try {
			skFactory = SecretKeyFactory.getInstance(algorithm);
		}
		catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		SecureRandom sRand = new SecureRandom();	
		
		if(this.salt == null) {
			this.salt = new byte[16];
			sRand.nextBytes(this.salt);
		}

		PBEKeySpec spec = new PBEKeySpec(this.password, this.salt, 65536, 128);

		try {
			SecretKey sk = skFactory.generateSecret(spec);
			this.hash = sk.getEncoded();
		}
		catch(InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}
	
	public boolean verifyPassword(User v) {
	    v.setSalt(this.salt);
	    v.setHashFromPassword();
		return Arrays.equals(this.hash, v.getHash());
	}
	
	@PostLoad
	public void makePasswordNotNull() {
		this.password = ("5chars").toCharArray();
	}
}