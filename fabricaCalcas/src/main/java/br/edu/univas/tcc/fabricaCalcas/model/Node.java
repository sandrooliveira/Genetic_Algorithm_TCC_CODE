package br.edu.univas.tcc.fabricaCalcas.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.edu.univas.tcc.fabricaCalcas.ga_code.ProcessoChromosome;
import br.edu.univas.tcc.ga_core.Chromosome;

public class Node {

	private List<Node> predecesoras = new ArrayList<Node>();
	private List<Chromosome> cromossomos;
	private Atividade atividade;
	
	public Node(Atividade atividade,Map<Integer,List<Chromosome>> atividadeCromossomos){
		this.atividade = atividade;
		cromossomos = atividadeCromossomos.get(atividade.getIdAtividade());
		Atividade atividadePredecessora = null;
		for(AtividadeOrdem predecessora : atividade.getAtividadeOrdemsForIdAtividade()){
			atividadePredecessora = predecessora.getAtividadePredecessora();
			predecesoras.add(new Node(atividadePredecessora,atividadeCromossomos));
		}
	}
	
	public void printNodes(){
		System.out.println(atividade.getHabilidade().getNomeHabilidade()+"\n");
		for(Chromosome chromosome : cromossomos){
			StringBuffer sb = new StringBuffer();
			ProcessoChromosome processoChromosome = (ProcessoChromosome)chromosome;
			sb.append(processoChromosome.getCostureiraHabilidade().getCostureira().getNomeCostureira()
					+":"+processoChromosome.getLotesToShow() + " | ");
			
			if(processoChromosome.getCostureirasPredecessoras() != null){
				for(String key : processoChromosome.predecessorasMap.keySet()){
					/*sb.append(costureira.getCostureira().getCostureira().getNomeCostureira() + " - " +costureira.getQtdeLotes()
							+" - " + costureira.getCostureira().getHabilidade().getNomeHabilidade()+" | ");*/
					
					sb.append(key+":"+processoChromosome.predecessorasMap.get(key)+" ");
				}
			}
			sb.append("\n");
			System.out.println(sb.toString());
		}
		System.out.println("---");
		for(Node node:predecesoras){
			node.printNodes();
		}
	}
	
	public long getValorTotal(){
		long valor =  0;
		long maior = 0;
		for(Chromosome chromosome : cromossomos){
			ProcessoChromosome processoChromosome = (ProcessoChromosome) chromosome;
			if(processoChromosome.getQuantidade_lotes() > 0){
				valor = getCromossomeValue(processoChromosome,processoChromosome.getQuantidade_lotes());
			}
			if(valor > maior){
				maior = valor;
			}
		}
		return maior;
	}
	
	public long getCromossomeValue(ProcessoChromosome processoChromosome,int qtdeLote){
		long valor = 0;
		valor = qtdeLote * 50 * processoChromosome.getCostureiraHabilidade().getTempoPorPeca();
		valor += getTempoRecebimentoPecas(processoChromosome,qtdeLote);
		return valor;
	}
	
	public long getTempoRecebimentoPecas(ProcessoChromosome processoChromosome, int qtdeLote){
		long valor = 0;
		long maior = 0;
		for(Node node : predecesoras){
			valor = node.getValueChromosomosPredecessores(processoChromosome, qtdeLote);
			if(valor > maior){
				maior = valor;
			}
		}
		return maior;
	}
	
	public long getValueChromosomosPredecessores(ProcessoChromosome processoChromosome,int qtdeLote){
		int qtdeEachCromossome = 0;
		long valor = 0;
		long maior = 0;
		for(Chromosome chromosome : cromossomos){
			ProcessoChromosome processoChromosomeBefore = (ProcessoChromosome) chromosome;
			qtdeEachCromossome = processoChromosomeBefore.getQtdeLotes(qtdeLote);
			if(qtdeEachCromossome > 0){
				processoChromosome.addCostureiraPredecessora(processoChromosomeBefore.getCostureiraHabilidade(), 
					qtdeEachCromossome);
				qtdeLote -= qtdeEachCromossome;
				valor = getCromossomeValue(processoChromosomeBefore, qtdeEachCromossome);
				valor += calcularTempoEntreCostureiras(processoChromosome,processoChromosomeBefore);
			}
			if(valor > maior){
				maior = valor;
			}
			if(qtdeLote == 0){
				break;
			}
		}
		return maior;
	}
	
	public long calcularTempoEntreCostureiras(ProcessoChromosome processoChromosome,
											  ProcessoChromosome proceChromosomeBefore){
		int loteCostureira = (int) (Math.random() * 100);
		//loteCostureira +=50;
		return 0;
	}
	
	public List<Node> getPredecesoras() {
		return predecesoras;
	}
	public void setPredecesoras(List<Node> predecesoras) {
		this.predecesoras = predecesoras;
	}
}
