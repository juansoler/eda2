package Practica;

import java.util.ArrayList;

public class Empresa {

	private String nombre;
	private boolean direccionVertido;
	private double flujo;
	private ArrayList<Double> cantidadContaminantes;
	private ArrayList<Double> concentracionContaminantes;



	public ArrayList<Double> getCantidadContaminantes() {
		return cantidadContaminantes;
	}

	public void setCantidadContaminantes(ArrayList<Double> cantidadContaminantes) {
		this.cantidadContaminantes = cantidadContaminantes;
	}

	public Empresa(String nombre, String direccionVertido, double flujo, ArrayList<Double> concentracion){
		this.nombre=nombre;
		this.direccionVertido = direccionVertido.equals("R") ? true: false;
		this.flujo=flujo;
		this.concentracionContaminantes=concentracion;
		this.cantidadContaminantes=new ArrayList<Double>();
		for(int i = 0; i < concentracion.size(); i++)
			cantidadContaminantes.add(i, flujo*concentracion.get(i));
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isDireccionVertido() {
		return direccionVertido;
	}

	public void setDireccionVertido(boolean direccionVertido) {
		this.direccionVertido = direccionVertido;
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
		if(direccionVertido)
			result+= ", derecha";
		else
			result+= ", izquierda";

		result += ", flujo: "+ flujo+ ", Concentracion de contaminantes "
		+concentracionContaminantes.toString()+ ", Cantidad de contaminantes "+cantidadContaminantes.toString();
		return result;

	}

	public double getFlujo() {
		return flujo;
	}

	public void setFlujo(double flujo) {
		this.flujo = flujo;
	}

	public ArrayList<Double> getConcentracionContaminantes() {
		return concentracionContaminantes;
	}

	public void setConcentracionContaminantes(ArrayList<Double> concentracionContaminantes) {
		this.concentracionContaminantes = concentracionContaminantes;
	}

}
