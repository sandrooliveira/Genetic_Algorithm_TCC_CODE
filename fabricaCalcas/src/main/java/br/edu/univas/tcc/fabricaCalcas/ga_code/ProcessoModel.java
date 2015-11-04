package br.edu.univas.tcc.fabricaCalcas.ga_code;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import br.edu.univas.tcc.fabricaCalcas.dao.AtividadeDAO;
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
	private int numeroLote;
	private int pecasPorLote;
	private int processo;
	private BigDecimal prazoEmSegundos;
	
	private CostureirasDAO cdao;
	private AtividadeDAO atividadeDao;

	public ProcessoModel(EntityManager manager, int processo) {
		cdao = new CostureirasDAO(manager);
		atividadeDao = new AtividadeDAO(manager);
		
		this.processo = processo;
		getInformacoesCostureiras();
	}

	@Override
	public void createInitialPopulation() {
		for (int i = 0; i < getPopulationSize(); i++) {
			population.add(new ProcessoIndividual(atividadeFinal, prazoEmSegundos, atividadesCostureiras,
													this.numeroLote, this.pecasPorLote));
		}
	}

	@Override
	public Individual createIndividual(ArrayList<Chromosome> chromosomes) {
		return new ProcessoIndividual(atividadeFinal, prazoEmSegundos, chromosomes,this.numeroLote,this.pecasPorLote);
	}

	@Override
	public Individual createIndividual() {
		return new ProcessoIndividual(atividadeFinal, prazoEmSegundos, atividadesCostureiras,this.numeroLote,this.pecasPorLote);
	}

	public void getInformacoesCostureiras() {
		List<CostureiraHabilidade> costureirasHabilidades = null;
		
		if (atividadesCostureiras != null && atividadesProcesso != null) {
			atividadesCostureiras.clear();
			atividadesProcesso.clear();
		}

		atividadesCostureiras = new HashMap<Integer, List<CostureiraHabilidade>>();
		atividadesProcesso = atividadeDao.listAtividadesByProcesso(processo);

		/* Montar um MAP tendo como chave cada atividade do processo e a lista
		 de costureiras que tenham a habilidade relacionada.*/
		for (Atividade atividade : atividadesProcesso) {
			costureirasHabilidades = cdao.getCostureirasByHabilidade(atividade.getHabilidade().getIdHabilidade());
			atividadesCostureiras.put(atividade.getIdAtividade(),costureirasHabilidades);
			if (atividade.isAtividadeFinal()) atividadeFinal = atividade;
		}
	}

	public Atividade getAtividadeInicial() {
		return atividadeFinal;
	}

	public void setAtividadeInicial(Atividade atividadeInicial) {
		this.atividadeFinal = atividadeInicial;
	}

	public int getNumeroLote() {
		return numeroLote;
	}

	public void setNumeroLote(int numeroLote) {
		this.numeroLote = numeroLote;
	}

	public int getPecasPorLote() {
		return pecasPorLote;
	}

	public void setPecasPorLote(int pecasPorLote) {
		this.pecasPorLote = pecasPorLote;
	}

	public int getProcesso() {
		return processo;
	}

	public void setProcesso(int processo) {
		this.processo = processo;
	}

	public BigDecimal getPrazoEmSegundos() {
		return prazoEmSegundos;
	}

	public void setPrazoEmSegundos(BigDecimal prazoEmSegundos) {
		this.prazoEmSegundos = prazoEmSegundos;
	}
	
}
