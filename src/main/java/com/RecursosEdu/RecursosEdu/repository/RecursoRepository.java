package com.RecursosEdu.RecursosEdu.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.RecursosEdu.RecursosEdu.models.Recursos;

public interface RecursoRepository extends CrudRepository <Recursos, String>{
	
	Recursos findByCodigo(long codigo);
	List<Recursos> findByNome(String nome);

}
