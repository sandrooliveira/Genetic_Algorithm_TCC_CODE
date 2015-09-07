package br.edu.univas.tcc.fabricaCalcas.model;

// Generated Aug 3, 2015 12:33:54 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Processo generated by hbm2java
 */
@Entity
@Table(name = "processo", catalog = "mydb")
public class Processo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idProcesso;
	private String nomeProcesso;
	private Set<Atividade> atividades = new HashSet<Atividade>(0);


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id_processo", unique = true, nullable = false)
	public Integer getIdProcesso() {
		return this.idProcesso;
	}

	public void setIdProcesso(Integer idProcesso) {
		this.idProcesso = idProcesso;
	}

	@Column(name = "nome_processo", length = 45)
	public String getNomeProcesso() {
		return this.nomeProcesso;
	}

	public void setNomeProcesso(String nomeProcesso) {
		this.nomeProcesso = nomeProcesso;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "processo")
	public Set<Atividade> getAtividades() {
		return this.atividades;
	}

	public void setAtividades(Set<Atividade> atividades) {
		this.atividades = atividades;
	}

}
