package com.mtran.mvc.controller;

import com.mtran.mvc.dto.EmployeeDTO;
import com.mtran.mvc.entity.EvotekEmployee;
import com.mtran.mvc.service.impl.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class EmployeeController {
  @Autowired
  private  EmployeeService employeeService;

  @GetMapping("/list")
  public List<EmployeeDTO> GetAll() {
    return employeeService.getAllEmployee();
  }

  @PostMapping("/create")
  public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO dto) {
    EvotekEmployee savedDto = employeeService.saveEmployee(dto);
    return ResponseEntity.ok(savedDto);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteEmployee(@PathVariable int id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.ok().build();
  }
}
