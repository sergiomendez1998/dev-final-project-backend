package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.DepartmentEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Qualifier("department")
public class DepartmentProcessingControllerServiceCrud extends CrudCatalogServiceProcessingInterceptor<DepartmentEntity> {

    private final DepartmentRepository departmentRepository;
    private  ResponseWrapper responseWrapper;

    public DepartmentProcessingControllerServiceCrud(DepartmentRepository departmentRepository){
        this.departmentRepository = departmentRepository;
    }

    @CacheEvict(value = "departments", allEntries = true)
    @Override
    public ResponseWrapper executeCreation(DepartmentEntity entity) {
        responseWrapper = new ResponseWrapper();
        departmentRepository.save(entity);

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Department created");
        responseWrapper.setData(Collections.singletonList("Department created"));
        return responseWrapper;
    }
    @CacheEvict(value = "departments", allEntries = true)
    @Override
    public ResponseWrapper executeUpdate(DepartmentEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<DepartmentEntity> departmentEntityFound = departmentRepository.findById(entity.getId());

        if (departmentEntityFound.isPresent()) {
            departmentEntityFound.get().setName(entity.getName() != null ? entity.getName() : departmentEntityFound.get().getName());
            departmentEntityFound.get().setDescription(entity.getDescription() != null ? entity.getDescription() : departmentEntityFound.get().getDescription());
            departmentRepository.save(departmentEntityFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Department updated");
            responseWrapper.setData(Collections.singletonList("Department updated"));
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Department not found");
        responseWrapper.addError("id", "Department not found");
        return responseWrapper;

    }

    @CacheEvict(value = "departments", allEntries = true)
    @Override
    public ResponseWrapper executeDeleteById(DepartmentEntity departmentEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<DepartmentEntity> analysisDocumentTypeEntityFound = departmentRepository.findById(departmentEntity.getId());

        analysisDocumentTypeEntityFound.ifPresent(analysisDocumentTypeEntity -> {
            analysisDocumentTypeEntity.setIsDeleted(true);
            departmentRepository.save(analysisDocumentTypeEntity);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("AnalysisDocumentType deleted");
        responseWrapper.setData(Collections.singletonList("AnalysisDocumentType deleted"));
        return responseWrapper;
    }
    @Cacheable(value = "departments")
     @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Departments found");

        List<CatalogWrapper> catalogWrapperList = departmentRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToCatalogWrapper)
                .toList();

        responseWrapper.setData(catalogWrapperList);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(DepartmentEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getName() ==null || entity.getName().isEmpty()) {
            responseWrapper.addError("nombre", "el nombre no puedo ser nullo o vacio");
        }

        if (entity.getDescription() ==null || entity.getDescription().isEmpty()) {
            responseWrapper.addError("descripcion", "la descripcion no puedo ser nullo o vacio");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validating");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForUpdate(DepartmentEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getId() == null || entity.getId() == 0) {
            responseWrapper.addError("id", "el id no puede ser nulo");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validating");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForDelete(DepartmentEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getId() == null || entity.getId() == 0) {
            responseWrapper.addError("id", "el id no puede ser nulo");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validating");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForRead(DepartmentEntity entity) {
        return null;
    }


    @Override
    public String getCatalogName() {
        return "department";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(DepartmentEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public DepartmentEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new DepartmentEntity(catalogDTO.getName(),catalogDTO.getDescription());
    }

    public DepartmentEntity getDepartmentByName(String name){
        return this.executeReadAll().getData()
                .stream()
                .map(DepartmentEntity.class::cast)
                .filter(departmentEntity -> departmentEntity.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}