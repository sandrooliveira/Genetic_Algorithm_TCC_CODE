package br.edu.univas.tcc.ga_core;

import java.util.ArrayList;
import java.util.Collections;

import br.edu.univas.tcc.fabricaCalcas.ga_code.ProcessoChromosome;

public class GAController {

	private GAModel model;
	
	public GAController(GAModel model){
		this.model = model;
	}
	
	public void execute(){
		model.createInitialPopulation();
		Individual lastBest = null;
		
		for(int i = 0; ; i++){
			ArrayList<Individual> population = model.getPopulation();
			ArrayList<Individual> newGeneration = new ArrayList<Individual>();
			
			classify(population);
			if(lastBest == null || lastBest != population.get(0)){
				lastBest = population.get(0);
				System.out.println(String.format("%6d",(i+1)) + " - " + population.get(0).getValue());
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
				//doMutation(pair.getIndividual1());
				//doMutation(pair.getIndividual2());
				
				newGeneration.add(pair.getIndividual1());
				newGeneration.add(pair.getIndividual2());
			}
			
			model.setPopulation(newGeneration);
		}
		//printResult();
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
		
		chromosomes.get(position).doMutation();;
	}

	public void doMutationPermutation(Individual individual) {
		ArrayList<Chromosome> chromosomes = individual.getChromosomes();
		
		int firstPosition = (int) (Math.random() * chromosomes.size());
		int secondPosition = 0;
		
		do{
			secondPosition = (int)(Math.random() * chromosomes.size());
		}while(firstPosition != secondPosition);
		
		Chromosome firstChromosome  = chromosomes.get(firstPosition);
		Chromosome secondChromosome =  chromosomes.get(secondPosition);
		
		chromosomes.set(firstPosition, secondChromosome);
		chromosomes.set(secondPosition, firstChromosome);
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
	
	public void classify(ArrayList<Individual> population){
		for(Individual individual : population){
			individual.calculateValue();
		}
		Collections.sort(population);
		
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
