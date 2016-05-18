//////////////////////////////////////////////////////////////////-*-java-*-//
//             // Classroom code for "Tecniche di Programmazione"           //
//   #####     // (!) Giovanni Squillero <giovanni.squillero@polito.it>     //
//  ######     //                                                           //
//  ###   \    // Copying and distribution of this file, with or without    //
//   ##G  c\   // modification, are permitted in any medium without royalty //
//   #     _\  // provided this notice is preserved.                        //
//   |   _/    // This file is offered as-is, without any warranty.         //
//   |  _/     //                                                           //
//             // See: http://bit.ly/tecn-progr                             //
//////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.simulation;

import java.util.*;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Core {
	public int getPazientiSalvati() {
		return pazientiSalvati;
	}

	public int getPazientiPersi() {
		return pazientiPersi;
	}

	int pazientiSalvati = 0;
	int pazientiPersi = 0;

	Queue<Evento> listaEventi = new PriorityQueue<Evento>();
	Map<Integer, Paziente> pazienti = new HashMap<Integer, Paziente>();
	Map<Integer,Dottore> dottori = new HashMap<Integer,Dottore>();
	Queue<Paziente> pazientiInAttesa = new PriorityQueue<Paziente>();
	long inizioSimulazione=0;
	int contatore=0;


	public void aggiungiEvento(Evento e) {
		listaEventi.add(e);
	}
	
	
	public void aggiungiDottore(int id,Dottore e) {
		dottori.put(id,e);
	}

	public void aggiungiPaziente(Paziente p) {
		pazienti.put(p.getId(), p);
	}

	public void passo() {
		Evento e = listaEventi.remove();
		if(contatore==0){
			inizioSimulazione=e.getTempo();
		}
		switch (e.getTipo()) {
		case PAZIENTE_ARRIVA:
			System.out.println("Arrivo paziente:" + e);
			pazientiInAttesa.add(pazienti.get(e.getDato()));
			for(Dottore d1:dottori.values()){
			if(!dottori.isEmpty() && d1.getStato()==Dottore.StatoDottore.IN_PAUSA){
				Dottore d=d1;
				this.aggiungiEvento(new Evento(inizioSimulazione + d.getOraInizioTurno(), Evento.TipoEvento.DOTTORE_INIZIA_TURNO, d.id));
				if(e.getTipo()==Evento.TipoEvento.DOTTORE_INIZIA_TURNO)
					d.setStato(Dottore.StatoDottore.IN_ATTESA);
				this.aggiungiEvento(new Evento(inizioSimulazione + d.getOraInizioTurno() + 8*60, Evento.TipoEvento.DOTTORE_TERMINA_TURNO, d.id));
				if(e.getTipo()==Evento.TipoEvento.DOTTORE_TERMINA_TURNO)
					d.setStato(Dottore.StatoDottore.IN_PAUSA);
			}
			switch (pazienti.get(e.getDato()).getStato()) {
			case BIANCO:
				break;
			case GIALLO:
				this.aggiungiEvento(new Evento(e.getTempo() + 6 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			case ROSSO:
				this.aggiungiEvento(new Evento(e.getTempo() + 1 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			case VERDE:
				this.aggiungiEvento(new Evento(e.getTempo() + 12 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			default:
				System.err.println("Panik!");
			}
			break;
			}
		case PAZIENTE_GUARISCE:
			if (pazienti.get(e.getDato()).getStato() != Paziente.StatoPaziente.NERO) {
				System.out.println("Paziente salvato: " + e);
				pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.SALVO);
				Paziente p=getPaziente(e.getDato());
				p.getD().setStato(Dottore.StatoDottore.IN_ATTESA);
				p.setD(null);
				p.getD().setPazienteAssegnato(null);
				pazientiSalvati++;
			}
			break;
		case PAZIENTE_MUORE:
			if (pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.SALVO) {
				System.out.println("Paziente gi� salvato: " + e);
			} else {
				pazientiPersi++;
				pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.NERO);
				System.out.println("Paziente morto: " + e);
				Paziente p=getPaziente(e.getDato());
				if (pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.IN_CURA) {
					p.getD().setStato(Dottore.StatoDottore.IN_ATTESA);
				}
			}
			break;
		default:
			System.err.println("Panik!");
		}
			while (cura(e.getTempo(),dottori.get(dottoreLibero())))
				;
		}

	private Paziente getPaziente(int dato) {
		for(Paziente p:pazienti.values()){
			if(p.getId()==dato)
				return p;
		}
			return null;
	}

	private Dottore dottoreLibero() {
		for(Dottore d:dottori.values()){
			if(d.pazienteAssegnato!=null && d.getStato()==Dottore.StatoDottore.IN_ATTESA)
				return d;
		}
		return null;
	}


	protected boolean cura(long adesso,Dottore d) {
		if (pazientiInAttesa.isEmpty())
			return false;
		
		Paziente p = pazientiInAttesa.remove();
		if (p.getStato() != Paziente.StatoPaziente.NERO) {
			d.setStato(Dottore.StatoDottore.IN_TURNO);
			d.setPazienteAssegnato(p);
			p.setD(d);
			pazienti.get(p.getId()).setStato(Paziente.StatoPaziente.IN_CURA);
			aggiungiEvento(new Evento(adesso + 30, Evento.TipoEvento.PAZIENTE_GUARISCE, p.getId()));
			System.out.println("Inizio a curare: " + p);
		}

		return true;
	}

	public void simula() {
		while (!listaEventi.isEmpty()) {
			passo();
		}
	}

}
