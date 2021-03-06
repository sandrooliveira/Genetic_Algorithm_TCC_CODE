package br.edu.univas.tcc.fabricaCalcas.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
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
import br.edu.univas.tcc.fabricaCalcas.util.HttpRequestUtil;
import br.edu.univas.tcc.ga_core.Chromosome;

@ManagedBean(name = "distribuicaoController")
@ViewScoped
public class DistribuicaoController {

	private List<Processo> processos;
	
	private ProcessoDAO processDao;
	
	private String totalPecas;
	private String totalPecasPorLote;
	private Processo processo;
	private Date dataInicio;
	private int numeroDeLotes;
	private int idProcesso;
	private TreeNode rootFluxograma;	
	private TreeNode rootTable  = new DefaultTreeNode(new TabDetailBean("-", "-", "-","-","-","-","-"));
	private List<Node> allNodes = new ArrayList<Node>();
	private boolean mostrarResult = false;
	private ProcessoIndividual pi;
	private BigDecimal prazEmSegundos;
	private boolean prazoAtendido;
	
	@PostConstruct
	public void init(){
		processDao = new ProcessoDAO(ConFactory.getConn());
		processos = processDao.listAllProcessos();
		getProjetoIdFromUrl();
	}

	public String abrirProcessoToDistribuicao(Integer idProcesso) throws IOException{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("/pages/abrir_processo_distribuicao?faces-redirect=true");
		stringBuffer.append("&amp;idProcesso=").append(idProcesso);
		FacesContext.getCurrentInstance().getExternalContext().redirect("abrir_processo_distribuicao.jsf?idProcesso="+idProcesso); 
		return null;
	}
	
	public void iniciarDistribuicao(){
		int totalPecasInt = Integer.parseInt(totalPecas);
		int totalPecasPorLoteInt = Integer.parseInt(totalPecasPorLote);
		
		if(totalPecasInt == 0 || totalPecasPorLoteInt == 0){
			sendMessageToView("Total de pe�as ou total de pe�as por lote � invalido!", FacesMessage.SEVERITY_ERROR);
			return;
		}
		
		numeroDeLotes = totalPecasInt / totalPecasPorLoteInt;
		if((numeroDeLotes * totalPecasPorLoteInt) != totalPecasInt){
			sendMessageToView("N�mero de lote n�o exato: "+numeroDeLotes+" * "+totalPecasPorLote+" = "+numeroDeLotes*totalPecasPorLoteInt, 
							   FacesMessage.SEVERITY_ERROR);
			return;
		}
		
		if(idProcesso <= 0 || processo == null){
			sendMessageToView("Processo inv�lido", FacesMessage.SEVERITY_ERROR);
			return;
		}
		
		prazEmSegundos = calcularPrazo();
		prazoAtendido = false;
		
		GeneticAlgorithmManagement gam = new GeneticAlgorithmManagement();
		pi = gam.iniciarDistribuicao(numeroDeLotes, prazEmSegundos, totalPecasPorLoteInt, idProcesso);
		
		//This is just to manage information messages in the view
		if(pi.getValue() <= prazEmSegundos.longValue()){
			prazoAtendido = true;
		}
		
		if(pi != null){
			rootFluxograma = construirArvore(pi.getNode(), null);
			allNodes = new ArrayList<Node>();
			rootTable  = new DefaultTreeNode(new TabDetailBean("-", "-", "-","-","-","-","-"));
			construirTableDetail(pi);
			mostrarResult = true;
		}
		
	}
	
	public BigDecimal calcularPrazo(){
		DateTime dataInical = new DateTime(dataInicio);
		DateTime dataFinal  = new DateTime(processo.getDataEntrega());
		return  new BigDecimal(Seconds.secondsBetween(dataInical, dataFinal).getSeconds());
	}
	
	public Date getDataEntrega(){
		if(processo != null){
			return processo.getDataEntrega();
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	public void construirTableDetail(ProcessoIndividual pi){
		getAllNodes(pi.getNode());
		for(Node n : allNodes){
			if(!n.getAtividade().getHabilidade().getNomeHabilidade().equals("Carimbo")){
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
		contexto.addMessage(null, new FacesMessage(severity, message, null));
	}

	public void getProjetoIdFromUrl() {
		Object idProcesso = HttpRequestUtil.getParameterValueInRequest("idProcesso");
		
		try {
			if (idProcesso != null) {
				this.idProcesso = Integer.parseInt(String.valueOf(idProcesso));
				this.processo = processDao.getProcessoByID(this.idProcesso);
			} else {
				this.idProcesso = 0;
			}

		} catch (NumberFormatException e) {
			this.idProcesso = 0;
		}
	}
	
	public String pegarDataFormatada(){
		if(dataInicio != null){
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			return sd.format(dataInicio);
		}
		return null;
	}
	
	/*Getters and setters*/
	public List<Processo> getProcessos() {
		return processos;
	}

	public void setProcessos(List<Processo> processos) {
		this.processos = processos;
	}
	
	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public String getTotalPecas() {
		return totalPecas;
	}

	public void setTotalPecas(String totalPecas) {
		this.totalPecas = totalPecas;
	}

	public String getTotalPecasPorLote() {
		return totalPecasPorLote;
	}

	public void setTotalPecasPorLote(String totalPecasPorLote) {
		this.totalPecasPorLote = totalPecasPorLote;
	}
	
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
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

	public ProcessoIndividual getPi() {
		return pi;
	}

	public void setPi(ProcessoIndividual pi) {
		this.pi = pi;
	}

	public BigDecimal getPrazEmSegundos() {
		return prazEmSegundos;
	}

	public void setPrazEmSegundos(BigDecimal prazEmSegundos) {
		this.prazEmSegundos = prazEmSegundos;
	}

	public boolean isPrazoAtendido() {
		return prazoAtendido;
	}

	public void setPrazoAtendido(boolean prazoAtendido) {
		this.prazoAtendido = prazoAtendido;
	}
	
}
