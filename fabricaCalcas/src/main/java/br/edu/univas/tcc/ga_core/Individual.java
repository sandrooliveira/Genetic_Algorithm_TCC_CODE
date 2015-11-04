package br.edu.univas.tcc.ga_core;

import java.util.ArrayList;

import br.edu.univas.tcc.fabricaCalcas.model.Node;

public abstract class Individual implements Comparable<Individual>{

	protected ArrayList<Chromosome> chromosomes;
	private float value = 0;
	private float custo = 0;
	private Node rootNode;
	
	
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
	
	public float getCusto() {
		return custo;
	}

	public void setCusto(float custo) {
		this.custo = custo;
	}
	
	public float sumOfValues(){
		return this.value + this.custo;
	}

	public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	public abstract Node getNode();

	public abstract int compareTo(Individual o);
	
	public abstract void setTipoDeClassificacao(int tipoDeClassificacao);
}
