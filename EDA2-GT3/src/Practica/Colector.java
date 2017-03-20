package Practica;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import Practica.Alarma;


public class Colector {
	private double flujo;
	private ArrayList<Double> contaminantes;

	private TreeMap<String, ColectorSecundario> colectorSec = new TreeMap<String, ColectorSecundario>();


	public Colector(String colector, double flujo, ArrayList<Double> contaminantes){
		this.flujo +=flujo;
		this.contaminantes = contaminantes;
		colectorSec.put(colector, new ColectorSecundario(flujo, contaminantes));

	}

	public Colector() {
		// TODO Auto-generated constructor stub
	}

	public double getFlujo() {
		return flujo;
	}

	public void setFlujo(double flujo) {
		this.flujo = flujo;
	}

	public ArrayList<Double> getContaminantes() {
		return contaminantes;
	}

	public void setContaminantes(ArrayList<Double> contaminantes) {
		this.contaminantes = contaminantes;
	}

	public String toString(){
		String result = "Flujo: "+this.flujo+"\n";

		for(int i = 0; i < contaminantes.size(); i++){
			result += "Contaminante "+i+": "+contaminantes.get(i);
			if(i<contaminantes.size()-1)
				result+="\n";
		}

		return result;
	}

	public TreeMap<String, ColectorSecundario> getColectorSec() {
		return colectorSec;
	}

	public void setColectorSec(TreeMap<String, ColectorSecundario> colectorSec) {
		this.colectorSec = colectorSec;
	}
}
