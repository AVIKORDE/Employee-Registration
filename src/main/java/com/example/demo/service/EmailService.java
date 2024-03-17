package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.REpo.DepMappinRepo;
import com.example.demo.entity.DepMapping;
import com.example.demo.entity.Depart;
import com.example.demo.entity.User;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	DepMappinRepo depMappinRepo;

	@Scheduled(fixedDelay = 300000)
	public void getListOfUser() {
		List<DepMapping> depMapping = depMappinRepo.findByEmailStatus(false);
		if (depMapping != null) {
			for (DepMapping depM : depMapping) {
				SendEmail(depM);
			}
		}
	}

	 private void SendEmail(DepMapping depMapping) {
	        MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message);
	        try {
	            helper.setTo(depMapping.getDepart().getEmail());
	            helper.setSubject("Subject: Notification User Registration");
	            helper.setText("Hello " + depMapping.getDepart().getDepartmentName() + ",\n\nThis is a notification email.\n\n User Is register to your departnment");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        depMapping.setEmailStatus(true);
            depMappinRepo.save(depMapping);
	        javaMailSender.send(message);
	        
	    }

}
