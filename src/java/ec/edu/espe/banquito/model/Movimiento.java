/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jonathan
 */
@Entity
@Table(name = "movimiento")
@XmlRootElement
public class Movimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_MOVIMIENTO", nullable = false)
    private Integer codMovimiento;
    @Column(name = "TIPO", length = 3)
    private String tipo;
    @Column(name = "VALOR", precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "NUM_CUENTA", referencedColumnName = "NUM_CUENTA")
    @ManyToOne
    private Cuenta numCuenta;

    public Movimiento() {
    }

    public Movimiento(Integer codMovimiento) {
        this.codMovimiento = codMovimiento;
    }

    public Integer getCodMovimiento() {
        return codMovimiento;
    }

    public void setCodMovimiento(Integer codMovimiento) {
        this.codMovimiento = codMovimiento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Cuenta getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(Cuenta numCuenta) {
        this.numCuenta = numCuenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codMovimiento != null ? codMovimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movimiento)) {
            return false;
        }
        Movimiento other = (Movimiento) object;
        if ((this.codMovimiento == null && other.codMovimiento != null) || (this.codMovimiento != null && !this.codMovimiento.equals(other.codMovimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe.banquito.model.Movimiento[ codMovimiento=" + codMovimiento + " ]";
    }

}
