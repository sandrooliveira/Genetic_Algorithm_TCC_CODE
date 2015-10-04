package br.edu.univas.tcc.fabricaCalcas.model;

import br.edu.univas.tcc.fabricaCalcas.ga_code.CostureiraPredecessora;
import br.edu.univas.tcc.fabricaCalcas.ga_code.ProcessoChromosome;

public class TabDetailBean {

	private String atividadeCostureira;
	private String parte;
	private String qtdeLote;
	private String pecasPorLote;
	private String totalPecas;
	private String tempoPorPeca;
	private String tempoProducao;
	private String tempoTransporte;
	private String tempoDeRecebimento;
	private String tempoTotal;
	
	public TabDetailBean(String atividadeCostureira, String parte, int qtdeLote, int pecasPorLote,
			int tempoPorPeca,long tempoTransporte,long tempoProducao, ProcessoChromosome pc){
		
		this.atividadeCostureira = atividadeCostureira;
		this.parte = parte;
		this.qtdeLote = String.valueOf(qtdeLote);
		this.pecasPorLote = String.valueOf(pecasPorLote);
		this.tempoPorPeca = String.valueOf(tempoPorPeca);
		this.tempoProducao = String.valueOf(tempoProducao);
		this.tempoTransporte = "-";
		calcularTotalDePecasETempoTotal(qtdeLote,pecasPorLote,tempoPorPeca,tempoTransporte,pc);
	}
	
	
	public TabDetailBean(String atividadeCostureira, String parte, int qtdeLote, 
			String pecasPorLote,String tempoPorPeca, long tempoTransporte,long tempoProducao, ProcessoChromosome pc){
		
		this.atividadeCostureira = atividadeCostureira;
		this.parte = parte;
		this.qtdeLote = String.valueOf(qtdeLote);
		this.pecasPorLote = pecasPorLote;
		this.tempoPorPeca = tempoPorPeca;
		this.tempoTransporte = String.valueOf(tempoTransporte);
		this.tempoDeRecebimento = "-";
		this.tempoProducao = String.valueOf(tempoProducao);
		this.totalPecas = "-";
		
		long tempoTotal = Long.parseLong(this.tempoProducao) + tempoTransporte;
		this.tempoTotal = String.valueOf(tempoTotal);
	}
	
	public TabDetailBean(String atividadeCostureira, String parte, String qtdeLote, String pecasPorLote,
			String tempoPorPeca, String tempoTransporte, String tempoProducao){
		
		this.atividadeCostureira = atividadeCostureira;
		this.parte = parte;
		this.qtdeLote = qtdeLote;
		this.pecasPorLote = pecasPorLote;
		this.tempoPorPeca = tempoPorPeca;
		this.tempoTransporte = tempoTransporte;
		this.tempoProducao = tempoProducao;
		totalPecas = "-";
		tempoDeRecebimento ="-";
	}
	
	public void calcularTotalDePecasETempoTotal(int qtdeLote, int pecasPorLote,int tempoPorPeca,long tempoTransporte,
			ProcessoChromosome pc){
		long tempoTotal  = 0;
		long tempoProducao = 0;
		
		int totalPecas = qtdeLote * pecasPorLote;
		this.totalPecas = String.valueOf(totalPecas);
		
		long maiortTempo = pegarMaiorTempo(pc);
		
		tempoProducao = totalPecas * tempoPorPeca; 
		tempoTotal = tempoProducao + maiortTempo;
		
		this.tempoProducao = String.valueOf(tempoProducao); 
		this.tempoDeRecebimento = String.valueOf(maiortTempo);
		this.tempoTotal = String.valueOf(tempoTotal);
		
	}
	
	public long pegarMaiorTempo(ProcessoChromosome pc){
		long maior = 0;
		if(pc.getCostureirasPredecessoras() != null){
			for(CostureiraPredecessora cp : pc.getCostureirasPredecessoras()){
				if((cp.getTempoDeProducao() + cp.getTempoDeTransporte()) > maior){
					maior = cp.getTempoDeProducao() + cp.getTempoDeTransporte();
				}
			}
		}
		return maior;
	}
	
		
	public String getAtividadeCostureira() {
		return atividadeCostureira;
	}
	
	public void setAtividadeCostureira(String atividadeCostureira) {
		this.atividadeCostureira = atividadeCostureira;
	}
	
	public String getParte() {
		return parte;
	}
	
	public void setParte(String parte) {
		this.parte = parte;
	}
	
	public String getQtdeLote() {
		return qtdeLote;
	}
	
	public void setQtdeLote(String qtdeLote) {
		this.qtdeLote = qtdeLote;
	}
	
	public String getPecasPorLote() {
		return pecasPorLote;
	}
	
	public void setPecasPorLote(String pecasPorLote) {
		this.pecasPorLote = pecasPorLote;
	}
	
	public String getTotalPecas() {
		return totalPecas;
	}
	
	public void setTotalPecas(String totalPecas) {
		this.totalPecas = totalPecas;
	}
	
	public String getTempoPorPeca() {
		return tempoPorPeca;
	}
	
	public void setTempoPorPeca(String tempoPorPeca) {
		this.tempoPorPeca = tempoPorPeca;
	}
	
	public String getTempoProducao() {
		return tempoProducao;
	}

	public void setTempoProducao(String tempoProducao) {
		this.tempoProducao = tempoProducao;
	}

	public String getTempoTransporte() {
		return tempoTransporte;
	}

	public void setTempoTransporte(String tempoTransporte) {
		this.tempoTransporte = tempoTransporte;
	}
	
	public String getTempoDeRecebimento() {
		return tempoDeRecebimento;
	}

	public void setTempoDeRecebimento(String tempoDeRecebimento) {
		this.tempoDeRecebimento = tempoDeRecebimento;
	}


	public String getTempoTotal() {
		return tempoTotal;
	}
	public void setTempoTotal(String tempoTotal) {
		this.tempoTotal = tempoTotal;
	}
}

