package br.edu.univas.tcc.fabricaCalcas.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.edu.univas.tcc.fabricaCalcas.dao.AtividadeDAO;
import br.edu.univas.tcc.fabricaCalcas.dao.ConFactory;
import br.edu.univas.tcc.fabricaCalcas.dao.HabilidadeDAO;
import br.edu.univas.tcc.fabricaCalcas.dao.ProcessoDAO;
import br.edu.univas.tcc.fabricaCalcas.model.Atividade;
import br.edu.univas.tcc.fabricaCalcas.model.AtividadeOrdem;
import br.edu.univas.tcc.fabricaCalcas.model.Habilidade;
import br.edu.univas.tcc.fabricaCalcas.model.Processo;
import br.edu.univas.tcc.fabricaCalcas.util.HttpRequestUtil;

@ManagedBean(name = "processosAtividadesController")
@ViewScoped
public class ProcessosAtividadesController {

	public List<Habilidade> habilidades;
	private List<Atividade> atividades;
	private List<Atividade> atividadesCombobox;
	private List<Atividade> atividadesComboboxPredecessora;
	
	private Atividade atividade;
	private Atividade atividadeToEdit;
	private Atividade atividadeToDelete;
	
	private Atividade atividadeToAddOnFluxograma;
	private Atividade atividadePredecessora;

	private HabilidadeDAO habilidadeDao;
	private AtividadeDAO atividadeDao;
	private ProcessoDAO processoDao;

	//TODO: pegar o parâmetro da requisição
	private int idProcessoRequest = 80;
	private Processo processo;
	private Atividade atividadeFinal;
	private Atividade atividadeInicial;
	
	private TreeNode root;

	@PostConstruct
	public void init() {

		EntityManager manager = ConFactory.getConn();
		
		getProjetoIdFromUrl();
		
		/*Instancia todos os DAOs a serem utilizados*/
		habilidadeDao = new HabilidadeDAO(manager);
		atividadeDao = new AtividadeDAO(manager);
		processoDao = new ProcessoDAO(manager);

		/*Recuperar objeto de processo através do ID vindo da requisição*/
		processo = processoDao.getProcessoByID(idProcessoRequest);
		
		/*Recupera atividades para listar na tabela*/
		atividades = new ArrayList<Atividade>(processo.getAtividades());
		atividadeFinal = getAtividadeFinal();
		atividades.remove(atividadeFinal);
		
		atividadesCombobox = new ArrayList<Atividade>();
		
		/*Recupera as atividades para listar no combobox de predecessoras*/
		atividadesComboboxPredecessora = new ArrayList<Atividade>(processo.getAtividades());
		
		/*Recupera todas as habilidades disponíveis*/
		habilidades = habilidadeDao.listAllHabilidades();
		
		atividade = new Atividade();

		/*Construir árvore*/
		if (atividadeFinal != null) {
			removerHabilidadesJaAdicionadas();
			root = construirArvore(atividadeFinal, null);
			
		} else {
			sendMessageToView("Não há atividade final definida!", FacesMessage.SEVERITY_ERROR);
		}
	}

	
	/*Fluxograma handling*/
	public TreeNode construirArvore(Atividade ttParent, TreeNode parent) {
		AtividadeOrdem aoToBeDeleted = null;
		AtividadeOrdem newAtividadeOrdem  = null;
		boolean removerAo = false;
		boolean setarValorInicial = false;
		
		/*Isso é para remover as atividades já adicionadas na árvore do combo de predecessoras*/
		atividadesComboboxPredecessora.remove(ttParent);
		
		/*Isso é para recuperar a atividade inicial (Carimbo) e removê-la
		 *da lista de atividades (tabela)*/
		if(ttParent.getHabilidade().getNomeHabilidade().equals("Carimbo")){
			atividadeInicial = ttParent;
			atividades.remove(atividadeInicial);
		}
		
		/*Recupera as atividades para listar no combobox de atividades*/
		if(!atividadesCombobox.contains(ttParent) && atividadeInicial != ttParent){
			atividadesCombobox.add(ttParent);	
		}
		
		TreeNode newNode = new DefaultTreeNode(ttParent, parent);
		newNode.setExpanded(true);
		aoToBeDeleted = checarSeAtividadeDeletadaEstaNoFluxograma(ttParent);
		for (AtividadeOrdem tt : ttParent.getAtividadeOrdemsForIdAtividade()) {
			
			/* Remove a atividade a ser deletada (se tiver) do fluxograma */
			if (aoToBeDeleted != null){
				removerAo = true;
				if(ttParent.getAtividadeOrdemsForIdAtividade().size() == 1 && !setarValorInicial){
					setarValorInicial = true;
				    newAtividadeOrdem = new AtividadeOrdem();
					newAtividadeOrdem.setAtividade(ttParent);
					newAtividadeOrdem.setAtividadePredecessora(atividadeInicial);
					atividadeDao.addAtividadeOrdem(newAtividadeOrdem);
					construirArvore(atividadeInicial, newNode);
				}else{
					if(aoToBeDeleted != tt){
						construirArvore(tt.getAtividadePredecessora(), newNode);
					}else{
						continue;
					}
				}
			}else{
				construirArvore(tt.getAtividadePredecessora(), newNode);
			}
		}
		if(setarValorInicial){
			ttParent.getAtividadeOrdemsForIdAtividade().add(newAtividadeOrdem);
			setarValorInicial = false;
		}
		if(removerAo){
			ttParent.getAtividadeOrdemsForIdAtividade().remove(aoToBeDeleted);
			removerAo = false;
		}
		/*Remover atividades predecessoras da atividade deletada e voltar as atividades nos combos*/
		rmApFromAtToBeDeleted(atividadeToDelete);
		
		return newNode;
	}
	
