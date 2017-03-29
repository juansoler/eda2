package Practica;

import java.util.ArrayList;

public class Alarma {

	private ArrayList<Double> limiteCritico;
	private ArrayList<Double> limiteDesvio;

	public Alarma(ArrayList<Double> limiteCritico,ArrayList<Double> limiteDesvio){
		this.limiteCritico=limiteCritico;
		this.limiteDesvio=limiteDesvio;
	}

	public ArrayList<Integer> contaminantesPeligrosos(EstacionMedida estacionMedida, EstacionMedida otra){
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Double> aux = new ArrayList<Double>();
		Double auxFlujo=0.0;
		if (otra != null && !estacionMedida.equals(otra)){
			auxFlujo=(otra.getFlujo()-estacionMedida.getFlujo());
			for(int i = 0; i< estacionMedida.getCantidadContaminantes().size();i++)
				aux.add(otra.getCantidadContaminantes().get(i)-estacionMedida.getCantidadContaminantes().get(i));
		}else{
			auxFlujo = estacionMedida.getFlujo();
			aux = new ArrayList<Double>(estacionMedida.getCantidadContaminantes());
		}

		for(int i = 0; i < aux.size(); i++){
			if(aux.get(i)/auxFlujo >= limiteCritico.get(i))
				result.add(i);
			else if(aux.get(i)/auxFlujo >= limiteDesvio.get(i))
				result.add(i);
		}

		return result;
	}

	public String detectar(EstacionMedida estacionMedida, EstacionMedida otra){
		String result = "";
		ArrayList<Double> aux = new ArrayList<Double>();
		Double auxFlujo=0.0;

		System.out.println("En alarma estacion: "+estacionMedida.toString());
		if(otra != null)
			System.out.println("En alarma otra: "+otra.toString());
		if (otra != null && !estacionMedida.equals(otra)){
			auxFlujo=(otra.getFlujo()-estacionMedida.getFlujo());
			for(int i = 0; i< estacionMedida.getCantidadContaminantes().size();i++)
				aux.add(otra.getCantidadContaminantes().get(i)-estacionMedida.getCantidadContaminantes().get(i));
		}else{
			auxFlujo = estacionMedida.getFlujo();
			aux = new ArrayList<Double>(estacionMedida.getCantidadContaminantes());
		}

		System.out.println("Flujo:"+auxFlujo+" "+aux.toString());

		for(int posicion = 0; posicion<limiteCritico.size();posicion++){
			if(aux.get(posicion)/auxFlujo >= limiteCritico.get(posicion))
				result += "Limite crítico del contaminante"+posicion+" en el colector: "+ estacionMedida.getUbicacion()+"\n";
			else if(aux.get(posicion)/auxFlujo >= limiteDesvio.get(posicion))
				result += "Limite desvio contaminante"+posicion+" en el colector: "+ estacionMedida.getUbicacion()+"\n";
		}

		System.out.println(result);
		if(result.length()==0)
			return null;

		return result;
	}
	
//	private void restarEstaciones(ArrayList<Double> cantidadContaminantes){
//		for(int i = 0; i < cantidadContaminantes.size(); i++)
//			this.cantidadContaminantes.set(i, (cantidadContaminantes.get(i)+this.cantidadContaminantes.get(i)));
//	}

	public String toString(){
		return "Limites críticos: "+limiteCritico.toString()+"\nLimites de desvio: "+limiteDesvio.toString();
	}
}