package br.edu.univas.tcc.fabricaCalcas.ga_code;

import br.edu.univas.tcc.fabricaCalcas.model.CostureiraHabilidade;

public class CostureiraPredecessora {

	private CostureiraHabilidade costureira;
	private int qtdeLotes;
	private long tempoDeProducao;
	private long tempoDeTransporte;

	public CostureiraPredecessora(CostureiraHabilidade costureira, int qtdeLote, long tempoDeTransporte,long tempoProducao){
		this.costureira = costureira;
		this.qtdeLotes  = qtdeLote;
		this.tempoDeTransporte = tempoDeTransporte;
		this.tempoDeProducao = tempoProducao;
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
	

	public long getTempoDeProducao() {
		return tempoDeProducao;
	}

	public void setTempoDeProducao(long tempoDeProducao) {
		this.tempoDeProducao = tempoDeProducao;
	}

	public long getTempoDeTransporte() {
		return tempoDeTransporte;
	}

	public void setTempoDeTransporte(long tempoDeTransporte) {
		this.tempoDeTransporte = tempoDeTransporte;
	}
}
