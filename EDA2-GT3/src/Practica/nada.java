package Practica;

public class nada {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] aux = new int[3][3];
		int cont = 0;
		for(int i = 0; i < aux.length; i++){
			for(int j = 0; j < aux[0].length;j++){
				aux[j][i] = cont;
				cont++;
			}
		}
		
		for(int i = 0; i < aux.length; i++){
			for(int j = 0; j < aux[0].length;j++){
				System.out.print(aux[j][i] + "\t");
			}
			System.out.println();
		}
		
		//System.out.println(aux.toString());
	}

}
