package br.com.alura.listavip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.alura.enviadorEmail.mailService;
import br.com.alura.listavip.model.Convidado;
import br.com.alura.listavip.service.ConvidadoService;

@Controller
public class ConvidadoController {
	
	@Autowired
	private ConvidadoService service;
	
	@RequestMapping("listavip")
	public String listaCOnvidados(Model model){
		
		return carregaLista(model);
	}
	
	@RequestMapping(value = "salvar", method = RequestMethod.POST)
	public String salvar(@RequestParam("nome") String nome, 
						 @RequestParam("email") String email, 
						 @RequestParam("telefone") String telefone,
						 Model model) {
		
		Convidado novoConvidado = new Convidado (nome, email, telefone);
		
		service.salvar(novoConvidado);
		
		new mailService().enviar(nome, email);
		
		return carregaLista(model);
	}
	
	@RequestMapping(value = "apagar/{id}", method = RequestMethod.GET)
	public String apagar(@PathVariable Long id, Model model) {
		
		service.apagar(id);
		return "redirect:/listavip";
	}
	
	private String carregaLista(Model model) {
		
		Iterable<Convidado> convidados = service.obterTodos();		
		model.addAttribute("convidados", convidados);
		return "listaconvidados";
	}
}
