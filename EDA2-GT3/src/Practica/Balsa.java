package Practica;

public class Balsa {

	private double capacidad;
	private boolean desvioActivo;

	public Balsa(){

	}

	public Balsa(Double capacidad){
		this.setCapacidad(capacidad);
	}

	public double getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(double capacidad) {
		this.capacidad = capacidad;
	}

	public boolean isDesvioActivo() {
		return desvioActivo;
	}

	public void setDesvioActivo(boolean desvioActivo) {
		this.desvioActivo = desvioActivo;
	}
}
