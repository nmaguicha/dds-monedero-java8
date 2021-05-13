package dds.monedero.model;

import java.time.LocalDate;

public abstract class Movimiento {
  private LocalDate fecha;
  //En ningún lenguaje de programación usen jamás doubles para modelar dinero en el mundo real
  //siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares
  private double monto;

  public Movimiento() {}
  public Movimiento(LocalDate fecha, double monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public boolean esDeLaFecha(LocalDate fecha) { return this.fecha.equals(fecha); }

  //Al tener una clase depósito y una clase extracción no voy a necesitar estos métodos
  /*public boolean fueDepositado(LocalDate fecha) {
    return isDeposito() && esDeLaFecha(fecha);
  }

  public boolean fueExtraido(LocalDate fecha) {
    return isExtraccion() && esDeLaFecha(fecha);
  }

  public boolean isExtraccion() { return !esDeposito;} */

  public abstract boolean isDeposito();

  public abstract double calcularValor(double saldo);
}
