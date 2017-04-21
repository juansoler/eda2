package Practica;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;

public class Depuradora {

	public static Sensor[][] matrizSensores = null;
	public static HashMap<String, HashSet<String>> sensorEmpresas = new HashMap<String, HashSet<String>>();
	private static HashMap<String, Double[]> limites = new HashMap<String, Double[]>();
	private static HashMap<String, Double[]> limitesSuperados = new HashMap<String, Double[]>();
	private static HashSet<String> empresasCulpables = new HashSet<String>();
	private static HashSet<String> zonaGris = new HashSet<String>();
	public static Balsa balsa = new Balsa(10.00);
	private static long tiempoInicio = 0;
	private static long tiempoActual = 0;
	private static long tiempoMax = 0;
	private static Scanner entrada;

	public static void main(String[] args) {

		String directorioEntrada = "";

		directorioEntrada = System.getProperty("user.dir") + File.separator +
				"src" + File.separator +
				"Practica" + File.separator + "Empresas12.txt";

		//Cargar las estructuras utilizadas
		matrizSensores = Cargar.loadFile(directorioEntrada, sensorEmpresas, limites);

		//Comprueba los contaminantes con problemas
		detectarContaminantes();

		entrada = new Scanner(System.in);

		for(Sensor it : matrizSensores[matrizSensores.length/2]){
			System.out.print("Sensor: "+it.getNombre());
			for(String s :it.getConcentracion().keySet())
				System.out.print("  "+s);
			System.out.println();
		}
			
		
		int opcion = 0;
		do{
			System.out.println("Seleccione la opción deseada: ");
			System.out.println("FuerzaBrutav1: 0  //  FuerzaBrutav2:1  //  DYVv1: 2  //  DYVv2: 3  //  DYVv3: 4  //  Greedyv1: 5  //  Greedy1v2: 6  //  Greedy2v1: 7");
			try{
				opcion = entrada.nextInt();
			}catch(Exception e){
				System.out.println("Has introducido un valor incorrecto");
			}
		}while(opcion < 0 || opcion > 7);


		switch(opcion){
		case 0: FuerzaBrutav1();
		break;
		case 1: FuerzaBrutav2();
		break;
		case 2: DYVv1();
		break;
		case 3: DYVv2();
		break;
		case 4: DYVv3();
		break;
		case 5: Greedy1v1();
		break;
		case 6: Greedy1v2();
		break;
		case 7: Greedy2v1();
		break;
		}

		for(String aux : empresasCulpables){
			System.out.println(aux);
		}
		int cont = 0;
		for(String aux : zonaGris)
			System.out.println("Zona gris: "+(cont++)+" "+aux);
	}


	//Detecta si en A1 se supera algun limite
	private static void detectarContaminantes(){
		limitesSuperados = matrizSensores[matrizSensores.length/2][0].determinarContaminantes(limites);
	}


	private static void FuerzaBrutav1(){
		for (int i = 0; i < matrizSensores.length; i++) {
			for (int j = 0; j < matrizSensores[0].length; j++) {
				Sensor sensor = matrizSensores[i][j];
				if(sensor.comprobarEstado(limitesSuperados)==0){
					System.out.println("SENSOR CORRECTO "+sensor.getNombre());
				}else{
					System.out.println("INFRACCION EN SENSOR "+sensor.getNombre());
					System.out.println("Las empresas culpables son: "+sensorEmpresas.get(sensor.getNombre()));
				}
			}
		}
	}


