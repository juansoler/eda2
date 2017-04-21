package generadorPruebas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class generadorPruebas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String directorio = System.getProperty("user.dir") + File.separator
				+ "src" + File.separator + "Practica" + File.separator;

		int numeroCalles = 25;
		int numeroAvenidas = 51;
		generarFichero(directorio + args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		// B= 66  Z=90
		
		
	}
	
	
	public static void generarFichero(String nombreFichero,
			int numeroCalles, int numeroAvenidas, int numContaminantes) {
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

			// Zona norte
			bw.write("@MatrizSensores\n");
			for (int i = (65+numeroAvenidasN); i>65 ; i--){
				for(int j=1 ; j<numeroCalles+1; j++){
					bw.write(Character.toString((char)i)+"N"+j+ " ");
				}
				contador++;
				contadorAvenidas++;
				bw.write("\n");
				if (contador == numeroAvenidasN) break;
			}
			//Avenida
			contadorAvenidas++;

			for(int j=1 ; j<numeroCalles+1; j++){
				bw.write("A"+j+ " ");
			}
			bw.write("\n");
			
			//Zona sur
			contador = 0;

			for (int i = 66; i<91 ; i++){
				contadorAvenidas++;

				for(int j=1 ; j<numeroCalles+1; j++){
					bw.write(Character.toString((char)i)+"S"+j+ " ");
				}
				
//				if (contadorAvenidas == numeroAvenidas-1) {
//					bw.write("\n");
//
//					int aux = i;
//					aux++;
//					for(int j=1 ; j<numeroCalles+1; j++){
//						bw.write(Character.toString((char)aux)+"S"+j+ " ");
//					}
//					System.out.println("Numero Avenidas " + numeroAvenidas  + "  ContadorAvenidas" + contadorAvenidas);
//
////					bw.write(Character.toString((char)i++)+"S"+numeroAvenidas+ " ");
//				}

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

					bw.write(Character.toString((char)i)+"N"+ " " + j+ " " +  generarIzqDcha() + " " + generarFlujo() + " ");
					
					// numeroContaminantes 25
					
					for (int k = 1; k < numContaminantes ; k++){
						bw.write(
							generarContaminante() + " " 
							);
					}
					bw.write("\n");
				}
				contador++;
				bw.write("\n");
				if (contador == (numeroAvenidasN)) {
					break;
				}
				
			}
			
			bw.write("\n");

			// AVENIDA 
			contador++;
			for(int j=1 ; j<numeroCalles+1; j++){

				bw.write("A"+ " " + j+ " " +  generarIzqDcha() + " " + generarFlujo() + " ");
				
				// numeroContaminantes 25
				
				for (int k = 1; k < numContaminantes ; k++){
					bw.write( 
						generarContaminante() + " " 
						);
				}
				bw.write("\n");
			}
			
			bw.write("\n");

			
			
			// ZONA SUR

						for (int i=66 ; i<90 ; i++){
							for(int j=1 ; j<numeroCalles+1; j++){

								bw.write(Character.toString((char)i)+"S"+" " + j+ " " +  generarIzqDcha() + " " + generarFlujo() + " ");
								
								// numeroContaminantes 25
								
								for (int k = 1; k < numContaminantes ; k++){
									bw.write(
										generarContaminante() + " " 
										);
								}
								bw.write("\n");
//								System.out.println(Character.toString((char)i)+"S"+j+"\n");
							}
							contador++;
							bw.write("\n");
							if (contador == (numeroAvenidasN+1+numeroAvenidasS)) {
								break;
							}
							
						}
			
			
//			bw.write(numeroIntercambiadores + "\n");
//			double presion = 400;
//			for (int i = 1; i <= numeroIntercambiadores * 2 + 2; i++) {
//				bw.write(presion + "\n");
//			}
			bw.close();
		} catch (Exception e) {

		}
	}
	
	public static String generarIzqDcha(){
		int resultado = (int) Math.round( Math.random() )  ;
		if (resultado == 0) {
			return "r";
		}else return "l";
		
	}
	
	public static double generarContaminante(){
		int resultado = (int) Math.round( Math.random() )  ;
		if (resultado == 0) {
			return generarContaminanteAlto();
		}else return generarContaminanteBajo();
		
	}
	
	public static double generarFlujo(){
			
		double lower = 10;
		double upper = 100;
		double result = Math.random() * (upper - lower) + lower;
		
		return result;
	}
	
	public static double generarContaminanteAlto(){
		
		double lower = 2;
		double upper = 3;
		double result = Math.random() * (upper - lower) + lower;
		
		return result;
	}
	public static double generarContaminanteBajo(){
		
		double lower = 0;
		double upper = 2;
		double result = Math.random() * (upper - lower) + lower;
		
		return result;
	}
	
	public static double generarLimiteDesvio(){
		
		double lower = 1;
		double upper = 2;
		double result = Math.random() * (upper - lower) + lower;
		
		return result;
	}

	public static double generarLimiteStop(){
		
		double lower = 2;
		double upper = 3;
		double result = Math.random() * (upper - lower) + lower;
		
		return result;
	}



	
}
