/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.webservice.service;

import ec.edu.espe.banquito.model.Cliente;
import ec.edu.espe.banquito.model.Credito;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
@Path("ec.edu.espe.banquito.model.credito")
public class CreditoFacadeREST extends AbstractFacade<Credito> {

    @Inject
    private ClienteFacadeREST clienteFacadeREST;

    @Inject
    private MovimientoFacadeREST movimientoFacadeREST;

    @PersistenceContext(unitName = "LocalRESTful_SerBanQuitoPU")
    private EntityManager em;

    public CreditoFacadeREST() {
        super(Credito.class);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Credito entity) {
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
    public Credito find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credito> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credito> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("create/{cedula}/{plazo}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Credito create(@PathParam("cedula") String cedula, @PathParam("plazo") Integer plazo) {
        Cliente cliente = this.clienteFacadeREST.findClienteByCedula(cedula);
        if (cliente != null) {
            Credito cred = new Credito();
            cred.setCodCliente(cliente);
            cred.setEstado("A");
            cred.setMontoMax(this.movimientoFacadeREST.obtenerMontoMaximo(cliente.getCedula()));
            cred.setPlazo(plazo);
            cred.setTipo("F");
            cred.setTasaInteres(BigDecimal.valueOf(16.0));
            super.create(cred);
            return cred;
        } else {
            return null;
        }
    }

    public boolean existeCreditoActivo(Cliente cliente) {
        Query query = em.createQuery("SELECT obj FROM Credito obj WHERE obj.codCliente = ?1");
        query.setParameter(1, cliente);
        List<Credito> creditos = query.getResultList();

        if (creditos.size() > 0) {
            for (Credito c : creditos) {
                if ("A".equalsIgnoreCase(c.getEstado())) {
                    return true;
                }
            }
        }
        return false;
    }
}
