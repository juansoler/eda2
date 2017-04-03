package Practica;

import java.util.ArrayList;
import java.util.HashMap;

public class Alarma {

	private HashMap<String, Double[]> limites;
//
	public Alarma(String nCont, Double[] limites){
		this.limites = new HashMap<String, Double[]>();
		this.limites.put(nCont, limites);
	}

	public HashMap<String, Double[]> getLimites() {
		return limites;
	}

	public void setLimites(HashMap<String, Double[]> limites) {
		this.limites = limites;
	}


}