package Practica;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import GeneradorPruebas.generadorPruebas;
import java.util.Scanner;

public class Depuradora {

	private static Sensor[][] matrizSensores = null;
	private static HashMap<String, HashSet<String>> sensorEmpresas = new HashMap<String, HashSet<String>>();
	private static HashMap<String, Double[]> limites = new HashMap<String, Double[]>();
	private static HashMap<String, Double[]> limitesSuperados = new HashMap<String, Double[]>();
	private static HashSet<String> empresasCulpables = new HashSet<String>();
	private static HashSet<String> zonaGris = new HashSet<String>();
	private static HashSet<String> greedy2v3 = new HashSet<String>();
	private static HashMap<String, HashSet<String>> greedy2v2 = new HashMap<String, HashSet<String>>();
	private static Balsa balsa = new Balsa();
	private static long tiempoInicio = 0;
	private static long tiempoActual = 0;
	private static long tiempoMax = 0;
	@SuppressWarnings("unused")
	private static Scanner entrada;
	private static boolean riesgoCritico = false;
	private static BufferedWriter out = null;
	public static PrintWriter pw = null;
	public static void main(String[] args) throws IOException {

		String directorio = System.getProperty("user.dir") + File.separator
				+ "src" + File.separator +  "archivosDePruebas"+ File.separator;

		@SuppressWarnings("resource")
		Scanner entrada = new Scanner(System.in);
		String nuevo = "0";
		System.out.println("¿Desea generar un nuevo archivo o cargar el por defecto? 1=Nuevo,  0=Por Defecto");
		nuevo = entrada.next();

		String nombreArchivo = "default.txt";
		String directorioEntrada = "";
		if(nuevo.trim().equals("1")){
		// Generar Archivo
		nombreArchivo = "default.txt";
		String numeroCalles  = "0";
		String numeroAvenidas  = "0";
		String numeroContaminantes = "0";
		String balsaCapacidad = "0";
		String peorPeculiar = "0";
		String peorCaso = "0";
		String mejorCaso = "0";
		String casoPromedio = "0";
		String todosIzq = "0";
		String calleMitadFull = "0";
		String empresasContaminantes = "0";
		System.out.println("Introduce el nombre del archivo a generar");
		nombreArchivo = entrada.next();

		System.out.println("Introduce el numero de calles");
		numeroCalles = entrada.next();
		
		System.out.println("Introduce el numero de Avenidas");
		numeroAvenidas = entrada.next();
		
		System.out.println("Introduce el numero de Contaminantes");
		numeroContaminantes = entrada.next();
		
		System.out.println("Introduce la capacidad de la balsa descontaminadora.  0 = Por defecto(100.000)");
		balsaCapacidad = entrada.next();
		
		System.out.println("Introduce el numero de empresas que desee que contaminen");
		empresasContaminantes = entrada.next();
		
		System.out.println("¿Desea vertir todas las empresas a la izquierda?  1=Si,  0=No");
		todosIzq = entrada.next();
	
		System.out.println("¿Desea que todas las empresas de dos calles adyacentes viertan en el mismo sensor?  1=Si,  0=No");
		calleMitadFull = entrada.next();
	
		
		System.out.println("¿Desea generar un casoPeculiar? Las empresas contaminantes se encuentran en las cuatro esquinas, además del numero de aleatorias que haya elegido  1=Si,  0=No");
		peorPeculiar = entrada.next();
		
		System.out.println("¿Desea generar un casoMejor? Serán 0 las empresas que contaminan sobrescribira el valor establecido. 1=Si,  0=No");
		mejorCaso = entrada.next();
		System.out.println("¿Desea generar un casoPeor? Serán " + (Integer.parseInt(numeroCalles)*Integer.parseInt(numeroAvenidas)+10)+" las empresas que contaminan sobrescribira el valor establecido. 1=Si,  0=No");
		peorCaso = entrada.next();
		System.out.println("¿Desea generar un casoPromedio? Serán " + (Integer.parseInt(numeroCalles)*Integer.parseInt(numeroAvenidas)+10)/2 +" las empresas que contaminan sobrescribira el valor establecido. 1=Si,  0=No");
		casoPromedio = entrada.next();
	
		String[] argumentos = new String[] {nombreArchivo+"_entrada.txt", numeroCalles, numeroAvenidas
				, numeroContaminantes
				, balsaCapacidad
				, todosIzq
				, calleMitadFull
				, peorPeculiar
				, empresasContaminantes
				, mejorCaso
				, peorCaso
				, casoPromedio
				};
		generadorPruebas.main(argumentos);
		

		directorioEntrada = directorio + nombreArchivo+ "_entrada.txt";
		}else{

			directorioEntrada = System.getProperty("user.dir") + File.separator +
					"src" + File.separator +
					"Practica" + File.separator + "Empresas12.txt";

			nombreArchivo = "Empresas12";
		}
		// salida
		out = new BufferedWriter(new FileWriter(directorio + nombreArchivo+"_salida.txt"));

		//Cargar las estructuras utilizadas
		matrizSensores = Cargar.loadFile(directorioEntrada, sensorEmpresas, limites, balsa);

		//Comprueba los contaminantes con problemas
		detectarContaminantes();

		entrada = new Scanner(System.in);

		int opcion = 0;
		do{
			System.out.println("Seleccione la opción deseada: ");
			System.out.println("FuerzaBrutav1: 0  //  FuerzaBrutav2: 1  //  FuerzaBrutav3: 2  //  DYVv1: 3  //  DYVv2: 4  //  DYVv3: 5");
			System.out.println("DYVv4: 6  //  Greedy1v1: 7  //  Greedy1v2: 8  //  Greedy2v1: 9  //  Greedy2v2: 10  //  Greedy2v3: 11");
			try{
				opcion = entrada.nextInt();
			}catch(Exception e){
				System.out.println("Has introducido un valor incorrecto");
			}
		}while(opcion < 0 || opcion > 11);


		switch(opcion){
		case 0: FuerzaBrutav1();
		break;
		case 1: FuerzaBrutav2();
		break;
		case 2: FuerzaBrutav3();
		break;
		case 3: DYVv1();
		break;
		case 4: DYVv2();
		break;
		case 5: DYVv3();
		break;
		case 6: DYVv4();
		break;
		case 7: Greedy1v1();
		break;
		case 8: Greedy1v2();
		break;
		case 9: Greedy2v1();
		break;
		case 10: Greedy2v2();
		break;
		case 11: Greedy2v3();
		break;
		}

		out.close();
	}


