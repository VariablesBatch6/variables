package com.var.var.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User{

	  	@Id
	  	@GeneratedValue(strategy=GenerationType.AUTO)
	    @Column(name = "userId", nullable = false, updatable = false)
	    private Long userId;
	    private String username;
	    private String password;
	    private String firstName;
	    private String lastName;

	    @Column(name = "email", nullable = false, unique = true)
	    private String email;
	    private String phone;

	    private boolean enabled=true;

	    @OneToOne
	    private PrimaryAccount primaryAccount;

	    @OneToOne
	    private SavingsAccount savingsAccount;
	    
	    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	    @JsonIgnore
	    private Set<UserRole> userRoles = new HashSet<>();
	    
	    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    @JsonIgnore
	    private List<Appointment> appointmentList;

	    public Set<UserRole> getUserRoles() {
	        return userRoles;
	    }

	    public void setUserRoles(Set<UserRole> userRoles) {
	        this.userRoles = userRoles;
	    }
	    
	    

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public PrimaryAccount getPrimaryAccount() {
			return primaryAccount;
		}

		public void setPrimaryAccount(PrimaryAccount primaryAccount) {
			this.primaryAccount = primaryAccount;
		}

		public SavingsAccount getSavingsAccount() {
			return savingsAccount;
		}

		public void setSavingsAccount(SavingsAccount savingsAccount) {
			this.savingsAccount = savingsAccount;
		}

		@Override
		public String toString() {
			return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", firstName="
					+ firstName + ", lastName=" + lastName + ", email=" + email + ", phone=" + phone + ", enabled="
					+ enabled + ", primaryAccount=" + primaryAccount + ", savingsAccount=" + savingsAccount + "]";
		}

	    


}
