package com.example.demo.service;

import java.io.IOException;
import java.lang.StackWalker.Option;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.REpo.userRepo;
import com.example.demo.entity.User;

@Service
public class UserService {
	@Autowired
	userRepo userRepo;

	public List<User> fetAlluser() {
		List<User> list = userRepo.findAll();

		return list;
	}

	public Optional<User> findById(long id) {

		Optional<User> user = userRepo.findById(id);
		return user;
	}

	public void saveUser(User user) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashedPassword = md.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : hashedPassword) {
				sb.append(String.format("%02x", b));
			}

			user.setPassword(sb.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		userRepo.save(user);
		return;

	}

	public User findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public boolean verifyPassword(String providedPassword, String storedHash) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] providedPasswordHash = md.digest(providedPassword.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : providedPasswordHash) {
				sb.append(String.format("%02x", b));
			}

			return sb.toString().equals(storedHash);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<User> getAllUsers() {
		List<User> user = userRepo.findAll();
		return user;
	}

	public User viewById(long id) {
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	public boolean isEmailExists(String username, String email) {

		User existingEmployee = userRepo.findByEmail(email);
		User existingUser = userRepo.findByUsername(username);
		return (existingEmployee != null || existingUser != null);

	}

	public void updateEmployee(User UpdateEmp, MultipartFile file, String plainPassword, long id)
			throws SerialException, SQLException {
		Optional<User> existingEmployee = userRepo.findById(id);
		if (existingEmployee.isPresent()) {
			User employeeToUpdate = existingEmployee.get();
			employeeToUpdate.setName(UpdateEmp.getName());
			employeeToUpdate.setEmail(UpdateEmp.getEmail());
			employeeToUpdate.setCity(UpdateEmp.getCity());
			employeeToUpdate.setUsername(UpdateEmp.getUsername());
			employeeToUpdate.setDateofbirth(UpdateEmp.getDateofbirth());
			employeeToUpdate.setCountry(UpdateEmp.getCountry());
			if (plainPassword != null && !plainPassword.isEmpty()) {

				employeeToUpdate.setPassword(plainPassword);
			}

			if (!file.isEmpty()) {
				try {
					byte[] bytes = file.getBytes();
					Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
					employeeToUpdate.setPhoto(blob);
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			MessageDigest md;
			if (plainPassword != null && !plainPassword.isEmpty()) {
				try {
					md = MessageDigest.getInstance("SHA-256");
					byte[] hashedPassword = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
					StringBuilder sb = new StringBuilder();
					for (byte b : hashedPassword) {
						sb.append(String.format("%02x", b));
					}
					System.out.println("plain password" + plainPassword);

					employeeToUpdate.setPassword(sb.toString());

				} catch (NoSuchAlgorithmException e) {

					e.printStackTrace();
				}
			}

			employeeToUpdate.setMobileno(UpdateEmp.getMobileno());

			userRepo.save(employeeToUpdate);
		}

	}

	public boolean existsByEmail(String email) {
		User user = userRepo.findByEmail(email);
		return user != null;
	}

	public boolean existsByUsername(String username) {
		User user = userRepo.findByUsername(username);
		return user != null;
	}

}
