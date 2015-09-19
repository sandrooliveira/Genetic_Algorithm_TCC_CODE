package br.edu.univas.tcc.fabricaCalcas.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.univas.tcc.fabricaCalcas.model.Habilidade;

public class HabilidadeDAO {
	
	private EntityManager manager;
	
	public HabilidadeDAO(EntityManager manager){
		this.manager=manager;
	}
	
	public List<Habilidade> listAllHabilidades(){
		String query  = "Select h FROM Habilidade h";
		
		TypedQuery<Habilidade> q = manager.createQuery(query,Habilidade.class);
		return q.getResultList();
	}
	

}
