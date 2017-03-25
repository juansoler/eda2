package Practica;

import java.util.ArrayList;

public class Alarma {

	private ArrayList<Double> limiteCritico;
	private ArrayList<Double> limiteDesvio;

	public Alarma(ArrayList<Double> limiteCritico,ArrayList<Double> limiteDesvio){
		this.limiteCritico=limiteCritico;
		this.limiteDesvio=limiteDesvio;
	}

	public String detectar(Colector colector){
		String result = "";
		for(int i = 0; i < colector.getCantidadContaminantes().size(); i++){
			if(colector.getCantidadContaminantes().get(i)/colector.getFlujo() >= limiteCritico.get(i))
				result += "Limite crítico del contaminante"+i+" en el colector: "+ colector.getUbicacion()+"\n";
			else if(colector.getCantidadContaminantes().get(i)/colector.getFlujo() >= limiteDesvio.get(i))
				result += "Limite desvio contaminante"+i+" en el colector: "+ colector.getUbicacion()+"\n";
		}

		if(result.length()==0)
			return "No hay riesgo de contaminación";

		return result;
	}

	public String toString(){
		return "Limites críticos: "+limiteCritico.toString()+"\nLimites de desvio: "+limiteDesvio.toString();
	}
}