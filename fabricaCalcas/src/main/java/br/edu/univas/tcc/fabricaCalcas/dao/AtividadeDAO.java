package br.edu.univas.tcc.fabricaCalcas.dao;

import javax.persistence.EntityManager;

import br.edu.univas.tcc.fabricaCalcas.model.Atividade;

public class AtividadeDAO {

	private EntityManager manager;
	
	public AtividadeDAO(EntityManager manager){
		this.manager = manager;
	}
	
	public void addNovaAtividade(Atividade atividade){
		manager.getTransaction().begin();
		
		if(atividade.getProcesso().getIdProcesso() == null ||
				atividade.getProcesso().getIdProcesso() == 0){
			manager.persist(atividade.getProcesso());
		}
		manager.persist(atividade);
		
		manager.getTransaction().commit();
	}
}
