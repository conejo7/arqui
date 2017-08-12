/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.webservice.service;

import ec.edu.espe.banquito.model.Credito;
import ec.edu.espe.banquito.model.Cuota;
import ec.edu.espe.banquito.model.TablaAmortizacion;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jonathan
 */
@RequestScoped
@Path("ec.edu.espe.banquito.model.tablaamortizacion")
public class TablaAmortizacionFacadeREST extends AbstractFacade<TablaAmortizacion> {

    @Inject
    private CuotaFacadeREST cuotaFacadeREST;
    
    @Inject
    private CreditoFacadeREST creditoFacadeREST;
    
    @PersistenceContext(unitName = "LocalRESTful_SerBanQuitoPU")
    private EntityManager em;

    public TablaAmortizacionFacadeREST() {
        super(TablaAmortizacion.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TablaAmortizacion crear(TablaAmortizacion entity) {
        super.create(entity);
        return entity;
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, TablaAmortizacion entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TablaAmortizacion find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TablaAmortizacion> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TablaAmortizacion> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("create/{codCredito}/{costoElec}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TablaAmortizacion crearTablaAmortizacion(@PathParam("codCredito") Integer codCredito, @PathParam("costoElec") Double costoElec) {
        Credito credito = this.creditoFacadeREST.find(codCredito);
        BigDecimal valorElec = BigDecimal.valueOf(costoElec);
        
        TablaAmortizacion ta = new TablaAmortizacion();

        double tasaPeriodo = 0.16 / 12;
        double den = (1 - (Math.pow((1 + tasaPeriodo), -credito.getPlazo()))) / (tasaPeriodo);

        BigDecimal cuotaFija = valorElec.divide(BigDecimal.valueOf(den), RoundingMode.HALF_UP);

        ta.setCodCredito(credito);
        ta.setCuota(cuotaFija);
        ta = crear(ta);

        Cuota cuota = new Cuota();
        cuota.setCodTa(ta);
        cuota.setNumero(0);
        cuota.setSaldo(valorElec);
        this.cuotaFacadeREST.create(cuota);

        for (int i = 0; i < credito.getPlazo(); i++) {
            cuota = new Cuota();
            cuota.setCodTa(ta);
            cuota.setNumero(i + 1);
            cuota.setValor(cuotaFija);
            Cuota cuotaAnterior = this.cuotaFacadeREST.cuotaAnterior(ta, i);
            BigDecimal interes = cuotaAnterior.getSaldo().multiply(BigDecimal.valueOf(0.16 / 12));
            cuota.setInteres(interes);
            BigDecimal capitalPagado = cuotaFija.subtract(interes);
            cuota.setCapital(capitalPagado);
            cuota.setSaldo(cuotaAnterior.getSaldo().subtract(capitalPagado));
            this.cuotaFacadeREST.create(cuota);
        }
        return ta;
    }
    
}
