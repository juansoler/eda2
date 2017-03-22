package Practica;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {
	public static Empresa[][] matrizEmpresas = null;
	public static ArrayList<ArrayList<ArrayList<Colector>>> colectores;

	public static void main(String[] args) {
		String directorioEntrada = "";

		directorioEntrada = System.getProperty("user.dir") + File.separator +
		"src" + File.separator +
		"Practica" + File.separator + "Empresas.txt";
		//Cargar matriz de empresas
		loadFile(directorioEntrada);


		for(Empresa[] emp :matrizEmpresas)
			for(Empresa emp2:emp)
				System.out.println(emp2.toString());

	}

	public static void loadFile(String file) {
		// Para los detalles sobre formato del archivo de entrada, ver archivo
		// de ejemplo (datos)

		Scanner sc = null;
		Empresa emp ;
		boolean empresas = false, limites = false;

		int avenidas=0;
		int calle=0;
		int posFila=0, posCol=0;
		ArrayList<Double> cont;

		try {
			sc = new Scanner(new File(file));
			while (sc.hasNextLine()) {

				String line = sc.nextLine();

				if (line.startsWith("%") || line.isEmpty()) continue;

				if (line.startsWith("@Avenidas")) {
					avenidas = Integer.parseInt(sc.nextLine());
				}else if (line.startsWith("@Calle")) {
					calle = Integer.parseInt(sc.nextLine());
					calle--;
					avenidas--;
					matrizEmpresas = new Empresa[avenidas][calle];
				}
				else if (line.startsWith("@Empresas")) {
					empresas = true;
					limites = false;
					continue;
				} else if (line.startsWith("@Limites")) {
					empresas = false;
					limites = true;
					continue;
				}

				String[] items = line.split(" ");

				if (empresas) {
					cont = new ArrayList<Double>();

					for(int i = 3; i < items.length;i++){
						cont.add(Double.parseDouble(items[i]));
					}

					Double flujo = Double.parseDouble(items[2]);

					emp = new Empresa(items[0],items[1],flujo,cont);

					if(posCol >= calle){
						posCol=0;
						posFila++;
					}
					//Lanzar excepcion
					//if(posFila >= avenidas)
					//	continue;

					System.out.print("Fila: "+posFila);
					System.out.println("\t Columna: "+posCol);
					System.out.println(matrizEmpresas.length+"\t "+matrizEmpresas[0].length);
					matrizEmpresas[posFila][posCol]= emp;
					posCol++;
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
	public void colectores(){
		Empresa emp = null;
		Colector auxCol = null;
		for(int i = 0; i < matrizEmpresas.length;i++){
			for(int j = 0; j < matrizEmpresas[0].length;j++){
				emp = matrizEmpresas[i][j];
				if(emp.isDireccionVertido()){
					if(colectores.get(0).get(j).get(i+1) == null){
						auxCol = new Colector(emp.getFlujo(),emp.getCantidadContaminantes(),emp.getConcentracionContaminantes(),emp);
						//colectores.get(0)
					}else{
						//auxCol
					}
				}

			}
		}

	}


}

