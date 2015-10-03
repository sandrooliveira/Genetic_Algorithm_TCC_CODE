package br.edu.univas.tcc.fabricaCalcas.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.edu.univas.tcc.fabricaCalcas.model.CostureiraHabilidade;
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
	
	public Habilidade selectHabilidadeById(int idHabilidade){
		String query = "Select h from Habilidade h where h.idHabilidade = :idHabilidade";
		TypedQuery<Habilidade> q = manager.createQuery(query,Habilidade.class);
		q.setParameter("idHabilidade", idHabilidade);
		return q.getSingleResult();
	}
	
	public void addHabilidade(Habilidade habilidade) {
		try {
			manager.getTransaction().begin();
			manager.persist(habilidade);
			manager.flush();
			manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
			e.printStackTrace();
			System.out.println("Problema ao add habilidade");
		}
	}
	
	public void addCostureiraNaHabilidade(CostureiraHabilidade cosHabilidade){
		try {
			manager.getTransaction().begin();
			manager.persist(cosHabilidade);
			manager.flush();
			manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
			e.printStackTrace();
			System.out.println("Problema ao add costureira na habilidade");
		}
	}
	
	public void removerCostureiraDaHabilidade(CostureiraHabilidade cosHabilidade) {
		try {
			manager.getTransaction().begin();
			String query = "DELETE from costureira_habilidade where id_costureira_habilidade = :id_costureira_habilidade";
			Query q = manager.createNativeQuery(query);
			q.setParameter("id_costureira_habilidade", cosHabilidade.getIdCostureiraHabilidade());
			q.executeUpdate();
			manager.flush();
			manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
			e.printStackTrace();
			System.out.println("Problema ao remover costureira da habilidade");
		}
	}
}
