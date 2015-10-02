package br.edu.univas.tcc.fabricaCalcas.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.edu.univas.tcc.fabricaCalcas.model.Atividade;
import br.edu.univas.tcc.fabricaCalcas.model.AtividadeOrdem;

public class AtividadeDAO {

	private EntityManager manager;
	
	public AtividadeDAO(EntityManager manager){
		this.manager = manager;
	}
	
	public void addNovaAtividade(Atividade atividade){
		try {
			manager.getTransaction().begin();
			manager.persist(atividade);
			manager.flush();
			manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
			e.printStackTrace();
			System.out.println("Problema ao add aitividade");
		}
	}
	
	public List<Atividade> listAtividadesByProcesso(int idProcesso){
		manager.clear();
		String query = "Select a from Atividade a where a.processo.idProcesso = :idProcesso";
		TypedQuery<Atividade> q = manager.createQuery(query,Atividade.class);
		q.setParameter("idProcesso", idProcesso);
		return q.getResultList();
	}
	
	public void updateAtividade(Atividade atividade){
		try {
			manager.getTransaction().begin();
			manager.merge(atividade);
			manager.flush();
			manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
			e.printStackTrace();
			System.out.println("Problema ao atualizar aitividade ordem");
		}
	}
	
	public void addAtividadeOrdem(AtividadeOrdem atividadeOrdem){
		try {
			manager.getTransaction().begin();
			manager.persist(atividadeOrdem);
			manager.flush();
			manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
			e.printStackTrace();
			System.out.println("Problema ao add aitividade ordem");
		}
	}
	
	public void removeAtividade(Atividade atividade) {
		try {
			manager.getTransaction().begin();
			String query = "DELETE from atividade where id_atividade = :idAtividade";
			Query q = manager.createNativeQuery(query);
			q.setParameter("idAtividade", atividade.getIdAtividade());
			q.executeUpdate();
			manager.flush();
			manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
			e.printStackTrace();
			System.out.println("Problema ao remover aitividade");
		}
	}
	
	public void removeAtividadeOrdem(AtividadeOrdem atividadeOrdem){
		try {
			manager.getTransaction().begin();
			String query = "DELETE from atividade_ordem where id_atividade_ordem = :idAtividadeOrdem";
			Query q = manager.createNativeQuery(query);
			q.setParameter("idAtividadeOrdem",
					atividadeOrdem.getIdAtividadeOrdem());
			q.executeUpdate();
			manager.flush();
			manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
			e.printStackTrace();
			System.out.println("Problema ao remover aitividade ordem");
		}
	}
}
