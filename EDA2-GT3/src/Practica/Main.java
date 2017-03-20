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

	private static Colector colector;
	private TreeMap<Integer, TreeSet<Colector>> colectorPrincipal = new TreeMap<Integer, TreeSet<Colector>>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub






		//		Empresa aux = new Empresa("CN0", "R");
		//		Empresa aux2 = new Empresa("CN5", "L");
		//
		//		System.out.println(aux.calcularUbicacion());
		//		System.out.println(aux.toString());
		//		System.out.println(aux.colectorVertido());
		//
		//		ArrayList<Double> arr = new ArrayList<Double>();
		//		for(int i = 0; i < 5; i++){
		//			arr.add((double) i);
		//		}
		//
		//		ColectorSecundario col1 = new ColectorSecundario(10.5, arr);
		//		col1.addEmpresa(aux);
		//		col1.addEmpresa(aux2);
		//		//System.out.println(col1.toString());
		//
		//		double flujo = 10.0;
		//
		//		ArrayList<Double> arrLimite = new ArrayList<Double>();
		//		arrLimite.add(0.5);
		//		arrLimite.add(0.3);
		//		arrLimite.add(0.1);
		//		arrLimite.add(0.7);
		//
		//		ArrayList<Double> arrDesvio = new ArrayList<Double>();
		//		arrDesvio.add(0.3);
		//		arrDesvio.add(0.25);
		//		arrDesvio.add(0.05);
		//		arrDesvio.add(0.35);
		//
		//		ArrayList<Double> Contaminantes = new ArrayList<Double>();
		//		Contaminantes.add(1.0);
		//		Contaminantes.add(2.9);
		//		Contaminantes.add(1.2);
		//		Contaminantes.add(6.0);
		//
		//		Alarma alarm = new Alarma(arrLimite, arrDesvio);
		//
		//		System.out.println(alarm.detectar(flujo, Contaminantes));
		//
		//		colector = new Colector();

		//		for (Entry<String, ColectorSecundario> it : colector.getColectorSec().entrySet()) {
		//			alarm.detectar(it.getValue().getFlujo(), it.getValue().getContaminantes());
		//		}
	}

	public void loadFile(String file) {
		// Para los detalles sobre formato del archivo de entrada, ver archivo
		// de ejemplo (datos)

		Scanner sc = null;
		Empresa emp ;
		ColectorSecundario sec;
		boolean empresas = false, limites = false, vertidos=false;

		try {
			sc = new Scanner(new File(file));
			while (sc.hasNextLine()) {

				String line = sc.nextLine();

				if (line.startsWith("%") || line.isEmpty()) continue;
				if (line.startsWith("@Empresas")) {
					empresas = true;
					limites = false;
					vertidos = false;
					continue;
				} else if (line.startsWith("@Limites")) {
					empresas = false;
					limites = true;
					vertidos = false;
					continue;
				} else if (line.startsWith("@Vertidos")) {
					empresas = false;
					limites = false;
					vertidos = true;
					continue;
				}

				String[] items = line.split(" ");

				if (empresas) {
					for(int i = 0; i< line.length();i+=2){
						emp = new Empresa(items[i],items[i+1]);
						sec = new ColectorSecundario();
						sec.addEmpresa(emp);
						if (emp.isDireccionVertido()){
							//colectorPrincipal.put(Integer.parseInt(items[i].charAt(2)+"")+1, sec);
						}
						else {
							//colectorPrincipal.put(Integer.parseInt(items[i].charAt(2)+""), sec);
						}
					}




				} else if (limites) {

				}
				else if(vertidos){

					TreeSet<Colector> colec = colectorPrincipal.get(colectorPrincipal.get(items[0].charAt(2)));


					for (int i =1 ; i<line.length(); i++){

					}
				}



			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}

	}
}

