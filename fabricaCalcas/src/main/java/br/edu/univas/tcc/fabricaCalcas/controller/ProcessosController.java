package br.edu.univas.tcc.fabricaCalcas.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.primefaces.context.RequestContext;

import br.edu.univas.tcc.fabricaCalcas.dao.AtividadeDAO;
import br.edu.univas.tcc.fabricaCalcas.dao.ConFactory;
import br.edu.univas.tcc.fabricaCalcas.dao.HabilidadeDAO;
import br.edu.univas.tcc.fabricaCalcas.dao.ProcessoDAO;
import br.edu.univas.tcc.fabricaCalcas.model.Atividade;
import br.edu.univas.tcc.fabricaCalcas.model.AtividadeOrdem;
import br.edu.univas.tcc.fabricaCalcas.model.Habilidade;
import br.edu.univas.tcc.fabricaCalcas.model.Processo;

@ManagedBean(name = "processosController")
@ViewScoped
public class ProcessosController {

	private Processo processo;
	private Processo processoToEdit;
	private Processo processoToDelete;
	private int idHabilidade;
	private List<Habilidade> habilidades;
	private List<Atividade> atividades;
	private List<Processo> processos;
	HabilidadeDAO habDao;
	AtividadeDAO atDao;
	ProcessoDAO processDao;

	@PostConstruct
	public void init() {
		EntityManager manager = ConFactory.getConn();

		habDao = new HabilidadeDAO(manager);
		atDao = new AtividadeDAO(manager);
		processDao = new ProcessoDAO(manager);
		processo = new Processo();
		habilidades = habDao.listAllHabilidades();
		processos = processDao.listAllProcessos();

	}

	public void addProcesso() {

		if (processo.getCliente() != null) {
			Habilidade finalizacao = getHabilidadeByName("Finalização");
			Habilidade carimbo = getHabilidadeByName("Carimbo");
			
			if(finalizacao != null && carimbo != null){
				
				processDao.addProcesso(processo);
				processos.add(processo);
				
				Atividade atFinalizacao = new Atividade();
				atFinalizacao.setAtividadeFinal(true);
				atFinalizacao.setHabilidade(finalizacao);
				atFinalizacao.setNomeAtividade("AT_Finalização");
				atFinalizacao.setProcesso(processo);
				
				Atividade atCarimbo = new Atividade();
				atCarimbo.setAtividadeFinal(false);
				atCarimbo.setHabilidade(carimbo);
				atCarimbo.setNomeAtividade("AT_Carimbo");
				atCarimbo.setProcesso(processo);
				atDao.addNovaAtividade(atFinalizacao);
				atDao.addNovaAtividade(atCarimbo);
				
				AtividadeOrdem ao = new AtividadeOrdem();
				ao.setAtividade(atFinalizacao);
				ao.setAtividadePredecessora(atCarimbo);
				atDao.addAtividadeOrdem(ao);
				
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
															 "Processo salvo com sucesso!", null));

				RequestContext request = RequestContext.getCurrentInstance();
				request.execute("PF('addProcesso').hide()");
				request.update("formAddProcesso");
				Collections.sort(processos, new Processo());
				processo = new Processo();
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						 "Insira as habilidades Finalização e Carimbo no banco!", null));
			}
		}
	}
	
	public Date getCurrentDate(){
		return new Date();
	}
	
	//TODO:Remover este workaround
	public Habilidade getHabilidadeByName(String name){
		for(Habilidade h : habilidades){
			if(h.getNomeHabilidade().equals(name)){
				return h;
			}
		}
		return null;
	}

	public void excluirProcesso() {
	    processDao.excluirProcesso(processoToDelete);	
	    processos.remove(processoToDelete);
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
													 "Processo removido com sucesso!", null));
	}

	public void loadProcessoToEdit(Processo processo) {
		this.processoToEdit = processo;
	}

	public void loadProcessoToDelete(Processo processo) {
		this.processoToDelete = processo;
	}

	public void updateProcesso() {
		processDao.updateProcesso(processoToEdit);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
												     "Processo atualizado com sucesso!", null));

		RequestContext request = RequestContext.getCurrentInstance();
		request.execute("PF('editProcesso').hide()");
	}
	
	public String abrirProcesso(int idProcesso) throws IOException{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("/pages/abrirProcesso?faces-redirect=true");
		stringBuffer.append("&amp;idProcesso=").append(idProcesso);

		FacesContext.getCurrentInstance().getExternalContext().redirect("abrirProcesso.jsf?idProcesso="+idProcesso); 
		
		return null;
	}

	public List<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(List<Atividade> atividades) {
		this.atividades = atividades;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public Processo getProcessoToEdit() {
		return processoToEdit;
	}

	public void setProcessoToEdit(Processo processoToEdit) {
		this.processoToEdit = processoToEdit;
	}

	public Processo getProcessoToDelete() {
		return processoToDelete;
	}

	public void setProcessoToDelete(Processo processoToDelete) {
		this.processoToDelete = processoToDelete;
	}

	public int getIdHabilidade() {
		return idHabilidade;
	}

	public void setIdHabilidade(int idHabilidade) {
		this.idHabilidade = idHabilidade;
	}

	public List<Habilidade> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}

	public List<Processo> getProcessos() {
		return processos;
	}

	public void setProcessos(List<Processo> processos) {
		this.processos = processos;
	}
}
