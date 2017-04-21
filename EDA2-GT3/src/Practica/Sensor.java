package Practica;

import java.util.HashMap;
import java.util.Map.Entry;

public class Sensor implements Comparable<Sensor>{

	private String nombre;
	private Double flujo;
	private HashMap<String,Double> concentracion;
	//Referencia al sensor anterior no nulo si lo hay
	private Sensor fantasma = null;

	public Sensor(){
		concentracion= new HashMap<String,Double>();
		flujo = 0.0;
	}

	public Sensor(String nombre){
		this.nombre = nombre;
		concentracion = new HashMap<String,Double>();
		flujo=0.0;
	}

	public Sensor(String nombre, Double flujo, HashMap<String,Double> concentracion){
		this.nombre=nombre;
		this.flujo=flujo;
		this.concentracion=concentracion;
	}

	//Añade los datos a un sensor ya creado y si tiene datos los suma
	public void addDatos(Double flujo, HashMap<String,Double> concentracion){
		double flujo1, cont1, cont2;
		flujo1=this.flujo;
		if(flujo!=0.0 || flujo1!=0.0){
			for(Entry<String, Double> it: concentracion.entrySet()){
				cont2 = it.getValue()*flujo;
				Double concentracion2 =  this.concentracion.get(it.getKey());
				cont1 = (concentracion2==null?0:concentracion2)*flujo1;
				this.concentracion.put(it.getKey(), (cont1+cont2)/(flujo1+flujo));
			}
			this.flujo = flujo1+flujo;
		}
	}


	//Para sumar dos sensores
	public void sumarValoresSensores(Sensor sensor){
		Double flujo1, flujo2, cont1, cont2;
		flujo2=sensor.getFlujo();
		flujo1=this.flujo;
		if(flujo1==0.0 && !this.nombre.startsWith("A")){
			this.addDatos(flujo2, sensor.concentracion);
			if(sensor.getFantasma()!=null)
				this.setFantasma(sensor.getFantasma());
			else
				this.setFantasma(sensor);
		}else{
			if(flujo2!=0.0 || flujo1!=0.0){
				for(Entry<String, Double> it: sensor.concentracion.entrySet()){
					cont2 = it.getValue()*flujo2;
					Double concentracion2 =  this.concentracion.get(it.getKey());
					cont1 = (concentracion2==null?0:concentracion2)*flujo1;
					this.concentracion.put(it.getKey(), (cont1+cont2)/(flujo1+flujo2));
				}
				this.flujo = flujo1+flujo2;

			}
		}
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

	public Sensor getFantasma() {
		return fantasma;
	}

	public void setFantasma(Sensor fantasma) {
		this.fantasma = fantasma;
	}

	public HashMap<String,Double> getConcentracion() {
		return concentracion;
	}

	public void setConcentracion(HashMap<String,Double> concentracion) {
		this.concentracion = concentracion;
	}

	// Comprueba si se supera un limite en el sensor,
	// 0 es que no hay peligo, 1 que supera el limite de desvio, 2 que supera el limite critico
	public int comprobarEstado(HashMap<String,Double[]> limites){
		Double[] nivel;

		//Si no tiene datos ese sensor
		if(this.concentracion.size() == 0)
			return 0;

		for(Entry<String, Double[]> cont:limites.entrySet()){
			nivel = cont.getValue();
			if(this.concentracion.get(cont.getKey()) >= nivel[1])
				return 2;
			else if(this.concentracion.get(cont.getKey()) >= nivel[0])
				return 1;
		}

		return 0;
	}

	// Comprueba si se supera un limite en el sensor,
	// 0 es que no hay peligo, 1 que supera el limite de desvio, 2 que supera el limite critico
	public String comprobarAlerta(HashMap<String,Double[]> limites){
		Double[] nivel;
		String result = "";
		for (Entry<String, Double> cont : this.concentracion.entrySet()) {
			nivel = limites.get(cont.getKey());
			if(cont.getValue()>=nivel[1])
				result += cont.getKey()+" R ";
			else if(cont.getValue()>=nivel[0])
				result += cont.getKey()+" N ";
			else if(cont.getValue()>=nivel[0]*0.25)
				result += cont.getKey()+" A ";
		}

		return result.length()==0?null:result;
	}


	//Devuelve los contaminantes que superan un limite en un sensor
	public HashMap<String, Double[]> determinarContaminantes(HashMap<String,Double[]> limites){
		HashMap<String, Double[]> result = new HashMap<String, Double[]>(limites);

		for (Entry<String, Double> it : this.concentracion.entrySet()) {
			Double[] nivel = limites.get(it.getKey());
			if(it.getValue()<nivel[0]){
				result.remove(it.getKey());
			}
		}

		return result;
	}

	@Override
	public int compareTo(Sensor o) {
		return this.nombre.compareTo(o.getNombre());
	}

	public String toString(){
		String result = "Sensor "+this.nombre+" con Flujo: "+this.flujo+" Contaminantes: [";
		for(Entry<String, Double> it: concentracion.entrySet())
			result += it.toString()+" ";
		return result+"]";
	}

	public void addContaminantes(String key, double d) {
		concentracion.put(key, d);
	}
}