package br.edu.univas.tcc.fabricaCalcas.ga_code;

import br.edu.univas.tcc.fabricaCalcas.model.CostureiraHabilidade;

public class CostureiraPredecessora {

	private CostureiraHabilidade costureira;
	private int qtdeLotes;

	public CostureiraPredecessora(CostureiraHabilidade costureira, int qtdeLote){
		this.costureira = costureira;
		this.qtdeLotes  = qtdeLote;
	}
	
	public CostureiraHabilidade getCostureira() {
		return costureira;
	}
	public void setCostureira(CostureiraHabilidade costureira) {
		this.costureira = costureira;
	}
	public int getQtdeLotes() {
		return qtdeLotes;
	}
	public void setQtdeLotes(int qtdeLotes) {
		this.qtdeLotes = qtdeLotes;
	}
}
