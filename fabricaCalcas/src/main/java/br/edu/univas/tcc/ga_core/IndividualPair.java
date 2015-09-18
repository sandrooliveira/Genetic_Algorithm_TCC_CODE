package br.edu.univas.tcc.ga_core;

public class IndividualPair {

	private Individual individual1;
	private Individual individual2;
	
	public IndividualPair() {
		// TODO Auto-generated constructor stub
	}
	
	public IndividualPair(Individual individual1, Individual individual2){
		super();
		this.individual1 = individual1;
		this.individual2 = individual2;
	}
	
	public Individual getIndividual1() {
		return individual1;
	}
	public void setIndividual1(Individual individual1) {
		this.individual1 = individual1;
	}
	public Individual getIndividual2() {
		return individual2;
	}
	public void setIndividual2(Individual individual2) {
		this.individual2 = individual2;
	}
}
