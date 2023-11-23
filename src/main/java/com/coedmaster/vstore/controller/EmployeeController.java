package com.coedmaster.vstore.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.EmployeeDto;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.model.Employee;
import com.coedmaster.vstore.respository.EmployeeRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
public class EmployeeController {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	private EmployeeRepository employeeRepository;

	public EmployeeController(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@GetMapping("/employee")
	public ResponseEntity<List<EmployeeDto>> getEmployees() {
		List<Employee> employees = employeeRepository.findAll();

		List<EmployeeDto> employeeDtos = employees.stream().map((e) -> modelMapper.map(e, EmployeeDto.class))
				.collect(Collectors.toList());

		return new ResponseEntity<List<EmployeeDto>>(employeeDtos, HttpStatus.OK);
	}

	@PostMapping("/employee")
	public ResponseEntity<EmployeeDto> createEmployee(@RequestBody Employee employee) {
		Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Employee savedEmployee = employeeRepository.save(employee);

		EmployeeDto employeeDto = modelMapper.map(savedEmployee, EmployeeDto.class);

		return new ResponseEntity<EmployeeDto>(employeeDto, HttpStatus.OK);
	}

	@PutMapping("/employee/{id}")
	public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable(value = "id") Long id,
			@RequestBody Employee payload) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Element does't exist"));

		Set<ConstraintViolation<Employee>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		employee.setFirstName(payload.getFirstName());
		employee.setLastName(payload.getLastName());
		employee.setEmail(payload.getEmail());

		Employee savedEmployee = employeeRepository.save(employee);

		EmployeeDto employeeDto = modelMapper.map(savedEmployee, EmployeeDto.class);

		return new ResponseEntity<EmployeeDto>(employeeDto, HttpStatus.OK);
	}

	@DeleteMapping("/employee/{id}")
	public ResponseEntity<Object> deleteEmployee(@PathVariable(value = "id") Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Element does't exist"));

		employeeRepository.delete(employee);

		return new ResponseEntity<Object>(HttpStatus.OK);
	}

}