	public AtividadeOrdem checarSeAtividadeDeletadaEstaNoFluxograma(Atividade atividade){
		for(AtividadeOrdem ao : atividade.getAtividadeOrdemsForIdAtividade()){
			if(ao != null){
				if(ao.getAtividadePredecessora() == atividadeToDelete){
					return ao;
				}
			}
		}
		return null;
	}
	
	public void rmApFromAtToBeDeleted(Atividade atividade){
		if(atividade != null){
			List<AtividadeOrdem> aoTobeRemoved = new ArrayList<AtividadeOrdem>();
			for(AtividadeOrdem ao : atividade.getAtividadeOrdemsForIdAtividade()){
				atividadeDao.removeAtividadeOrdem(ao);
				aoTobeRemoved.add(ao);
				rmApFromAtToBeDeleted(ao.getAtividadePredecessora());
				
				/*Voltar/remover atividades dos combos*/
				System.out.println(ao.getAtividadePredecessora().getNomeAtividade());
				if(!ao.getAtividadePredecessora().getNomeAtividade().equals("AT_Carimbo")){
					if(!atividadesComboboxPredecessora.contains(ao.getAtividadePredecessora())){
						atividadesComboboxPredecessora.add(ao.getAtividadePredecessora());
					}
					atividadesCombobox.remove(ao.getAtividadePredecessora());
				}
			}
			atividade.getAtividadeOrdemsForIdAtividade().removeAll(aoTobeRemoved);
		}
	}
	
