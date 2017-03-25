package Practica;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static Empresa[][] matrizEmpresas = null;
	public static ArrayList<ArrayList<Colector>> colectores;

	public static void main(String[] args) {
		String directorioEntrada = "";

		directorioEntrada = System.getProperty("user.dir") + File.separator +
				"src" + File.separator +
				"Practica" + File.separator + "Empresas.txt";
		//Cargar matriz de empresas
		loadFile(directorioEntrada);


		//for(Empresa[] emp :matrizEmpresas)
		//	for(Empresa emp2:emp)
		//		System.out.println(emp2.toString());

		//for(int i = 0; i < matrizEmpresas.length;i++)
		//System.out.println(matrizEmpresas[i][0].toString());




		inicializarColectores();

		cargarColectores();

		for(int i = 0; i < colectores.size();i++){
			for (int j = 0; j < colectores.get(i).size();j++){
				System.out.print(colectores.get(i).get(j)+"\t  ");
			}
			System.out.println();
		}
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

					//					System.out.print("Fila: "+posFila);
					//					System.out.println("\t Columna: "+posCol);
					//					System.out.println(matrizEmpresas.length+"\t "+matrizEmpresas[0].length);
					matrizEmpresas[posFila][posCol]= emp;
					posCol++;
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}

	public static void inicializarColectores(){
		colectores = new ArrayList<ArrayList<Colector>>();
		Colector auxCol = null;
		ArrayList<Colector> auxArray = new ArrayList<Colector>();

		for(int i = 0; i < matrizEmpresas[0].length+1; i++){
			auxArray = new ArrayList<Colector>();
			for(int j = 0; j < matrizEmpresas.length; j++){
				auxCol = new Colector("F"+j+"C"+i);
				auxArray.add(auxCol);
			}
			colectores.add(auxArray);
		}
	}

	public static void cargarColectores(){
		Empresa emp = null;
		Colector auxCol = null;
		Colector actual = null;

		//		for(int j = matrizEmpresas[0].length-1; j >= 0;j--){
		//			for(int i = 0; i < matrizEmpresas.length; i++){
		//				emp = matrizEmpresas[i][j];
		//				if(emp.isDireccionVertido()){
		//					auxCol = colectores.get(i).get(j+1);
		//					colectores.get(i).get(j+1).establecerDatos(emp.getFlujo(), emp.getCantidadContaminantes(), emp.getConcentracionContaminantes(), emp);
		//				}else{
		//					colectores.get(i).get(j).establecerDatos(emp.getFlujo(), emp.getCantidadContaminantes(), emp.getConcentracionContaminantes(), emp);
		//				}
		//			}
		//		}



		for(int i = matrizEmpresas[0].length-1; i >= 0; i--){
			for(int j = 0; j < matrizEmpresas.length; j++){
				emp = matrizEmpresas[j][i];
				//System.out.println(emp.toString());
				if(emp.getDireccionVertido()){
					auxCol = colectores.get(i+1).get(j);
					colectores.get(i+1).get(j).establecerDatos(emp.getFlujo(), emp.getCantidadContaminantes(), emp);
				}else{
					colectores.get(i).get(j).establecerDatos(emp.getFlujo(), emp.getCantidadContaminantes(), emp);
				}
			}
		}
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");

		for(int i = 0; i < colectores.size();i++){
			for (int j = 0; j < colectores.get(i).size();j++){
				System.out.print(colectores.get(i).get(j)+"\t  ");
			}
			System.out.println();
		}
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");

		//System.out.println("\n\n"+colectores.get(0).toString());
		//for(ArrayList<Colector> colec:colectores)
		//	organizarColectores(colec);

	}

	public static void organizarColectores(ArrayList<Colector> colector){
		int centro = colector.size()/2-1;
		//zona norte
		for(int i = 0; i < centro; i++){
			if(colector.get(i).getHabilitado()){
				for(int j = i+1;j < colector.size(); j++){
					if(colector.get(j).getHabilitado()){
						colector.get(j).setFlujo(colector.get(j).getFlujo()+colector.get(i).getFlujo());
						break;
					}
				}
			}
		}

		//zona sur
		for(int i = colector.size()-1; i > centro; i--){
			if(colector.get(i).getHabilitado()){
				for(int j = i-1;j > centro; j--){
					if(colector.get(j).getHabilitado()){
						//System.out.println("Colector j: "+colector.get(j).getUbicacion()+" "+ colector.get(j).getFlujo()+" Colector i: "+colector.get(i).getUbicacion()+" "+ colector.get(i).getFlujo());
						colector.get(j).setFlujo(colector.get(j).getFlujo()+colector.get(i).getFlujo());
						break;
					}
				}
			}
		}
	}

	private static void sumarContaminantes(Colector colector, Empresa empresa){
		if(empresa.getCantidadContaminantes().size()>0){
			System.out.println(empresa.getFlujo()+colector.getFlujo());
			empresa.setFlujo(empresa.getFlujo()+colector.getFlujo());
			for(int i = 0; i < empresa.getCantidadContaminantes().size();i++)
				empresa.getCantidadContaminantes().set(i, (colector.getCantidadContaminantes().get(i)+empresa.getCantidadContaminantes().get(i)));



			System.out.println(empresa.getFlujo());
		}
	}

}

