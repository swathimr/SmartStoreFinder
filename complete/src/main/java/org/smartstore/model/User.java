package org.smartstore.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;

	// The user's email
	@NotNull
	private String email;

	// The user's name
	@NotNull
	private String name;

	@NotNull
	private String password;
	
	@OneToMany(mappedBy="user", targetEntity=Order.class, fetch=FetchType.EAGER)
	private List<Order> listOrders;
	

	public User( String email, String name, String password){
		this.email = email;
		this.name = name;
		this.password = password;
	}
	
	

	public long getUserId() {
		return userId;
	}



	public void setUserId(long userId) {
		this.userId = userId;
	}



	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
