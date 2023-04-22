package com.example.application.services.user;

import com.example.application.entities.user.ProfessorCodeEntity;
import com.example.application.repositories.user.ProfessorCodeRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProfessorCodeService {

    private final ProfessorCodeRepository repository;

    public ProfessorCodeService(ProfessorCodeRepository repository) {
        this.repository = repository;
    }

    private ProfessorCodeEntity getByCode(String code) throws NotFoundException {
        ProfessorCodeEntity result = this.repository.getByCode(code);
        if (result == null) throw new NotFoundException("Código não encontrado na base de dados!");

        return result;
    }

    public boolean isValidCode(String code) throws NotFoundException {
        return this.getByCode(code).getIsValid();
    }

    public void save(String code) {
        this.repository.save(new ProfessorCodeEntity(code, true));
    }

    public void invalidateCode(String code) throws NotFoundException {
        ProfessorCodeEntity professorCodeEntity = this.getByCode(code);
        professorCodeEntity.setIsValid(false);
        this.repository.save(professorCodeEntity);
    }
}
