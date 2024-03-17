package com.example.demo.entity;

import java.sql.Blob;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "full_name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "designation")
	private String designation;
	@Column(name="dob")
	private String dateofbirth;
	@Column(name = "mobile")
	private String mobileno;
	@Column(name="city")
	private String city;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Lob
    @Column(columnDefinition = "MEDIUMBLOB" ,name="photo")
	private Blob photo;
	@Column(name = "country")
	private String country;
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", username=" + username + ", password="
				+ password + ", designation=" + designation + ", dateofbirth=" + dateofbirth + ", mobileno=" + mobileno
				+ ", photo=" + photo + ", country=" + country + "]";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getDateofbirth() {
		return dateofbirth;
	}
	public void setDateofbirth(String dateofbirth) {
		this.dateofbirth = dateofbirth;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public Blob getPhoto() {
		return photo;
	}
	public void setPhoto(Blob photo) {
		this.photo = photo;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

}
