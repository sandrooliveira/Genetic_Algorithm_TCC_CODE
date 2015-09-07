package br.edu.univas.tcc.fabricaCalcas.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.edu.univas.tcc.fabricaCalcas.model.Costureira;

@ManagedBean(name = "costureirasController")
@ViewScoped
public class CostureirasController {

	private List<Costureira> costureiras;
	
	@PostConstruct
	public void init(){
		costureiras = new ArrayList<Costureira>();
		
		Costureira costureira1 = new Costureira();
		costureira1.setIdCostureira(1);
		costureira1.setNomeCostureira("Chica");
		costureiras.add(costureira1);
		
		Costureira costureira2 = new Costureira();
		costureira2.setIdCostureira(2);
		costureira2.setNomeCostureira("Benta");
		costureiras.add(costureira2);
	}
	
	
	/*Getters and setters*/
	public List<Costureira> getCostureiras() {
		return costureiras;
	}

	public void setCostureiras(List<Costureira> costureiras) {
		this.costureiras = costureiras;
	}
}
