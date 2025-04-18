package com.mtran.mvc.service.impl;


import com.mtran.mvc.converter.EmployeeConverter;
import com.mtran.mvc.dto.EmployeeDTO;
import com.mtran.mvc.entity.EvotekEmployee;
import com.mtran.mvc.repository.EmployeeRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    @Autowired
    private EmployeeRepository repository;
    @Autowired
    private EmployeeConverter converter;
    private final HashSet<EmployeeDTO> empSet = new HashSet<>();
    private final TreeSet<EmployeeDTO> treeSet = new TreeSet<>();

    public List<EmployeeDTO> getAllEmployee() {
        List<EvotekEmployee> employeeList = repository.findAll();
        treeSet.addAll(employeeList.stream().map(converter::covertToDTO).collect(Collectors.toList()));
        return treeSet.stream().collect(Collectors.toList());
    }

    public EvotekEmployee saveEmployee(EmployeeDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Employee can not be null");
        }
        if (dto.getId() <= 0) {
            throw new IllegalArgumentException("Employee id can must be > 0");
        }
        if (dto.getAge() <= 18) {
            throw new IllegalArgumentException("Employee age can not be less than 18");
        }
        dto.getName().trim();
        if (dto.getName().isEmpty() || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Employee name can not be empty");
        }

        //check trùng id bằng hashSet
        if (!(empSet.add(dto))) {
            throw new IllegalArgumentException("Employee with ID " + dto.getId() + " already exists");
        }

        dto.setName(capitalizeFirstLetters(dto.getName()));
        EvotekEmployee a = converter.covertToEntity(dto);
        return repository.save(a);
    }

    public void deleteEmployee(int id) {
        EvotekEmployee a = repository.findById(id).orElse(null);
        EmployeeDTO dto = converter.covertToDTO(a);
        empSet.remove(dto);
        treeSet.remove(dto);
        repository.delete(a);
    }





    //=================================================================================
    private String capitalizeFirstLetters(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String[] words = input.split("\\s+");
        StringBuilder capitalizedString = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                capitalizedString.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return capitalizedString.toString().trim();
    }

    //nâng cao
    private int getAgeByString(String input) {
        HashMap<String, Integer> wordToNumber = new HashMap<>();
        wordToNumber.put("không", 0);
        wordToNumber.put("một", 1);
        wordToNumber.put("hai", 2);
        wordToNumber.put("ba", 3);
        wordToNumber.put("bốn", 4);
        wordToNumber.put("năm", 5);
        wordToNumber.put("sáu", 6);
        wordToNumber.put("bảy", 7);
        wordToNumber.put("tám", 8);
        wordToNumber.put("chín", 9);

        input = input.trim().toLowerCase();
        String[] words = input.split("\\s+");
        if (words.length < 1 || words.length > 3) {
            throw new IllegalArgumentException("Invalid input format");
        }

        int age = 0;
        if (words.length == 1) {
            if (wordToNumber.containsKey(words[0])) {
                age = wordToNumber.get(words[0]);
            } else {
                try {
                    age = Integer.parseInt(words[0]);
                    if (age < 0 || age > 9) {
                        throw new IllegalArgumentException("Number must be between 0 and 9");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid word: " + words[0]);
                }

            }
        } else if (words.length == 2) {
            if (!words[1].equals("mươi")) {
                throw new IllegalArgumentException("Expected 'mươi' as second word, found: " + words[1]);
            }
            String tensWord = words[0];
            int tensValue;
            if (wordToNumber.containsKey(tensWord)) {
                tensValue = wordToNumber.get(tensWord);
            } else {
                try {
                    tensValue = Integer.parseInt(tensWord);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid tens word: " + tensWord);
                }
            }
            if (tensValue < 1 || tensValue > 9) {
                throw new IllegalArgumentException("Tens must be between 1 and 9");
            }
            age = tensValue * 10;
            return age;
        } else {
            if (!words[1].equals("mươi")) {
                throw new IllegalArgumentException("Expected 'mươi' as second word, found: " + words[1]);
            }
            String tensWord = words[0];
            String unitWord = words[2];
            int tensValue, unitValue;
            if (wordToNumber.containsKey(tensWord)) {
                tensValue = wordToNumber.get(tensWord);
            } else {
                try {
                    tensValue = Integer.parseInt(tensWord);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid tens word: " + tensWord);
                }
            }
            if (tensValue < 1 || tensValue > 9) {
                throw new IllegalArgumentException("Tens must be between 1 and 9");
            }
            if (wordToNumber.containsKey(unitWord)) {
                unitValue = wordToNumber.get(unitWord);
            } else {
                try {
                    unitValue = Integer.parseInt(unitWord);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid unit word: " + unitWord);
                }
            }
            if (unitValue < 1 || unitValue > 9) {
                throw new IllegalArgumentException("Unit must be between 1 and 9");
            }
         age = (tensValue * 10) + unitValue;
            return age;
        }
        return age;
    }


}
