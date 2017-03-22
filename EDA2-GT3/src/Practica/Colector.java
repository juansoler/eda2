package Practica;

import java.util.ArrayList;



public class Colector {
	private double flujo;
	private boolean habilitado;
	private String ubicacion;
	private ArrayList<Double> cantidadContaminantes;
	private ArrayList<Double> concentracionContaminantes;
	private ArrayList<Empresa> empresas;

	public Colector(String ubicacion){
		this.ubicacion=ubicacion;
		setHabilitado(false);
		setCantidadContaminantes(new ArrayList<Double>());
		setConcentracionContaminantes(new ArrayList<Double>());
		empresas=new ArrayList<Empresa>();
	}

	public Colector(double flujo, ArrayList<Double> cantidadContaminantes, ArrayList<Double> concentracionContaminantes, Empresa empresa){
		this.flujo +=flujo;
		this.setCantidadContaminantes(cantidadContaminantes);
		this.setConcentracionContaminantes(concentracionContaminantes);
		empresas = new ArrayList<Empresa>();
		empresas.add(empresa);
	}

	public void establecerDatos(double flujo, ArrayList<Double> cantidadContaminantes, ArrayList<Double> concentracionContaminantes, Empresa empresa){
		if(!habilitado){
			this.flujo=flujo;
			this.cantidadContaminantes=cantidadContaminantes;
			this.concentracionContaminantes=concentracionContaminantes;
			this.empresas.add(empresa);
			this.habilitado=true;
		}else{
			this.flujo+=flujo;
			sumarContaminantes(cantidadContaminantes, concentracionContaminantes);
		}
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public ArrayList<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(ArrayList<Empresa> empresas) {
		this.empresas = empresas;
	}

	private void sumarContaminantes(ArrayList<Double> cantidadContaminantes, ArrayList<Double> concentracionContaminantes){
		for(int i = 0; i < cantidadContaminantes.size();i++)
			this.cantidadContaminantes.set(i, (cantidadContaminantes.get(i)+this.cantidadContaminantes.get(i)));

		for(int i = 0; i < concentracionContaminantes.size();i++)
			this.concentracionContaminantes.set(i, (concentracionContaminantes.get(i)+this.concentracionContaminantes.get(i)));
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
		String result = "Colector: "+this.ubicacion+" Flujo: "+this.flujo;


		return result;
	}

	public boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public ArrayList<Double> getCantidadContaminantes() {
		return cantidadContaminantes;
	}

	public void setCantidadContaminantes(ArrayList<Double> cantidadContaminantes) {
		this.cantidadContaminantes = cantidadContaminantes;
	}

	public ArrayList<Double> getConcentracionContaminantes() {
		return concentracionContaminantes;
	}

	public void setConcentracionContaminantes(ArrayList<Double> concentracionContaminantes) {
		this.concentracionContaminantes = concentracionContaminantes;
	}
}
