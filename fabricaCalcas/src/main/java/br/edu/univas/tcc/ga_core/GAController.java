package br.edu.univas.tcc.ga_core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.edu.univas.tcc.fabricaCalcas.Costants.Constants;
import br.edu.univas.tcc.fabricaCalcas.ga_code.ProcessoChromosome;
import br.edu.univas.tcc.fabricaCalcas.ga_code.ProcessoIndividual;

public class GAController {

	private GAModel model;
	
	public GAController(GAModel model){
		this.model = model;
	}
	
	public Individual execute(){
		model.createInitialPopulation();
		Individual lastBest = null;
		
		for(int i = 0; ; i++){
			ArrayList<Individual> population = model.getPopulation();
			ArrayList<Individual> newGeneration = new ArrayList<Individual>();
			
			classify(population);
			if(lastBest == null || lastBest != population.get(0)){
				lastBest = population.get(0);
				System.out.println(String.format("%6d",(i+1)) + " - " + population.get(0).getValue());
				System.out.println(String.format("%6d",(i+1)) + " - " + population.get(0).getCusto());
				population.get(0).getNode().printNodes();
			}
			
			//verifica o final da execução
			if(i == model.getGenerationQuantity()){
				break;
			}
			
			//elitismo
			if(model.isElitism()){
				doElistim(newGeneration);
			}
			
			int foreignQuantity = Math.round(model.getPopulationSize() * model.getForeignIndividualRate());
			foreignQuantity = foreignQuantity % 2 == 0 ? foreignQuantity : foreignQuantity +1;
				
			for(int j = 0;j < foreignQuantity;j++ ){
				newGeneration.add(model.createIndividual());
			}
			
			while(newGeneration.size() < model.getPopulationSize()){
				//seleção
				Individual individual1 = doSelection();
				Individual individual2;
				
				do{
					individual2 = doSelection();
				}while(individual1 == individual2);
				
				//cruzamento
				IndividualPair pair = doCrossing(individual1, individual2);
				
				//mutação
				doMutation(pair.getIndividual1());
				doMutation(pair.getIndividual2());
				
				newGeneration.add(pair.getIndividual1());
				newGeneration.add(pair.getIndividual2());
			}
			
			model.setPopulation(newGeneration);
		}
		return lastBest;
	}
	
	public void doMutation(Individual individual){
		if(Math.random() < model.getMutationRate()){
			for(int i = 0; i < model.getMutationQuantity();i++){
				switch (model.getMutation()) {
					case BINARY: doMutationBinary(individual);break;
					case NUMERICAL: break;
					case PERMUTATION: doMutationPermutation(individual);break;
				}
			}
		}
	}
	
	private void doMutationBinary(Individual individual) {
		ArrayList<Chromosome> chromosomes = individual.getChromosomes();
		
		int position = (int) (Math.random() * chromosomes.size());
		
		chromosomes.get(position).doMutation();
	}

	public void doMutationPermutation(Individual individual) {
		
		Integer lastAtividade = null;
		ArrayList<ProcessoChromosome> chromossomesToMutate = new ArrayList<ProcessoChromosome>(); 
		
		for(Chromosome chromosome : individual.getChromosomes()){
			ProcessoChromosome processoChromosome = (ProcessoChromosome) chromosome;
			
			if(lastAtividade == null || !lastAtividade.equals(processoChromosome.getAtividade())){
				if(!chromossomesToMutate.isEmpty() && chromossomesToMutate.size() > 1){
					doMutationOnChromossome(chromossomesToMutate);
				}
				chromossomesToMutate.clear();
				lastAtividade = processoChromosome.getAtividade();
			}
			chromossomesToMutate.add(processoChromosome);
		}
		//For the last one
		if(!chromossomesToMutate.isEmpty() && chromossomesToMutate.size() > 1){
			doMutationOnChromossome(chromossomesToMutate);
		}
	}
	
	private void doMutationOnChromossome(ArrayList<ProcessoChromosome> chromossomesToMutate){
		int position1;
		int position2;
		int varAux;
		int varAux2;
		
		ProcessoChromosome chromosome1 = null;
		ProcessoChromosome chromosome2 = null;
		
		position1 = (int) (Math.random() * (chromossomesToMutate.size()));
		do{
			 position2 = (int) (Math.random() * (chromossomesToMutate.size()));
		}while(position1 == position2);
		
		chromosome1 = chromossomesToMutate.get(position1);
		chromosome2 = chromossomesToMutate.get(position2);
		
		varAux  = chromosome2.getQuantidade_lotes();
		varAux2 = chromosome2.getLotesToShow();
		
		chromosome2.setQuantidade_lotes(chromosome1.getQuantidade_lotes());
		chromosome2.setLotesToShow(chromosome1.getLotesToShow());

		chromosome1.setQuantidade_lotes(varAux);
		chromosome1.setLotesToShow(varAux2);
	}

