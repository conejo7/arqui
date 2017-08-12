/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.webservice.service;

import ec.edu.espe.banquito.model.Cliente;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
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
@RequestScoped
@Path("ec.edu.espe.banquito.model.cliente")
public class ClienteFacadeREST extends AbstractFacade<Cliente> {

    @Inject
    private MovimientoFacadeREST movimientoFacadeREST;

    @Inject
    private CuentaFacadeREST cuentaFacadeREST;

    @Inject
    private CreditoFacadeREST creditoFacadeREST;

    @PersistenceContext(unitName = "LocalRESTful_SerBanQuitoPU")
    private EntityManager em;

    public ClienteFacadeREST() {
        super(Cliente.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Cliente entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Cliente entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Cliente find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Cliente> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Cliente> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("essujetocredito/{cedula}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String esSujetoCredito(@PathParam("cedula") String cedula) {
        Cliente cliente = findClienteByCedula(cedula);
        if (cliente != null) {
            Cliente cTemp = login(cliente.getCedula(), cliente.getClave());
            if (cTemp == null) {
                System.out.println("El cliente " + cliente.getCedula() + " no es cliente del Banco.");
                return Boolean.FALSE.toString();
            } else {
                if (!this.movimientoFacadeREST.depositoUltimoMes(this.cuentaFacadeREST.cuentaPorCliente(cliente))) {
                    System.out.println("El cliente " + cliente.getCedula() + " no tiene depósitos el último mes.");
                    return Boolean.FALSE.toString();
                } else {
                    Date date = new Date();
                    long actual = date.getTime();
                    long nacimiento = cliente.getFechaNacimiento().getTime();
                    long edad = actual - nacimiento;
                    if (cliente.getEstadoCivil() == "C") {
                        if (edad >= 25) {
                            if (this.creditoFacadeREST.existeCreditoActivo(cliente)) {
                                System.out.println("El cliente " + cliente.getCedula() + " tiene un crédito activo.");
                                return Boolean.FALSE.toString();
                            } else {
                                return Boolean.TRUE.toString();
                            }
                        } else {
                            System.out.println("El cliente " + cliente.getCedula() + " no cumple con la edad y estado civil.");
                            return Boolean.FALSE.toString();
                        }
                    } else {
                        if (this.creditoFacadeREST.existeCreditoActivo(cliente)) {
                            System.out.println("El cliente " + cliente.getCedula() + " tiene un crédito activo.");
                            return Boolean.FALSE.toString();
                        } else {
                            return Boolean.TRUE.toString();
                        }
                    }
                }
            }
        } else {
            return Boolean.FALSE.toString();
        }
    }

    public Cliente login(String userName, String clave) {
        Cliente user = findClienteByCedula(userName);
        if (user != null) {
            if (clave.equals(user.getClave())) {
                return user;
            }
        }
        return null;
    }
    
    @GET
    @Path("clienteCed/{cedula}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Cliente findClienteByCedula(@PathParam("cedula") String cedula) {
        Query query = em.createQuery("SELECT obj FROM Cliente obj WHERE obj.cedula = ?1");
        query.setParameter(1, cedula);
        List<Cliente> cliente = query.getResultList();
        
        if(cliente.size() > 0) {
            return cliente.get(0);
        } else
            return null;
    }
}
