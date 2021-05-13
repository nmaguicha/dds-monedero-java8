package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;
  private Deposito depositoDelDia;
  private Extraccion extraccionDelDia;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
    depositoDelDia = new Deposito(LocalDate.now(),1000);
    extraccionDelDia = new Extraccion(LocalDate.now(), 100);
  }

  @Test
  void Poner() {
    cuenta.poner(1500);
    assertEquals(cuenta.getSaldo(),1500.0);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(cuenta.getSaldo(), 3856.0);
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  public void ExtraerTodoElSaldo(){
    cuenta.poner(200);
    cuenta.sacar(200);
    assertEquals(cuenta.getSaldo(),0);
  }

  @Test
  public void LuegoDeUnDepositoCalculoValor(){
    cuenta.setSaldo(100);
    assertEquals(depositoDelDia.calcularValor(cuenta.getSaldo()), 1100.0);
  }

  @Test
  public void LuegoDeExtraccionCalculoValor(){
    cuenta.setSaldo(100);
    assertEquals(extraccionDelDia.calcularValor(cuenta.getSaldo()), 0.0);
  }

  @Test
  public void MontoTotalExtraidoEnElDia(){
    cuenta.agregarMovimiento(depositoDelDia);
    cuenta.agregarMovimiento(extraccionDelDia);
    assertEquals(cuenta.getMontoExtraidoA(LocalDate.now()), 100);
  }


}