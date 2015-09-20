package br.edu.univas.tcc.fabricaCalcas.controller;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.primefaces.context.RequestContext;

import br.edu.univas.tcc.fabricaCalcas.dao.AtividadeDAO;
import br.edu.univas.tcc.fabricaCalcas.dao.HabilidadeDAO;
import br.edu.univas.tcc.fabricaCalcas.dao.ProcessoDAO;
import br.edu.univas.tcc.fabricaCalcas.model.Atividade;
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

		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("db_pu");
		EntityManager manager = factory.createEntityManager();

		habDao = new HabilidadeDAO(manager);
		atDao = new AtividadeDAO(manager);
		processDao = new ProcessoDAO(manager);
		processo = new Processo();
		habilidades = habDao.listAllHabilidades();
		processos = processDao.listAllProcessos();

	}

	public void addAtividade() {
		Habilidade habilidade = habDao.selectHabilidadeById(idHabilidade);
		Atividade atividade = new Atividade();
		atividade.setHabilidade(habilidade);
		atividade.setProcesso(processo);
		atDao.addNovaAtividade(atividade);
	}

	public void addProcesso() {

		if (processo.getCliente() != null) {
			processDao.addProcesso(processo);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Processo salvo com sucesso!", null));
			processos.add(processo);

			RequestContext request = RequestContext.getCurrentInstance();
			request.execute("PF('addProcesso').hide()");
			request.update("formAddProcesso");
			Collections.sort(processos, new Processo());
			processo = new Processo();
		}
	}
	

	public void excluirProcesso() {
	    processDao.excluirProcesso(processoToDelete);	
	    processos.remove(processoToDelete);
	    FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
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
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Processo atualizado com sucesso!", null));

		RequestContext request = RequestContext.getCurrentInstance();
		request.execute("PF('editProcesso').hide()");
	}
	
	public String abrirProcesso(int idProcesso){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("/pages/abrirProcesso?faces-redirect=true");
		stringBuffer.append("&amp;idProcesso=").append(idProcesso);

		return stringBuffer.toString();
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
