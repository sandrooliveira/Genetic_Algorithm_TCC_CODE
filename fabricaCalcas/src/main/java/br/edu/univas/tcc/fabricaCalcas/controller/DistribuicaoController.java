package br.edu.univas.tcc.fabricaCalcas.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.edu.univas.tcc.fabricaCalcas.dao.ConFactory;
import br.edu.univas.tcc.fabricaCalcas.dao.ProcessoDAO;
import br.edu.univas.tcc.fabricaCalcas.ga_code.CostureiraPredecessora;
import br.edu.univas.tcc.fabricaCalcas.ga_code.GeneticAlgorithmManagement;
import br.edu.univas.tcc.fabricaCalcas.ga_code.ProcessoChromosome;
import br.edu.univas.tcc.fabricaCalcas.ga_code.ProcessoIndividual;
import br.edu.univas.tcc.fabricaCalcas.model.Node;
import br.edu.univas.tcc.fabricaCalcas.model.Processo;
import br.edu.univas.tcc.fabricaCalcas.model.TabDetailBean;
import br.edu.univas.tcc.ga_core.Chromosome;

@ManagedBean(name = "distribuicaoController")
@ViewScoped
public class DistribuicaoController {

	private List<Processo> processos;
	
	private ProcessoDAO processDao;
	
	private int totalPecas;
	private int totalPecasPorLote;
	private int numeroDeLotes;
	private int idProcesso = 80; //TODO:recuperar pelo ID
	private TreeNode rootFluxograma;	
	private TreeNode rootTable  = new DefaultTreeNode(new TabDetailBean("-", "-", "-","-","-","-","-"));
	private List<Node> allNodes = new ArrayList<Node>();
	private boolean mostrarResult = false;
	private float tempoTotal;
	
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
			return;
		}
		
		GeneticAlgorithmManagement gam = new GeneticAlgorithmManagement();
		ProcessoIndividual pi = gam.iniciarDistribuicao(numeroDeLotes, totalPecasPorLote, idProcesso);
		
		if(pi != null){
			rootFluxograma = construirArvore(pi.getNode(), null);
			allNodes = new ArrayList<Node>();
			rootTable  = new DefaultTreeNode(new TabDetailBean("-", "-", "-","-","-","-","-"));
			construirTableDetail(pi);
			tempoTotal = pi.getValue();
			mostrarResult = true;
		}
		
	}
	
	@SuppressWarnings("unused")
	public void construirTableDetail(ProcessoIndividual pi){
		getAllNodes(pi.getNode());
		for(Node n : allNodes){
			TreeNode atividade = new DefaultTreeNode(new TabDetailBean(n.getAtividade().getNomeAtividade(), 
																	  "-","-", "-", "-","-","-"),rootTable);
			for(Chromosome c : n.getCromossomos()){
				ProcessoChromosome pc = (ProcessoChromosome) c;
				TreeNode chromossome = new DefaultTreeNode(new TabDetailBean(pc.getCostureiraHabilidade().getCostureira().getNomeCostureira(),
																			 "-", pc.getLotesToShow(), pi.getPecasPorLote(), 
																			 pc.getCostureiraHabilidade().getTempoPorPeca(),
																			 0,0,pc),atividade);
				
				if(pc.getCostureirasPredecessoras() != null){
					for(CostureiraPredecessora cp : pc.getCostureirasPredecessoras()){
						TreeNode nodeCp = new DefaultTreeNode(new TabDetailBean(cp.getCostureira().getCostureira().getNomeCostureira(),
																				cp.getCostureira().getHabilidade().getNomeHabilidade(),
																				cp.getQtdeLotes(), "-", "-",
																				cp.getTempoDeTransporte(),
																				cp.getTempoDeProducao(),null),chromossome);
					}
				}
			}
		}
	}
	
	public void getAllNodes(Node node){
		allNodes.add(node);
		for(Node n : node.getPredecesoras()){
			getAllNodes(n);
		}
	}
	
	
	/*Fluxograma handling*/
	public TreeNode construirArvore(Node ttParent, TreeNode parent) {
		TreeNode newNode = new DefaultTreeNode(ttParent, parent);
		newNode.setExpanded(true);
		for (Node tt : ttParent.getPredecesoras()) {
			construirArvore(tt, newNode);
		}	
		return newNode;
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

	public TreeNode getRootFluxograma() {
		return rootFluxograma;
	}

	public void setRootFluxograma(TreeNode rootFluxograma) {
		this.rootFluxograma = rootFluxograma;
	}

	public TreeNode getRootTable() {
		return rootTable;
	}

	public void setRootTable(TreeNode rootTable) {
		this.rootTable = rootTable;
	}

	public boolean isMostrarResult() {
		return mostrarResult;
	}

	public void setMostrarResult(boolean mostrarResult) {
		this.mostrarResult = mostrarResult;
	}

	public float getTempoTotal() {
		return tempoTotal;
	}

	public void setTempoTotal(float tempoTotal) {
		this.tempoTotal = tempoTotal;
	}
	
}
