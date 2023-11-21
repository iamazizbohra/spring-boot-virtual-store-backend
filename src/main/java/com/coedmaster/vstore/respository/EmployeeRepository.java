package com.coedmaster.vstore.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
