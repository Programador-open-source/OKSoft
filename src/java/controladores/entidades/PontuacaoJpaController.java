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
import entidades.Parametro;
import entidades.Avaliacao;
import entidades.Pontuacao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PROGRAMADOR
 */
public class PontuacaoJpaController implements Serializable {

    public PontuacaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pontuacao pontuacao) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Parametro idparametro = pontuacao.getIdparametro();
            if (idparametro != null) {
                idparametro = em.getReference(idparametro.getClass(), idparametro.getIdparam());
                pontuacao.setIdparametro(idparametro);
            }
            Avaliacao idaval = pontuacao.getIdaval();
            if (idaval != null) {
                idaval = em.getReference(idaval.getClass(), idaval.getIdaval());
                pontuacao.setIdaval(idaval);
            }
            em.persist(pontuacao);
            if (idparametro != null) {
                idparametro.getPontuacaoList().add(pontuacao);
                idparametro = em.merge(idparametro);
            }
            if (idaval != null) {
                idaval.getPontuacaoList().add(pontuacao);
                idaval = em.merge(idaval);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pontuacao pontuacao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pontuacao persistentPontuacao = em.find(Pontuacao.class, pontuacao.getIdpont());
            Parametro idparametroOld = persistentPontuacao.getIdparametro();
            Parametro idparametroNew = pontuacao.getIdparametro();
            Avaliacao idavalOld = persistentPontuacao.getIdaval();
            Avaliacao idavalNew = pontuacao.getIdaval();
            if (idparametroNew != null) {
                idparametroNew = em.getReference(idparametroNew.getClass(), idparametroNew.getIdparam());
                pontuacao.setIdparametro(idparametroNew);
            }
            if (idavalNew != null) {
                idavalNew = em.getReference(idavalNew.getClass(), idavalNew.getIdaval());
                pontuacao.setIdaval(idavalNew);
            }
            pontuacao = em.merge(pontuacao);
            if (idparametroOld != null && !idparametroOld.equals(idparametroNew)) {
                idparametroOld.getPontuacaoList().remove(pontuacao);
                idparametroOld = em.merge(idparametroOld);
            }
            if (idparametroNew != null && !idparametroNew.equals(idparametroOld)) {
                idparametroNew.getPontuacaoList().add(pontuacao);
                idparametroNew = em.merge(idparametroNew);
            }
            if (idavalOld != null && !idavalOld.equals(idavalNew)) {
                idavalOld.getPontuacaoList().remove(pontuacao);
                idavalOld = em.merge(idavalOld);
            }
            if (idavalNew != null && !idavalNew.equals(idavalOld)) {
                idavalNew.getPontuacaoList().add(pontuacao);
                idavalNew = em.merge(idavalNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pontuacao.getIdpont();
                if (findPontuacao(id) == null) {
                    throw new NonexistentEntityException("The pontuacao with id " + id + " no longer exists.");
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
            Pontuacao pontuacao;
            try {
                pontuacao = em.getReference(Pontuacao.class, id);
                pontuacao.getIdpont();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pontuacao with id " + id + " no longer exists.", enfe);
            }
            Parametro idparametro = pontuacao.getIdparametro();
            if (idparametro != null) {
                idparametro.getPontuacaoList().remove(pontuacao);
                idparametro = em.merge(idparametro);
            }
            Avaliacao idaval = pontuacao.getIdaval();
            if (idaval != null) {
                idaval.getPontuacaoList().remove(pontuacao);
                idaval = em.merge(idaval);
            }
            em.remove(pontuacao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pontuacao> findPontuacaoEntities() {
        return findPontuacaoEntities(true, -1, -1);
    }

    public List<Pontuacao> findPontuacaoEntities(int maxResults, int firstResult) {
        return findPontuacaoEntities(false, maxResults, firstResult);
    }

    private List<Pontuacao> findPontuacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pontuacao.class));
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

    public Pontuacao findPontuacao(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pontuacao.class, id);
        } finally {
            em.close();
        }
    }

    public int getPontuacaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pontuacao> rt = cq.from(Pontuacao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
