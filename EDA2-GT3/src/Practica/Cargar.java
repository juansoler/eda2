package Practica;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Cargar {

		public static Sensor[][] loadFile(String file, HashMap<String, HashSet<String>> sensorEmpresas, HashMap<String, Double[]> limitesDep, Balsa balsa) {
		Sensor[][] matrizSensores = null;
		Scanner sc = null;
		boolean leerLimites = false, leerEmpresas = false, maSensores = false, capacidadBalsa = false;;
		Sensor aux = null;
		int nAvenidas=0, posicion, nCalles=0, posFila=0;
		HashMap<String,Double> mgContaminantesPorLitro;
		Double[] limites;
		Double flujo;
		String line;
		String[] items;

		try {
			sc = new Scanner(new File(file));
			while (sc.hasNextLine()) {

				line = sc.nextLine();

				if (line.startsWith("%") || line.isEmpty()) continue;

				if (line.startsWith("@Avenidas")) {
					nAvenidas = Integer.parseInt(sc.nextLine());
				}else if (line.startsWith("@Calle")) {
					nCalles = Integer.parseInt(sc.nextLine())+1;
					matrizSensores = new Sensor[nAvenidas][nCalles];
				}else if(line.startsWith("@MatrizSensores")){
					leerLimites = false;
					leerEmpresas = false;
					capacidadBalsa = false;
					maSensores = true;
					posFila=0;
					continue;
				} else if (line.startsWith("@Limites")) {
					leerLimites = true;
					leerEmpresas = false;
					maSensores = false;
					capacidadBalsa = false;
					continue;
				} else if (line.startsWith("@Empresas")) {
					leerLimites = false;
					leerEmpresas = true;
					maSensores = false;
					capacidadBalsa = false;
					posFila=0;
					continue;
				}else if (line.startsWith("@CapacidadBalsa")) {
					leerLimites = false;
					leerEmpresas = false;
					maSensores = false;
					capacidadBalsa = true;
					posFila=0;
					continue;
				}

				items = line.split(" ");

				if(maSensores){
					//Para crear la matriz de sensores
					for(int i = 0; i < items.length; i++){
						matrizSensores[posFila][i] = new Sensor(items[i]);
						sensorEmpresas.put(matrizSensores[posFila][i].getNombre(), new HashSet<String>());
						if(i == matrizSensores[0].length-1){
							i = 0;
							posFila++;
							break;
						}
					}
				}else if (leerEmpresas) {
					//Para asignar valores a los sensores de la matriz
					mgContaminantesPorLitro = new HashMap<String,Double>();

					flujo = Double.parseDouble(items[3]);

					posicion = Integer.parseInt(items[1])-1;

					if(items[2].equalsIgnoreCase("r"))
						posicion +=1;

					if (posicion == -1)
						posicion = 0;

					if(items[0].startsWith("BS") || items[0].startsWith("A"))
						posFila = (matrizSensores.length/2);

					aux = matrizSensores[posFila][posicion];
					sensorEmpresas.get(aux.getNombre()).add(items[0]+items[1]);

					for(int i = 4, cont = 1; i < items.length;i++, cont++)
						mgContaminantesPorLitro.put("Contaminante"+cont, Double.parseDouble(items[i]));


					aux.addDatos(flujo, mgContaminantesPorLitro);

					if(Integer.parseInt(items[1]) == nCalles-1)
						posFila++;

				}else if(leerLimites){
					//Para obtener los limites
					limites=new Double[2];
					items = line.split(" ");
					limites[0]=Double.parseDouble(items[1]);
					limites[1]=Double.parseDouble(items[2]);
					limitesDep.put(items[0],limites);
				}else if(capacidadBalsa){
					balsa.setCapacidad(Integer.parseInt(line));
				}
			}

			//Sumar zona norte
			for(int j = 0; j < matrizSensores[0].length;j++)
				for(int i = 1; i <= matrizSensores.length/2; i++)
					matrizSensores[i][j].sumarValoresSensores(matrizSensores[i-1][j]);

			//Sumar zona sur
			for(int j = 0; j < matrizSensores[0].length;j++)
				for(int i = matrizSensores.length-2; i >= matrizSensores.length/2; i--)
					matrizSensores[i][j].sumarValoresSensores(matrizSensores[i+1][j]);

			//Sumar avenida de derecha a izquierda
			for(int j = matrizSensores[0].length-2; j >= 0; j--)
				matrizSensores[matrizSensores.length/2][j].sumarValoresSensores(matrizSensores[matrizSensores.length/2][j+1]);


		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		return matrizSensores;
	}
}
