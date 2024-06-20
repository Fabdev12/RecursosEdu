package com.RecursosEdu.RecursosEdu.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.RecursosEdu.RecursosEdu.models.Aluno;
import com.RecursosEdu.RecursosEdu.models.Recursos;

public interface AlunoRepository extends CrudRepository<Aluno, String>{
	
	Iterable<Aluno>findByRecursos(Recursos recurso);
	
	Aluno findByMatricula(String matricula);
	
	Aluno findById(long id);
	
	List<Aluno>findByNomeAluno(String nomeAluno);


		
	

}
