package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.BadPaddingException;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> esami;
	private double bestMedia;
	private Set<Esame> bestSoluzione;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
		this.bestMedia = 0.0;
		this.bestSoluzione = null;
	}
	
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set<Esame> parziale = new HashSet<Esame>();
		cerca(parziale, 0, numeroCrediti);
		return this.bestSoluzione;
	}
	
	
	// complessità 2^N
	private void cerca(Set<Esame> parziale, int L, int m) {
		// terminazaione
		int crediti = sommaCrediti(parziale);
		if(crediti > m)
			return;
		
		// controllo soluzione ottima
		if(crediti == m) {
			double media = calcolaMedia(parziale);
			if(media > this.bestMedia) {
				this.bestMedia = media;
				this.bestSoluzione = new HashSet<>(parziale);
			}
		}
		
		// finiti esami e crediti < m
		if(L == this.esami.size())
			return;
		
		// prova ad aggiungerlo
		parziale.add(this.esami.get(L));
		cerca(parziale, L+1, m);
		parziale.remove(this.esami.get(L));
		
		// provo a non aggiungerlo
		cerca(parziale, L+1, m);
		
	}
	
	private int sommaCrediti(Set<Esame> parziale) {
		int somma = 0;
		for(Esame e : parziale) {
			somma += e.getCrediti();
		}
		return somma;
	}
	
	public double calcolaMedia(Set<Esame> parziale) {
		int crediti = 0;
		int somma = 0;
		
		for(Esame e: parziale) {
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		return somma/crediti;
		
	}

}
