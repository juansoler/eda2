package Practica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class Sensor {

	private String nombre;
	private Double flujo;
	private HashMap<String,Double> concentracion;

//	public HashMap<Integer, Integer> getLimites() {
//		return limites;
//	}
//
//	public void setLimites(HashMap<Integer, Integer> limites) {
//		this.limites = limites;
//	}

	public Sensor(){
		concentracion= new HashMap<String,Double>();
	}

	public Sensor(String nombre, Double flujo, HashMap<String,Double> concentracion){
		this.nombre=nombre;
		this.flujo=flujo;
		this.concentracion=concentracion;
		//limites = new HashMap<Integer,Integer>();
	}

	public void addContaminantes(String contaminante, Double cantidad){
		this.concentracion.put(contaminante, cantidad);
	}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getFlujo() {
		return flujo;
	}

	public void setFlujo(Double flujo) {
		this.flujo = flujo;
	}

	public HashMap<String,Double> getConcentracion() {
		return concentracion;
	}

	public void setConcentracion(HashMap<String,Double> concentracion) {
		this.concentracion = concentracion;
	}

	// 0 = sin peligo, 1 = limite desvio, 2 = limite critico
	public int comprobarEstado(Alarma alarm){
		for (Entry<String, Double> it : this.concentracion.entrySet()) {
			Double[] nivel = alarm.getLimites().get(it.getKey());
			if(it.getValue()>=nivel[0]){
				return 2;
			}else if(it.getValue()>=nivel[1]){
				return 1;
			}
		}
		return 0;
	}

	public HashSet<String> determinarContaminantes(Alarma alarm){
		HashSet<String> result = new HashSet<String>();
		for (Entry<String, Double> it : this.concentracion.entrySet()) {
			Double[] nivel = alarm.getLimites().get(it.getKey());
			if(it.getValue()>=nivel[1]){
				result.add(it.getKey());
			}
		}
		return result.size()==0?null:result;
	}
}
