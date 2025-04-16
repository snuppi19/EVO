package com.mtran.mvc.converter;

import com.mtran.mvc.dto.EmployeeDTO;
import com.mtran.mvc.entity.EvotekEmployee;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter {
    @Autowired
    private ModelMapper modelMapper;

    public EmployeeDTO covertToDTO(EvotekEmployee employee) {
        EmployeeDTO employeeDTO =new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setAge(employee.getAge());
        return employeeDTO;
    }
    public EvotekEmployee covertToEntity(EmployeeDTO employeeDTO) {
        return modelMapper.map(employeeDTO, EvotekEmployee.class);
    }
}
