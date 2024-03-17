package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.REpo.DepMappinRepo;
import com.example.demo.REpo.depRepo;
import com.example.demo.REpo.userRepo;
import com.example.demo.entity.DepMapping;
import com.example.demo.entity.Depart;
import com.example.demo.entity.User;

@Service
public class DepDService {

	@Autowired
	depRepo depRepo;

	@Autowired
	DepMappinRepo depMappinRepo;
	@Autowired
	userRepo userRepo;

	public void saveAll(Depart dep) {
		depRepo.save(dep);

	}

	

	public void updateDepMapping(Long departmentId, Long employeeId ) {
		Depart depart = depRepo.getById(departmentId);
		Optional<User> user = userRepo.findById(employeeId);
		Optional<DepMapping> depmapping = depMappinRepo.findByDepartAndUser(depart,user.get());
		if (depmapping.isPresent()) {
			depMappinRepo.deleteById(depmapping.get().getId());
			DepMapping dep = new DepMapping();
			dep.setDepart(depart);
			
			dep.setUser(user.get());
			

			depMappinRepo.save(dep);
		} else {
			
			DepMapping dep = new DepMapping();
			dep.setDepart(depart);
			
			dep.setUser(user.get());
			

			depMappinRepo.save(dep);
		}

	}



	public List<Depart> findAll() {
		List<Depart> depart=depRepo.findAll();
		return depart;
	}
	

}