	private static void FuerzaBrutav2(){
		int medio = matrizSensores.length/2;
		Sensor resultado = new Sensor();
		Sensor sensorMedida = new Sensor();

		//Comprueba la Zona norte
		for(int j = 0; j < matrizSensores[0].length; j++){
			for (int i = 0; i < medio; i++){
				sensorMedida = resultado = matrizSensores[i][j];
				if(i != 0)
					resultado = restarEstacionesConTodosContaminantes(resultado, matrizSensores[i-1][j]);

				if(resultado.comprobarEstado(limitesSuperados) == 0){
					System.out.println("SENSOR CORRECTO "+sensorMedida.getNombre());
				}else{
					System.out.println("INFRACCION EN SENSOR "+sensorMedida.getNombre());
					System.out.println("Las empresas culpables son: "+sensorEmpresas.get(sensorMedida.getNombre()));
				}

			}


			//Comprueba la Zona Sur
			for (int i = matrizSensores.length-1; i > medio; i--){
				sensorMedida = resultado = matrizSensores[i][j];
				if(i != matrizSensores.length-1)
					resultado = restarEstacionesConTodosContaminantes(resultado, matrizSensores[i+1][j]);

				if(resultado.comprobarEstado(limitesSuperados) == 0){
					System.out.println("SENSOR CORRECTO "+sensorMedida.getNombre());
				}else{
					System.out.println("INFRACCION EN SENSOR "+sensorMedida.getNombre());
					System.out.println("Las empresas culpables son: "+sensorEmpresas.get(sensorMedida.getNombre()));
				}
			}


			//Comprueba la Avenida
			sensorMedida = resultado = matrizSensores[medio][j];
			if(j != matrizSensores[medio].length-1)
				resultado = restarEstacionesConTodosContaminantes(resultado, matrizSensores[medio][j+1]);

			//Restamos zona norte
			resultado = restarEstacionesConTodosContaminantes(resultado, matrizSensores[medio-1][j]);

			//Restamos zona sur
			resultado = restarEstacionesConTodosContaminantes(resultado, matrizSensores[medio+1][j]);

			if(resultado.comprobarEstado(limitesSuperados) == 0){
				System.out.println("SENSOR CORRECTO "+sensorMedida.getNombre());
			}else{
				System.out.println("INFRACCION EN SENSOR "+sensorMedida.getNombre());
				System.out.println("Las empresas culpables son: "+sensorEmpresas.get(sensorMedida.getNombre()));
			}

		}

	}


	private static void DYVv1(){
		DyVAvenidav1(0,matrizSensores[0].length-1);
	}