	//Detecta si en A1 se supera algun limite
	private static void detectarContaminantes(){
		limitesSuperados = matrizSensores[matrizSensores.length/2][0].determinarContaminantes(limites);
	}


	private static void FuerzaBrutav1() throws IOException{
		out.write("***************************************************");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");
		out.write("Metodo elegido FuerzaBrutav1");
		out.write("\n");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		for (int i = 0; i < matrizSensores.length; i++) {
			for (int j = 0; j < matrizSensores[0].length; j++) {
				Sensor sensor = matrizSensores[i][j];
				if(sensor.comprobarEstado(limitesSuperados)==0){
					out.write("SENSOR CORRECTO "+sensor.getNombre());
					out.write("\n");
				}else{
					out.write("INFRACCION EN SENSOR "+sensor.getNombre());
					out.write("\n");
					out.write("Las empresas culpables son: "+sensorEmpresas.get(sensor.getNombre()));
					out.write("\n");
				}
			}
		}
	}


	private static void FuerzaBrutav2() throws IOException{
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		out.write("Metodo elegido FuerzaBrutav2");
		out.write("\n");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		int medio = matrizSensores.length/2, comprobar;
		Sensor resultado = new Sensor();
		Sensor sensorMedida = new Sensor();

		//Comprueba la Zona norte
		for(int j = 0; j < matrizSensores[0].length; j++){
			for (int i = 0; i < medio; i++){
				sensorMedida = resultado = matrizSensores[i][j];
				if(i != 0)
					resultado = restarEstaciones(resultado, matrizSensores[i-1][j]);

				comprobar = resultado.comprobarEstado(limitesSuperados);
				if(comprobar == 0){
					out.write("SENSOR CORRECTO "+sensorMedida.getNombre());
					out.write("\n");

				}else{
					out.write("INFRACCION EN SENSOR "+sensorMedida.getNombre());
					out.write("\n");
					manejarBalsa(comprobar);
					out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorMedida.getNombre()));
					out.write("\n");
				}

			}


