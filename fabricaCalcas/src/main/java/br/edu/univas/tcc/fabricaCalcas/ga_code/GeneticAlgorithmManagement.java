package br.edu.univas.tcc.fabricaCalcas.ga_code;

import javax.persistence.EntityManager;

import br.edu.univas.tcc.fabricaCalcas.dao.ConFactory;
import br.edu.univas.tcc.ga_core.GAController;
import br.edu.univas.tcc.ga_core.GAModel;
import br.edu.univas.tcc.ga_core.GAModel.CrossType;

public class GeneticAlgorithmManagement {

	public ProcessoIndividual iniciarDistribuicao(int numeroLote, int pecasPorLote, int idProcesso) {
		
		EntityManager manager = ConFactory.getConn(); 
		
		ProcessoModel model = new ProcessoModel(manager,idProcesso);
		model.setNumeroLote(numeroLote);
		model.setPecasPorLote(pecasPorLote);
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
		return (ProcessoIndividual) controller.execute();
	}

}
