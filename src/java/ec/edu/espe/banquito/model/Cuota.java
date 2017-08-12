/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jonathan
 */
@Entity
@Table(name = "cuota")
@XmlRootElement
public class Cuota implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_CUOTA", nullable = false)
    private Integer codCuota;
    @Column(name = "NUMERO")
    private Integer numero;
    @Column(name = "VALOR", precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "INTERES", precision = 10, scale = 2)
    private BigDecimal interes;
    @Column(name = "CAPITAL", precision = 10, scale = 2)
    private BigDecimal capital;
    @Column(name = "SALDO", precision = 10, scale = 2)
    private BigDecimal saldo;
    @JoinColumn(name = "COD_TA", referencedColumnName = "COD_TA")
    @ManyToOne
    private TablaAmortizacion codTa;

    public Cuota() {
    }

    public Cuota(Integer codCuota) {
        this.codCuota = codCuota;
    }

    public Integer getCodCuota() {
        return codCuota;
    }

    public void setCodCuota(Integer codCuota) {
        this.codCuota = codCuota;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public BigDecimal getValor() {
        if (valor != null) {
            return valor.setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return valor;
        }
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getInteres() {
        if (interes != null) {
            return interes.setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return interes;
        }
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getCapital() {
        if (capital != null) {
            return capital.setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return capital;
        }
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public BigDecimal getSaldo() {
        if (saldo != null) {
            return saldo.setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return saldo;
        }
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public TablaAmortizacion getCodTa() {
        return codTa;
    }

    public void setCodTa(TablaAmortizacion codTa) {
        this.codTa = codTa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codCuota != null ? codCuota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuota)) {
            return false;
        }
        Cuota other = (Cuota) object;
        if ((this.codCuota == null && other.codCuota != null) || (this.codCuota != null && !this.codCuota.equals(other.codCuota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe.banquito.model.Cuota[ codCuota=" + codCuota + " ]";
    }

}
