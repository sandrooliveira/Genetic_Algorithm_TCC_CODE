package br.edu.univas.tcc.fabricaCalcas.ga_code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.univas.tcc.fabricaCalcas.model.Atividade;
import br.edu.univas.tcc.fabricaCalcas.model.CostureiraHabilidade;
import br.edu.univas.tcc.fabricaCalcas.model.Node;
import br.edu.univas.tcc.ga_core.Chromosome;
import br.edu.univas.tcc.ga_core.Individual;

public class ProcessoIndividual extends Individual {

	private Atividade atividadeFinal;
	private Node node;
	private int numeroLote;
	private int pecasPorLote;

	public ProcessoIndividual(Atividade atividadeFinal,Map<Integer, List<CostureiraHabilidade>> atividadesCostureiras,
			int numeroLote, int pecasPorLote){
		
		chromosomes = new ArrayList<Chromosome>();
		Map<Integer, ProcessoChromosome> chromossomosMap = new HashMap<Integer, ProcessoChromosome>();
		
		this.atividadeFinal = atividadeFinal;
		this.numeroLote = numeroLote;
		this.pecasPorLote = pecasPorLote;
		
		boolean distribuiuPorTodasCostureiras = false;

		int qtdeLote = 0;
		int cont = 0;

		for (Integer key : atividadesCostureiras.keySet()) {
			qtdeLote = numeroLote;
			cont = 0;
			int loteCostureira = 0;
			
			distribuiuPorTodasCostureiras = false;

			while (true){
				if(qtdeLote == 1){
					loteCostureira  = Math.round((float) Math.random() * 1);
				}else{
					loteCostureira = (int) (Math.random() * qtdeLote);
				}
				
				
				CostureiraHabilidade costureiraHabilidade = atividadesCostureiras.get(key).get(cont);
				
				ProcessoChromosome intermediario = chromossomosMap.get(costureiraHabilidade.getIdCostureiraHabilidade());
				if(intermediario == null){
					ProcessoChromosome pc = new ProcessoChromosome(key, costureiraHabilidade,loteCostureira); 
					chromossomosMap.put(costureiraHabilidade.getIdCostureiraHabilidade(), pc);
					chromosomes.add(pc);
				}else{
					int oldValue = intermediario.getQuantidade_lotes();
					int newValue = oldValue + loteCostureira;
					intermediario.setQuantidade_lotes(newValue);
					intermediario.setLotesToShow(newValue);
				}
				
				if (cont == atividadesCostureiras.get(key).size() - 1) {
					cont = -1;
					distribuiuPorTodasCostureiras = true;
				} 
				
				qtdeLote -= loteCostureira;
				
				if(qtdeLote == 0 && distribuiuPorTodasCostureiras){
					break;
				}
				cont++;
			}
		}
	}

	public ProcessoIndividual(Atividade atividadeInicial,ArrayList<Chromosome> chromosomes,
			int numeroLote, int pecasPorLote) {
		super(chromosomes);
		this.atividadeFinal = atividadeInicial;
		this.numeroLote = numeroLote;
		this.pecasPorLote = pecasPorLote;
	}

	public void calculateValue() {
		Map<Integer, List<Chromosome>> atividadeCromossomos = new HashMap<Integer, List<Chromosome>>();
		Integer lastAtividade = null;
		List<Chromosome> cromossomos = null;

		for (Chromosome chromosome : chromosomes) {
			ProcessoChromosome processoChromossome = (ProcessoChromosome) chromosome;
			
			if (lastAtividade == null || lastAtividade != processoChromossome.getAtividade()) {
				cromossomos = new ArrayList<Chromosome>();
				
				atividadeCromossomos.put(processoChromossome.getAtividade(),cromossomos);
				lastAtividade = processoChromossome.getAtividade();
			}
			cromossomos.add(processoChromossome);
		}
		node = new Node(atividadeFinal, atividadeCromossomos,this.pecasPorLote);

		/*
		 * Só deve-se calcular o valor do indivíduo se ele nao foi calculado
		 * ainda porque o valor dos lotes no objeto cromossomo foi decrementado
		 */
		if (getValue() == 0) {
			setValue(node.getValorTotal());
			setRootNode(node);
		}
	}

	@Override
	public int compareTo(Individual o) {
		if (getValue() < o.getValue()) {
			return -1;
		} else if (getValue() > o.getValue()) {
			return 1;
		} else {
			return 0;
		}
	}

	public Atividade getAtividadeFinal() {
		return atividadeFinal;
	}

	public void setAtividadeFinal(Atividade atividadeFinal) {
		this.atividadeFinal = atividadeFinal;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public int getNumeroLote() {
		return numeroLote;
	}

	public void setNumeroLote(int numeroLote) {
		this.numeroLote = numeroLote;
	}

	public int getPecasPorLote() {
		return pecasPorLote;
	}

	public void setPecasPorLote(int pecasPorLote) {
		this.pecasPorLote = pecasPorLote;
	}
}