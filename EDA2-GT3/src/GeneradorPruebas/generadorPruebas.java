package GeneradorPruebas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class generadorPruebas {

	private static int contadorEmpresasAltas = 0;  // va contando el numero de contaminantes altos generados, cuando llegue al max no genera mas
	private static int numMaxEmpresasContaminantes = 0;   // seleciona el numero máximo de empresas con contaminantes altos.
	private static boolean contaminadorAltoPorEmpresa = false;  // si una empresa ya tiene un contaminante alto, no genera otro. // debe estar a false
	private static boolean todoIzquierda = false;
	private static boolean calleMitadFull = false;

	private static boolean peorCaso = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String directorio = System.getProperty("user.dir") + File.separator
				+ "src" + File.separator + "Practica" + File.separator;

		int numeroCalles = 10; // numero de calles
		int numeroAvenidas = 10;  // numero de avenidas
		String nombreFichero = directorio + "pruebaN7.txt";
		int numContaminantes = 2;  // numero de contaminantes
		int balsaCapacidad = 100000;   // capacidad de la balsa

		if (args.length == 0){
			generarFichero(nombreFichero, numeroCalles, numeroAvenidas, numContaminantes, balsaCapacidad);
		}else {
			if (Integer.parseInt(args[4]) == 0) balsaCapacidad = 100000; else balsaCapacidad = Integer.parseInt(args[4]);
			if (Integer.parseInt(args[5])==1) todoIzquierda = true;
			if (Integer.parseInt(args[6])==1) calleMitadFull = true;
			if (Integer.parseInt(args[7])==1) peorCaso = true;
			numMaxEmpresasContaminantes = Integer.parseInt(args[8]);

			generarFichero(directorio + args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]),balsaCapacidad);
		}
		// B= 66  Z=90


	}


	public static void generarFichero(String nombreFichero,
			int numeroCalles, int numeroAvenidas, int numContaminantes, int balsCapacidad) {
		try {
			int numeroAvenidasN, numeroAvenidasS;
			if (numeroAvenidas%2 !=0) {
				numeroAvenidasN = numeroAvenidas/2;
				numeroAvenidasS = numeroAvenidas/2;
			}else {
				numeroAvenidasN = numeroAvenidas/2;
				numeroAvenidasS = (numeroAvenidas/2)-1;
			}


			BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero));
			bw.write("@Avenidas\n");
			bw.write(numeroAvenidas + "\n");
			bw.write("@Calles\n");
			bw.write(numeroCalles+"\n");
			int contador = 0;
			int contadorAvenidas = 0;

			bw.write("@CapacidadBalsa\n");
			bw.write(balsCapacidad + "\n");

			// Zona norte
			bw.write("@MatrizSensores\n");
			for (int i = (65+numeroAvenidasN); i>65 ; i--){
				for(int j=1 ; j<numeroCalles+2; j++){
					bw.write(Character.toString((char)i)+"N"+j+ " ");
				}
				contador++;
				contadorAvenidas++;
				bw.write("\n");
				if (contador == numeroAvenidasN) break;
			}
			//Avenida
			contadorAvenidas++;

			for(int j=1 ; j<numeroCalles+2; j++){
				bw.write("A"+j+ " ");
			}
			bw.write("\n");

			//Zona sur
			contador = 0;

			for (int i = 66; i<91 ; i++){
				contadorAvenidas++;

				for(int j=1 ; j<numeroCalles+2; j++){
					bw.write(Character.toString((char)i)+"S"+j+ " ");
				}

				contador++;
				bw.write("\n");
				if (contadorAvenidas == (numeroAvenidasN+1+numeroAvenidasS)) break;



			}

			bw.write("\n");


			bw.write("@Limites\n");

			for (int i = 1; i<numContaminantes+1; i++){
				bw.write("Contaminante" + i + " " + generarLimiteDesvio() + " " + generarLimiteStop() + "\n");
			}

			bw.write("\n");


			bw.write("@Empresas\n");
			// ZONA NORTE
			contador = 0;

			for (int i=(65+numeroAvenidasN) ; i>65 ; i--){
				for(int j=1 ; j<numeroCalles+1; j++){

					if (calleMitadFull && j == numeroCalles/2){
						bw.write(Character.toString((char)i)+"N"+ " " + j+ " " + "r" + " " + generarFlujo() + " ");
					}else if (calleMitadFull && j == numeroCalles/2+1){
						bw.write(Character.toString((char)i)+"N"+ " " + j+ " " + "l" + " " + generarFlujo() + " ");

					}else
					bw.write(Character.toString((char)i)+"N"+ " " + j+ " " +  generarIzqDcha() + " " + generarFlujo() + " ");

					// numeroContaminantes 25
					int aux = (int)Math.floor(Math.random()*numContaminantes+1);

					for (int k = 1; k <= numContaminantes ; k++){

						if (peorCaso && (i == 65+numeroAvenidasN) && !contaminadorAltoPorEmpresa) {

							if (j == 1 && k == aux){
								bw.write(
										generarContaminanteAlto() + " ");
								System.out.println("1");


							}else
							if (j == numeroCalles && k == aux){
								bw.write(
										generarContaminanteAlto() + " ");
								System.out.println("2");

							}else {bw.write(
									generarContaminante() + " "
									);
								}

						}

						else{bw.write(
							generarContaminante() + " "
							);
						}
					}
					bw.write("\n");
					contaminadorAltoPorEmpresa = false;

				}
				contador++;
				contaminadorAltoPorEmpresa = false;
				peorCaso = true;
				bw.write("\n");
				if (contador == (numeroAvenidasN)) {
					break;
				}

			}

			bw.write("\n");

			// AVENIDA
			contador++;
			for(int j=1 ; j<numeroCalles+1; j++){
				if (calleMitadFull && j == numeroCalles/2){
					bw.write("A"+ " " + j+ " " +  "r" + " " + generarFlujo() + " ");

				}else if (calleMitadFull && j == numeroCalles/2+1){
					bw.write("A"+ " " + j+ " " +  "l" + " " + generarFlujo() + " ");

				}else
				bw.write("A"+ " " + j+ " " +  generarIzqDcha() + " " + generarFlujo() + " ");


				for (int k = 1; k <= numContaminantes ; k++){
					bw.write(
						generarContaminante() + " "
						);
				}
				bw.write("\n");
				contaminadorAltoPorEmpresa = false;

			}

			bw.write("\n");



			// ZONA SUR

						for (int i=66 ; i<90 ; i++){
							int aux = (int)Math.floor(Math.random()*numContaminantes+1);

							for(int j=1 ; j<numeroCalles+1; j++){
								if (calleMitadFull && j == numeroCalles/2){
									bw.write(Character.toString((char)i)+"S"+" " + j+ " " +  "r" + " " + generarFlujo() + " ");

								}else if (calleMitadFull && j == numeroCalles/2+1){
									bw.write(Character.toString((char)i)+"S"+" " + j+ " " +  "l" + " " + generarFlujo() + " ");

								}else
								bw.write(Character.toString((char)i)+"S"+" " + j+ " " +  generarIzqDcha() + " " + generarFlujo() + " ");


								for (int k = 1; k <= numContaminantes ; k++){
									if (peorCaso && (contador == numeroAvenidasN+1+numeroAvenidasS) && !contaminadorAltoPorEmpresa) {

										if (j == 1 && k == aux){
											bw.write(
													generarContaminanteAlto() + " ");
											System.out.println("3");
										}else
										if (j == numeroCalles && k == aux){
											bw.write(
													generarContaminanteAlto() + " ");
											System.out.println("4");

										}else {bw.write(
												generarContaminante() + " "
												);
											}

									}

									else{bw.write(
										generarContaminante() + " "
										);
									}
								}
								bw.write("\n");
								contaminadorAltoPorEmpresa = false;

							}
							contador++;
							contaminadorAltoPorEmpresa = false;

							bw.write("\n");
							if (contador == (numeroAvenidasN+1+numeroAvenidasS+1)) {
								break;
							}

						}


			bw.close();
		} catch (Exception e) {

		}
	}

	// genera aleatoriamente suministro a la izq o a la dch
	public static String generarIzqDcha(){
		Random random = new Random();
	    boolean aleatorio = random.nextBoolean();

	    if (todoIzquierda){
	    	return "l";
	    }

	    if (aleatorio) {
			return "r";
		}else return "l";


	}




	// genera contaminante, si ya ha alcanzado el max o ya ha generado uno en esa empresa, siempre genera bajo.
	public static double generarContaminante(){
		Random random = new Random();
	    boolean aleatorio = random.nextBoolean();
	    if (aleatorio) {
	    	if (peorCaso || contaminadorAltoPorEmpresa) return generarContaminanteBajo();
			if (contadorEmpresasAltas == numMaxEmpresasContaminantes || contaminadorAltoPorEmpresa) return generarContaminanteBajo();
			else return generarContaminanteAlto();
		}else return generarContaminanteBajo();

	}

	//genera un flujo aleatorio  entre los niveles indicados
	public static double generarFlujo(){

		double lower = 1;
		double upper = 100;
		double result = Math.random() * (upper - lower) + lower;

		return result;
	}

	// genera contaminante alto entre los niveles indicados, suma uno al contador y pone boolean a true
	// para que no se vuelva a generar para la misma empresa
	public static double generarContaminanteAlto(){

		double lower = 600;
		double upper = 900;
		double result = Math.random() * (upper - lower) + lower;
		contadorEmpresasAltas++;
		contaminadorAltoPorEmpresa = true;
		System.out.println("Contaminante Alto " + result);
		return result;
	}
	// genera contaminante bajo entre los niveles indicados

	public static double generarContaminanteBajo(){

		double lower = 0;
		double upper = 1;
		double result = Math.random() * (upper - lower) + lower;

		return result;
	}

	// genera limite para desvio en los niveles indicados

	public static double generarLimiteDesvio(){

		double lower = 1.1;
		double upper = 2;
		double result = Math.random() * (upper - lower) + lower;

		return result;
	}
	// genera limite critico para en los niveles indicados

	public static double generarLimiteStop(){

		double lower = 20;
		double upper = 50;
		double result = Math.random() * (upper - lower) + lower;

		return result;
	}




}
