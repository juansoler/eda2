package Practica;

public class Empresa {

	private String nombre;
	private boolean direccionVertido;


	public Empresa(String nombre, String direccionVertido){
		this.nombre=nombre;
		this.direccionVertido = direccionVertido.equals("R") ? true: false;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isDireccionVertido() {
		return direccionVertido;
	}

	public void setDireccionVertido(boolean direccionVertido) {
		this.direccionVertido = direccionVertido;
	}

	public String calcularUbicacion(){
		String ubicacion = "Avenida "+ this.nombre.charAt(0);
		if(ubicacion.charAt(1)=='N')
			ubicacion += ", Zona Norte, Calle "+this.nombre.charAt(2);
		else
			ubicacion += ", Zona Sur, Calle "+this.nombre.charAt(2);

		return ubicacion;
	}

	public String colectorVertido(){
		String ubicacion = "Avenida "+ this.nombre.subSequence(0, 1);
		if(direccionVertido)
			ubicacion += " "+(Integer.parseInt(this.nombre.charAt(this.nombre.length()-1)+"")+1);
		else
			ubicacion += " "+this.nombre.charAt(this.nombre.length()-1);

		return ubicacion;
	}

	public String toString(){
		String result = nombre;
		if(direccionVertido)
			result+= ", derecha";
		else
			result+= ", izquierda";

		return result;

	}

}
