package com.example.demo.REpo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Depart;

@Repository
public interface depRepo extends JpaRepository<Depart, Long>{

    Optional<Depart> findByDepartmentName(String departmentName);


}
