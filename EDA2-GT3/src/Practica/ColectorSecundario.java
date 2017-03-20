package Practica;

import java.util.ArrayList;
import java.util.TreeMap;

public class ColectorSecundario extends Colector{

//	private ArrayList<Empresa> empresas = new ArrayList<Empresa>();
	private TreeMap<String, Empresa> empresas = new TreeMap<String, Empresa>();


	public ColectorSecundario(){
		super();
	}

	public ColectorSecundario(double flujo, ArrayList<Double> contaminantes) {
		super("",flujo, contaminantes);
	}

	public void addEmpresa(Empresa empresa){
		empresas.put(empresa.getNombre(), empresa);
	}

	public TreeMap<String, Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(TreeMap<String, Empresa> empresas) {
		this.empresas = empresas;
	}

	public String toString(){
		String result = super.toString()+"\n";

		for(String  emp : empresas.keySet())
			result += emp+"\n";

		return result;
	}
}
