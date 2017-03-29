package Practica;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class MainTreeMap {
	public static Empresa[][] matrizEmpresas = null;
	public static TreeMap<EstacionMedida,ArrayList<EstacionMedida>> EstacionesMedida;
	public static ArrayList<EstacionMedida> avenidaPrincipal;
	public static Alarma alarma=null;

	public static void main(String[] args) {
		String directorioEntrada = "";

		directorioEntrada = System.getProperty("user.dir") + File.separator +
				"src" + File.separator +
				"Practica" + File.separator + "Empresas.txt";

		//Cargar las estructuras utilizadas
		loadFile(directorioEntrada);


//		for(Entry<EstacionMedida, ArrayList<EstacionMedida>> col : EstacionesMedida.entrySet()){
//			System.out.println(col.getKey().toString());
//			for(EstacionMedida emp: col.getValue())
//				if(emp.getHabilitado())
//					System.out.println(emp.toString());
//		}
		System.out.println(fuerzaBruta(avenidaPrincipal).toString());
		System.out.println();
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

			inicializarEstacionesMedida();

			//Inicia el ArrayList que contiene los datos de la avenida
			avenidaPrincipal = new ArrayList<EstacionMedida>();

			for(EstacionMedida EstacionMedidaAvenida: EstacionesMedida.keySet())
				avenidaPrincipal.add(EstacionMedidaAvenida);

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}

	private static void inicializarEstacionesMedida(){
		EstacionesMedida = new TreeMap<EstacionMedida,ArrayList<EstacionMedida>>();
		EstacionMedida auxEstMedida = null;
		ArrayList<EstacionMedida> auxArray = new ArrayList<EstacionMedida>();

		for(int i = 0; i <= matrizEmpresas[0].length; i++){
			auxArray = new ArrayList<EstacionMedida>();
			for(int j = 0; j < matrizEmpresas.length; j++){
				if(j == (matrizEmpresas.length/2) || j == (matrizEmpresas.length/2-1))
					continue;
				auxEstMedida = new EstacionMedida("F"+j+"C"+i);
				auxArray.add(auxEstMedida);
			}
			auxEstMedida = new EstacionMedida("A"+i);
			EstacionesMedida.put(auxEstMedida, auxArray);
		}

		cargarEstacionesMedida();
	}

	private static void cargarEstacionesMedida(){
		Empresa emp = null;
		EstacionMedida avenida = null;

		for(int i = matrizEmpresas[0].length-1; i >= 0; i--){
			for(int j = 0; j < matrizEmpresas.length; j++){
				emp = matrizEmpresas[j][i];
				if(emp.getDireccionVertido())
					avenida = new EstacionMedida("A"+(i+1));
				else
					avenida = new EstacionMedida("A"+(i));

				if(j == (matrizEmpresas.length/2) || j == (matrizEmpresas.length/2-1))
					EstacionesMedida.ceilingKey(avenida).establecerDatos(emp);
				else{
					if(j > matrizEmpresas.length/2)
						EstacionesMedida.get(avenida).get(j-2).establecerDatos(emp);
					else
						EstacionesMedida.get(avenida).get(j).establecerDatos(emp);
				}
			}
		}

		for(ArrayList<EstacionMedida> estaciones:EstacionesMedida.values())
			organizarEstacionesMedida(estaciones);

		for(Entry<EstacionMedida, ArrayList<EstacionMedida>> estacionMedida:EstacionesMedida.entrySet())
			CalcularEstacionesMedidaAvenida(estacionMedida.getKey(), estacionMedida.getValue());

		for(int i = EstacionesMedida.size()-1; i > 0; i--)
			EstacionesMedida.ceilingKey(new EstacionMedida("A"+(i-1))).sumarDatos(EstacionesMedida.ceilingKey(new EstacionMedida("A"+i)));

	}

	private static void organizarEstacionesMedida(ArrayList<EstacionMedida> estacionMedida){
		int centroInf = estacionMedida.size()/2-1;
		int centroSup = estacionMedida.size()/2;

		//zona norte
		for(int i = 0; i < centroInf; i++){
			if(estacionMedida.get(i).getHabilitado()){
				for(int j = i+1;j <= centroInf; j++){
					if(estacionMedida.get(j).getHabilitado()){
						estacionMedida.get(j).sumarDatos(estacionMedida.get(i));
						break;
					}
				}
			}
		}

		//zona sur
		for(int i = estacionMedida.size()-1; i > centroSup; i--){
			if(estacionMedida.get(i).getHabilitado()){
				for(int j = i-1;j >= centroSup; j--)
					if(estacionMedida.get(j).getHabilitado()){
						estacionMedida.get(j).sumarDatos(estacionMedida.get(i));
						break;
					}
			}
		}
	}

	private static void CalcularEstacionesMedidaAvenida(EstacionMedida estacionMedida, ArrayList<EstacionMedida> estacionesMedida){
		int centroInf = estacionesMedida.size()/2-1;
		int centroSup = estacionesMedida.size()/2;

		while(centroInf >= 0){
			if(estacionesMedida.get(centroInf).getHabilitado()){
				estacionMedida.sumarDatos(estacionesMedida.get(centroInf));
				break;
			}
			centroInf--;
		}

		while(centroSup < estacionesMedida.size()){
			if(estacionesMedida.get(centroSup).getHabilitado()){
				estacionMedida.sumarDatos(estacionesMedida.get(centroSup));
				break;
			}
			centroSup++;
		}
	}

	public static TreeSet<String> fuerzaBruta(ArrayList<EstacionMedida> Avenida){
		TreeSet<String> result = new TreeSet<String>();
		ArrayList<Integer> cont = new ArrayList<Integer>();

		for(int i = Avenida.size()-1; i >= 0; i--){
			if(i == Avenida.size()-1){
				cont = alarma.contaminantesPeligrosos(Avenida.get(i), null);
			}else{
				cont = alarma.contaminantesPeligrosos(Avenida.get(i), Avenida.get(i+1));
			}
			if(cont.size()>0){
				result.addAll(fuerzaAux(EstacionesMedida.get(Avenida.get(i))));
			}
				
		}
		
		return result;
	}

	private static TreeSet<String> fuerzaAux(ArrayList<EstacionMedida> est){
		TreeSet<String> result = new TreeSet<String>();
		System.out.println(est.get(0).getUbicacion());
		//Zona Norte
		for(int i = est.size()/2-1; i >= 0;i--){
			if(est.get(i).getHabilitado()){
				if(i == est.size()/2-1){
					if(alarma.detectar(est.get(i),null) != null){
						System.out.println("añade por defecto "+ est.get(i).getEmpresas().toString());
					
					}
						
				}
				else
					if(est.get(i+1).getHabilitado())
						if(alarma.detectar(est.get(i), est.get(i+1))!=null)
							result.addAll(est.get(i).getEmpresas());			
				
				for(int j = i-1; j >= 0;j--){
					if(est.get(j).getHabilitado()){
						System.out.println("1 "+est.get(j).getEmpresas().toString());
						System.out.println("2 "+est.get(j+1).getEmpresas().toString());
						if(alarma.detectar(est.get(j),est.get(i)) == null){
							System.out.println("borra: "+est.get(j).getEmpresas().toString());
							result.removeAll(est.get(j).getEmpresas());
							break;
						}
					}
				}
					
			}

		}

//		for(int i = est.size()/2; i < est.size();i++){
//			if(i == est.size()/2){
//				if(alarma.detectar(est.get(i), null,contaminantes) != null){
//					result.addAll(est.get(i).getEmpresas());
//				}
//			}else
//				if(alarma.detectar(est.get(i-1), est.get(i),contaminantes) != null
//				){
//					result.addAll(est.get(i).getEmpresas());
//					//System.out.println(alarma.detectar(est.get(i)).toString());
//					//System.out.println(est.get(i).toString());
//					//System.out.println(result.toString());
//					for(int j = i; j < est.size(); j++)
//						if(alarma.detectar(est.get(j), est.get(i),contaminantes).length()==0){
//							//System.out.println("Aqui "+est.get(j).toString());
//							result.removeAll(est.get(j).getEmpresas());
//						}
//
//					//System.out.println(result.toString());
//					//System.out.println(est.get(i).toString()+"Tus mueros "+est.get(i).getEmpresas().toString());
//				}
//		}
		return result;
	}

}