package br.edu.univas.tcc.ga_core;

public abstract class Chromosome{

	public abstract boolean equals(Object obj);
	
	public abstract void doMutation();
	
	public abstract Chromosome clone();
	
}
