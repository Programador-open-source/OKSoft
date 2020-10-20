/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import entidades.Avaliacao;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Departamento;
import entidades.Funcionario;
import entidades.Pontuacao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PROGRAMADOR
 */
public class AvaliacaoJpaController implements Serializable {

    public AvaliacaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Avaliacao avaliacao) {
        if (avaliacao.getPontuacaoList() == null) {
            avaliacao.setPontuacaoList(new ArrayList<Pontuacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento iddepart = avaliacao.getIddepart();
            if (iddepart != null) {
                iddepart = em.getReference(iddepart.getClass(), iddepart.getIddeparta());
                avaliacao.setIddepart(iddepart);
            }
            Funcionario idfunc = avaliacao.getIdfunc();
            if (idfunc != null) {
                idfunc = em.getReference(idfunc.getClass(), idfunc.getIdfunc());
                avaliacao.setIdfunc(idfunc);
            }
            List<Pontuacao> attachedPontuacaoList = new ArrayList<Pontuacao>();
            for (Pontuacao pontuacaoListPontuacaoToAttach : avaliacao.getPontuacaoList()) {
                pontuacaoListPontuacaoToAttach = em.getReference(pontuacaoListPontuacaoToAttach.getClass(), pontuacaoListPontuacaoToAttach.getIdpont());
                attachedPontuacaoList.add(pontuacaoListPontuacaoToAttach);
            }
            avaliacao.setPontuacaoList(attachedPontuacaoList);
            em.persist(avaliacao);
            if (iddepart != null) {
                iddepart.getAvaliacaoList().add(avaliacao);
                iddepart = em.merge(iddepart);
            }
            if (idfunc != null) {
                idfunc.getAvaliacaoList().add(avaliacao);
                idfunc = em.merge(idfunc);
            }
            for (Pontuacao pontuacaoListPontuacao : avaliacao.getPontuacaoList()) {
                Avaliacao oldIdavalOfPontuacaoListPontuacao = pontuacaoListPontuacao.getIdaval();
                pontuacaoListPontuacao.setIdaval(avaliacao);
                pontuacaoListPontuacao = em.merge(pontuacaoListPontuacao);
                if (oldIdavalOfPontuacaoListPontuacao != null) {
                    oldIdavalOfPontuacaoListPontuacao.getPontuacaoList().remove(pontuacaoListPontuacao);
                    oldIdavalOfPontuacaoListPontuacao = em.merge(oldIdavalOfPontuacaoListPontuacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Avaliacao avaliacao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Avaliacao persistentAvaliacao = em.find(Avaliacao.class, avaliacao.getIdaval());
            Departamento iddepartOld = persistentAvaliacao.getIddepart();
            Departamento iddepartNew = avaliacao.getIddepart();
            Funcionario idfuncOld = persistentAvaliacao.getIdfunc();
            Funcionario idfuncNew = avaliacao.getIdfunc();
            List<Pontuacao> pontuacaoListOld = persistentAvaliacao.getPontuacaoList();
            List<Pontuacao> pontuacaoListNew = avaliacao.getPontuacaoList();
            if (iddepartNew != null) {
                iddepartNew = em.getReference(iddepartNew.getClass(), iddepartNew.getIddeparta());
                avaliacao.setIddepart(iddepartNew);
            }
            if (idfuncNew != null) {
                idfuncNew = em.getReference(idfuncNew.getClass(), idfuncNew.getIdfunc());
                avaliacao.setIdfunc(idfuncNew);
            }
            List<Pontuacao> attachedPontuacaoListNew = new ArrayList<Pontuacao>();
            for (Pontuacao pontuacaoListNewPontuacaoToAttach : pontuacaoListNew) {
                pontuacaoListNewPontuacaoToAttach = em.getReference(pontuacaoListNewPontuacaoToAttach.getClass(), pontuacaoListNewPontuacaoToAttach.getIdpont());
                attachedPontuacaoListNew.add(pontuacaoListNewPontuacaoToAttach);
            }
            pontuacaoListNew = attachedPontuacaoListNew;
            avaliacao.setPontuacaoList(pontuacaoListNew);
            avaliacao = em.merge(avaliacao);
            if (iddepartOld != null && !iddepartOld.equals(iddepartNew)) {
                iddepartOld.getAvaliacaoList().remove(avaliacao);
                iddepartOld = em.merge(iddepartOld);
            }
            if (iddepartNew != null && !iddepartNew.equals(iddepartOld)) {
                iddepartNew.getAvaliacaoList().add(avaliacao);
                iddepartNew = em.merge(iddepartNew);
            }
            if (idfuncOld != null && !idfuncOld.equals(idfuncNew)) {
                idfuncOld.getAvaliacaoList().remove(avaliacao);
                idfuncOld = em.merge(idfuncOld);
            }
            if (idfuncNew != null && !idfuncNew.equals(idfuncOld)) {
                idfuncNew.getAvaliacaoList().add(avaliacao);
                idfuncNew = em.merge(idfuncNew);
            }
            for (Pontuacao pontuacaoListOldPontuacao : pontuacaoListOld) {
                if (!pontuacaoListNew.contains(pontuacaoListOldPontuacao)) {
                    pontuacaoListOldPontuacao.setIdaval(null);
                    pontuacaoListOldPontuacao = em.merge(pontuacaoListOldPontuacao);
                }
            }
            for (Pontuacao pontuacaoListNewPontuacao : pontuacaoListNew) {
                if (!pontuacaoListOld.contains(pontuacaoListNewPontuacao)) {
                    Avaliacao oldIdavalOfPontuacaoListNewPontuacao = pontuacaoListNewPontuacao.getIdaval();
                    pontuacaoListNewPontuacao.setIdaval(avaliacao);
                    pontuacaoListNewPontuacao = em.merge(pontuacaoListNewPontuacao);
                    if (oldIdavalOfPontuacaoListNewPontuacao != null && !oldIdavalOfPontuacaoListNewPontuacao.equals(avaliacao)) {
                        oldIdavalOfPontuacaoListNewPontuacao.getPontuacaoList().remove(pontuacaoListNewPontuacao);
                        oldIdavalOfPontuacaoListNewPontuacao = em.merge(oldIdavalOfPontuacaoListNewPontuacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = avaliacao.getIdaval();
                if (findAvaliacao(id) == null) {
                    throw new NonexistentEntityException("The avaliacao with id " + id + " no longer exists.");
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
            Avaliacao avaliacao;
            try {
                avaliacao = em.getReference(Avaliacao.class, id);
                avaliacao.getIdaval();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The avaliacao with id " + id + " no longer exists.", enfe);
            }
            Departamento iddepart = avaliacao.getIddepart();
            if (iddepart != null) {
                iddepart.getAvaliacaoList().remove(avaliacao);
                iddepart = em.merge(iddepart);
            }
            Funcionario idfunc = avaliacao.getIdfunc();
            if (idfunc != null) {
                idfunc.getAvaliacaoList().remove(avaliacao);
                idfunc = em.merge(idfunc);
            }
            List<Pontuacao> pontuacaoList = avaliacao.getPontuacaoList();
            for (Pontuacao pontuacaoListPontuacao : pontuacaoList) {
                pontuacaoListPontuacao.setIdaval(null);
                pontuacaoListPontuacao = em.merge(pontuacaoListPontuacao);
            }
            em.remove(avaliacao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Avaliacao> findAvaliacaoEntities() {
        return findAvaliacaoEntities(true, -1, -1);
    }

    public List<Avaliacao> findAvaliacaoEntities(int maxResults, int firstResult) {
        return findAvaliacaoEntities(false, maxResults, firstResult);
    }

    private List<Avaliacao> findAvaliacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Avaliacao.class));
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

    public Avaliacao findAvaliacao(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Avaliacao.class, id);
        } finally {
            em.close();
        }
    }

    public int getAvaliacaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Avaliacao> rt = cq.from(Avaliacao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