	private Individual doSelection() {
		switch (model.getSelectionType()) {
			case CLASSIFICATION : return doSelectionByClassification();
			case ROULETTE: return doSelectionByRoulette();
		}
		return null;
	}
	
	private Individual doSelectionByClassification() {
		int maxValue = 0;
		
		for(int i = 0; i< model.getPopulationSize(); i++){
			maxValue += (i + 1);
		}
		
		double index = Math.random() * maxValue;
		int cursor = 0;
		
		for(int i = 0; i < model.getPopulationSize(); i++){
			cursor += model.getPopulationSize() - 1;
			
			if(index <= cursor){
				//System.out.println("[Selection] " + (i + 1) + " - " + model.getPopulation().get(i).toString());
				return model.getPopulation().get(i);
			}
		}
		
		return null;
	}
	
	private IndividualPair doCrossing(Individual individual1, Individual individual2){
			switch (model.getCrossType()) {
			case ARITMETIC: break;
			case BINARY: return doBinaryCrossing(individual1,individual2);
			case PERMUTATION: return doPermutationCrossing(individual1, individual2);
			case UNIFORM:break;
		}
		return null;
	}
	
	private Individual doSelectionByRoulette() {
		// TODO Auto-generated method stub
		return null;
	}

	private IndividualPair doBinaryCrossing(Individual individual1,Individual individual2) {
		int crossPoint = (int) (Math.random() * (individual1.getChromosomes().size() - 1)) + 1;
		
		ArrayList<Chromosome> chromosomes1 = new ArrayList<Chromosome>();
		ArrayList<Chromosome> chromosomes2 = new ArrayList<Chromosome>();
		
		for(int i = 0;i < crossPoint;i++){
			chromosomes1.add(individual1.getChromosomes().get(i).clone());
			chromosomes2.add(individual2.getChromosomes().get(i).clone());
		}
		
		for(int i = crossPoint;i < individual1.getChromosomes().size();i++){
			chromosomes1.add(individual2.getChromosomes().get(i).clone());
			chromosomes2.add(individual1.getChromosomes().get(i).clone());
		}
		
		Individual newIndividual1 = model.createIndividual(chromosomes1);
		Individual newIndividual2 = model.createIndividual(chromosomes2);
		
		return new IndividualPair(newIndividual1,newIndividual2);
	}
	
	private IndividualPair doPermutationCrossing(Individual individual1, Individual individual2){
		ArrayList<Chromosome> chromosomes1 = new ArrayList<Chromosome>();
		ArrayList<Chromosome> chromosomes2 = new ArrayList<Chromosome>();
		Integer lastAtividade = null;
		float chooseIndividual = 0;
		int i = 0;
		
		for(Chromosome chromosome : individual1.getChromosomes()){
			ProcessoChromosome processoChromosome = (ProcessoChromosome) chromosome;
			
			if(lastAtividade == null || !lastAtividade.equals(processoChromosome.getAtividade())){
				chooseIndividual = Math.round((float) Math.random() * 1);
				lastAtividade = processoChromosome.getAtividade();
			}
			
			if(chooseIndividual == 1){
				chromosomes1.add(individual1.getChromosomes().get(i).clone());
				chromosomes2.add(individual2.getChromosomes().get(i).clone());
			}else{
				chromosomes1.add(individual2.getChromosomes().get(i).clone());
				chromosomes2.add(individual1.getChromosomes().get(i).clone());
			}
			i++;
		}
		
		Individual newIndividual1 = model.createIndividual(chromosomes1);
		Individual newIndividual2 = model.createIndividual(chromosomes2);
		//printTest(individual1, individual2, newIndividual1,newIndividual2);
		
		return new IndividualPair(newIndividual1,newIndividual2);
	}
	
