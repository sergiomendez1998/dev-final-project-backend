package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.ExamTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.ExamTypeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("examType")
public class ExamTypeServiceProcessingInterceptorCrud extends CrudCatalogServiceProcessingInterceptor<ExamTypeEntity> {
    private final ExamTypeRepository examTypeRepository;
    private  ResponseWrapper responseWrapper;

    public ExamTypeServiceProcessingInterceptorCrud(ExamTypeRepository examTypeRepository){
        this.examTypeRepository = examTypeRepository;
    }

    @CacheEvict(value = "examTypes",allEntries = true)
    @Override
    public ResponseWrapper executeCreation(ExamTypeEntity examTypeEntity) {
        responseWrapper = new ResponseWrapper();
        examTypeRepository.save(examTypeEntity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Exam type created");
        responseWrapper.setData(Collections.singletonList("Exam type created"));
        return responseWrapper;
    }

    @CacheEvict(value = "examTypes",allEntries = true)
    @Override
    public ResponseWrapper executeUpdate(ExamTypeEntity examTypeEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<ExamTypeEntity> examTypeFound = examTypeRepository.findById(examTypeEntity.getId());

        if (examTypeFound.isPresent()) {
            examTypeFound.get().setName(examTypeEntity.getName() != null ? examTypeEntity.getName() : examTypeFound.get().getName());
            examTypeFound.get().setDescription(examTypeEntity.getDescription() != null ? examTypeEntity.getDescription() : examTypeFound.get().getDescription());
            examTypeRepository.save(examTypeFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Department updated");
            responseWrapper.setData(Collections.singletonList("Department updated"));
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("examTypeEntity not found");
        responseWrapper.addError("id","examTypeEntity not found");
        return responseWrapper;
    }

    @CacheEvict(value = "examTypes",allEntries = true)
    @Override
    public ResponseWrapper executeDeleteById(ExamTypeEntity examTypeEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<ExamTypeEntity> analysisDocumentTypeEntityFound = examTypeRepository.findById(examTypeEntity.getId());

        analysisDocumentTypeEntityFound.ifPresent(analysisDocumentTypeEntity -> {
            analysisDocumentTypeEntity.setIsDeleted(true);
            examTypeRepository.save(analysisDocumentTypeEntity);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Exam type deleted");
        responseWrapper.setData(Collections.singletonList("Exam Type deleted"));
        return responseWrapper;
    }

    @Cacheable (value = "examTypes")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Exam types found");
        List<CatalogWrapper> catalogWrapperList = examTypeRepository
                .findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToCatalogWrapper)
                .toList();
        responseWrapper.setData(catalogWrapperList);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(ExamTypeEntity entity) {
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
    protected ResponseWrapper validateForUpdate(ExamTypeEntity entity) {
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
    protected ResponseWrapper validateForDelete(ExamTypeEntity entity) {
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
    protected ResponseWrapper validateForRead(ExamTypeEntity entity) {
        return null;
    }

    @Override
    public String getCatalogName() {
        return "examType";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(ExamTypeEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public ExamTypeEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new ExamTypeEntity(catalogDTO.getName(),catalogDTO.getDescription());
    }
}