package br.edu.univas.tcc.fabricaCalcas.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.edu.univas.tcc.fabricaCalcas.dao.ConFactory;
import br.edu.univas.tcc.fabricaCalcas.dao.ProcessoDAO;
import br.edu.univas.tcc.fabricaCalcas.ga_code.GeneticAlgorithmManagement;
import br.edu.univas.tcc.fabricaCalcas.ga_code.ProcessoIndividual;
import br.edu.univas.tcc.fabricaCalcas.model.Processo;

@ManagedBean(name = "distribuicaoController")
@ViewScoped
public class DistribuicaoController {

	private List<Processo> processos;
	
	private ProcessoDAO processDao;
	
	private int totalPecas;
	private int totalPecasPorLote;
	private int numeroDeLotes;
	private int idProcesso = 80; //TODO:recuperar pelo ID
	
	@PostConstruct
	public void init(){
		processDao = new ProcessoDAO(ConFactory.getConn());
		processos = processDao.listAllProcessos();
	}

	public String abrirProcessoToDistribuicao(Integer idProcesso) throws IOException{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("/pages/abrir_processo_distribuicao?faces-redirect=true");
		stringBuffer.append("&amp;idProcesso=").append(idProcesso);
		FacesContext.getCurrentInstance().getExternalContext().redirect("abrir_processo_distribuicao.jsf?idProcesso="+idProcesso); 
		return null;
	}
	
	public void iniciarDistribuicao(){
		if(totalPecas == 0 || totalPecasPorLote == 0){
			sendMessageToView("Total de peças ou total de peças por lote é invalido!", FacesMessage.SEVERITY_ERROR);
			return;
		}
		
		numeroDeLotes = totalPecas / totalPecasPorLote;
		if((numeroDeLotes * totalPecasPorLote) != totalPecas){
			sendMessageToView("Número de lote não exato: "+numeroDeLotes+" * "+totalPecasPorLote+" = "+numeroDeLotes*totalPecasPorLote, 
							   FacesMessage.SEVERITY_ERROR);
		}
		
		GeneticAlgorithmManagement gam = new GeneticAlgorithmManagement();
		ProcessoIndividual pi = gam.iniciarDistribuicao(numeroDeLotes, totalPecasPorLote, idProcesso);
		pi.getNode().printNodes();
	}
	
	/*Messages Handling*/
	public void sendMessageToView(String message,Severity severity) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		contexto.addMessage(null, 
							new FacesMessage(severity, message, null));
	}
	
	/*Getters and setters*/
	public List<Processo> getProcessos() {
		return processos;
	}

	public void setProcessos(List<Processo> processos) {
		this.processos = processos;
	}

	public int getTotalPecas() {
		return totalPecas;
	}

	public void setTotalPecas(int totalPecas) {
		this.totalPecas = totalPecas;
	}

	public int getTotalPecasPorLote() {
		return totalPecasPorLote;
	}

	public void setTotalPecasPorLote(int totalPecasPorLote) {
		this.totalPecasPorLote = totalPecasPorLote;
	}

	public int getNumeroDeLotes() {
		return numeroDeLotes;
	}

	public void setNumeroDeLotes(int numeroDeLotes) {
		this.numeroDeLotes = numeroDeLotes;
	}
}