	private static void DyVAvenidav1(int posInicio, int posFin) {
		if(posInicio>=posFin){
			DyVCallesNortev1(matrizSensores.length/2, 0, posInicio);
			DyVCallesSurv1(matrizSensores.length/2+1, matrizSensores.length-1, posInicio);
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[matrizSensores.length/2][posMitad];
			if(sensorMitad.comprobarEstado(limitesSuperados)==0){
				System.out.println("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				DyVAvenidav1(posInicio, posMitad-1);
			}else{
				DyVAvenidav1(posMitad+1, posFin);
				DyVAvenidav1(posInicio, posMitad);
			}
		}
	}


	private static void DyVCallesSurv1(int posInicio, int posFin, int columna) {
		if(posInicio>=posFin){
			Sensor sensorInicio = matrizSensores[posInicio][columna];
			if(sensorInicio.comprobarEstado(limitesSuperados)>0){
				System.out.println("INFRACCION EN SENSOR "+sensorInicio.getNombre());
				System.out.println("Las empresas culpables son: "+sensorEmpresas.get(sensorInicio.getNombre()));
			}else{
				System.out.println("SENSOR CORRECTO "+sensorInicio.getNombre());
			}
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[posMitad][columna];
			if(sensorMitad.comprobarEstado(limitesSuperados)==0){
				System.out.println("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				DyVCallesSurv1(posInicio, posMitad-1, columna);
			}else{
				DyVCallesSurv1(posMitad+1, posFin, columna);
				DyVCallesSurv1(posInicio, posMitad, columna);
			}
		}

	}

	private static void DyVCallesNortev1(int posInicio, int posFin, int columna) {
		if(posInicio<=posFin){
			Sensor sensorFin = matrizSensores[posFin][columna];
			if(sensorFin.comprobarEstado(limitesSuperados)>0){
				System.out.println("INFRACCION EN SENSOR "+sensorFin.getNombre());
				System.out.println("Las empresas culpables son: "+sensorEmpresas.get(sensorFin.getNombre()));
			}else{
				System.out.println("SENSOR CORRECTO "+sensorFin.getNombre());
			}
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[posMitad][columna];
			if(sensorMitad.comprobarEstado(limitesSuperados)==0){
				System.out.println("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				DyVCallesNortev1(posInicio, posMitad+1, columna);
			}else{
				DyVCallesNortev1(posMitad, posFin, columna);
				DyVCallesNortev1(posInicio, posMitad+1, columna);
			}
		}

	}



	private static void DYVv2(){
		DyVAvenidav2(0,matrizSensores[0].length-1);
	}


	private static void DyVAvenidav2(int posInicio, int posFin) {
		if(posInicio>=posFin){
			DyVCallesNortev2(matrizSensores.length/2, 0, posInicio);
			DyVCallesSurv2(matrizSensores.length/2+1, matrizSensores.length-1, posInicio);
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[matrizSensores.length/2][posMitad];
			if(sensorMitad.comprobarEstado(limitesSuperados)==0){
				System.out.println("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				DyVAvenidav2(posInicio, posMitad-1);
			}else{
				DyVAvenidav2(posMitad+1, posFin);
				DyVAvenidav2(posInicio, posMitad);
			}
		}
	}


	private static void DyVCallesSurv2(int posInicio, int posFin, int columna) {
		if(posInicio>=posFin){
			Sensor sensorInicio = matrizSensores[posInicio][columna];

			Sensor aux = sensorInicio;
			//Si no es el primer sensor, le restamos el anterior
			if(posInicio != matrizSensores.length-1)
				aux = restarEstaciones(aux, matrizSensores[posInicio+1][columna]);

			if(aux.comprobarEstado(limitesSuperados)>0){
				System.out.println("INFRACCION EN SENSOR "+sensorInicio.getNombre());
				System.out.println("Las empresas culpables son: "+sensorEmpresas.get(sensorInicio.getNombre()));
			}else{
				System.out.println("DYVSUR SENSOR CORRECTO "+sensorInicio.getNombre());
			}
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[posMitad][columna];
			if(sensorMitad.comprobarEstado(limitesSuperados)==0){
				System.out.println("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				DyVCallesSurv2(posInicio, posMitad-1, columna);
			}else{
				DyVCallesSurv2(posMitad+1, posFin, columna);
				DyVCallesSurv2(posInicio, posMitad, columna);
			}
		}

	}

	private static void DyVCallesNortev2(int posInicio, int posFin, int columna) {
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


			if(aux.comprobarEstado(limitesSuperados)>0){
				System.out.println("INFRACCION EN SENSOR "+sensorFin.getNombre());
				System.out.println("Las empresas culpables son: "+sensorEmpresas.get(sensorFin.getNombre()));
			}else{
				System.out.println("SENSOR CORRECTO "+sensorFin.getNombre());
			}
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[posMitad][columna];
			if(sensorMitad.comprobarEstado(limitesSuperados)==0){
				System.out.println("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				DyVCallesNortev2(posInicio, posMitad+1, columna);
			}else{
				DyVCallesNortev2(posMitad, posFin, columna);
				DyVCallesNortev2(posInicio, posMitad+1, columna);
			}
		}
	}


	private static void DYVv3(){
		DyVAvenidav3(0,matrizSensores[0].length-1);
	}


	private static void DyVAvenidav3(int posInicio, int posFin) {
		if(posInicio>=posFin){
			DyVCallesv3(matrizSensores.length/2, 0, posInicio);
			DyVCallesv3(matrizSensores.length/2+1, matrizSensores.length-1, posInicio);
		}else{
			int posMitad = (posInicio + posFin)/2;
			Sensor sensorMitad = matrizSensores[matrizSensores.length/2][posMitad];
			if(sensorMitad.comprobarEstado(limitesSuperados)==0){
				System.out.println("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
				DyVAvenidav3(posInicio, posMitad-1);
			}else{
				DyVAvenidav3(posMitad+1, posFin);
				DyVAvenidav3(posInicio, posMitad);
			}
		}
	}


	private static void DyVCallesv3(int posInicio, int posFin, int columna) {
		//True zona norte, false zona sur
		boolean calle;

		if(posInicio <= matrizSensores.length/2)
			calle = true;
		else
			calle = false;

		if(!calle){
			if(posInicio>=posFin){
				Sensor sensorInicio = matrizSensores[posInicio][columna];
				Sensor aux = sensorInicio;
				//Si no es el primer sensor, le restamos el anterior
				if(posInicio != matrizSensores.length-1)
					aux = restarEstaciones(aux, matrizSensores[posInicio+1][columna]);

				if(aux.comprobarEstado(limitesSuperados)>0){
					System.out.println("INFRACCION EN SENSOR "+sensorInicio.getNombre());
					System.out.println("Las empresas culpables son: "+sensorEmpresas.get(sensorInicio.getNombre()));
				}else{
					System.out.println("DYVSUR SENSOR CORRECTO "+sensorInicio.getNombre());
				}
			}else{
				int posMitad = (posInicio + posFin)/2;
				Sensor sensorMitad = matrizSensores[posMitad][columna];
				if(sensorMitad.comprobarEstado(limitesSuperados)==0){
					System.out.println("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
					DyVCallesv3(posInicio, posMitad-1, columna);
				}else{
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


				if(aux.comprobarEstado(limitesSuperados)>0){
					System.out.println("INFRACCION EN SENSOR "+sensorFin.getNombre());
					System.out.println("Las empresas culpables son: "+sensorEmpresas.get(sensorFin.getNombre()));
				}else{
					System.out.println("SENSOR CORRECTO "+sensorFin.getNombre());
				}
			}else{
				int posMitad = (posInicio + posFin)/2;
				Sensor sensorMitad = matrizSensores[posMitad][columna];
				if(sensorMitad.comprobarEstado(limitesSuperados)==0){
					System.out.println("SENSOR CORRECTO A PARTIR DE "+sensorMitad.getNombre());
					DyVCallesv3(posInicio, posMitad+1, columna);
				}else{
					DyVCallesv3(posMitad, posFin, columna);
					DyVCallesv3(posInicio, posMitad+1, columna);
				}
			}

		}
	}


	private static void Greedy1v1(){
		System.out.println(Greedy1v1(matrizSensores).toString());
	}

	private static HashSet<String> Greedy1v1(Sensor[][] matriz){
		int medio = matriz.length/2;
		HashSet<String> culpables = new HashSet<String>();
		Sensor resultado = new Sensor();
		//Zona norte
		for(int j = 0; j < matriz[0].length; j++){
			for (int i = 0; i < medio; i++){
				resultado = matriz[i][j];
				if(i != 0)
					resultado = restarEstaciones(resultado, matriz[i-1][j]);

				if(resultado.comprobarEstado(limitesSuperados) > 0){
					System.out.println("Problema en sensor: "+matriz[i][j].getNombre());
					culpables.addAll(sensorEmpresas.get(matriz[i][j].getNombre()));
				}else{
					System.out.println("Sensor correcto: "+matriz[i][j].getNombre());
				}
			}
		}

		//Zona Sur
		for(int j = 0; j < matriz[0].length; j++){
			for (int i = matriz.length-1; i > medio; i--){
				resultado = matriz[i][j];
				if(i != matriz.length-1)
					resultado = restarEstaciones(resultado, matriz[i+1][j]);

				if(resultado.comprobarEstado(limitesSuperados) > 0){
					System.out.println("Problema en sensor: "+matriz[i][j].getNombre());
					culpables.addAll(sensorEmpresas.get(matriz[i][j].getNombre()));
				}else{
					System.out.println("Sensor correcto: "+matriz[i][j].getNombre());
				}
			}
		}

		//Avenida
		for(int i = matriz[medio].length-1; i >= 0; i--){
			resultado = matriz[medio][i];
			if(i != matriz[medio].length-1){
				resultado = restarEstaciones(resultado, matriz[medio][i+1]);
			}

			//Restamos zona norte
			resultado = restarEstaciones(resultado, matriz[medio-1][i]);

			//Restamos zona sur
			resultado = restarEstaciones(resultado, matriz[medio+1][i]);

			if(resultado.comprobarEstado(limitesSuperados) > 0){
				System.out.println("Problema en sensor: "+matriz[medio][i].getNombre());
				culpables.addAll(sensorEmpresas.get(matriz[medio][i].getNombre()));
			}else{
				System.out.println("Sensor correcto: "+matriz[medio][i].getNombre());
			}
		}

		return culpables;
	}


	private static void Greedy1v2(){
		System.out.println(Greedy1v2(matrizSensores).toString());
	}

	private static HashSet<String> Greedy1v2(Sensor[][] matriz){
		int medio = matriz.length/2;
		HashSet<String> culpables = new HashSet<String>();
		Sensor resultado = new Sensor();
		HashSet<Integer> callesCandidatas = new HashSet<Integer>();
		int comprobar = 0;
		boolean riesgoCritico = false;

		//Selecciona las calles conflictivas
		for(int i = matriz[medio].length-1; i >= 0; i--){
			resultado = matriz[medio][i];
			//Comrpueba si hay un problema en ese sensor
			comprobar = resultado.comprobarEstado(limitesSuperados);
			if(comprobar != 0){
				if(comprobar==1 && !riesgoCritico){
					tiempoInicio = System.nanoTime();
					if (balsa.isDesvioActivo()){
						System.out.println("El devio se encuentra activo");
					}else {
						System.out.println("Se activa desvio a la balsa "  );
						balsa.setDesvioActivo(true);
						tiempoMax = (long) ((long) (balsa.getCantidad()) / matrizSensores[matrizSensores.length/2][0].getFlujo());
						System.out.println("Tiempo máximo de desvio " + tiempoMax + " segundos");
					}
				}else{
					riesgoCritico=true;
					if(balsa.isDesvioActivo())
						System.out.println("Se cancela el desvío, se cierran las zonas sin explorar");
					balsa.setDesvioActivo(false);
				}

				//Calcula lo que vierte esa calle
				if(i != matriz[medio].length-1)
					resultado = restarEstaciones(resultado, matriz[medio][i+1]);
				//Comprueba si esa calle está contaminando
				if(resultado.comprobarEstado(limitesSuperados) > 0){
					callesCandidatas.add(i);
					System.out.println("Se tienen que cerrar las empresas que vierten a la calle "+(i+1));
				}else
					System.out.println("Las empresas que vierten a la calle "+(i+1)+" pueden trabajar");
			}
		}

		//Detecta los sensores con incidencias de las calles candidatas
		for(Integer calle : callesCandidatas){
			if(balsa.isDesvioActivo()){
				tiempoActual = System.nanoTime();
				if((tiempoActual-tiempoInicio) > tiempoMax){
					System.out.println("Se ha llenado la balsa, se tiene que cerrar las zonas no exploradas");
					balsa.setDesvioActivo(false);
				}
			}

			//Comprueba los sensores de la Zona norte
			for (int i = 0; i < medio; i++){
				resultado = matriz[i][calle];
				if(i != 0)
					resultado = restarEstaciones(resultado, matriz[i-1][calle]);

				if(resultado.comprobarEstado(limitesSuperados) > 0){
					System.out.println("Problema en sensor: "+matriz[i][calle].getNombre());
					culpables.addAll(sensorEmpresas.get(matriz[i][calle].getNombre()));
				}else{
					System.out.println("Sensor correcto: "+matriz[i][calle].getNombre());
				}
			}

			//Comprueba los sensores de la Zona Sur
			for (int i = matriz.length-1; i > medio; i--){
				resultado = matriz[i][calle];
				if(i != matriz.length-1)
					resultado = restarEstaciones(resultado, matriz[i+1][calle]);

				if(resultado.comprobarEstado(limitesSuperados) > 0){
					System.out.println("Problema en sensor: "+matriz[i][calle].getNombre());
					culpables.addAll(sensorEmpresas.get(matriz[i][calle].getNombre()));
				}else{
					System.out.println("Sensor correcto: "+matriz[i][calle].getNombre());
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
				System.out.println("Problema en sensor: "+matriz[medio][calle].getNombre());
				culpables.addAll(sensorEmpresas.get(matriz[medio][calle].getNombre()));
			}else{
				System.out.println("Sensor correcto: "+matriz[medio][calle].getNombre());
			}
		}

		//Devuelve todas las empresas culpables
		return culpables;
	}


	private static void Greedy2v1(){
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
					System.out.println(it);
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

}