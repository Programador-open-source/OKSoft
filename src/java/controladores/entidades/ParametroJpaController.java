/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Indicador;
import entidades.Parametro;
import entidades.Pontuacao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PROGRAMADOR
 */
public class ParametroJpaController implements Serializable {

    public ParametroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Parametro parametro) {
        if (parametro.getPontuacaoList() == null) {
            parametro.setPontuacaoList(new ArrayList<Pontuacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Indicador idindicador = parametro.getIdindicador();
            if (idindicador != null) {
                idindicador = em.getReference(idindicador.getClass(), idindicador.getIdindic());
                parametro.setIdindicador(idindicador);
            }
            List<Pontuacao> attachedPontuacaoList = new ArrayList<Pontuacao>();
            for (Pontuacao pontuacaoListPontuacaoToAttach : parametro.getPontuacaoList()) {
                pontuacaoListPontuacaoToAttach = em.getReference(pontuacaoListPontuacaoToAttach.getClass(), pontuacaoListPontuacaoToAttach.getIdpont());
                attachedPontuacaoList.add(pontuacaoListPontuacaoToAttach);
            }
            parametro.setPontuacaoList(attachedPontuacaoList);
            em.persist(parametro);
            if (idindicador != null) {
                idindicador.getParametroList().add(parametro);
                idindicador = em.merge(idindicador);
            }
            for (Pontuacao pontuacaoListPontuacao : parametro.getPontuacaoList()) {
                Parametro oldIdparametroOfPontuacaoListPontuacao = pontuacaoListPontuacao.getIdparametro();
                pontuacaoListPontuacao.setIdparametro(parametro);
                pontuacaoListPontuacao = em.merge(pontuacaoListPontuacao);
                if (oldIdparametroOfPontuacaoListPontuacao != null) {
                    oldIdparametroOfPontuacaoListPontuacao.getPontuacaoList().remove(pontuacaoListPontuacao);
                    oldIdparametroOfPontuacaoListPontuacao = em.merge(oldIdparametroOfPontuacaoListPontuacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Parametro parametro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Parametro persistentParametro = em.find(Parametro.class, parametro.getIdparam());
            Indicador idindicadorOld = persistentParametro.getIdindicador();
            Indicador idindicadorNew = parametro.getIdindicador();
            List<Pontuacao> pontuacaoListOld = persistentParametro.getPontuacaoList();
            List<Pontuacao> pontuacaoListNew = parametro.getPontuacaoList();
            if (idindicadorNew != null) {
                idindicadorNew = em.getReference(idindicadorNew.getClass(), idindicadorNew.getIdindic());
                parametro.setIdindicador(idindicadorNew);
            }
            List<Pontuacao> attachedPontuacaoListNew = new ArrayList<Pontuacao>();
            for (Pontuacao pontuacaoListNewPontuacaoToAttach : pontuacaoListNew) {
                pontuacaoListNewPontuacaoToAttach = em.getReference(pontuacaoListNewPontuacaoToAttach.getClass(), pontuacaoListNewPontuacaoToAttach.getIdpont());
                attachedPontuacaoListNew.add(pontuacaoListNewPontuacaoToAttach);
            }
            pontuacaoListNew = attachedPontuacaoListNew;
            parametro.setPontuacaoList(pontuacaoListNew);
            parametro = em.merge(parametro);
            if (idindicadorOld != null && !idindicadorOld.equals(idindicadorNew)) {
                idindicadorOld.getParametroList().remove(parametro);
                idindicadorOld = em.merge(idindicadorOld);
            }
            if (idindicadorNew != null && !idindicadorNew.equals(idindicadorOld)) {
                idindicadorNew.getParametroList().add(parametro);
                idindicadorNew = em.merge(idindicadorNew);
            }
            for (Pontuacao pontuacaoListOldPontuacao : pontuacaoListOld) {
                if (!pontuacaoListNew.contains(pontuacaoListOldPontuacao)) {
                    pontuacaoListOldPontuacao.setIdparametro(null);
                    pontuacaoListOldPontuacao = em.merge(pontuacaoListOldPontuacao);
                }
            }
            for (Pontuacao pontuacaoListNewPontuacao : pontuacaoListNew) {
                if (!pontuacaoListOld.contains(pontuacaoListNewPontuacao)) {
                    Parametro oldIdparametroOfPontuacaoListNewPontuacao = pontuacaoListNewPontuacao.getIdparametro();
                    pontuacaoListNewPontuacao.setIdparametro(parametro);
                    pontuacaoListNewPontuacao = em.merge(pontuacaoListNewPontuacao);
                    if (oldIdparametroOfPontuacaoListNewPontuacao != null && !oldIdparametroOfPontuacaoListNewPontuacao.equals(parametro)) {
                        oldIdparametroOfPontuacaoListNewPontuacao.getPontuacaoList().remove(pontuacaoListNewPontuacao);
                        oldIdparametroOfPontuacaoListNewPontuacao = em.merge(oldIdparametroOfPontuacaoListNewPontuacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = parametro.getIdparam();
                if (findParametro(id) == null) {
                    throw new NonexistentEntityException("The parametro with id " + id + " no longer exists.");
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
            Parametro parametro;
            try {
                parametro = em.getReference(Parametro.class, id);
                parametro.getIdparam();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The parametro with id " + id + " no longer exists.", enfe);
            }
            Indicador idindicador = parametro.getIdindicador();
            if (idindicador != null) {
                idindicador.getParametroList().remove(parametro);
                idindicador = em.merge(idindicador);
            }
            List<Pontuacao> pontuacaoList = parametro.getPontuacaoList();
            for (Pontuacao pontuacaoListPontuacao : pontuacaoList) {
                pontuacaoListPontuacao.setIdparametro(null);
                pontuacaoListPontuacao = em.merge(pontuacaoListPontuacao);
            }
            em.remove(parametro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Parametro> findParametroEntities() {
        return findParametroEntities(true, -1, -1);
    }

    public List<Parametro> findParametroEntities(int maxResults, int firstResult) {
        return findParametroEntities(false, maxResults, firstResult);
    }

    private List<Parametro> findParametroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Parametro.class));
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

    public Parametro findParametro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Parametro.class, id);
        } finally {
            em.close();
        }
    }

    public int getParametroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Parametro> rt = cq.from(Parametro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
