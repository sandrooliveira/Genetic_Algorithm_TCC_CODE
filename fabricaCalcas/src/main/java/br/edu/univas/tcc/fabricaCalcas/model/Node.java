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
	private int pecasPorLote;
	
	public Node(Atividade atividade,Map<Integer,List<Chromosome>> atividadeCromossomos, int pecasPorLote){
		this.atividade = atividade;
		this.pecasPorLote = pecasPorLote;
		cromossomos = atividadeCromossomos.get(atividade.getIdAtividade());
		Atividade atividadePredecessora = null;
		
		for(AtividadeOrdem predecessora : atividade.getAtividadeOrdemsForIdAtividade()){
			atividadePredecessora = predecessora.getAtividadePredecessora();
			predecesoras.add(new Node(atividadePredecessora,atividadeCromossomos,pecasPorLote));
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
		valor = qtdeLote * pecasPorLote * processoChromosome.getCostureiraHabilidade().getTempoPorPeca();
		valor += getTempoRecebimentoPecas(processoChromosome,qtdeLote);
		return valor;
	}
	
	public long getTempoRecebimentoPecas(ProcessoChromosome processoChromosome, int qtdeLote){
		long valor = 0;
		long maior = 0;
		for(Node node : predecesoras){
			/*Se a atividade anterior for Carimbo, então só é calculado a distância entre
			 * a costureira e o Marcelo (Distribuidor de tarefas)*/
			if(node.getAtividade().getHabilidade().getNomeHabilidade().equals("Carimbo")){
				ProcessoChromosome processoChromosomeCarimbo = (ProcessoChromosome) node.getCromossomos().get(0);
				valor = calcularTempoEntreCostureiras(processoChromosome, processoChromosomeCarimbo);
			}else{
				valor = node.getValueChromosomosPredecessores(processoChromosome, qtdeLote);
			}
			
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
											  ProcessoChromosome processoChromosomeBefore){
		int posicaoCostureiraX = processoChromosome.getCostureiraHabilidade().getCostureira().getPositionX();
		int posicaoCostureiraY = processoChromosome.getCostureiraHabilidade().getCostureira().getPositionY();
		
		int posicaoCostureiraBeforeX = processoChromosomeBefore.getCostureiraHabilidade().getCostureira().getPositionX();
		int posicaoCostureiraBeforeY = processoChromosomeBefore.getCostureiraHabilidade().getCostureira().getPositionY();
		
		long distance = (long) Math.sqrt(
				Math.pow(posicaoCostureiraX - posicaoCostureiraBeforeX, 2)+
				Math.pow(posicaoCostureiraY - posicaoCostureiraBeforeY, 2));
		
		return distance * 100;
	}
	
	@Override
	public String toString() {
		String sb = null;
		sb = "<"+atividade+"> (" ;
		for(Chromosome chromosome : cromossomos){
			ProcessoChromosome pc = (ProcessoChromosome) chromosome;
			sb += pc.getCostureiraHabilidade().getCostureira().getNomeCostureira()+":";
			sb += pc.getLotesToShow()+" ";
		}
		sb += ")";
		return sb;
	}
	
	/*Getters and setters*/
	public List<Node> getPredecesoras() {
		return predecesoras;
	}
	
	public void setPredecesoras(List<Node> predecesoras) {
		this.predecesoras = predecesoras;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public List<Chromosome> getCromossomos() {
		return cromossomos;
	}

	public void setCromossomos(List<Chromosome> cromossomos) {
		this.cromossomos = cromossomos;
	}

	public int getPecasPorLote() {
		return pecasPorLote;
	}

	public void setPecasPorLote(int pecasPorLote) {
		this.pecasPorLote = pecasPorLote;
	}
	
}
