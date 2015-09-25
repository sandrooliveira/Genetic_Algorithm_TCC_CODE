package br.edu.univas.tcc.fabricaCalcas.controller;

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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.edu.univas.tcc.fabricaCalcas.dao.AtividadeDAO;
import br.edu.univas.tcc.fabricaCalcas.dao.HabilidadeDAO;
import br.edu.univas.tcc.fabricaCalcas.dao.ProcessoDAO;
import br.edu.univas.tcc.fabricaCalcas.model.Atividade;
import br.edu.univas.tcc.fabricaCalcas.model.AtividadeOrdem;
import br.edu.univas.tcc.fabricaCalcas.model.Habilidade;
import br.edu.univas.tcc.fabricaCalcas.model.Processo;

@ManagedBean(name = "processosAtividadesController")
@ViewScoped
public class ProcessosAtividadesController {

	private Habilidade habilidade;
	public static List<Habilidade> habilidades;
	private List<Atividade> atividades;
	private Atividade atividade;
	private Atividade atividadeToEdit;
	private Atividade atividadeToDelete;

	HabilidadeDAO habDao;
	AtividadeDAO atDao;
	ProcessoDAO processDao;

	private int idProcessoRequest = 80;
	private Processo processo;

	private TreeNode root;

	@PostConstruct
	public void init() {

		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("db_pu");
		EntityManager manager = factory.createEntityManager();

		habDao = new HabilidadeDAO(manager);
		atDao = new AtividadeDAO(manager);
		processDao = new ProcessoDAO(manager);

		processo = processDao.getProcessoByID(idProcessoRequest);
		atividades = atDao.listAtividadesByProcesso(idProcessoRequest);
		habilidades = habDao.listAllHabilidades();
		atividade = new Atividade();

		Atividade atividadeFinal = getAtividadeFinal();
		if (atividadeFinal != null) {
			root = newNodeWithChildren(atividadeFinal, null);
			Atividade teste = (Atividade) root.getData();
			System.out.println(teste.getNomeAtividade());
		} else {
			// TODO: Colocar msg de erro
		}
	}

	public TreeNode newNodeWithChildren(Atividade ttParent, TreeNode parent) {
		TreeNode newNode = new DefaultTreeNode(ttParent, parent);
		for (AtividadeOrdem tt : ttParent.getAtividadeOrdemsForIdAtividade()) {
			TreeNode newNode2 = newNodeWithChildren(
					tt.getAtividadePredecessora(), newNode);
		}
		return newNode;
	}

	public void addAtividade() {
		// Habilidade habilidade = habDao.selectHabilidadeById(idHabilidade);
		atividade.setProcesso(processo);
		atDao.addNovaAtividade(atividade);

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Atividade adicionada com sucesso!", null));
		atividades.add(atividade);
		habilidades.remove(atividade.getHabilidade());
		atividade = new Atividade();
	}

	public Habilidade getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(Habilidade habilidade) {
		this.habilidade = habilidade;
	}

	public void updateAtividade() {
		// Habilidade habilidade = habDao.selectHabilidadeById(idHabilidade);
		atDao.updateAtividade(atividadeToEdit);

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Atividade atualizada com sucesso!", null));

		RequestContext request = RequestContext.getCurrentInstance();
		request.execute("PF('editAtividade').hide()");
	}

	public Atividade getAtividadeFinal() {
		for (Atividade atividade : atividades) {
			if (atividade.isAtividadeFinal())
				return atividade;
		}
		return null;
	}

	public void loadAtividadeToEdit(Atividade atividade) {
		this.atividadeToEdit = atividade;
		habilidade = atividade.getHabilidade();
	}

	public void loadAtividadeToDelete(Atividade atividade) {
		this.atividadeToDelete = atividade;
	}

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

}