	public void addPredecessora(){
		if(atividadeFinal != null){
			if(atividadePredecessora != null){
				if(atividadeToAddOnFluxograma.getIdAtividade() != atividadePredecessora.getIdAtividade()){
					AtividadeOrdem ao = new AtividadeOrdem();
					ao.setAtividade(atividadeToAddOnFluxograma);
					ao.setAtividadePredecessora(atividadePredecessora);
					
					atividadeDao.addAtividadeOrdem(ao);
					atividadeToAddOnFluxograma.getAtividadeOrdemsForIdAtividade().add(ao);
					
					atividadesComboboxPredecessora.remove(atividadePredecessora);
					atividadesCombobox.add(atividadePredecessora);
					
					atividadeInicialHandling();
					
					/*Construir árvore*/
					root = construirArvore(atividadeFinal, null);
					
					sendMessageToView("Atividade adicionada com sucesso ao fluxograma", FacesMessage.SEVERITY_INFO);
					
				}else{
					sendMessageToView("As atividades são iguais", FacesMessage.SEVERITY_ERROR);
					return;
				}
			}else{
				sendMessageToView("Todas as atividades já foram adicionadas!", FacesMessage.SEVERITY_WARN);
				return;
			}
		}else{
			root = null;
			sendMessageToView("Não há atividade final definida!", FacesMessage.SEVERITY_ERROR);
			return;
		}
		
	}

	public void atividadeInicialHandling(){
		AtividadeOrdem atComPredecessoraCarimbo = null;
		for(AtividadeOrdem ao : atividadeToAddOnFluxograma.getAtividadeOrdemsForIdAtividade()){
			if(ao.getAtividadePredecessora().getHabilidade().getNomeHabilidade().equals("Carimbo")){
				atComPredecessoraCarimbo = ao;
				atividadeDao.removeAtividadeOrdem(atComPredecessoraCarimbo);
				break;
			}
		}
		if(atComPredecessoraCarimbo != null){
		   atividadeToAddOnFluxograma.getAtividadeOrdemsForIdAtividade().remove(atComPredecessoraCarimbo);
		}
		
		AtividadeOrdem newAtividadeOrdem = new AtividadeOrdem();
		newAtividadeOrdem.setAtividade(atividadePredecessora);
		newAtividadeOrdem.setAtividadePredecessora(atividadeInicial);
		atividadeDao.addAtividadeOrdem(newAtividadeOrdem);
		
		atividadePredecessora.getAtividadeOrdemsForIdAtividade().add(newAtividadeOrdem);
	}
	
