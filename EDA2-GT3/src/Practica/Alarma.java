package Practica;

import java.util.ArrayList;

public class Alarma {

	private ArrayList<Double> limiteCritico;
	private ArrayList<Double> limiteDesvio;

	public Alarma(ArrayList<Double> limiteCritico,ArrayList<Double> limiteDesvio){
		this.limiteCritico=limiteCritico;
		this.limiteDesvio=limiteDesvio;
	}

	public String detectar(double flujo, ArrayList<Double> contaminantes){
		String result = "";
		for(int i = 0; i < contaminantes.size(); i++){
			if(contaminantes.get(i)/flujo >= limiteCritico.get(i))
				result += "Limite crítico contaminante"+i;
			else if(contaminantes.get(i)/flujo >= limiteDesvio.get(i))
				result += "Limite desvio contaminante"+i;
			result+="\n";
		}

		if(result.length()==0)
			return "No hay riesgo de contaminación";

		return result;
	}

	public String toString(){
		return "Limites críticos: "+limiteCritico.toString()+"\nLimites de desvio: "+limiteDesvio.toString();
	}
}