			//Comprueba la Zona Sur
			for (int i = matrizSensores.length-1; i > medio; i--){
				sensorMedida = resultado = matrizSensores[i][j];
				if(i != matrizSensores.length-1)
					resultado = restarEstaciones(resultado, matrizSensores[i+1][j]);

				comprobar = resultado.comprobarEstado(limitesSuperados);

				if(comprobar == 0){
					out.write("SENSOR CORRECTO "+sensorMedida.getNombre());
					out.write("\n");
				}else{
					out.write("INFRACCION EN SENSOR "+sensorMedida.getNombre());
					out.write("\n");
					manejarBalsa(comprobar);
					out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorMedida.getNombre()));
					out.write("\n");
				}
			}


			//Comprueba la Avenida
			sensorMedida = resultado = matrizSensores[medio][j];
			if(j != matrizSensores[medio].length-1)
				resultado = restarEstaciones(resultado, matrizSensores[medio][j+1]);

			//Restamos zona norte
			resultado = restarEstaciones(resultado, matrizSensores[medio-1][j]);

			//Restamos zona sur
			resultado = restarEstaciones(resultado, matrizSensores[medio+1][j]);

			comprobar = resultado.comprobarEstado(limitesSuperados);

			if(comprobar == 0){
				out.write("SENSOR CORRECTO "+sensorMedida.getNombre());
				out.write("\n");
			}else{
				out.write("INFRACCION EN SENSOR "+sensorMedida.getNombre());
				out.write("\n");
				manejarBalsa(comprobar);
				out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorMedida.getNombre()));
				out.write("\n");
			}

		}

	}


	private static void FuerzaBrutav3() throws IOException{
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		out.write("Metodo elegido FuerzaBrutav3");
		out.write("\n");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		int medio = matrizSensores.length/2, comprobar;
		Sensor resultado = new Sensor();
		Sensor sensorMedida = new Sensor();

		//Comprueba la Zona norte
		for(int j = 0; j < matrizSensores[0].length; j++){

			sensorMedida = matrizSensores[medio][j];
			if(sensorMedida.comprobarEstado(limitesSuperados) == 0){
				out.write("SENSOR CORRECTO A PARTIR DE "+sensorMedida.getNombre());
				out.write("\n");
				break;
			}


			for (int i = medio-1; i >= 0; i--){
				sensorMedida = resultado = matrizSensores[i][j];

				if(sensorMedida.comprobarEstado(limitesSuperados)==0){
					out.write("SENSOR CORRECTO A PARTIR DE "+sensorMedida.getNombre());
					out.write("\n");
					break;
				}

				if(i != 0)
					resultado = restarEstaciones(resultado, matrizSensores[i-1][j]);

				comprobar = resultado.comprobarEstado(limitesSuperados);
				if(comprobar == 0){
					out.write("SENSOR CORRECTO "+sensorMedida.getNombre());
				}else{
					out.write("INFRACCION EN SENSOR "+sensorMedida.getNombre());
					out.write("\n");

					manejarBalsa(comprobar);
					out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorMedida.getNombre()));
					out.write("\n");
				}

			}

			//Comprueba la Zona Sur
			for (int i = medio+1; i < matrizSensores.length; i++){
				sensorMedida = resultado = matrizSensores[i][j];
				if(sensorMedida.comprobarEstado(limitesSuperados)==0){
					out.write("SENSOR CORRECTO A PARTIR DE "+sensorMedida.getNombre());
					out.write("\n");
					break;
				}
				if(i != matrizSensores.length-1)
					resultado = restarEstaciones(resultado, matrizSensores[i+1][j]);

				comprobar = resultado.comprobarEstado(limitesSuperados);

				if(comprobar == 0){
					out.write("SENSOR CORRECTO "+sensorMedida.getNombre());
					out.write("\n");
				}else{
					out.write("INFRACCION EN SENSOR "+sensorMedida.getNombre());
					out.write("\n");
					manejarBalsa(comprobar);
					out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorMedida.getNombre()));
					out.write("\n");
				}
			}


			//Comprueba la Avenida
			sensorMedida = resultado = matrizSensores[medio][j];
			if(j != matrizSensores[medio].length-1)
				resultado = restarEstaciones(resultado, matrizSensores[medio][j+1]);

			//Restamos zona norte
			resultado = restarEstaciones(resultado, matrizSensores[medio-1][j]);

			//Restamos zona sur
			resultado = restarEstaciones(resultado, matrizSensores[medio+1][j]);

			comprobar = resultado.comprobarEstado(limitesSuperados);

			if(comprobar == 0){
				out.write("SENSOR CORRECTO "+sensorMedida.getNombre());
				out.write("\n");
			}else{
				out.write("INFRACCION EN SENSOR "+sensorMedida.getNombre());
				out.write("\n");
				manejarBalsa(comprobar);
				out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorMedida.getNombre()));
				out.write("\n");
			}
		}

	}


	private static void DYVv1() throws IOException{
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		out.write("Metodo elegido DyV v1");
		out.write("\n");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		if(limitesSuperados.size()>0)
			DyVAvenidav1(0,matrizSensores[0].length-1);
		else
			out.write("No hay ningún problema de contaminacion en el polígono.");
		out.write("\n");
	}

	private static void DyVAvenidav1(int posInicio, int posFin) throws IOException {
		if(posInicio>=posFin){
			DyVCallesNortev1(matrizSensores.length/2, 0, posInicio);
			DyVCallesSurv1(matrizSensores.length/2+1, matrizSensores.length-1, posInicio);
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[matrizSensores.length/2][posMitad];
			int comprobar = sensorMitad.comprobarEstado(limitesSuperados);
			if(comprobar==0){
				out.write("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				out.write("\n");
				DyVAvenidav1(posInicio, posMitad-1);
			}else{
				manejarBalsa(comprobar);
				DyVAvenidav1(posMitad+1, posFin);
				DyVAvenidav1(posInicio, posMitad);
			}
		}
	}


	private static void DyVCallesSurv1(int posInicio, int posFin, int columna) throws IOException {
		if(posInicio>=posFin){
			Sensor sensorInicio = matrizSensores[posInicio][columna];
			int comprobar = sensorInicio.comprobarEstado(limitesSuperados);
			if(comprobar>0){
				out.write("INFRACCION EN SENSOR "+sensorInicio.getNombre());
				out.write("\n");
				manejarBalsa(comprobar);
				out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorInicio.getNombre()));
				out.write("\n");
			}else{
				out.write("SENSOR CORRECTO "+sensorInicio.getNombre());
				out.write("\n");
			}
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[posMitad][columna];
			int comprobar = sensorMitad.comprobarEstado(limitesSuperados);
			if(comprobar==0){
				out.write("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				out.write("\n");
				DyVCallesSurv1(posInicio, posMitad-1, columna);
			}else{
				manejarBalsa(comprobar);
				DyVCallesSurv1(posMitad+1, posFin, columna);
				DyVCallesSurv1(posInicio, posMitad, columna);
			}
		}

	}

	private static void DyVCallesNortev1(int posInicio, int posFin, int columna) throws IOException {
		if(posInicio<=posFin){
			Sensor sensorFin = matrizSensores[posFin][columna];
			int comprobar = sensorFin.comprobarEstado(limitesSuperados);
			if(comprobar>0){
				out.write("INFRACCION EN SENSOR "+sensorFin.getNombre());
				out.write("\n");
				manejarBalsa(comprobar);
				out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorFin.getNombre()));
				out.write("\n");
			}else{
				out.write("SENSOR CORRECTO "+sensorFin.getNombre());
				out.write("\n");
			}
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[posMitad][columna];
			int comprobar = sensorMitad.comprobarEstado(limitesSuperados);
			if(comprobar==0){
				out.write("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				out.write("\n");
				DyVCallesNortev1(posInicio, posMitad+1, columna);
			}else{
				manejarBalsa(comprobar);
				DyVCallesNortev1(posMitad, posFin, columna);
				DyVCallesNortev1(posInicio, posMitad+1, columna);
			}
		}

	}



	private static void DYVv2() throws IOException{
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		out.write("Metodo elegido DyV v2");
		out.write("\n");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		if(limitesSuperados.size()>0)
			DyVAvenidav2(0,matrizSensores[0].length-1);
		else
			out.write("No hay ningún problema de contaminacion en el polígono.");
		out.write("\n");
	}


	private static void DyVAvenidav2(int posInicio, int posFin) throws IOException {
		if(posInicio>=posFin){
			DyVCallesNortev2(matrizSensores.length/2, 0, posInicio);
			DyVCallesSurv2(matrizSensores.length/2+1, matrizSensores.length-1, posInicio);
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[matrizSensores.length/2][posMitad];

			int comprobar = sensorMitad.comprobarEstado(limitesSuperados);

			if(comprobar == 0){
				out.write("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				out.write("\n");
				DyVAvenidav2(posInicio, posMitad-1);
			}else{
				manejarBalsa(comprobar);
				DyVAvenidav2(posMitad+1, posFin);
				DyVAvenidav2(posInicio, posMitad);
			}
		}
	}


	private static void DyVCallesSurv2(int posInicio, int posFin, int columna) throws IOException {
		if(posInicio>=posFin){
			Sensor sensorInicio = matrizSensores[posInicio][columna];

			Sensor aux = sensorInicio;
			//Si no es el primer sensor, le restamos el anterior
			if(posInicio != matrizSensores.length-1)
				aux = restarEstaciones(aux, matrizSensores[posInicio+1][columna]);

			int comprobar = aux.comprobarEstado(limitesSuperados);
			if(comprobar>0){
				out.write("INFRACCION EN SENSOR "+sensorInicio.getNombre());
				out.write("\n");
				manejarBalsa(comprobar);
				out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorInicio.getNombre()));
				out.write("\n");
			}else{
				out.write("SENSOR CORRECTO "+sensorInicio.getNombre());
				out.write("\n");
			}
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[posMitad][columna];
			int comprobar = sensorMitad.comprobarEstado(limitesSuperados);
			if(comprobar==0){
				out.write("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				out.write("\n");
				DyVCallesSurv2(posInicio, posMitad-1, columna);
			}else{
				manejarBalsa(comprobar);
				DyVCallesSurv2(posMitad+1, posFin, columna);
				DyVCallesSurv2(posInicio, posMitad, columna);
			}
		}
	}

	private static void DyVCallesNortev2(int posInicio, int posFin, int columna) throws IOException {
		if(posInicio<=posFin){
			Sensor sensorFin = matrizSensores[posFin][columna];
			Sensor aux = sensorFin;
			if(posFin != 0)
				aux = restarEstaciones(aux, matrizSensores[posFin-1][columna]);

			//Si estamos en la avenida la restamos el sensor anterior si no es el último
			if(posFin == matrizSensores.length/2){
				if(columna != matrizSensores[0].length-1)
					aux = restarEstaciones(aux, matrizSensores[posFin][columna+1]);
				aux = restarEstaciones(aux, matrizSensores[posFin+1][columna]);
			}

			int comprobar = aux.comprobarEstado(limitesSuperados);
			if(comprobar > 0){
				out.write("INFRACCION EN SENSOR "+sensorFin.getNombre());
				out.write("\n");
				manejarBalsa(comprobar);
				out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorFin.getNombre()));
				out.write("\n");
			}else{
				out.write("SENSOR CORRECTO "+sensorFin.getNombre());
				out.write("\n");
			}
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[posMitad][columna];
			int comprobar = sensorMitad.comprobarEstado(limitesSuperados);
			if(comprobar==0){
				out.write("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				out.write("\n");
				DyVCallesNortev2(posInicio, posMitad+1, columna);
			}else{
				manejarBalsa(comprobar);
				DyVCallesNortev2(posMitad, posFin, columna);
				DyVCallesNortev2(posInicio, posMitad+1, columna);
			}
		}
	}





	private static void DYVv3() throws IOException{
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		out.write("Metodo elegido DyV v3");
		out.write("\n");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		if(limitesSuperados.size()>0)
			DyVAvenidav3(0,matrizSensores[0].length-1);
		else
			out.write("No hay ningún problema de contaminacion en el polígono.");
		out.write("\n");
	}


	private static void DyVAvenidav3(int posInicio, int posFin) throws IOException {
		if(posInicio>=posFin){
			DyVCallesv3(matrizSensores.length/2, 0, posInicio);
			DyVCallesv3(matrizSensores.length/2+1, matrizSensores.length-1, posInicio);
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[matrizSensores.length/2][posMitad];
			int comprobar = sensorMitad.comprobarEstado(limitesSuperados);
			if(comprobar==0){
				out.write("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				out.write("\n");
				DyVAvenidav3(posInicio, posMitad-1);
			}else{
				manejarBalsa(comprobar);
				DyVAvenidav3(posMitad+1, posFin);
				DyVAvenidav3(posInicio, posMitad);
			}
		}
	}


	private static void DyVCallesv3(int posInicio, int posFin, int columna) throws IOException {
		//True zona norte, false zona sur
		boolean calleN;
		int comprobar;

		if(posInicio <= matrizSensores.length/2)
			calleN = true;
		else
			calleN = false;

		if(!calleN){
			if(posInicio>=posFin){
				Sensor sensorInicio = matrizSensores[posInicio][columna];
				Sensor aux = sensorInicio;
				//Si no es el primer sensor, le restamos el anterior
				if(posInicio != matrizSensores.length-1)
					aux = restarEstaciones(aux, matrizSensores[posInicio+1][columna]);

				comprobar = aux.comprobarEstado(limitesSuperados);
				if(comprobar>0){
					out.write("INFRACCION EN SENSOR "+sensorInicio.getNombre());
					out.write("\n");
					manejarBalsa(comprobar);
					out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorInicio.getNombre()));
					out.write("\n");
				}else{
					out.write("SENSOR CORRECTO "+sensorInicio.getNombre());
					out.write("\n");
				}
			}else{
				int posMitad = (posInicio + posFin)/2;
				Sensor sensorMitad = matrizSensores[posMitad][columna];
				comprobar = sensorMitad.comprobarEstado(limitesSuperados);
				if(comprobar==0){
					out.write("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
					out.write("\n");
					DyVCallesv3(posInicio, posMitad-1, columna);
				}else{
					manejarBalsa(comprobar);
					DyVCallesv3(posMitad+1, posFin, columna);
					DyVCallesv3(posInicio, posMitad, columna);
				}
			}
		}else{
			if(posInicio<=posFin){
				Sensor sensorFin = matrizSensores[posFin][columna];
				Sensor aux = sensorFin;
				if(posFin != 0)
					aux = restarEstaciones(aux, matrizSensores[posFin-1][columna]);

				//Si estamos en la avenida la restamos el sensor anterior si no es el último
				if(posFin == matrizSensores.length/2){
					if(columna != matrizSensores[0].length-1)
						aux = restarEstaciones(aux, matrizSensores[posFin][columna+1]);
					aux = restarEstaciones(aux, matrizSensores[posFin+1][columna]);
				}

				comprobar = aux.comprobarEstado(limitesSuperados);
				if(comprobar > 0){
					out.write("INFRACCION EN SENSOR "+sensorFin.getNombre());
					out.write("\n");
					manejarBalsa(comprobar);
					out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorFin.getNombre()));
					out.write("\n");
				}else{
					out.write("SENSOR CORRECTO "+sensorFin.getNombre());
					out.write("\n");
				}
			}else{
				int posMitad = (posInicio + posFin)/2;
				Sensor sensorMitad = matrizSensores[posMitad][columna];
				comprobar = sensorMitad.comprobarEstado(limitesSuperados);
				if(comprobar==0){
					out.write("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
					out.write("\n");
					DyVCallesv3(posInicio, posMitad+1, columna);
				}else{
					manejarBalsa(comprobar);
					DyVCallesv3(posMitad, posFin, columna);
					DyVCallesv3(posInicio, posMitad+1, columna);
				}
			}

		}
	}


	private static void DYVv4() throws IOException{
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		out.write("Metodo elegido DyV v4");
		out.write("\n");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		if(limitesSuperados.size()>0)
			DyVAvenidav4(0,matrizSensores[0].length-1);
		else
			out.write("No hay ningún problema de contaminacion en el polígono.");
		out.write("\n");
	}


	private static void DyVAvenidav4(int posInicio, int posFin) throws IOException {
		if(posInicio>=posFin){
			Sensor aux = matrizSensores[matrizSensores.length/2-1][posInicio];
			if(aux.comprobarEstado(limitesSuperados)!=0)
				DyVCallesv4(matrizSensores.length/2-1, 0, posInicio);
			else{
				out.write("SENSOR CORRECTO A PARTIR DE "+aux.getNombre());
				out.write("\n");
			}

			aux = matrizSensores[matrizSensores.length/2+1][posInicio];
			if(aux.comprobarEstado(limitesSuperados) != 0)
				DyVCallesv4(matrizSensores.length/2+1, matrizSensores.length-1, posInicio);
			else{
				out.write("SENSOR CORRECTO A PARTIR DE "+aux.getNombre());
				out.write("\n");
			}

			DyVCallesv4(matrizSensores.length/2, matrizSensores.length/2, posInicio);
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[matrizSensores.length/2][posMitad];
			int comprobar = sensorMitad.comprobarEstado(limitesSuperados);
			if(comprobar==0){
				out.write("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				out.write("\n");
				DyVAvenidav4(posInicio, posMitad-1);
			}else{
				manejarBalsa(comprobar);
				DyVAvenidav4(posMitad+1, posFin);
				DyVAvenidav4(posInicio, posMitad);
			}
		}
	}


	private static void DyVCallesv4(int posInicio, int posFin, int columna) throws IOException {
		//True zona norte, false zona sur
		boolean calleN;
		int comprobar;

		if(posInicio <= matrizSensores.length/2)
			calleN = true;
		else
			calleN = false;

		if(!calleN){
			if(posInicio>=posFin){
				Sensor sensorInicio = matrizSensores[posInicio][columna];
				Sensor aux = sensorInicio;
				//Si no es el primer sensor, le restamos el anterior
				if(posInicio != matrizSensores.length-1)
					aux = restarEstaciones(aux, matrizSensores[posInicio+1][columna]);

				comprobar = aux.comprobarEstado(limitesSuperados);
				if(comprobar>0){
					out.write("INFRACCION EN SENSOR "+sensorInicio.getNombre());
					out.write("\n");
					manejarBalsa(comprobar);
					out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorInicio.getNombre()));
					out.write("\n");
				}else{
					out.write("SENSOR CORRECTO "+sensorInicio.getNombre());
					out.write("\n");
				}
			}else{
				int posMitad = (posInicio + posFin)/2;
				Sensor sensorMitad = matrizSensores[posMitad][columna];
				comprobar = sensorMitad.comprobarEstado(limitesSuperados);
				if(comprobar==0){
					out.write("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
					out.write("\n");
					DyVCallesv4(posInicio, posMitad-1, columna);
				}else{
					manejarBalsa(comprobar);
					DyVCallesv4(posMitad+1, posFin, columna);
					DyVCallesv4(posInicio, posMitad, columna);
				}
			}
		}else{
			if(posInicio<=posFin){
				Sensor sensorFin = matrizSensores[posFin][columna];
				Sensor aux = sensorFin;
				if(posFin != 0)
					aux = restarEstaciones(aux, matrizSensores[posFin-1][columna]);

				//Si estamos en la avenida la restamos el sensor anterior si no es el último
				if(posFin == matrizSensores.length/2){
					if(columna != matrizSensores[0].length-1)
						aux = restarEstaciones(aux, matrizSensores[posFin][columna+1]);
					aux = restarEstaciones(aux, matrizSensores[posFin+1][columna]);
				}

				comprobar = aux.comprobarEstado(limitesSuperados);
				if(comprobar > 0){
					out.write("INFRACCION EN SENSOR "+sensorFin.getNombre());
					out.write("\n");
					manejarBalsa(comprobar);
					out.write("Las empresas culpables son: "+sensorEmpresas.get(sensorFin.getNombre()));
					out.write("\n");
				}else{
					out.write("SENSOR CORRECTO "+sensorFin.getNombre());
					out.write("\n");
				}
			}else{
				int posMitad = (posInicio + posFin)/2;
				Sensor sensorMitad = matrizSensores[posMitad][columna];
				comprobar = sensorMitad.comprobarEstado(limitesSuperados);
				if(comprobar==0){
					out.write("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
					out.write("\n");
					DyVCallesv4(posInicio, posMitad+1, columna);
				}else{
					manejarBalsa(comprobar);
					DyVCallesv4(posMitad, posFin, columna);
					DyVCallesv4(posInicio, posMitad+1, columna);
				}
			}

		}
	}


	private static void Greedy1v1() throws IOException{
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		out.write("Metodo elegido Greedy1 v1");
		out.write("\n");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		if(limitesSuperados.size()>0){

			out.write(Greedy1v1(matrizSensores).toString());
			out.write("\n");
		}

		else
			out.write("No hay ningún problema de contaminacion en el polígono.");
		out.write("\n");
	}

	private static HashSet<String> Greedy1v1(Sensor[][] matriz) throws IOException{
		int medio = matriz.length/2;
		HashSet<String> culpables = new HashSet<String>();
		Sensor resultado = new Sensor();
		HashSet<Integer> callesCandidatas = new HashSet<Integer>();
		int comprobar = 0;

		//Selecciona las calles conflictivas
		for(int i = 0; i < matriz[medio].length; i++){
			resultado = matriz[medio][i];
			//Comprueba si hay un problema en ese sensor
			comprobar = resultado.comprobarEstado(limitesSuperados);
			if(comprobar != 0){
				manejarBalsa(comprobar);

				//Calcula lo que vierte esa calle
				if(i != matriz[medio].length-1)
					resultado = restarEstaciones(resultado, matriz[medio][i+1]);
				//Comprueba si esa calle está contaminando
				if(resultado.comprobarEstado(limitesSuperados) > 0){
					callesCandidatas.add(i);
					out.write("Se tienen que cerrar las empresas que vierten a la calle "+(i+1));
					out.write("\n");
				}else{
					out.write("Las empresas que vierten a la calle "+(i+1)+" pueden trabajar");
					out.write("\n");
				}
			}else{
				out.write("Correcto a partir del sensor "+resultado.getNombre());
				out.write("\n");
				break;
			}
		}

		//Detecta los sensores con incidencias de las calles candidatas
		for(Integer calle : callesCandidatas){
			comprobarBalsaLLena();

			//Comprueba los sensores de la Zona norte
			for (int i = 0; i < medio; i++){
				resultado = matriz[i][calle];
				if(i != 0)
					resultado = restarEstaciones(resultado, matriz[i-1][calle]);

				if(resultado.comprobarEstado(limitesSuperados) > 0){
					out.write("Problema en sensor: "+matriz[i][calle].getNombre());
					out.write("\n");
					culpables.addAll(sensorEmpresas.get(matriz[i][calle].getNombre()));
				}else{
					out.write("Sensor correcto: "+matriz[i][calle].getNombre());
					out.write("\n");
				}
			}

			//Comprueba los sensores de la Zona Sur
			for (int i = matriz.length-1; i > medio; i--){
				resultado = matriz[i][calle];
				if(i != matriz.length-1)
					resultado = restarEstaciones(resultado, matriz[i+1][calle]);

				if(resultado.comprobarEstado(limitesSuperados) > 0){
					out.write("Problema en sensor: "+matriz[i][calle].getNombre());
					out.write("\n");
					culpables.addAll(sensorEmpresas.get(matriz[i][calle].getNombre()));
				}else{
					out.write("Sensor correcto: "+matriz[i][calle].getNombre());
					out.write("\n");
				}
			}


			//Comprueba los sensores de la Avenida
			resultado = matriz[medio][calle];
			if(calle != matriz[medio].length-1)
				resultado = restarEstaciones(resultado, matriz[medio][calle+1]);

			//Restamos zona norte
			resultado = restarEstaciones(resultado, matriz[medio-1][calle]);

			//Restamos zona sur
			resultado = restarEstaciones(resultado, matriz[medio+1][calle]);

			if(resultado.comprobarEstado(limitesSuperados) > 0){
				out.write("Problema en sensor: "+matriz[medio][calle].getNombre());
				out.write("\n");
				culpables.addAll(sensorEmpresas.get(matriz[medio][calle].getNombre()));
			}else{
				out.write("Sensor correcto: "+matriz[medio][calle].getNombre());
				out.write("\n");
			}
		}

		//Devuelve todas las empresas culpables
		return culpables;
	}


	private static void Greedy1v2() throws IOException{
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		out.write("Metodo elegido Greedy1 v2");
		out.write("\n");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		if(limitesSuperados.size()>0){
			out.write(Greedy1v2(matrizSensores).toString());
			out.write("\n");
		}

		else{
			out.write("No hay ningún problema de contaminacion en el polígono.");
			out.write("\n");
		}
	}

	private static HashSet<String> Greedy1v2(Sensor[][] matriz) throws IOException{
		int medio = matriz.length/2;
		HashSet<String> culpables = new HashSet<String>();
		Sensor resultado = new Sensor();
		HashSet<Integer> callesCandidatas = new HashSet<Integer>();
		int comprobar = 0;

		//Selecciona las calles conflictivas, si empieza el bucle desde el principio se pueden descartar calles del final
		for(int i = 0; i < matriz[medio].length; i++){
			resultado = matriz[medio][i];
			//Comprueba si hay un problema en ese sensor
			comprobar = resultado.comprobarEstado(limitesSuperados);
			if(comprobar != 0){
				manejarBalsa(comprobar);

				//Calcula lo que vierte esa calle
				if(i != matriz[medio].length-1)
					resultado = restarEstaciones(resultado, matriz[medio][i+1]);
				//Comprueba si esa calle está contaminando
				if(resultado.comprobarEstado(limitesSuperados) > 0){
					callesCandidatas.add(i);
					out.write("Se tienen que cerrar las empresas que vierten a la calle "+(i+1));
					out.write("\n");
				}else{
					out.write("Las empresas que vierten a la calle "+(i+1)+" pueden trabajar");
					out.write("\n");
				}
			}else{
				out.write("Pueden trabajar las empresas que vierten a partir de la calle"+(i+1));
				out.write("\n");
				break;
			}

		}

		//Detecta los sensores con incidencias de las calles candidatas
		for(Integer calle : callesCandidatas){
			comprobarBalsaLLena();

			//Comprueba los sensores de la Zona norte
			for (int i = medio-1; i >= 0; i--){
				resultado = matriz[i][calle];
				if(resultado.comprobarEstado(limitesSuperados)==0){
					out.write("Sensor correcto a partir de: "+resultado.getNombre());
					out.write("\n");
					break;
				}else{

					if(i != 0)
						resultado = restarEstaciones(resultado, matriz[i-1][calle]);

					if(resultado.comprobarEstado(limitesSuperados) > 0){
						out.write("Problema en sensor: "+matriz[i][calle].getNombre());
						out.write("\n");
						culpables.addAll(sensorEmpresas.get(matriz[i][calle].getNombre()));
					}else{
						out.write("Sensor correcto: "+matriz[i][calle].getNombre());
						out.write("\n");
					}
				}
			}

			//Comprueba los sensores de la Zona Sur
			for (int i = medio+1; i < matriz.length; i++){
				resultado = matriz[i][calle];
				if(resultado.comprobarEstado(limitesSuperados)==0){
					out.write("Sensor correcto a partir de: "+resultado.getNombre());
					out.write("\n");
					break;
				}else{
					if(i != matriz.length-1)
						resultado = restarEstaciones(resultado, matriz[i+1][calle]);

					if(resultado.comprobarEstado(limitesSuperados) > 0){
						out.write("Problema en sensor: "+matriz[i][calle].getNombre());
						out.write("\n");
						culpables.addAll(sensorEmpresas.get(matriz[i][calle].getNombre()));
					}else{
						out.write("Sensor correcto: "+matriz[i][calle].getNombre());
						out.write("\n");
					}
				}
			}


			//Comprueba los sensores de la Avenida
			resultado = matriz[medio][calle];
			if(calle != matriz[medio].length-1)
				resultado = restarEstaciones(resultado, matriz[medio][calle+1]);

			//Restamos zona norte
			resultado = restarEstaciones(resultado, matriz[medio-1][calle]);

			//Restamos zona sur
			resultado = restarEstaciones(resultado, matriz[medio+1][calle]);

			if(resultado.comprobarEstado(limitesSuperados) > 0){
				out.write("Problema en sensor: "+matriz[medio][calle].getNombre());
				out.write("\n");
				culpables.addAll(sensorEmpresas.get(matriz[medio][calle].getNombre()));
			}else{
				out.write("Sensor correcto: "+matriz[medio][calle].getNombre());
				out.write("\n");
			}
		}

		//Devuelve todas las empresas culpables
		return culpables;
	}


	private static void Greedy2v1() throws IOException{
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		out.write("Metodo elegido Greedy2 v1");
		out.write("\n");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		Greedy2v1(matrizSensores, empresasCulpables, zonaGris);
	}

	private static void Greedy2v1(Sensor[][] matriz, HashSet<String> empresasCulpables, HashSet<String> zonaGris){
		int medio = matriz.length/2;
		HashSet<String> aux = new HashSet<String>();
		Sensor resultado = new Sensor();
		Sensor sensorMedida = new Sensor();
		String datos = "";

		//Comprueba la Zona norte
		for(int j = 0; j < matriz[0].length; j++){
			for (int i = 0; i < medio; i++){
				sensorMedida = resultado = matriz[i][j];
				if(i != 0)
					resultado = restarEstacionesConTodosContaminantes(resultado, matriz[i-1][j]);

				if(resultado.comprobarAlerta(limites) != null){
					aux.clear();
					aux.addAll(sensorEmpresas.get(sensorMedida.getNombre()));
					if(aux.size()==1){
						datos = aux.toString();
						datos += resultado.comprobarAlerta(limites);
						empresasCulpables.add(datos);
					}else
						zonaGris.add(aux.toString()+" "+resultado.comprobarAlerta(limites));

				}
			}


			//Comprueba la Zona Sur
			for (int i = matriz.length-1; i > medio; i--){
				sensorMedida = resultado = matriz[i][j];
				if(i != matriz.length-1)
					resultado = restarEstacionesConTodosContaminantes(resultado, matriz[i+1][j]);

				if(resultado.comprobarAlerta(limites) != null){
					aux.clear();
					aux.addAll(sensorEmpresas.get(sensorMedida.getNombre()));
					if(aux.size()==1){
						datos = aux.toString();
						datos += resultado.comprobarAlerta(limites);
						empresasCulpables.add(datos);
					}else
						zonaGris.add(aux.toString()+" "+resultado.comprobarAlerta(limites));
				}
			}


			//Comprueba la Avenida
			sensorMedida = resultado = matriz[medio][j];
			if(j != matriz[medio].length-1)
				resultado = restarEstacionesConTodosContaminantes(resultado, matriz[medio][j+1]);

			//Restamos zona norte
			resultado = restarEstacionesConTodosContaminantes(resultado, matriz[medio-1][j]);

			//Restamos zona sur
			resultado = restarEstacionesConTodosContaminantes(resultado, matriz[medio+1][j]);

			if(resultado.comprobarAlerta(limites) != null){
				aux.clear();
				aux.addAll(sensorEmpresas.get(sensorMedida.getNombre()));
				if(aux.size()==1){
					datos = aux.toString();
					datos += resultado.comprobarAlerta(limites);
					empresasCulpables.add(datos);
				}else
					zonaGris.add(aux.toString()+" "+resultado.comprobarAlerta(limites));

			}
		}
	}


	private static void Greedy2v2() throws IOException{
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		out.write("Metodo elegido Greedy2 v2");
		out.write("\n");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		Greedy2v2(matrizSensores, empresasCulpables, zonaGris);
	}

	private static void Greedy2v2(Sensor[][] matriz, HashSet<String> empresasCulpables, HashSet<String> zonaGris){
		int medio = matriz.length/2;
		HashSet<String> aux = new HashSet<String>();
		Sensor resultado = new Sensor();
		Sensor sensorMedida = new Sensor();
		String datos = "";
		greedy2v2.put("Empresas con problemas:", new HashSet<String>());
		greedy2v2.put("Zonas grises:", new HashSet<String>());
		//Comprueba la Zona norte
		for(int j = 0; j < matriz[0].length; j++){
			for (int i = 0; i < medio; i++){
				sensorMedida = resultado = matriz[i][j];
				if(i != 0)
					resultado = restarEstacionesConTodosContaminantes(resultado, matriz[i-1][j]);

				if(resultado.comprobarAlerta(limites) != null){
					aux.clear();
					aux.addAll(sensorEmpresas.get(sensorMedida.getNombre()));
					if(aux.size()==1){
						datos = aux.toString();
						datos += " "+resultado.comprobarAlerta(limites);
						greedy2v2.get("Empresas con problemas:").add(datos);
					}else{
						greedy2v2.get("Zonas grises:").add(aux.toString()+" "+resultado.comprobarAlerta(limites));
					}

				}
			}


			//Comprueba la Zona Sur
			for (int i = matriz.length-1; i > medio; i--){
				sensorMedida = resultado = matriz[i][j];
				if(i != matriz.length-1)
					resultado = restarEstacionesConTodosContaminantes(resultado, matriz[i+1][j]);

				if(resultado.comprobarAlerta(limites) != null){
					aux.clear();
					aux.addAll(sensorEmpresas.get(sensorMedida.getNombre()));
					if(aux.size()==1){
						datos = aux.toString();
						datos += " "+resultado.comprobarAlerta(limites);
						greedy2v2.get("Empresas con problemas:").add(datos);
					}else{
						greedy2v2.get("Zonas grises:").add(aux.toString()+" "+resultado.comprobarAlerta(limites));
					}

				}
			}


			//Comprueba la Avenida
			sensorMedida = resultado = matriz[medio][j];
			if(j != matriz[medio].length-1)
				resultado = restarEstacionesConTodosContaminantes(resultado, matriz[medio][j+1]);

			//Restamos zona norte
			resultado = restarEstacionesConTodosContaminantes(resultado, matriz[medio-1][j]);

			//Restamos zona sur
			resultado = restarEstacionesConTodosContaminantes(resultado, matriz[medio+1][j]);

			if(resultado.comprobarAlerta(limites) != null){
				aux.clear();
				aux.addAll(sensorEmpresas.get(sensorMedida.getNombre()));
				if(aux.size()==1){
					datos = aux.toString();
					datos += " "+resultado.comprobarAlerta(limites);
					greedy2v2.get("Empresas con problemas:").add(datos);
				}else{
					greedy2v2.get("Zonas grises:").add(aux.toString()+" "+resultado.comprobarAlerta(limites));
				}

			}
		}
	}


	private static void Greedy2v3() throws IOException{
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		out.write("Metodo elegido Greedy1 v1");
		out.write("\n");
		out.write("\n");
		out.write("***************************************************");
		out.write("\n");

		out.write("***************************************************");
		out.write("\n");

		Greedy2v3(matrizSensores, empresasCulpables, zonaGris);
	}

	private static void Greedy2v3(Sensor[][] matriz, HashSet<String> empresasCulpables, HashSet<String> zonaGris){
		int medio = matriz.length/2;
		HashSet<String> aux = new HashSet<String>();
		Sensor resultado = new Sensor();
		Sensor sensorMedida = new Sensor();
		String datos = "";

		
		for(int j = 0; j < matriz[0].length; j++){
			//Comprueba la Zona norte
			for (int i = 0; i < medio; i++){
				sensorMedida = resultado = matriz[i][j];
				if(i != 0)
					resultado = restarEstacionesConTodosContaminantes(resultado, matriz[i-1][j]);

				if(resultado.comprobarAlerta(limites) != null){
					aux.clear();
					aux.addAll(sensorEmpresas.get(sensorMedida.getNombre()));
					if(aux.size()==1){
						datos = aux.toString();
						datos += resultado.comprobarAlerta(limites);
						greedy2v3.add("Empresa culpable: "+datos);
					}else
						greedy2v3.add("Zona gris: "+aux.toString()+" "+resultado.comprobarAlerta(limites));

				}
			}


			//Comprueba la Zona Sur
			for (int i = matriz.length-1; i > medio; i--){
				sensorMedida = resultado = matriz[i][j];
				if(i != matriz.length-1)
					resultado = restarEstacionesConTodosContaminantes(resultado, matriz[i+1][j]);

				if(resultado.comprobarAlerta(limites) != null){
					aux.clear();
					aux.addAll(sensorEmpresas.get(sensorMedida.getNombre()));
					if(aux.size()==1){
						datos = aux.toString();
						datos += resultado.comprobarAlerta(limites);
						greedy2v3.add("Empresa culpable: "+datos);
					}else
						greedy2v3.add("Zona gris: "+aux.toString()+" "+resultado.comprobarAlerta(limites));
				}
			}


			//Comprueba la Avenida
			sensorMedida = resultado = matriz[medio][j];
			if(j != matriz[medio].length-1)
				resultado = restarEstacionesConTodosContaminantes(resultado, matriz[medio][j+1]);

			//Restamos zona norte
			resultado = restarEstacionesConTodosContaminantes(resultado, matriz[medio-1][j]);

			//Restamos zona sur
			resultado = restarEstacionesConTodosContaminantes(resultado, matriz[medio+1][j]);

			if(resultado.comprobarAlerta(limites) != null){
				aux.clear();
				aux.addAll(sensorEmpresas.get(sensorMedida.getNombre()));
				if(aux.size()==1){
					datos = aux.toString();
					datos += resultado.comprobarAlerta(limites);
					greedy2v3.add("Empresa culpable: "+datos);
				}else
					greedy2v3.add("Zona gris: "+aux.toString()+" "+resultado.comprobarAlerta(limites));

			}
		}
	}


	//Para calcular lo que se ha vertido en un sensor en concreto, restando solo los contaminantes con problemas
	private static Sensor restarEstaciones(Sensor restar, Sensor menor){
		if(restar.getFantasma() != null)
			return new Sensor();

		if(restar.getFlujo()==0.0 || menor.getFlujo() == 0.0)
			return restar;

		Sensor resultado = new Sensor("prueba");
		double flujo1, flujo2, cont1, cont2, difFlujo;
		flujo1=restar.getFlujo();
		flujo2=menor.getFlujo();
		difFlujo = flujo1-flujo2;
		HashMap<String, Double> concentracion1 = restar.getConcentracion();
		HashMap<String, Double> concentracion2 = menor.getConcentracion();
		if(difFlujo!=0.0)
			for(String it: limitesSuperados.keySet()){
				cont1 = concentracion1.get(it)*flujo1;
				cont2 = concentracion2.get(it)*flujo2;
				resultado.addContaminantes(it, (cont1-cont2)/(difFlujo));
			}

		resultado.setFlujo(difFlujo);
		return resultado;
	}


	//Para calcular lo que se ha vertido en un sensor en concreto, restando todos los contaminantes
	private static Sensor restarEstacionesConTodosContaminantes(Sensor restar, Sensor menor){
		if(restar.getFantasma() != null)
			return new Sensor();

		if(restar.getFlujo()==0.0 || menor.getFlujo() == 0.0){
			return restar;
		}

		Sensor resultado = new Sensor("prueba");
		double flujo1, flujo2, cont1, cont2, difFlujo;
		flujo1=restar.getFlujo();
		flujo2=menor.getFlujo();
		difFlujo = flujo1-flujo2;
		if(difFlujo!=0.0)
			for(Entry<String, Double> it: restar.getConcentracion().entrySet()){
				cont1 = it.getValue()*flujo1;
				cont2 = menor.getConcentracion().get(it.getKey())*flujo2;
				resultado.addContaminantes(it.getKey(), (cont1-cont2)/(difFlujo));
			}
		resultado.setFlujo(difFlujo);

		return resultado;
	}

	private static void manejarBalsa(int comprobar) throws IOException{
		if(comprobar != 0){
			if(comprobar == 1 && !riesgoCritico){
				if (balsa.isDesvioActivo()){
					//Mira si ya se ha superado el tiempo máximo de desvio
					if(!comprobarBalsaLLena()){
						out.write("El devio se encuentra activo, aun"
								+ " puede estar activado: "+(tiempoMax-(System.nanoTime()-tiempoInicio)/1000000000)+" segundos");
						out.write("\n");
					}else {
						out.write("Se ha llenado la balsa, se tiene que cerrar las zonas no exploradas");
						out.write("\n");
					}

				}else {
					tiempoInicio = System.nanoTime();
					out.write("Se activa desvio a la balsa "  );
					out.write("\n");
					balsa.setDesvioActivo(true);
					tiempoMax = (long) ((long) (balsa.getCapacidad()) / matrizSensores[matrizSensores.length/2][0].getFlujo());
					out.write("Tiempo máximo de desvio " + tiempoMax + " segundos");
					out.write("\n");
				}
			}else{
				riesgoCritico=true;
				if(balsa.isDesvioActivo())
					out.write("Se cancela el desvío, se cierran las zonas sin explorar");
				out.write("\n");
				balsa.setDesvioActivo(false);
			}
		}
	}

	private static boolean comprobarBalsaLLena(){
		if(balsa.isDesvioActivo()){
			tiempoActual = System.nanoTime();
			if((tiempoActual-tiempoInicio)/1000000000 > tiempoMax){
				balsa.setDesvioActivo(false);
				return true;
			}
		}
		return false;
	}

}