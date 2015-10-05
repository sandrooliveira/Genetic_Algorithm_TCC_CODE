package br.edu.univas.tcc.fabricaCalcas.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.edu.univas.tcc.fabricaCalcas.model.Costureira;
import br.edu.univas.tcc.fabricaCalcas.model.CostureiraHabilidade;

public class CostureirasDAO {
	
	EntityManager entityManager;
	
	public CostureirasDAO(EntityManager entityManager){
		this.entityManager = entityManager;
	}

	public List<CostureiraHabilidade> getCostureirasByHabilidade(int habilidade){
		List<CostureiraHabilidade> costureiras = null;
		
		TypedQuery<CostureiraHabilidade> query = entityManager.createQuery("select c from CostureiraHabilidade c " +
				"where c.habilidade.idHabilidade = :idHabilidade",CostureiraHabilidade.class);
		
		query.setParameter("idHabilidade", habilidade);
		costureiras = query.getResultList();
		
		return costureiras;
	}
	
	public List<Costureira> listAllCostureiras(){
		String query  = "Select c FROM Costureira c";
		TypedQuery<Costureira> q = entityManager.createQuery(query,Costureira.class);
		return q.getResultList();
	}
	
	public void addCostureira(Costureira costureira){
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(costureira);
			entityManager.flush();
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			System.out.println("Problema ao add costureira");
		}
	}
	
	public void updateCostureira(Costureira costureira){
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(costureira);
			entityManager.flush();
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			System.out.println("Problema ao atualizar costureira");
		}
	}
	
	public void removeCostureira(Costureira costureira) throws Exception {
		try {
			entityManager.getTransaction().begin();
			String query = "DELETE from costureira where id_costureira = :idCostureira";
			Query q = entityManager.createNativeQuery(query);
			q.setParameter("idCostureira", costureira.getIdCostureira());
			q.executeUpdate();
			entityManager.flush();
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			System.out.println("Problema ao remover costureira");
			throw e;
		}
	}
	
}
