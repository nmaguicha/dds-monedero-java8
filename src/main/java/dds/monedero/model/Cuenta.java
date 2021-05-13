package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  public static final int LIMITE_DEPOSITO = 3;
  public static final double LIMITE_EXTRACCION = 1000;
  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(double cuanto) {
    validarMonto(cuanto);
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= LIMITE_DEPOSITO) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + LIMITE_DEPOSITO + " depositos diarios");
    }
    agregarMovimiento(new Deposito(LocalDate.now(), cuanto));
  }

  private void validarMonto(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void sacar(double cuanto) {
    validarMonto(cuanto);
    validarSaldoNegativo(cuanto);
    if (cuanto > limiteDeExtraccion()) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + LIMITE_EXTRACCION + " diarios, límite: " + limiteDeExtraccion());
    }
    agregarMovimiento(new Extraccion(LocalDate.now(), cuanto));
  }

  private double limiteDeExtraccion() {
    return LIMITE_EXTRACCION - getMontoExtraidoA(LocalDate.now());
  }

  private void validarSaldoNegativo(double cuanto) {
    if (this.getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + this.getSaldo() + " $");
    }
  }

  public void agregarMovimiento(Movimiento movimiento) {
    this.setSaldo(movimiento.calcularValor(this.getSaldo()));
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
