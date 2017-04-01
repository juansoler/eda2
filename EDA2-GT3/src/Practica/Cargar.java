package Practica;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;

public class Cargar {

	public static Sensor[][] matrizSensores = null;
	public static HashMap<String, HashSet<String>> sensorEmpresas = new HashMap<String, HashSet<String>>();
	public static Alarma alarma = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String directorioEntrada = "";

		directorioEntrada = System.getProperty("user.dir") + File.separator +
				"src" + File.separator +
				"Practica" + File.separator + "Empresas.txt";

		//Cargar las estructuras utilizadas
		loadFile(directorioEntrada);

		for(int i = 0; i < matrizSensores.length;i++){
			for(int j = 0; j < matrizSensores[i].length;j++){
				System.out.print(matrizSensores[i][j].getNombre()+"  ");
			}
			System.out.println();
		}
		for(Entry<String, Double[]> al: alarma.getLimites().entrySet()){
		System.out.println(al.getKey()+" "+al.getValue()[0]+" "+al.getValue()[1]);

		}
		
		
		for(Entry<String, HashSet<String>> sensor: sensorEmpresas.entrySet())
			System.out.println("Al sensor: "+sensor.getKey()+" le viernten: "+sensor.getValue().toString());
		
		//System.out.println(sensorEmpresas.get("DN2").toString());
		FuerzaBruta(matrizSensores);
	}

	private static void loadFile(String file) {
		// Para los detalles sobre formato del archivo de entrada, ver archivo
		// de ejemplo (datos)

		Scanner sc = null;
		boolean leerSensores = false, leerLimites = false, leerEmpresas = false;
		Sensor sensor = null;
		int nAvenidas=0;
		int nCalles=0;
		int posFila=0, posCol=0, avenidaEmpresas=0, calleEmpresas=0;
		HashMap<String,Double> mgContaminantesPorLitro;
		Double[] limites;
		Double flujo;
		String line;
		String[] empresas;

		try {
			sc = new Scanner(new File(file));
			while (sc.hasNextLine()) {

				line = sc.nextLine();

				if (line.startsWith("%") || line.isEmpty()) continue;

				if (line.startsWith("@Avenidas")) {
					nAvenidas = Integer.parseInt(sc.nextLine())-2;
				}else if (line.startsWith("@Calle")) {
					nCalles = Integer.parseInt(sc.nextLine());
					matrizSensores = new Sensor[nAvenidas][nCalles];
				}
				else if (line.startsWith("@Sensor")) {
					leerSensores = true;
					leerLimites = false;
					leerEmpresas = false;
					continue;
				} else if (line.startsWith("@Limites")) {
					leerSensores = false;
					leerLimites = true;
					leerEmpresas = false;
					continue;
				} else if (line.startsWith("@Empresas")) {
					leerSensores = false;
					leerLimites = false;
					leerEmpresas = true;
					continue;
				}

				String[] items = line.split(" ");

				if (leerSensores) {
					mgContaminantesPorLitro = new HashMap<String,Double>();

					for(int i = 2, cont = 0; i < items.length;i++, cont++){
						mgContaminantesPorLitro.put("Contaminante"+cont, Double.parseDouble(items[i]));
					}

					flujo = Double.parseDouble(items[1]);

					sensor = new Sensor(items[0],flujo,mgContaminantesPorLitro);
					//sensor.comprobarEstado(alarma);
					if(posCol == nCalles){
						posCol = 0;
						posFila++;
					}
					//Lanzar excepcion
					//if(posFila == nAvenidas){
					//empresas = false;
					//continue;
					//}
					matrizSensores[posFila][posCol++] = sensor;
				}else if(leerLimites){
					limites=new Double[2];
					items = line.split(" ");
					limites[0]=Double.parseDouble(items[1]);
					limites[1]=Double.parseDouble(items[2]);
					if(alarma == null)
						alarma = new Alarma(items[0],limites);
					else
						alarma.getLimites().put(items[0],limites);
				}else if(leerEmpresas){
					HashSet<String> emp;
					for(int i = 0; i < items.length;i++){
						empresas = items[i].split("-");
						if(empresas[0].startsWith("B")){
							avenidaEmpresas = matrizSensores.length/2;
						}
						
						if(empresas[1].equalsIgnoreCase("R"))
							calleEmpresas=i+1;	
						else
							calleEmpresas=i;
						
						emp = sensorEmpresas.get(matrizSensores[avenidaEmpresas][calleEmpresas].getNombre());	
						if(emp == null)
							emp = new HashSet<String>();

						emp.add(empresas[0].trim());
						sensorEmpresas.put(matrizSensores[avenidaEmpresas][calleEmpresas].getNombre(),emp);
					}
					avenidaEmpresas++;
				}

			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}

//	public static String fuerzaBruta(Sensor[][] sensores){
//		String result = "";
//
//		for(int i = 0; i < sensores[0].length; i++){
//			for(Entry<Integer, Integer> sen: sensores[sensores.length/2][i].getLimites().entrySet()){
//				if(sen.getValue()==2){
//					result += "Limite critico del contaminante"+sen.getKey()+" en "+sensores[sensores.length/2][i].toString() +"\n";
//					for(int j = 0; j < sensores.length; j++){
//						if(j == sensores.length/2)
//							continue;
//						if(sensores[j][i].getLimites().get(sen.getKey())==2){
//							result += "El problema esta en:"+sensores[j][i].toString();
//						}
//					}
//				}
//
//				else if(sen.getValue()==1){
//					result += "Limite desvio del contaminante"+sen.getKey()+" en "+sensores[sensores.length/2][i].toString() +"\n";
//					for(int j = 0; j < sensores.length; j++){
//						if(j == sensores.length/2)
//							continue;
//						if(sensores[j][i].getLimites().get(sen.getKey())==1){
//							result += "El problema esta en:"+sensores[j][i].toString();
//						}
//					}
//				}
//
//			}
//		}
//
//		return result;
//	}
//
	private static void FuerzaBruta(Sensor[][] matriz){
		Sensor sensorAvenida = null;
		for(int i = 0; i < matriz[matriz.length/2].length;i++){
			sensorAvenida = matriz[matriz.length/2][i];
			if(sensorAvenida.comprobarEstado(alarma)==0){
				System.out.println("SENSOR CORRECTO "+sensorAvenida.getNombre());
			}else{
				System.out.println("INFRACCION EN SENSOR "+sensorAvenida.getNombre());
				FuerzaBrutaCalle(matriz, i);
			}
			System.out.println("/////////////////////");
		}


	}

	private static void FuerzaBrutaCalle(Sensor[][] matriz, int columna){
		Sensor sensorCalle = null;
		for(int i = 0; i < matriz.length;i++){
			sensorCalle = matriz[i][columna];
			if(sensorCalle.comprobarEstado(alarma)==0){
				System.out.println("SENSOR CORRECTO "+sensorCalle.getNombre());
			}else{
				System.out.println("INFRACCION EN SENSOR "+sensorCalle.getNombre());
				System.out.println("Las empresas culpables son: "+sensorEmpresas.get(sensorCalle.getNombre()));
			}
		}
	}

}
