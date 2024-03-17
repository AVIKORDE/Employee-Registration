package com.example.demo.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DepMapping {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
	private User user;
	@ManyToOne
	@JoinColumn(name = "depId",referencedColumnName = "id",nullable = false)
	private Depart depart;
	
	@Column(name="emailStatus")
	private boolean emailStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Depart getDepart() {
		return depart;
	}

	public void setDepart(Depart depart) {
		this.depart = depart;
	}

	public boolean isEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(boolean emailStatus) {
		this.emailStatus = emailStatus;
	}
	
	
	

	

}
