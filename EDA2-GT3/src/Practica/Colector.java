package Practica;

import java.util.ArrayList;
import java.util.TreeSet;


public class Colector implements Comparable<Colector>{
	private double flujo;
	private boolean habilitado;
	private String ubicacion;
	private ArrayList<Double> cantidadContaminantes;
	private TreeSet<Empresa> empresas;

	public Colector(String ubicacion){
		this.ubicacion=ubicacion;
		this.habilitado=false;
		this.cantidadContaminantes = new ArrayList<Double>();
		this.empresas=new TreeSet<Empresa>();
	}

	public Colector(double flujo, ArrayList<Double> cantidadContaminantes, Empresa empresa){
		this.flujo +=flujo;
		this.cantidadContaminantes=cantidadContaminantes;
		empresas = new TreeSet<Empresa>();
		empresas.add(empresa);
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

		this.empresas.add(empresa);
	}

	public void sumarDatos(Colector colector){
		this.flujo+=colector.flujo;

		if(this.cantidadContaminantes.size()==0)
			this.cantidadContaminantes=colector.getCantidadContaminantes();
		else
			for(int i = 0; i < colector.getCantidadContaminantes().size(); i++)
				this.cantidadContaminantes.set(i,(this.cantidadContaminantes.get(i)+colector.cantidadContaminantes.get(i)));

		this.empresas.addAll(colector.empresas);
	}

	public String mostrarEmpresas(){
		String result = "";
		for(Empresa emp: empresas)
			result+=emp.toString()+"\n";

		return result;
	}

	public String toString(){
		return "Colector: "+this.ubicacion+" Flujo: "+this.flujo+", Contaminantes: "+cantidadContaminantes.toString();
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

	public TreeSet<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(TreeSet<Empresa> empresas) {
		this.empresas = empresas;
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

	public int compareTo(Colector other){
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
