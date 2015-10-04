package br.edu.univas.tcc.fabricaCalcas.model;

public class TabDetailBean {

	private String atividadeCostureira;
	private String parte;
	private String qtdeLote;
	private String pecasPorLote;
	private String totalPecas;
	private String tempoPorPeca;
	private String tempoTotal;
	
	public TabDetailBean(String atividadeCostureira, String parte, int qtdeLote, int pecasPorLote,
			int tempoPorPeca){
		
		this.atividadeCostureira = atividadeCostureira;
		this.parte = parte;
		this.qtdeLote = String.valueOf(qtdeLote);
		this.pecasPorLote = String.valueOf(pecasPorLote);
		this.tempoPorPeca = String.valueOf(tempoPorPeca);
	}
	
	public TabDetailBean(String atividadeCostureira, String parte, String qtdeLote, String pecasPorLote,
			String tempoPorPeca){
		
		this.atividadeCostureira = atividadeCostureira;
		this.parte = parte;
		this.qtdeLote = qtdeLote;
		this.pecasPorLote = pecasPorLote;
		this.tempoPorPeca = tempoPorPeca;
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
	public String getTempoTotal() {
		return tempoTotal;
	}
	public void setTempoTotal(String tempoTotal) {
		this.tempoTotal = tempoTotal;
	}
}

