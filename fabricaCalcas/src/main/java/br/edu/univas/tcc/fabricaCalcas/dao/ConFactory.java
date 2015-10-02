package br.edu.univas.tcc.fabricaCalcas.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConFactory {

	private static EntityManager manager;
	
	public static EntityManager getConn(){
		if(manager == null){
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("db_pu");
			manager = factory.createEntityManager();
		}
		return manager;
	}
}
