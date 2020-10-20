/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import entidades.Indicador;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Parametro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PROGRAMADOR
 */
public class IndicadorJpaController implements Serializable {

    public IndicadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Indicador indicador) {
        if (indicador.getParametroList() == null) {
            indicador.setParametroList(new ArrayList<Parametro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Parametro> attachedParametroList = new ArrayList<Parametro>();
            for (Parametro parametroListParametroToAttach : indicador.getParametroList()) {
                parametroListParametroToAttach = em.getReference(parametroListParametroToAttach.getClass(), parametroListParametroToAttach.getIdparam());
                attachedParametroList.add(parametroListParametroToAttach);
            }
            indicador.setParametroList(attachedParametroList);
            em.persist(indicador);
            for (Parametro parametroListParametro : indicador.getParametroList()) {
                Indicador oldIdindicadorOfParametroListParametro = parametroListParametro.getIdindicador();
                parametroListParametro.setIdindicador(indicador);
                parametroListParametro = em.merge(parametroListParametro);
                if (oldIdindicadorOfParametroListParametro != null) {
                    oldIdindicadorOfParametroListParametro.getParametroList().remove(parametroListParametro);
                    oldIdindicadorOfParametroListParametro = em.merge(oldIdindicadorOfParametroListParametro);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Indicador indicador) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Indicador persistentIndicador = em.find(Indicador.class, indicador.getIdindic());
            List<Parametro> parametroListOld = persistentIndicador.getParametroList();
            List<Parametro> parametroListNew = indicador.getParametroList();
            List<Parametro> attachedParametroListNew = new ArrayList<Parametro>();
            for (Parametro parametroListNewParametroToAttach : parametroListNew) {
                parametroListNewParametroToAttach = em.getReference(parametroListNewParametroToAttach.getClass(), parametroListNewParametroToAttach.getIdparam());
                attachedParametroListNew.add(parametroListNewParametroToAttach);
            }
            parametroListNew = attachedParametroListNew;
            indicador.setParametroList(parametroListNew);
            indicador = em.merge(indicador);
            for (Parametro parametroListOldParametro : parametroListOld) {
                if (!parametroListNew.contains(parametroListOldParametro)) {
                    parametroListOldParametro.setIdindicador(null);
                    parametroListOldParametro = em.merge(parametroListOldParametro);
                }
            }
            for (Parametro parametroListNewParametro : parametroListNew) {
                if (!parametroListOld.contains(parametroListNewParametro)) {
                    Indicador oldIdindicadorOfParametroListNewParametro = parametroListNewParametro.getIdindicador();
                    parametroListNewParametro.setIdindicador(indicador);
                    parametroListNewParametro = em.merge(parametroListNewParametro);
                    if (oldIdindicadorOfParametroListNewParametro != null && !oldIdindicadorOfParametroListNewParametro.equals(indicador)) {
                        oldIdindicadorOfParametroListNewParametro.getParametroList().remove(parametroListNewParametro);
                        oldIdindicadorOfParametroListNewParametro = em.merge(oldIdindicadorOfParametroListNewParametro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = indicador.getIdindic();
                if (findIndicador(id) == null) {
                    throw new NonexistentEntityException("The indicador with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Indicador indicador;
            try {
                indicador = em.getReference(Indicador.class, id);
                indicador.getIdindic();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The indicador with id " + id + " no longer exists.", enfe);
            }
            List<Parametro> parametroList = indicador.getParametroList();
            for (Parametro parametroListParametro : parametroList) {
                parametroListParametro.setIdindicador(null);
                parametroListParametro = em.merge(parametroListParametro);
            }
            em.remove(indicador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Indicador> findIndicadorEntities() {
        return findIndicadorEntities(true, -1, -1);
    }

    public List<Indicador> findIndicadorEntities(int maxResults, int firstResult) {
        return findIndicadorEntities(false, maxResults, firstResult);
    }

    private List<Indicador> findIndicadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Indicador.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Indicador findIndicador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Indicador.class, id);
        } finally {
            em.close();
        }
    }

    public int getIndicadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Indicador> rt = cq.from(Indicador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
