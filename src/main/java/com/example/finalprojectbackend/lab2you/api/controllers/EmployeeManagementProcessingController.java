package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.db.model.dto.EmployeeDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.DepartmentEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.EmployeeEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.RoleEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.service.EmployeeService;
import com.example.finalprojectbackend.lab2you.service.UserService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.DepartmentService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.RoleService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeManagementProcessingController {

    private final EmployeeService employeeService;

    private final UserService userService;

    private final RoleService roleServiceCRUD;

    private final DepartmentService departmentServiceCRUD;

    private ResponseWrapper responseWrapper;

    public EmployeeManagementProcessingController(EmployeeService employeeService, UserService userService, RoleService roleServiceCRUD, DepartmentService departmentServiceCRUD) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.roleServiceCRUD = roleServiceCRUD;
        this.departmentServiceCRUD = departmentServiceCRUD;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getAll() {
        responseWrapper = employeeService.executeReadAll();
        return ResponseEntity.ok(responseWrapper);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper> register(@RequestBody EmployeeDTO employeeDTO) {
        UserEntity userEntity = new UserEntity();

        RoleEntity role = roleServiceCRUD.getRoleByName(employeeDTO.getUser().getRole().getName());
        DepartmentEntity department = departmentServiceCRUD.getDepartmentByName(employeeDTO.getDepartment().getName());

        userEntity.setNickName(employeeDTO.getUser().getNickName());
        userEntity.setUserType(Lab2YouConstants.lab2YouUserTypes.EMPLOYEE.getUserType());
        userEntity.setEmail(employeeDTO.getUser().getEmail());
        userEntity.setPassword(Lab2YouUtils.encodePassword(employeeDTO.getUser().getPassword()));
        userEntity.setEnabled(true);

        userEntity.setRole(role);

        EmployeeEntity employeeEntity = employeeService.mapToEntityEmployee(employeeDTO);
        employeeEntity.setUser(userEntity);
        employeeEntity.setDepartmentEntity(department);

        responseWrapper = employeeService.validate(employeeEntity, Lab2YouConstants.operationTypes.CREATE.getOperationType());

        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }
        userService.save(userEntity);
        employeeService.execute(employeeEntity, Lab2YouConstants.operationTypes.CREATE.getOperationType());
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage(Lab2YouConstants.lab2YouSuccessCodes.REGISTRATION_SUCCESS.getDescription());
        return ResponseEntity.ok(responseWrapper);
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> update(@RequestBody EmployeeDTO employeeDTO) {
        UserEntity userEntity = new UserEntity();

        RoleEntity role = roleServiceCRUD.getRoleByName(employeeDTO.getUser().getRole().getName());
        userEntity.setId(employeeDTO.getUser().getId());
        userEntity.setEmail(employeeDTO.getUser().getEmail());
        userEntity.setRole(role);

        EmployeeEntity employeeEntity = employeeService.mapToEntityEmployee(employeeDTO);
        employeeEntity.setUser(userEntity);

        responseWrapper = employeeService.validate(employeeEntity, Lab2YouConstants.operationTypes.UPDATE.getOperationType());

        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }
        userService.executeUpdate(userEntity);
        employeeService.execute(employeeEntity, Lab2YouConstants.operationTypes.UPDATE.getOperationType());
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage(Lab2YouConstants.lab2YouSuccessCodes.REGISTRATION_SUCCESS.getDescription());
        return ResponseEntity.ok(responseWrapper);
    }

    @DeleteMapping
    public ResponseEntity<ResponseWrapper> delete(@PathParam("id") Long id) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setId(id);
        responseWrapper = employeeService.validate(employeeEntity, Lab2YouConstants.operationTypes.DELETE.getOperationType());
        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }
        responseWrapper = employeeService.execute(employeeEntity, Lab2YouConstants.operationTypes.DELETE.getOperationType());
        return ResponseEntity.ok(responseWrapper);
    }

}
