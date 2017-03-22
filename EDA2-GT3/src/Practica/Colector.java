package Practica;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import Practica.Alarma;


public class Colector {
	private double flujo;
	private ArrayList<Double> cantidadContaminantes;
	private ArrayList<Double> concentracionContaminantes;
	private ArrayList<Empresa> empresas;

	public Colector(double flujo, ArrayList<Double> cantidadContaminantes, ArrayList<Double> concentracionContaminantes, Empresa empresa){
		this.flujo +=flujo;
		this.cantidadContaminantes=cantidadContaminantes;
		this.concentracionContaminantes=concentracionContaminantes;
		empresas = new ArrayList<Empresa>();
		empresas.add(empresa);
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

	public void addEmpresa(Empresa empresa){
		empresas.add(empresa);
	}

	public String toString(){
		String result = "Flujo: "+this.flujo+"\n";


		return result;
	}
}
