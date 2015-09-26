package br.edu.univas.tcc.fabricaCalcas.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.univas.tcc.fabricaCalcas.model.Atividade;
import br.edu.univas.tcc.fabricaCalcas.model.AtividadeOrdem;

public class AtividadeDAO {

	private EntityManager manager;
	
	public AtividadeDAO(EntityManager manager){
		this.manager = manager;
	}
	
	public void addNovaAtividade(Atividade atividade){
		manager.getTransaction().begin();
		manager.persist(atividade);
		manager.getTransaction().commit();
	}
	
	public List<Atividade> listAtividadesByProcesso(int idProcesso){
		String query = "Select a from Atividade a where a.processo.idProcesso = :idProcesso";
		TypedQuery<Atividade> q = manager.createQuery(query,Atividade.class);
		q.setParameter("idProcesso", idProcesso);
		return q.getResultList();
	}
	
	public void updateAtividade(Atividade atividade){
		manager.getTransaction().begin();
		manager.merge(atividade);
		manager.getTransaction().commit();
	}
	
	public void addAtividadeOrdem(AtividadeOrdem atividadeOrdem){
		manager.getTransaction().begin();
		manager.persist(atividadeOrdem);
		manager.getTransaction().commit();
	}
	
	public void removeAtividadeOrdem(AtividadeOrdem atividadeOrdem){
		manager.getTransaction().begin();
		manager.remove(atividadeOrdem);
		manager.getTransaction().commit();
	}
}
