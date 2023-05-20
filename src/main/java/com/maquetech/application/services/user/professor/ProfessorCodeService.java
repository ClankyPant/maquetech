package com.maquetech.application.services.user.professor;

import com.maquetech.application.entities.user.professor.ProfessorCodeEntity;
import com.maquetech.application.repositories.user.professor.ProfessorCodeRepository;
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
