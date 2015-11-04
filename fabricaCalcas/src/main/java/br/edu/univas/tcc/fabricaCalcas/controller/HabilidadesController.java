package br.edu.univas.tcc.fabricaCalcas.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.edu.univas.tcc.fabricaCalcas.dao.ConFactory;
import br.edu.univas.tcc.fabricaCalcas.dao.CostureirasDAO;
import br.edu.univas.tcc.fabricaCalcas.dao.HabilidadeDAO;
import br.edu.univas.tcc.fabricaCalcas.model.Costureira;
import br.edu.univas.tcc.fabricaCalcas.model.CostureiraHabilidade;
import br.edu.univas.tcc.fabricaCalcas.model.Habilidade;

@ManagedBean(name = "habilidadesController")
@ViewScoped
public class HabilidadesController {

	private List<Habilidade> habilidades;
	private HabilidadeDAO habDao;
	private CostureirasDAO costDao;
	private Habilidade habilidade;
	private Habilidade habilidadeToEdit;

	private List<Costureira> costureiras;
	private CostureiraHabilidade newCostureiraHabilidade;
	private CostureiraHabilidade cosHabToBeRemoved;
	
	@PostConstruct
	public void init(){
		habDao = new HabilidadeDAO(ConFactory.getConn());
		costDao = new CostureirasDAO(ConFactory.getConn());
		habilidades = habDao.listAllHabilidades();
		costureiras = costDao.listAllCostureiras();
		
		habilidade = new Habilidade();
		newCostureiraHabilidade = new CostureiraHabilidade();
	}
	
	public void addHabilidade(){
		habDao.addHabilidade(habilidade);
		habilidades.add(habilidade);
		habilidade = new Habilidade();
		sendMessageToView("Habilidade adicionada com sucesso!", FacesMessage.SEVERITY_INFO);
	}
	
	/*Messages Handling*/
	public void sendMessageToView(String message,Severity severity) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		contexto.addMessage(null, 
							new FacesMessage(severity, message, null));
	}
	
	public void abrirHabilidade(Habilidade habilidadeToEdit){
		this.habilidadeToEdit = habilidadeToEdit;
		retirarCostureirasJaAdicionadas();
	}
	
	public void addCostureiraNaHabilidade(){
		if(newCostureiraHabilidade.getTempoPorPeca() != 0 && newCostureiraHabilidade.getCostureira() != null){
			inserirCostureira();
		}else if(newCostureiraHabilidade.getTempoPorPeca() == 0 && 
				newCostureiraHabilidade.getCostureira().getNomeCostureira().equals("Marcelo")){
			inserirCostureira();
		}else if(newCostureiraHabilidade.getTempoPorPeca() == 0){
			sendMessageToView("Insira um valor diferente de 0", FacesMessage.SEVERITY_ERROR);
			return;
		}else{
			sendMessageToView("Todas as costureiras já foram adicionadas para esta habilidade!",
							   FacesMessage.SEVERITY_WARN);
			return;
		}
	}

	public void inserirCostureira() {
		costureiras.remove(newCostureiraHabilidade.getCostureira());
		newCostureiraHabilidade.setHabilidade(habilidadeToEdit);
		habilidadeToEdit.getCostureiraHabilidades().add(newCostureiraHabilidade);
		habDao.addCostureiraNaHabilidade(newCostureiraHabilidade);
		newCostureiraHabilidade = new CostureiraHabilidade();
		sendMessageToView("Costureira adicionada com sucesso!",
				   FacesMessage.SEVERITY_INFO);
	}
	
	public void retirarCostureirasJaAdicionadas(){
		for(CostureiraHabilidade hab : habilidadeToEdit.getCostureiraHabilidades()){
			costureiras.remove(hab.getCostureira());
		}
	}
	
	public void setarCostureiraASerRemovida(CostureiraHabilidade costureiraHab){
		this.cosHabToBeRemoved = costureiraHab;
	}
	
	public void cancelarDeleting(){
		this.cosHabToBeRemoved = null;
	}
	
	public void removerCostureira(){
		habDao.removerCostureiraDaHabilidade(cosHabToBeRemoved);
		costureiras.add(cosHabToBeRemoved.getCostureira());
		habilidadeToEdit.getCostureiraHabilidades().remove(cosHabToBeRemoved);
		sendMessageToView("Costureira removida com sucesso!",
				   FacesMessage.SEVERITY_INFO);
	}
	
	/*Getters and Setters*/
	public Habilidade getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(Habilidade habilidade) {
		this.habilidade = habilidade;
	}

	public List<Habilidade> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}

	public Habilidade getHabilidadeToEdit() {
		return habilidadeToEdit;
	}

	public void setHabilidadeToEdit(Habilidade habilidadeToEdit) {
		this.habilidadeToEdit = habilidadeToEdit;
	}

	public List<Costureira> getCostureiras() {
		return costureiras;
	}

	public void setCostureiras(List<Costureira> costureiras) {
		this.costureiras = costureiras;
	}

	public CostureiraHabilidade getNewCostureiraHabilidade() {
		return newCostureiraHabilidade;
	}

	public void setNewCostureiraHabilidade(
			CostureiraHabilidade newCostureiraHabilidade) {
		this.newCostureiraHabilidade = newCostureiraHabilidade;
	}
}
