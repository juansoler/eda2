package Practica;

public class Balsa {

	private double cantidad;
	private boolean desvioActivo;

	public Balsa(Double cantidad){
		this.setCantidad(cantidad);
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public boolean isDesvioActivo() {
		return desvioActivo;
	}

	public void setDesvioActivo(boolean desvioActivo) {
		this.desvioActivo = desvioActivo;
	}
}
