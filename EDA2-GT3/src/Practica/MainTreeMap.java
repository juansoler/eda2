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
	public static ArrayList<Colector> avenidaPrincipal;
	public static Alarma alarma=null;

	public static void main(String[] args) {
		String directorioEntrada = "";

		directorioEntrada = System.getProperty("user.dir") + File.separator +
				"src" + File.separator +
				"Practica" + File.separator + "Empresas25.txt";

		//Cargar las estructuras utilizadas
		loadFile(directorioEntrada);

		
		for(Entry<Colector, ArrayList<Colector>> col : colectores.entrySet())
			System.out.println(col.getKey().getUbicacion().toString()+"    "+col.getKey().getEmpresas().toString());
		
		System.out.println(colectores.lastKey().getEmpresas().size());
		//System.out.println(colectores.lastKey().mostrarEmpresas());
	}

	private static void loadFile(String file) {
		// Para los detalles sobre formato del archivo de entrada, ver archivo
		// de ejemplo (datos)

		Scanner sc = null;
		Empresa empresa;
		boolean leerEmpresas = false, leerLimites = false;

		int nAvenidas=0;
		int nCalles=0;
		int posFila=0, posCol=0;
		ArrayList<Double> mgContaminantesPorLitro, limiteCritico = null, limiteDesvio = null;
		Double flujo;

		try {
			sc = new Scanner(new File(file));
			while (sc.hasNextLine()) {

				String line = sc.nextLine();

				if (line.startsWith("%") || line.isEmpty()) continue;

				if (line.startsWith("@Avenidas")) {
					nAvenidas = Integer.parseInt(sc.nextLine())-1;
				}else if (line.startsWith("@Calle")) {
					nCalles = Integer.parseInt(sc.nextLine())-1;
					matrizEmpresas = new Empresa[nAvenidas][nCalles];
				}
				else if (line.startsWith("@Empresas")) {
					leerEmpresas = true;
					leerLimites = false;
					continue;
				} else if (line.startsWith("@Limites")) {
					leerEmpresas = false;
					leerLimites = true;
					continue;
				}

				String[] items = line.split(" ");

				if (leerEmpresas) {
					mgContaminantesPorLitro = new ArrayList<Double>();

					for(int i = 3; i < items.length;i++){
						mgContaminantesPorLitro.add(Double.parseDouble(items[i]));
					}
					flujo = Double.parseDouble(items[2]);

					empresa = new Empresa(items[0],items[1],flujo,mgContaminantesPorLitro);

					if(posCol == nCalles){
						posCol = 0;
						posFila++;
					}
					//Lanzar excepcion
					//if(posFila == nAvenidas){
					//empresas = false;
					//continue;
					//}
					matrizEmpresas[posFila][posCol++] = empresa;
				}else if(leerLimites){

					limiteCritico=new ArrayList<Double>();
					for(int i = 1; i < items.length;i++)
						limiteCritico.add(Double.parseDouble(items[i]));

					line = sc.nextLine();
					items = line.split(" ");

					limiteDesvio=new ArrayList<Double>();
					for(int i = 1; i < items.length;i++)
						limiteDesvio.add(Double.parseDouble(items[i]));

					alarma = new Alarma(limiteCritico,limiteDesvio);
				}
			}

			inicializarColectores();

			//Inicia el ArrayList que contiene los datos de la avenida
			avenidaPrincipal = new ArrayList<Colector>();

			for(Colector colectorAvenida: colectores.keySet())
				avenidaPrincipal.add(colectorAvenida);

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}

	private static void inicializarColectores(){
		colectores = new TreeMap<Colector,ArrayList<Colector>>();
		Colector auxCol = null;
		ArrayList<Colector> auxArray = new ArrayList<Colector>();

		for(int i = 0; i <= matrizEmpresas[0].length; i++){
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
				if(emp.getDireccionVertido())
					avenida = new Colector("A"+(i+1));
				else
					avenida = new Colector("A"+(i));

				colectores.get(avenida).get(j).establecerDatos(emp);
			}
		}

		for(ArrayList<Colector> colec:colectores.values())
			organizarColectores(colec);
		
		for(Entry<Colector, ArrayList<Colector>> col:colectores.entrySet())
			CalcularColectoresAvenida(col.getKey(), col.getValue());
		
		for(int i = colectores.size()-1; i > 0; i--)
			colectores.ceilingKey(new Colector("A"+(i-1))).sumarDatos(colectores.ceilingKey(new Colector("A"+i)));
		
	}

	private static void organizarColectores(ArrayList<Colector> colector){
		int centroInf = colector.size()/2-1;
		int centroSup = colector.size()/2;
		
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