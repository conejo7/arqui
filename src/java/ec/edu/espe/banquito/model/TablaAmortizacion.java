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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jonathan
 */
@Entity
@Table(name = "tabla_amortizacion")
@XmlRootElement
public class TablaAmortizacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_TA", nullable = false)
    private Integer codTa;
    @Column(name = "CUOTA", precision = 10, scale = 2)
    private BigDecimal cuota;
    @JoinColumn(name = "COD_CREDITO", referencedColumnName = "COD_CREDITO")
    @ManyToOne
    private Credito codCredito;
    @OneToMany(mappedBy = "codTa")
    private List<Cuota> cuotaList;

    public TablaAmortizacion() {
    }

    public TablaAmortizacion(Integer codTa) {
        this.codTa = codTa;
    }

    public Integer getCodTa() {
        return codTa;
    }

    public void setCodTa(Integer codTa) {
        this.codTa = codTa;
    }

    public BigDecimal getCuota() {
        if (cuota != null) {
            return cuota.setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return cuota;
        }
    }

    public void setCuota(BigDecimal cuota) {
        this.cuota = cuota;
    }

    public Credito getCodCredito() {
        return codCredito;
    }

    public void setCodCredito(Credito codCredito) {
        this.codCredito = codCredito;
    }

    @XmlTransient
    public List<Cuota> getCuotaList() {
        return cuotaList;
    }

    public void setCuotaList(List<Cuota> cuotaList) {
        this.cuotaList = cuotaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codTa != null ? codTa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TablaAmortizacion)) {
            return false;
        }
        TablaAmortizacion other = (TablaAmortizacion) object;
        if ((this.codTa == null && other.codTa != null) || (this.codTa != null && !this.codTa.equals(other.codTa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe.banquito.model.TablaAmortizacion[ codTa=" + codTa + " ]";
    }

}