	public Atividade getAtividadeFinal() {
		for (Atividade atividade : atividades) {
			if (atividade.isAtividadeFinal())
				return atividade;
		}
		return null;
	}
	
	
	/*Messages Handling*/
	public void sendMessageToView(String message,Severity severity) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		contexto.addMessage(null, 
							new FacesMessage(severity, message, null));
	}
	
	/*////////CRUD//////////*/
	public void checkNovAtividade(){
		if(!habilidades.isEmpty()){
			RequestContext request = RequestContext.getCurrentInstance();
			request.execute("PF('addAtividade').show();");
		}else{
			sendMessageToView("Todas as habilidades já foram distribuídas em atividades!", FacesMessage.SEVERITY_WARN);
		}
	}
	
	public void addAtividade() {
		atividade.setProcesso(processo);
		atividade.setNomeAtividade("AT_"+atividade.getHabilidade().getNomeHabilidade());
		atividadeDao.addNovaAtividade(atividade);

		sendMessageToView("Atividade adicionada com sucesso!",FacesMessage.SEVERITY_INFO);
		atividades.add(atividade);
		atividadesComboboxPredecessora.add(atividade);
		habilidades.remove(atividade.getHabilidade());
		
		/*Construir árvore*/
		if (atividadeFinal != null) {
			root = construirArvore(atividadeFinal, null);
		} else {
			root = null;
			sendMessageToView("Não há atividade final definida!", FacesMessage.SEVERITY_ERROR);
		}
		
		removerHabilidadesJaAdicionadas();
		atividade = new Atividade();
	}

	public void loadAtividadeToEdit(Atividade atividade) {
		this.atividadeToEdit = atividade;
		if(!habilidades.contains(atividade.getHabilidade())){
			habilidades.add(atividade.getHabilidade());
		}
	}
	
	public void updateAtividade() {
		atividadeToEdit.setNomeAtividade("AT_" + atividadeToEdit.getHabilidade().getNomeHabilidade());
		atividadeDao.updateAtividade(atividadeToEdit);
		habilidades.remove(atividadeToEdit.getHabilidade());
		
		/*Construir árvore*/
		if (atividadeFinal != null) {
			root = construirArvore(atividadeFinal, null);
		} else {
			root = null;
			sendMessageToView("Não há atividade final definida!", FacesMessage.SEVERITY_ERROR);
		}
		
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Atividade atualizada com sucesso!", null));

		RequestContext request = RequestContext.getCurrentInstance();
		request.execute("PF('editAtividade').hide()");
	}

	public void loadAtividadeToDelete(Atividade atividade) {
		this.atividadeToDelete = atividade;
	}
	
	public void removeAtToDelete(){
		this.atividadeToDelete = null;
	}
	
	public void excluirAtividade(){
		atividadeDao.removeAtividade(atividadeToDelete);
		habilidades.add(atividadeToDelete.getHabilidade());
		atividades.remove(atividadeToDelete);
		atividadesCombobox.remove(atividadeToDelete);
		atividadesComboboxPredecessora.remove(atividadeToDelete);
		
		/*Construir árvore*/
		if (atividadeFinal != null) {
			root = construirArvore(atividadeFinal, null);
		} else {
			root = null;
			sendMessageToView("Não há atividade final definida!", FacesMessage.SEVERITY_ERROR);
		}
		
		sendMessageToView("Atividade deletada com sucesso!", FacesMessage.SEVERITY_INFO);
	}
	
	public void removerHabilidadesJaAdicionadas(){
		for(Atividade atividade : atividades){
			if(habilidades.contains(atividade.getHabilidade())){
				habilidades.remove(atividade.getHabilidade());
			}
		}
		habilidades.remove(atividadeFinal.getHabilidade());
	}
	
	public void getProjetoIdFromUrl() {
		Object idProcesso = HttpRequestUtil.getParameterValueInRequest("idProcesso");
		
		try {
			if (idProcesso != null) {
				this.idProcessoRequest = Integer.parseInt(String.valueOf(idProcesso));
			} else {
				this.idProcessoRequest = 0;
			}

		} catch (NumberFormatException e) {
			this.idProcessoRequest = 0;
		}
	}

	
	/*////////GETTERS AND SETTERS//////////*/
	public List<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(List<Atividade> atividades) {
		this.atividades = atividades;
	}

	public List<Habilidade> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public Atividade getAtividadeToEdit() {
		return atividadeToEdit;
	}

	public void setAtividadeToEdit(Atividade atividadeToEdit) {
		this.atividadeToEdit = atividadeToEdit;
	}

	public Atividade getAtividadeToDelete() {
		return atividadeToDelete;
	}

	public void setAtividadeToDelete(Atividade atividadeToDelete) {
		this.atividadeToDelete = atividadeToDelete;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public Atividade getAtividadeToAddOnFluxograma() {
		return atividadeToAddOnFluxograma;
	}

	public void setAtividadeToAddOnFluxograma(Atividade atividadeToAddOnFluxograma) {
		this.atividadeToAddOnFluxograma = atividadeToAddOnFluxograma;
	}

	public Atividade getAtividadePredecessora() {
		return atividadePredecessora;
	}

	public void setAtividadePredecessora(Atividade atividadePredecessora) {
		this.atividadePredecessora = atividadePredecessora;
	}

	public List<Atividade> getAtividadesCombobox() {
		return atividadesCombobox;
	}

	public void setAtividadesCombobox(List<Atividade> atividadesCombobox) {
		this.atividadesCombobox = atividadesCombobox;
	}

	public List<Atividade> getAtividadesComboboxPredecessora() {
		return atividadesComboboxPredecessora;
	}

	public void setAtividadesComboboxPredecessora(
			List<Atividade> atividadesComboboxPredecessora) {
		this.atividadesComboboxPredecessora = atividadesComboboxPredecessora;
	}

}
