package br.edu.univas.tcc.fabricaCalcas.ga_code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.edu.univas.tcc.fabricaCalcas.dao.CostureirasDAO;
import br.edu.univas.tcc.fabricaCalcas.model.Atividade;
import br.edu.univas.tcc.fabricaCalcas.model.CostureiraHabilidade;
import br.edu.univas.tcc.ga_core.Chromosome;
import br.edu.univas.tcc.ga_core.GAModel;
import br.edu.univas.tcc.ga_core.Individual;

public class ProcessoModel extends GAModel {

	private Map<Integer, List<CostureiraHabilidade>> atividadesCostureiras;
	private List<Atividade> atividadesProcesso;
	private Atividade atividadeFinal;
	private EntityManager manager;
	private long processo = 1;

	public ProcessoModel(EntityManager manager, long processo) {
		this.manager = manager;
		this.processo = processo;
		getInformacoesCostureiras();
	}

	@Override
	public void createInitialPopulation() {
		for (int i = 0; i < getPopulationSize(); i++) {
			population.add(new ProcessoIndividual(atividadeFinal,
					atividadesCostureiras));
		}
	}

	@Override
	public Individual createIndividual(ArrayList<Chromosome> chromosomes) {
		return new ProcessoIndividual(atividadeFinal, chromosomes);
	}

	@Override
	public Individual createIndividual() {
		return new ProcessoIndividual(atividadeFinal, atividadesCostureiras);
	}

	@SuppressWarnings("unchecked")
	public void getInformacoesCostureiras() {
		if (atividadesCostureiras != null && atividadesProcesso != null) {
			atividadesCostureiras.clear();
			atividadesProcesso.clear();
		}

		Query query = manager
				.createQuery("select a from Atividade as a where a.processo.idProcesso = :idProcesso");
		query.setParameter("idProcesso", 2);

		CostureirasDAO cdao = new CostureirasDAO(manager);

		atividadesCostureiras = new HashMap<Integer, List<CostureiraHabilidade>>();
		List<CostureiraHabilidade> costureirasHabilidades;

		// Montar um MAP tendo como chave cada atividade do processo e a lista
		// de costureiras que tenham
		// a habilidade relacionada.
		atividadesProcesso = query.getResultList();

		for (Atividade atividade : atividadesProcesso) {
			costureirasHabilidades = cdao.getCostureirasByHabilidade(atividade
					.getHabilidade().getIdHabilidade());
			atividadesCostureiras.put(atividade.getIdAtividade(),costureirasHabilidades);
			atividadeFinal = atividade.isAtividadeInicial() == 1 ? atividade: null;
		}
	}

	public Atividade getAtividadeInicial() {
		return atividadeFinal;
	}

	public void setAtividadeInicial(Atividade atividadeInicial) {
		this.atividadeFinal = atividadeInicial;
	}

	public long getProcesso() {
		return processo;
	}

	public void setProcesso(long processo) {
		this.processo = processo;
	}

}
