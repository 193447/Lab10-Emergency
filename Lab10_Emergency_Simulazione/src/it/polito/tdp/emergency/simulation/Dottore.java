package it.polito.tdp.emergency.simulation;


public class Dottore {
	
	public enum StatoDottore {
		IN_TURNO, IN_PAUSA, IN_ATTESA
	};

	private StatoDottore stato;
	
	int id;
	String nome;
	int oraInizioTurno;
	Paziente pazienteAssegnato=null;
	

	public Dottore(int id,String nome, int oraInizioTurno) {
		super();
		this.id=id;
		this.nome = nome;
		this.oraInizioTurno = oraInizioTurno;
	}

	public int getOraInizioTurno() {
		return oraInizioTurno;
	}

	public void setOraInizioTurno(int oraInizioTurno) {
		this.oraInizioTurno = oraInizioTurno;
	}

	public String getNome() {
		return nome;
	}
	
	public Paziente getPazienteAssegnato() {
		return pazienteAssegnato;
	}

	public void setPazienteAssegnato(Paziente pazienteAssegnato) {
		this.pazienteAssegnato = pazienteAssegnato;
	}

	public StatoDottore getStato() {
		return stato;
	}

	public void setStato(StatoDottore stato) {
		this.stato = stato;
	}
	
	
	
	
	

}
