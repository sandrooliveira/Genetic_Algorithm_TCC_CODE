package br.edu.univas.tcc.fabricaCalcas.ga_code;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.edu.univas.tcc.ga_core.GAController;
import br.edu.univas.tcc.ga_core.GAModel;
import br.edu.univas.tcc.ga_core.GAModel.CrossType;

public class ProcessoApp {

	public static void main(String[] args) {
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("db_pu");
		EntityManager manager = factory.createEntityManager(); 
		
		ProcessoModel model = new ProcessoModel(manager,1);
		model.setGenerationQuantity(10000);
		model.setPopulationSize(80);
		model.setElitism(true);
		model.setSelectionType(GAModel.SelectionType.CLASSIFICATION);
		model.setCrossType(CrossType.PERMUTATION);
		model.setForeignIndividualRate(0.3f);
		model.setMutation(GAModel.MutationType.PERMUTATION);
		model.setMutationRate(0.05f);
		model.setMutationQuantity(1);
		
		GAController controller = new GAController(model);
		controller.execute();
	}

}
