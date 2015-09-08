package br.edu.univas.tcc.fabricaCalcas.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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
		
		/*for(Costureira costureira : costureiras){
			System.out.println(costureira.getNomeCostureira());
		}*/
		
		return costureiras;
	}
	
}
