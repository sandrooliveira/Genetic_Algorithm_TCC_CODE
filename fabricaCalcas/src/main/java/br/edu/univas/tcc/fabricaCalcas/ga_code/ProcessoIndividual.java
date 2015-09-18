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

	public ProcessoIndividual(Atividade atividadeInicial,
			Map<Integer, List<CostureiraHabilidade>> atividadesCostureiras) {
		chromosomes = new ArrayList<Chromosome>();
		this.atividadeFinal = atividadeInicial;

		int qtdeLote = 10;
		int cont = 0;

		for (Integer key : atividadesCostureiras.keySet()) {
			qtdeLote = 10;
			cont = 0;

			for (CostureiraHabilidade costureiraHabilidade : atividadesCostureiras
					.get(key)) {
				int loteCostureira = (int) (Math.random() * qtdeLote);
				if (cont != atividadesCostureiras.get(key).size() - 1) {
					chromosomes.add(new ProcessoChromosome(key,
							costureiraHabilidade, loteCostureira));
				} else {
					chromosomes.add(new ProcessoChromosome(key,
							costureiraHabilidade, qtdeLote));
				}
				qtdeLote -= loteCostureira;
				cont++;
			}
		}
	}

	public ProcessoIndividual(Atividade atividadeInicial,
			ArrayList<Chromosome> chromosomes) {
		super(chromosomes);
		this.atividadeFinal = atividadeInicial;
	}

	public void calculateValue() {
		Map<Integer, List<Chromosome>> atividadeCromossomos = new HashMap<Integer, List<Chromosome>>();
		Integer lastAtividade = null;
		List<Chromosome> cromossomos = null;

		for (Chromosome chromosome : chromosomes) {
			ProcessoChromosome processoChromossome = (ProcessoChromosome) chromosome;

			//System.out.println("Costureira: " + processoChromossome.getCostureiraHabilidade().getCostureira().getNomeCostureira());
			//System.out.println("Lotes: " + processoChromossome.getQuantidade_lotes());
			
			if (lastAtividade == null
					|| lastAtividade != processoChromossome.getAtividade()) {
				cromossomos = new ArrayList<Chromosome>();
				atividadeCromossomos.put(processoChromossome.getAtividade(),
						cromossomos);
				lastAtividade = processoChromossome.getAtividade();
			}
			cromossomos.add(processoChromossome);
		}
		node = new Node(atividadeFinal, atividadeCromossomos);

		/*
		 * Só deve-se calcular o valor do indivíduo se ele nao foi calculado
		 * ainda porque o valor dos lotes no objeto cromossomo foi decrementado
		 */
		if (getValue() == 0) {
			setValue(node.getValorTotal());
		}
		//System.out.println(getValue());
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
}