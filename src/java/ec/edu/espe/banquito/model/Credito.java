/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Jonathan
 */
@Entity
@Table(name = "credito")
public class Credito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_CREDITO", nullable = false)
    private Integer codCredito;
    @Column(name = "ESTADO", length = 1)
    private String estado;
    @Column(name = "MONTO_MAX", precision = 10, scale = 2)
    private BigDecimal montoMax;
    @Column(name = "PLAZO")
    private Integer plazo;
    @Column(name = "TIPO", length = 1)
    private String tipo;
    @Column(name = "TASA_INTERES", precision = 4, scale = 2)
    private BigDecimal tasaInteres;
    @OneToMany(mappedBy = "codCredito")
    private List<TablaAmortizacion> tablaAmortizacionList;
    @JoinColumn(name = "COD_CLIENTE", referencedColumnName = "COD_CLIENTE")
    @ManyToOne
    private Cliente codCliente;

    public Credito() {
    }

    public Credito(Integer codCredito) {
        this.codCredito = codCredito;
    }

    public Integer getCodCredito() {
        return codCredito;
    }

    public void setCodCredito(Integer codCredito) {
        this.codCredito = codCredito;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getMontoMax() {
        if (montoMax != null) {
            return montoMax.setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return montoMax;
        }
    }

    public void setMontoMax(BigDecimal montoMax) {
        this.montoMax = montoMax;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getTasaInteres() {
        if (tasaInteres != null) {
            return tasaInteres.setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return tasaInteres;
        }
    }

    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public List<TablaAmortizacion> getTablaAmortizacionList() {
        return tablaAmortizacionList;
    }

    public void setTablaAmortizacionList(List<TablaAmortizacion> tablaAmortizacionList) {
        this.tablaAmortizacionList = tablaAmortizacionList;
    }

    public Cliente getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Cliente codCliente) {
        this.codCliente = codCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codCredito != null ? codCredito.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Credito)) {
            return false;
        }
        Credito other = (Credito) object;
        if ((this.codCredito == null && other.codCredito != null) || (this.codCredito != null && !this.codCredito.equals(other.codCredito))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe.banquito.model.Credito[ codCredito=" + codCredito + " ]";
    }

}
