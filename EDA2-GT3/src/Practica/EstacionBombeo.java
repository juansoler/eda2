package Practica;

import java.util.ArrayList;
import java.util.TreeSet;


public class EstacionBombeo{

	private ArrayList<Double> limiteCritico;
	private ArrayList<Double> limiteDesvio;

	public EstacionBombeo(){

	}

	public EstacionBombeo(ArrayList<Double> limiteCritico, ArrayList<Double> limiteDesvio){
		this.limiteCritico=limiteCritico;
		this.limiteDesvio=limiteDesvio;
	}
}
