package Practica;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

public class MainTreeMap {
	public static Empresa[][] matrizEmpresas = null;
	public static TreeMap<Colector,ArrayList<Colector>> colectores;
	public static Alarma alarma=null;

	public static void main(String[] args) {
		String directorioEntrada = "";

		directorioEntrada = System.getProperty("user.dir") + File.separator +
				"src" + File.separator +
				"Practica" + File.separator + "Empresas.txt";

		//Cargar matriz de empresas
		loadFile(directorioEntrada);


		inicializarColectores();


		//for(ArrayList<Colector> col: colectores.values())
		//	System.out.println(col.toString());

		for(Entry<Colector, ArrayList<Colector>> col : colectores.entrySet())
			System.out.println(col.getKey().toString()+":\t"+col.getValue().toString());

		//for(Colector col: colectores.keySet())
		//	System.out.println(col.getUbicacion()+" Empresas:\n"+col.mostrarEmpresas());
		System.out.println(alarma.toString());
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
		ArrayList<Double> cont, limiteCritico = null, limiteDesvio = null;
		Double flujo;

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

					flujo = Double.parseDouble(items[2]);

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
				}else if(limites){
					
					limiteCritico=new ArrayList<Double>();
					for(int i = 1; i < items.length;i++)
						limiteCritico.add(Double.parseDouble(items[i]));
					
					line = sc.nextLine();
					items = line.split(" ");

					limiteDesvio=new ArrayList<Double>();
					for(int i = 1; i < items.length;i++)
						limiteDesvio.add(Double.parseDouble(items[i]));
					
					alarma = new Alarma(limiteCritico,limiteDesvio);
					limites = false;
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}

	private static void inicializarColectores(){
		colectores = new TreeMap<Colector,ArrayList<Colector>>();
		Colector auxCol = null;
		ArrayList<Colector> auxArray = new ArrayList<Colector>();

		for(int i = 0; i < matrizEmpresas[0].length+1; i++){
			auxArray = new ArrayList<Colector>();
			for(int j = 0; j < matrizEmpresas.length; j++){
				auxCol = new Colector("F"+j+"C"+i);
				auxArray.add(auxCol);
			}
			auxCol = new Colector("A"+i);
			colectores.put(auxCol, auxArray);
		}
		
		cargarColectores();
	}

	private static void cargarColectores(){
		Empresa emp = null;
		Colector avenida = null;

		for(int i = matrizEmpresas[0].length-1; i >= 0; i--){
			for(int j = 0; j < matrizEmpresas.length; j++){
				emp = matrizEmpresas[j][i];
				if(emp.isDireccionVertido())
					avenida = new Colector("A"+(i+1));
				else
					avenida = new Colector("A"+(i));

				colectores.get(avenida).get(j).establecerDatos(emp.getFlujo(), emp.getCantidadContaminantes(), emp.getConcentracionContaminantes(), emp);
			}
		}

		for(ArrayList<Colector> colec:colectores.values())
			organizarColectores(colec);



		for(Entry<Colector, ArrayList<Colector>> col:colectores.entrySet()){
			CalcularColectoresAvenida(col.getKey(), col.getValue());
		}

		for(int i = colectores.size()-1; i > 0; i--){
			colectores.ceilingKey(new Colector("A"+(i-1))).sumarDatos(colectores.ceilingKey(new Colector("A"+i)));
		}
	}

	private static void organizarColectores(ArrayList<Colector> colector){
		int centroInf = colectores.size()/2-1;
		int centroSup = colectores.size()/2;
		//zona norte
		for(int i = 0; i < centroInf; i++){
			if(colector.get(i).getHabilitado()){
				for(int j = i+1;j <= centroInf; j++){
					if(colector.get(j).getHabilitado()){
						colector.get(j).sumarDatos(colector.get(i));
						break;
					}
				}
			}
		}

		//zona sur
		for(int i = colector.size()-1; i > centroSup; i--){
			if(colector.get(i).getHabilitado()){
				for(int j = i-1;j >= centroSup; j--)
					if(colector.get(j).getHabilitado()){
						colector.get(j).sumarDatos(colector.get(i));
						break;
					}
			}
		}
	}

	private static void CalcularColectoresAvenida(Colector colector, ArrayList<Colector> colectores){
		int centroInf = colectores.size()/2-1;
		int centroSup = colectores.size()/2;

		while(centroInf >= 0){
			if(colectores.get(centroInf).getHabilitado()){
				colector.sumarDatos(colectores.get(centroInf));
				break;
			}
			centroInf--;
		}

		while(centroSup < colectores.size()){
			if(colectores.get(centroSup).getHabilitado()){
				colector.sumarDatos(colectores.get(centroSup));
				break;
			}
			centroSup++;
		}
	}
}