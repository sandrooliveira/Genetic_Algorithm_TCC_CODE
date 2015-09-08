package br.edu.univas.tcc.ga_core;

import java.util.ArrayList;

import br.edu.univas.tcc.fabricaCalcas.model.Node;

public abstract class Individual implements Comparable<Individual>{

	protected ArrayList<Chromosome> chromosomes;
	private float value = 0;
	
	
	public Individual(ArrayList<Chromosome> chromosomes){
		this.chromosomes = chromosomes;
	}
	
	public Individual(){
		
	}
	
	/*
	 * Método para calcular o valor do indivíduo
	 * */
	public abstract void calculateValue();

	/*
	 * Getters and Setters
	 * */
	public ArrayList<Chromosome> getChromosomes() {
		return chromosomes;
	}

	public void setChromosomes(ArrayList<Chromosome> chromosomes) {
		this.chromosomes = chromosomes;
	}
	
	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
	
	public abstract Node getNode();

	public abstract int compareTo(Individual o);
}
