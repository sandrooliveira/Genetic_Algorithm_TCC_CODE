package br.edu.univas.tcc.fabricaCalcas.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.univas.tcc.fabricaCalcas.model.Processo;

public class ProcessoDAO {

	EntityManager manager;
	
	public ProcessoDAO(EntityManager manager){
		this.manager = manager;
	}
	
	public void addProcesso(Processo processo){
		manager.getTransaction().begin();
		manager.persist(processo);
		manager.getTransaction().commit();
	}
	
	public List<Processo> listAllProcessos(){
		String q = "Select p from Processo p ORDER BY p.idProcesso DESC";
		TypedQuery<Processo> query = manager.createQuery(q,Processo.class);
		return query.getResultList();
	}
	
	public Processo getProcessoByID(int idProcesso){
		String query = "SELECT p from Processo p where p.idProcesso = :idProcesso";
		TypedQuery<Processo> q = manager.createQuery(query,Processo.class);
		q.setParameter("idProcesso", idProcesso);
		return q.getSingleResult();
	}
	
	public void updateProcesso(Processo processo){
		manager.getTransaction().begin();
		manager.merge(processo);
		manager.getTransaction().commit();
	}
	
	public void excluirProcesso(Processo processo){
		manager.getTransaction().begin();
		manager.remove(processo);
		manager.getTransaction().commit();
	}
}