	public void printTest(Individual pai, Individual mae, Individual filho1, Individual filho2){
		Integer lastAtividade = null;
		StringBuffer sb = new StringBuffer();
		
		sb.append("\nCromossomo Pai\n");
		for(Chromosome chromosome : pai.getChromosomes()){
			ProcessoChromosome processoChromosome = (ProcessoChromosome) chromosome;
			if(lastAtividade == null || lastAtividade != processoChromosome.getAtividade()){
				sb.append(processoChromosome.getCostureiraHabilidade().getHabilidade().getNomeHabilidade()+" - ");
				lastAtividade = processoChromosome.getAtividade();
			}
			sb.append(processoChromosome.getQuantidade_lotes()+" ");
		}
		sb.append("\nCromossomo Mae\n");
		for(Chromosome chromosome : mae.getChromosomes()){
			ProcessoChromosome processoChromosome = (ProcessoChromosome) chromosome;
			if(lastAtividade == null || lastAtividade != processoChromosome.getAtividade()){
				sb.append(processoChromosome.getCostureiraHabilidade().getHabilidade().getNomeHabilidade()+" - ");
				lastAtividade = processoChromosome.getAtividade();
			}
			sb.append(processoChromosome.getQuantidade_lotes()+" ");
		}
		sb.append("\nCromossomo Filho1\n");
		for(Chromosome chromosome : filho1.getChromosomes()){
			ProcessoChromosome processoChromosome = (ProcessoChromosome) chromosome;
			if(lastAtividade == null || lastAtividade != processoChromosome.getAtividade()){
				sb.append(processoChromosome.getCostureiraHabilidade().getHabilidade().getNomeHabilidade()+" - ");
				lastAtividade = processoChromosome.getAtividade();
			}
			sb.append(processoChromosome.getQuantidade_lotes()+" ");
		}
		sb.append("\nCromossomo Filho2\n");
		for(Chromosome chromosome : filho2.getChromosomes()){
			ProcessoChromosome processoChromosome = (ProcessoChromosome) chromosome;
			if(lastAtividade == null || lastAtividade != processoChromosome.getAtividade()){
				sb.append(processoChromosome.getCostureiraHabilidade().getHabilidade().getNomeHabilidade()+" - ");
				lastAtividade = processoChromosome.getAtividade();
			}
			sb.append(processoChromosome.getQuantidade_lotes()+" ");
		}
		System.out.println(sb.toString());
	}
	
	/*private IndividualPair doPermutationCrossing(Individual individual1, Individual individual2){
		int crossPoint = (int) (Math.random() * (individual1.getChromosomes().size() - 1)) + 1;
	
		ArrayList<Chromosome> chromosomes1 = new ArrayList<Chromosome>();
		ArrayList<Chromosome> chromosomes2 = new ArrayList<Chromosome>();
		
		for(int i = 0;i < crossPoint;i++){
			chromosomes1.add(individual1.getChromosomes().get(i));
			chromosomes2.add(individual2.getChromosomes().get(i));
		}
		
		for(int i = crossPoint; i < individual1.getChromosomes().size();i++){
			chromosomes1.add(individual2.getChromosomes().get(i));
			chromosomes2.add(individual1.getChromosomes().get(i));
		}
		
		for(int i = 0;i < individual1.getChromosomes().size();i++){
			if(!chromosomes1.contains(individual2.getChromosomes().get(i))){
				chromosomes1.add(individual2.getChromosomes().get(i));
			}
			
			if(!chromosomes2.contains(individual1.getChromosomes().get(i))){
				chromosomes2.add(individual1.getChromosomes().get(i));
			}
		}
		Individual newIndividual1 = model.createIndividual(chromosomes1);
		Individual newIndividual2 = model.createIndividual(chromosomes2);
		
		return new IndividualPair(newIndividual1,newIndividual2);
	}*/


	private void doElistim(ArrayList<Individual> newGeneration ) {
		if(model.getPopulationSize() % 2 == 0){
			newGeneration.add(model.getPopulation().get(0));
			newGeneration.add(model.getPopulation().get(1));
		}else{
			newGeneration.add(model.getPopulation().get(0));
		}
	}

	/*public void printResult(){
		for(Individual individual : model.getPopulation()){
			System.out.println(individual.toString());
		}
	}*/
	
	public void classify(List<Individual> population){
		List<Individual> populationAux = new ArrayList<Individual>();
		List<Individual> populationGood = new ArrayList<Individual>();
		
		for(Individual individual : population){
			individual.calculateValue();
		}
		Collections.sort(population);
		
		
		for(Individual individual : population){
			
			ProcessoIndividual pi = (ProcessoIndividual) individual;
			if(individual.getValue() <= pi.getPrazo().longValue()){
				individual.setTipoDeClassificacao(Constants.CLASSIFICACAO_POR_CUSTO);
				populationGood.add(individual);
			}
		}
		Collections.sort(populationGood);
		
		for(Individual individual : populationGood){
			individual.setTipoDeClassificacao(Constants.CLASSIFICACAO_POR_TEMPO);
		}
		
		//Remove todos os objetos com tempo < prazo da população
		population.removeAll(populationGood);
		
		//Adiciona-os na poplação auxiliar
		populationAux.addAll(populationGood);
		
		//adiciona os demais indivíduos da população
		populationAux.addAll(population);
		
		//Redefine a população
		population.clear();
		population.addAll(populationAux);
		
		
		/*for(Chromosome chromosome : population.get(0).getChromosomes()){
			ProcessoChromosome processoChromosome = (ProcessoChromosome) chromosome;
			System.out.println(population.get(0));
			System.out.println(processoChromosome);
			System.out.println(processoChromosome.getAtividade());
			System.out.println(processoChromosome.getQuantidade_lotes());
			System.out.println(processoChromosome.getCostureiraHabilidade().getCostureira().getNomeCostureira());
		}*/
	}
}
