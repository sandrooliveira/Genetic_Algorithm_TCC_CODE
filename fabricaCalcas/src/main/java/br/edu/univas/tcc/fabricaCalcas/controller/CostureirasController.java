package br.edu.univas.tcc.fabricaCalcas.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.edu.univas.tcc.fabricaCalcas.dao.ConFactory;
import br.edu.univas.tcc.fabricaCalcas.dao.CostureirasDAO;
import br.edu.univas.tcc.fabricaCalcas.model.Costureira;

@ManagedBean(name = "costureirasController")
@ViewScoped
public class CostureirasController {

	private List<Costureira> costureiras;
	private CostureirasDAO cosDao;
	
	private Costureira costureira;
	private Costureira costureiraToEdit;
	private Costureira costureiraToDelete;
	
	@PostConstruct
	public void init(){
		cosDao = new CostureirasDAO(ConFactory.getConn());
		costureiras = cosDao.listAllCostureiras();
		costureira = new Costureira();
	}
	
	public void addCostureira(){
		cosDao.addCostureira(costureira);
		costureiras.add(costureira);
		
		RequestContext request = RequestContext.getCurrentInstance();
		request.execute("PF('addCostureira').hide()");
		request.update("formAddCostureira");
		costureira = new Costureira();
		sendMessageToView("Costureira adicionada com sucesso!", FacesMessage.SEVERITY_INFO);
	}
	
	public void loadCostureiraToEdit(Costureira costureira){
		costureiraToEdit = costureira;
	}
	
	public void loadCostureiraToDelete(Costureira costureira){
		costureiraToDelete = costureira;
	}
	
	public void updateCostureira(){
		cosDao.updateCostureira(costureiraToEdit);
		sendMessageToView("Costureira atualizada com sucesso!", FacesMessage.SEVERITY_INFO);
		
		RequestContext request = RequestContext.getCurrentInstance();
		request.execute("PF('editCostureira').hide()");
	}
	
	public void excluirCostureira(){
		
		try{
			cosDao.removeCostureira(costureiraToDelete);
			costureiras.remove(costureiraToDelete);
			sendMessageToView("Costureira removida com sucesso!", FacesMessage.SEVERITY_INFO);
		}catch (Exception e) {
			sendMessageToView("Esta costureira está relacionada com alguma habilidade e não pode ser excluida!", 
							   FacesMessage.SEVERITY_FATAL);
		}
		
		
	}
	
	/*Messages Handling*/
	public void sendMessageToView(String message,Severity severity) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		contexto.addMessage(null, new FacesMessage(severity, message, null));
	}
	
	/*Getters and setters*/
	public List<Costureira> getCostureiras() {
		return costureiras;
	}

	public void setCostureiras(List<Costureira> costureiras) {
		this.costureiras = costureiras;
	}

	public Costureira getCostureira() {
		return costureira;
	}

	public void setCostureira(Costureira costureira) {
		this.costureira = costureira;
	}

	public Costureira getCostureiraToEdit() {
		return costureiraToEdit;
	}

	public void setCostureiraToEdit(Costureira costureiraToEdit) {
		this.costureiraToEdit = costureiraToEdit;
	}

	public Costureira getCostureiraToDelete() {
		return costureiraToDelete;
	}

	public void setCostureiraToDelete(Costureira costureiraToDelete) {
		this.costureiraToDelete = costureiraToDelete;
	}
}
