package Practica;

import java.util.ArrayList;
import java.util.TreeSet;


public class EstacionBombeo implements Comparable<EstacionBombeo>{
	private double flujo;
	private boolean habilitado;
	private String ubicacion;
	private ArrayList<Double> cantidadContaminantes;
	private TreeSet<String> empresas;

	public EstacionBombeo(String ubicacion){
		this.ubicacion=ubicacion;
		this.habilitado=false;
		this.cantidadContaminantes = new ArrayList<Double>();
		this.empresas=new TreeSet<String>();
	}

	public EstacionBombeo(double flujo, ArrayList<Double> cantidadContaminantes, Empresa empresa){
		this.flujo +=flujo;
		this.cantidadContaminantes=cantidadContaminantes;
		empresas = new TreeSet<String>();
		empresas.add(empresa.getNombre());
	}

	public void establecerDatos(Empresa empresa){
		if(!habilitado){
			this.flujo=empresa.getFlujo();
			this.cantidadContaminantes=empresa.getCantidadContaminantes();
			this.habilitado=true;
		}else{
			this.flujo+=empresa.getFlujo();
			sumarContaminantes(empresa.getCantidadContaminantes());
		}

		this.empresas.add(empresa.getNombre());
	}

	public void sumarDatos(EstacionBombeo estacionBombeo){
		this.flujo+=estacionBombeo.flujo;

		if(this.cantidadContaminantes.size()==0)
			this.cantidadContaminantes=estacionBombeo.getCantidadContaminantes();
		else
			for(int i = 0; i < estacionBombeo.getCantidadContaminantes().size(); i++)
				this.cantidadContaminantes.set(i,(this.cantidadContaminantes.get(i)+estacionBombeo.cantidadContaminantes.get(i)));

		this.empresas.addAll(estacionBombeo.empresas);
	}

	public String mostrarEmpresas(){
		String result = "";
		for(String emp: empresas)
			result+=emp.toString()+"\n";

		return result;
	}

	public String toString(){
		return "estacionBombeo: "+this.ubicacion+" Flujo: "+this.flujo+", Contaminantes: "+cantidadContaminantes.toString();
	}

	private void sumarContaminantes(ArrayList<Double> cantidadContaminantes){
		for(int i = 0; i < cantidadContaminantes.size(); i++)
			this.cantidadContaminantes.set(i, (cantidadContaminantes.get(i)+this.cantidadContaminantes.get(i)));
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public TreeSet<String> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(TreeSet<String> empresas) {
		this.empresas = empresas;
	}

	public double getFlujo() {
		return flujo;
	}

	public void setFlujo(double flujo) {
		this.flujo = flujo;
	}

	public int compareTo(EstacionBombeo other){
		return this.ubicacion.compareTo(other.ubicacion);
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
}
