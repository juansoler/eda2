package Practica;

import java.util.ArrayList;
import java.util.TreeSet;


public class EstacionMedida implements Comparable<EstacionMedida>{
	private double flujo;
	private boolean habilitado;
	private String ubicacion;
	private ArrayList<Double> cantidadContaminantes;
	private TreeSet<String> empresas;

	public EstacionMedida(String ubicacion){
		this.ubicacion=ubicacion;
		this.habilitado=false;
		this.cantidadContaminantes = new ArrayList<Double>();
		this.empresas=new TreeSet<String>();
	}

	public EstacionMedida(double flujo, ArrayList<Double> cantidadContaminantes, Empresa empresa){
		this.flujo +=flujo;
		for(int i = 0; i< cantidadContaminantes.size();i++)
			this.cantidadContaminantes.set(i,cantidadContaminantes.get(i)*flujo);
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

	public void sumarDatos(EstacionMedida estacionMedida){
		this.flujo+=estacionMedida.flujo;

		if(this.cantidadContaminantes.size()==0)
			this.cantidadContaminantes=estacionMedida.getCantidadContaminantes();
		else
			for(int i = 0; i < estacionMedida.getCantidadContaminantes().size(); i++)
				this.cantidadContaminantes.set(i,(this.cantidadContaminantes.get(i)+estacionMedida.cantidadContaminantes.get(i)));

		this.empresas.addAll(estacionMedida.empresas);
	}

	public String mostrarEmpresas(){
		String result = "";
		for(String emp: empresas)
			result+=emp.toString()+"\n";

		return result;
	}

	public String toString(){
		return "Estacion de Medida: "+this.ubicacion+" Flujo: "+this.flujo+", Contaminantes: "+cantidadContaminantes.toString()+" empresas: "+this.empresas.toString();
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

	public int compareTo(EstacionMedida other){
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
