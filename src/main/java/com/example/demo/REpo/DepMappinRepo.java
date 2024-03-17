package com.example.demo.REpo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DepMapping;
import com.example.demo.entity.Depart;
import com.example.demo.entity.User;

@Repository
public interface DepMappinRepo extends JpaRepository<DepMapping, Long> {

	Optional<DepMapping> findByDepartAndUser(Depart depart, User user);

	List<DepMapping> findByEmailStatus(boolean emailStatus);

}
