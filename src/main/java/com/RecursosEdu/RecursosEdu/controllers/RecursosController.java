package com.RecursosEdu.RecursosEdu.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.RecursosEdu.RecursosEdu.models.Aluno;
import com.RecursosEdu.RecursosEdu.models.Recursos;
import com.RecursosEdu.RecursosEdu.repository.AlunoRepository;
import com.RecursosEdu.RecursosEdu.repository.RecursoRepository;

import jakarta.validation.Valid;

@Controller
public class RecursosController {
	
	@Autowired
	private RecursoRepository rr;
	
	@Autowired
	private AlunoRepository ar;
	
	//CADASTRA RECURSOS
	@RequestMapping("/cadastrarRecurso")
	public String form() {
		
		return "recurso/formRecurso";
	}
	
	@RequestMapping(value = "/cadastrarRecurso", method = RequestMethod.POST)
	public String form(@Valid Recursos recurso, BindingResult result, RedirectAttributes attributes) {
	    
	    if(result.hasErrors()) {
	        for (FieldError error : result.getFieldErrors()) {
	            System.out.println(error.getField() + ": " + error.getDefaultMessage());
	        }
	        attributes.addFlashAttribute("mensagem", "Verifique os campos...");
	        return "redirect:/cadastrarRecurso";
	    }
	    
	    rr.save(recurso);
	    attributes.addFlashAttribute("mensagem", "Recurso cadastrado com sucesso!");
	    return "redirect:/cadastrarRecurso";
	}
	
	//LISTAR RECURSOS
	
	@RequestMapping("/recurso")
	public ModelAndView listaRecurso() {
		ModelAndView mv = new ModelAndView("recurso/listaRecurso");
		Iterable<Recursos>recurso = rr.findAll();
		mv.addObject("recurso", recurso);
		return mv;
	}
	
	@RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
	public ModelAndView detalhesRecurso(@PathVariable("codigo") long codigo) {
		Recursos recurso = rr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("recurso/detalhesRecurso");
		mv.addObject("recurso", recurso);
		
		Iterable<Aluno>aluno = ar.findByRecursos(recurso);
		mv.addObject("aluno", aluno);
		return mv;
	}
	
	//DELETA RECURSO
	@RequestMapping("/deletarRecurso")
	public String deletarRecurso(long codigo) {
		Recursos recurso = rr.findByCodigo(codigo);
		rr.delete(recurso);
		return "redirect:/recurso";
	}
	
	@RequestMapping(value = "/{codigo}", method = RequestMethod.POST)
	public String detalhesRecursoPost(@PathVariable("codigo") long codigo, @Valid Aluno aluno, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/{codigo}";
		}
		//MATRICULA DUPLICADA
		if(ar.findByMatricula(aluno.getMatricula()) != null) {
			attributes.addFlashAttribute("mensagem_erro", "Matrícula ja existente!");
			return "redirect:/{codigo}";
		}
		
		Recursos recurso = rr.findByCodigo(codigo);
		aluno.setRecursos(recurso);
		ar.save(aluno);
		attributes.addFlashAttribute("mensagem", "Aluno adicionado com sucesso!");
		return "redirect:/{codigo}";
	}
	
	//DELETA ALUNO PELA MATRICULA
	@RequestMapping("/deleteAluno")
	public String deletarAluno(String matricula) {
		Aluno aluno = ar.findByMatricula(matricula);
		Recursos recurso = aluno.getRecursos();
		String codigo = "" + recurso.getCodigo();
		
		ar.delete(aluno);
		
		return "redirect:/"+codigo;
		
	}
	
	//METODOS QUE ATUALIZAM VRECURSO
	//FORMULARIO DE EDIÇÃO DE RECURSO
	
	@RequestMapping(value="/editar-recurso", method = RequestMethod.GET)
	public ModelAndView editarRecurso(long codigo) {
		Recursos recurso = rr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("recurso/updateRecurso");
		mv.addObject("recurso", recurso);
		return mv;
	}
	
	//UPDATE RECURSO
	@RequestMapping(value = "editar-recurso", method = RequestMethod.POST)
	public String updateRecurso(@Valid Recursos recurso, BindingResult result, RedirectAttributes attributes ) {
		rr.save(recurso);
		attributes.addFlashAttribute("sucess", "Recurso educacional alterado com sucesso!");
		
		long codigoLong = recurso.getCodigo();
		String codigo = "" + codigoLong;
		return "redirect:/" + codigo;
		
	}
	
	
	
	
	
	
}
