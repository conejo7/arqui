/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.webservice.service;

import ec.edu.espe.banquito.model.Cliente;
import ec.edu.espe.banquito.model.Cuenta;
import ec.edu.espe.banquito.model.Movimiento;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
@Stateless
@Path("ec.edu.espe.banquito.model.movimiento")
public class MovimientoFacadeREST extends AbstractFacade<Movimiento> {

    @Inject
    private ClienteFacadeREST clienteFacadeREST;
    
    @Inject
    private CuentaFacadeREST cuentaFacadeREST;
    
    @PersistenceContext(unitName = "LocalRESTful_SerBanQuitoPU")
    private EntityManager em;

    public MovimientoFacadeREST() {
        super(Movimiento.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Movimiento entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Movimiento entity) {
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
    public Movimiento find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Movimiento> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Movimiento> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("montomax/{cedula}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public BigDecimal obtenerMontoMaximo(@PathParam("cedula") String cedula) {
        Cliente cliente = this.clienteFacadeREST.findClienteByCedula(cedula);
        if (cliente != null) {
            BigDecimal promDep = promedioDepUltimos3Meses(this.cuentaFacadeREST.cuentaPorCliente(cliente));
            BigDecimal promRet = promedioRetUltimos3Meses(this.cuentaFacadeREST.cuentaPorCliente(cliente));
            BigDecimal promedios = (promDep.subtract(promRet)).abs();
            BigDecimal montoMax = ((promedios).multiply(BigDecimal.valueOf(0.6))).multiply(BigDecimal.valueOf(9.0));
            return montoMax.setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return null;
        }
    }

    public boolean depositoUltimoMes(Cuenta cuenta) {
        Query query = em.createQuery("SELECT obj FROM Movimiento obj WHERE obj.numCuenta = ?1 AND "
                + "obj.tipo = 'DEP'");
        query.setParameter(1, cuenta);
        List<Movimiento> mvs = query.getResultList();

        if (mvs.size() > 0) {
            Calendar cal = Calendar.getInstance();
            Date date = new Date();

            cal.setTime(date);
            int monthActual = cal.get(Calendar.MONTH);
            int monthDep;

            for (Movimiento m : mvs) {
                cal.setTime(m.getFecha());
                monthDep = cal.get(Calendar.MONTH);
                if (monthActual == monthDep) {
                    return true;
                }
            }

        } else {
            return false;
        }
        return false;
    }

    public BigDecimal promedioDepUltimos3Meses(Cuenta cuenta) {
        Query query = em.createQuery("SELECT obj FROM Movimiento obj WHERE obj.numCuenta = ?1 AND "
                + "obj.tipo = 'DEP'");
        query.setParameter(1, cuenta);
        List<Movimiento> mvs = query.getResultList();
        BigDecimal suma = BigDecimal.valueOf(0.0);
        long cont = 0;

        if (mvs.size() > 0) {
            Calendar cal = Calendar.getInstance();
            Date date = new Date();

            cal.setTime(date);
            int monthActual = cal.get(Calendar.MONTH);
            int monthDep;

            for (Movimiento m : mvs) {
                cal.setTime(m.getFecha());
                monthDep = cal.get(Calendar.MONTH);
                if (monthDep >= (monthActual - 3) || monthDep <= monthActual) {
                    suma = suma.add(m.getValor());
                    cont++;
                }
            }
            return suma.divide(BigDecimal.valueOf(cont), RoundingMode.HALF_UP);
        } else {
            return null;
        }
    }

    public BigDecimal promedioRetUltimos3Meses(Cuenta cuenta) {
        Query query = em.createQuery("SELECT obj FROM Movimiento obj WHERE obj.numCuenta = ?1 AND "
                + "obj.tipo = 'RET'");
        query.setParameter(1, cuenta);
        List<Movimiento> mvs = query.getResultList();
        BigDecimal suma = BigDecimal.valueOf(0.0);
        long cont = 0;

        if (mvs.size() > 0) {
            Calendar cal = Calendar.getInstance();
            Date date = new Date();

            cal.setTime(date);
            int monthActual = cal.get(Calendar.MONTH);
            int monthDep;

            for (Movimiento m : mvs) {
                cal.setTime(m.getFecha());
                monthDep = cal.get(Calendar.MONTH);
                if (monthDep >= (monthActual - 3) || monthDep <= monthActual) {
                    suma = suma.add(m.getValor());
                    cont++;
                }
            }
            return suma.divide(BigDecimal.valueOf(cont), RoundingMode.HALF_UP);
        } else {
            return null;
        }
    }
}
