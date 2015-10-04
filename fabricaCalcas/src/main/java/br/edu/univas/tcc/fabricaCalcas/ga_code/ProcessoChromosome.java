package br.edu.univas.tcc.fabricaCalcas.ga_code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.edu.univas.tcc.fabricaCalcas.model.CostureiraHabilidade;
import br.edu.univas.tcc.ga_core.Chromosome;

public class ProcessoChromosome extends Chromosome{

	private Integer atividade;
	private CostureiraHabilidade costureiraHabilidade;
	private ArrayList<CostureiraPredecessora> costureirasPredecessoras;
	public Map<String,Integer> predecessorasMap;
	private int quantidade_lotes;
	private int lotesToShow;

	public ProcessoChromosome(Integer atividade, CostureiraHabilidade costureiraHabilidade) {
		this.atividade = atividade;
		this.costureiraHabilidade = costureiraHabilidade;
	}
	
	public ProcessoChromosome(Integer atividade, CostureiraHabilidade costureiraHabilidade,
			int qtde_lote) {
		this.atividade = atividade;
		this.costureiraHabilidade = costureiraHabilidade;
		this.quantidade_lotes = qtde_lote;
		this.lotesToShow = qtde_lote;
	}
	
	//Este método serve para ver quantos lotes será pego de um cromossomo
	//decrementar este valor e devolver quantos foi retirado para poder 
	//ser fazer o cálculo.
	public int getQtdeLotes(int qtdeLoteNeeded){
		int oldValue = 0;
		if((this.quantidade_lotes - qtdeLoteNeeded) < 0){
			oldValue = this.quantidade_lotes;
			this.quantidade_lotes = 0;
			return oldValue;
		}else{
			this.quantidade_lotes -= qtdeLoteNeeded;
			return qtdeLoteNeeded;
		}
	}
	
	public void addCostureiraPredecessora(CostureiraHabilidade costureiraHabilidade,int qtdeLote, long tempoDeTransporte,
			long tempoProducao){
		if(costureirasPredecessoras==null){
			costureirasPredecessoras = new ArrayList<CostureiraPredecessora>();
		}
		costureirasPredecessoras.add(new CostureiraPredecessora(costureiraHabilidade,qtdeLote,tempoDeTransporte,tempoProducao));
		if(predecessorasMap == null){
			predecessorasMap = new HashMap<String, Integer>();
		}
		String key = costureiraHabilidade.getHabilidade().getNomeHabilidade();
		key+="_"+costureiraHabilidade.getCostureira().getNomeCostureira();
		
		Integer oldValue = 0;
		oldValue = predecessorasMap.get(key);
		
		if(oldValue != null){
		  oldValue+=qtdeLote;
		  predecessorasMap.put(key, oldValue);
		}else{
		  predecessorasMap.put(key, qtdeLote);
		}
	}
	
	public Integer getAtividade() {
		return atividade;
	}

	public void setAtividade(Integer atividade) {
		this.atividade = atividade;
	}
	
	public CostureiraHabilidade getCostureiraHabilidade() {
		return costureiraHabilidade;
	}

	public void setCostureiraHabilidade(CostureiraHabilidade costureiraHabilidade) {
		this.costureiraHabilidade = costureiraHabilidade;
	}
	
	public ArrayList<CostureiraPredecessora> getCostureirasPredecessoras() {
		return costureirasPredecessoras;
	}

	public void setCostureirasPredecessoras(
			ArrayList<CostureiraPredecessora> costureirasPredecessoras) {
		this.costureirasPredecessoras = costureirasPredecessoras;
	}

	public int getQuantidade_lotes() {
		return quantidade_lotes;
	}

	public void setQuantidade_lotes(int quantidade_lotes) {
		this.quantidade_lotes = quantidade_lotes;
	}

	public void decrementQtdeLote(int qtde){
		this.quantidade_lotes-=qtde;
	}
	
	public int getLotesToShow() {
		return lotesToShow;
	}

	public void setLotesToShow(int lotesToShow) {
		this.lotesToShow = lotesToShow;
	}

	@Override
	public void doMutation() {
		// TODO Auto-generated method stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((atividade == null) ? 0 : atividade.hashCode());
		result = prime
				* result
				+ ((costureiraHabilidade == null) ? 0 : costureiraHabilidade
						.hashCode());
		result = prime * result + quantidade_lotes;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessoChromosome other = (ProcessoChromosome) obj;
		if (atividade == null) {
			if (other.atividade != null)
				return false;
		} else if (!atividade.equals(other.atividade))
			return false;
		if (costureiraHabilidade == null) {
			if (other.costureiraHabilidade != null)
				return false;
		} else if (!costureiraHabilidade.equals(other.costureiraHabilidade))
			return false;
		if (quantidade_lotes != other.quantidade_lotes)
			return false;
		return true;
	}

	@Override
	public Chromosome clone() {
		return new ProcessoChromosome(atividade,costureiraHabilidade,lotesToShow);
	}
}
