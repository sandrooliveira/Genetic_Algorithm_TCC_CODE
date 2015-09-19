package br.edu.univas.tcc.fabricaCalcas.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.edu.univas.tcc.fabricaCalcas.dao.HabilidadeDAO;
import br.edu.univas.tcc.fabricaCalcas.model.Habilidade;
import br.edu.univas.tcc.fabricaCalcas.model.Processo;


@ManagedBean(name = "processosController")
@ViewScoped
public class ProcessosController {
	
	private Processo processo;
	private int idHabilidade;
	private List<Habilidade> habilidades;
	
	@PostConstruct
	public void init(){
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("db_pu");
		EntityManager manager = factory.createEntityManager(); 
		HabilidadeDAO habDao = new HabilidadeDAO(manager);
		
		processo = new Processo();
		habilidades = habDao.listAllHabilidades();
		
	}
	
	public void addAtividade(){
		System.out.println("teste");
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public int getIdHabilidade() {
		return idHabilidade;
	}

	public void setIdHabilidade(int idHabilidade) {
		this.idHabilidade = idHabilidade;
	}

	public List<Habilidade> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}
	
	

}
