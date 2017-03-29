package Practica;

import java.util.ArrayList;

public class Empresa implements Comparable<Empresa>{

	private String nombre;
	private boolean direccionVertido;
	private double flujo;
	private ArrayList<Double> cantidadContaminantes;


	public ArrayList<Double> getCantidadContaminantes() {
		return cantidadContaminantes;
	}

	public void setCantidadContaminantes(ArrayList<Double> cantidadContaminantes) {
		this.cantidadContaminantes = cantidadContaminantes;
	}

	public Empresa(String nombre, String direccionVertido, double flujo, ArrayList<Double> concentracion){
		this.nombre=nombre;
		this.direccionVertido = direccionVertido.equalsIgnoreCase("R") ? true: false;
		this.flujo=flujo;
		this.cantidadContaminantes= new ArrayList<Double>();
		for(int i = 0; i< concentracion.size();i++)
			this.cantidadContaminantes.add(concentracion.get(i)*flujo);
	}

	public String calcularUbicacion(){
		String ubicacion = "Avenida "+ this.nombre.charAt(0);
		if(ubicacion.charAt(1)=='N')
			ubicacion += ", Zona Norte, Calle "+this.nombre.charAt(2);
		else
			ubicacion += ", Zona Sur, Calle "+this.nombre.charAt(2);

		return ubicacion;
	}

	public String colectorVertido(){
		String ubicacion = "Avenida "+ this.nombre.subSequence(0, 1);
		if(direccionVertido)
			ubicacion += " "+(Integer.parseInt(this.nombre.charAt(this.nombre.length()-1)+"")+1);
		else
			ubicacion += " "+this.nombre.charAt(this.nombre.length()-1);

		return ubicacion;
	}

	public String toString(){
		String result = nombre;
//		if(direccionVertido)
//			result+= ", derecha";
//		else
//			result+= ", izquierda";
//
//		result += ", flujo: "+ flujo +", Cantidad de contaminantes "+cantidadContaminantes.toString();
		return result;

	}

	public int compareTo(Empresa other) {
		return this.nombre.compareTo(other.nombre);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean getDireccionVertido() {
		return direccionVertido;
	}

	public void setDireccionVertido(boolean direccionVertido) {
		this.direccionVertido = direccionVertido;
	}

	public double getFlujo() {
		return flujo;
	}

	public void setFlujo(double flujo) {
		this.flujo = flujo;
	}
}
