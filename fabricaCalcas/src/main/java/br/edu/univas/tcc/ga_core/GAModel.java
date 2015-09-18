package br.edu.univas.tcc.ga_core;

import java.util.ArrayList;

public abstract class GAModel {
	
	public enum SelectionType { ROULETTE, CLASSIFICATION}; //another type of variable
	public enum CrossType { BINARY, PERMUTATION, UNIFORM, ARITMETIC}; //another type of variable
	public static enum MutationType {PERMUTATION, BINARY, NUMERICAL};
	
	protected ArrayList<Individual> population;
	
	/* 
	 * Parameters
	 */
	private int populationSize = 25;
	private int generationQuantity = 100;
	private boolean elitism = true;
	private float foreignIndividualRate = 0;
	private float mutationRate = 1;
	private float mutationQuantity = 1;
	private SelectionType selectionType = SelectionType.CLASSIFICATION;
	private CrossType crossType = CrossType.BINARY;
	private MutationType mutation = MutationType.PERMUTATION;
	
	public abstract void createInitialPopulation();
	
	//used by GAController to create a Individual with a ArrayList of Chromosomes
	public abstract Individual createIndividual(ArrayList<Chromosome> chromosomes);
	
	// used by Foreign Individual in GAController
	public abstract Individual createIndividual();
	
	public GAModel() {
		population = new ArrayList<Individual>();
	}
	
	/*
	 * GET/SET
	 */
	public ArrayList<Individual> getPopulation() {
		return population;
	}
	
	public void setPopulation(ArrayList<Individual> population) {
		this.population = population;
	}
	
	public int getPopulationSize() {
		return populationSize;
	}
	
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
	
	public int getGenerationQuantity() {
		return generationQuantity;
	}
	
	public void setGenerationQuantity(int generationQuantity) {
		this.generationQuantity = generationQuantity;
	}

	public boolean isElitism() {
		return elitism;
	}

	public void setElitism(boolean elitism) {
		this.elitism = elitism;
	}

	public SelectionType getSelectionType() {
		return selectionType;
	}

	public void setSelectionType(SelectionType selectionType) {
		this.selectionType = selectionType;
	}
	
	public CrossType getCrossType() {
		return crossType;
	}
	
	public void setCrossType(CrossType crossType) {
		this.crossType = crossType;
	}
	
	public float getForeignIndividualRate() {
		return foreignIndividualRate;
	}
	
	public void setForeignIndividualRate(float foreignIndividualRate) {
		this.foreignIndividualRate = foreignIndividualRate;
	}

	public float getMutationRate() {
		return mutationRate;
	}

	public void setMutationRate(float mutationRate) {
		this.mutationRate = mutationRate;
	}

	public MutationType getMutation() {
		return mutation;
	}

	public void setMutation(MutationType mutation) {
		this.mutation = mutation;
	}

	public float getMutationQuantity() {
		return mutationQuantity;
	}

	public void setMutationQuantity(float mutationQuantity) {
		this.mutationQuantity = mutationQuantity;
	}
	
	
}
