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
import entidades.Funcionario;
import java.util.ArrayList;
import java.util.List;
import entidades.Avaliacao;
import entidades.Departamento;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PROGRAMADOR
 */
public class DepartamentoJpaController implements Serializable {

    public DepartamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) {
        if (departamento.getFuncionarioList() == null) {
            departamento.setFuncionarioList(new ArrayList<Funcionario>());
        }
        if (departamento.getAvaliacaoList() == null) {
            departamento.setAvaliacaoList(new ArrayList<Avaliacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Funcionario> attachedFuncionarioList = new ArrayList<Funcionario>();
            for (Funcionario funcionarioListFuncionarioToAttach : departamento.getFuncionarioList()) {
                funcionarioListFuncionarioToAttach = em.getReference(funcionarioListFuncionarioToAttach.getClass(), funcionarioListFuncionarioToAttach.getIdfunc());
                attachedFuncionarioList.add(funcionarioListFuncionarioToAttach);
            }
            departamento.setFuncionarioList(attachedFuncionarioList);
            List<Avaliacao> attachedAvaliacaoList = new ArrayList<Avaliacao>();
            for (Avaliacao avaliacaoListAvaliacaoToAttach : departamento.getAvaliacaoList()) {
                avaliacaoListAvaliacaoToAttach = em.getReference(avaliacaoListAvaliacaoToAttach.getClass(), avaliacaoListAvaliacaoToAttach.getIdaval());
                attachedAvaliacaoList.add(avaliacaoListAvaliacaoToAttach);
            }
            departamento.setAvaliacaoList(attachedAvaliacaoList);
            em.persist(departamento);
            for (Funcionario funcionarioListFuncionario : departamento.getFuncionarioList()) {
                Departamento oldIddepartOfFuncionarioListFuncionario = funcionarioListFuncionario.getIddepart();
                funcionarioListFuncionario.setIddepart(departamento);
                funcionarioListFuncionario = em.merge(funcionarioListFuncionario);
                if (oldIddepartOfFuncionarioListFuncionario != null) {
                    oldIddepartOfFuncionarioListFuncionario.getFuncionarioList().remove(funcionarioListFuncionario);
                    oldIddepartOfFuncionarioListFuncionario = em.merge(oldIddepartOfFuncionarioListFuncionario);
                }
            }
            for (Avaliacao avaliacaoListAvaliacao : departamento.getAvaliacaoList()) {
                Departamento oldIddepartOfAvaliacaoListAvaliacao = avaliacaoListAvaliacao.getIddepart();
                avaliacaoListAvaliacao.setIddepart(departamento);
                avaliacaoListAvaliacao = em.merge(avaliacaoListAvaliacao);
                if (oldIddepartOfAvaliacaoListAvaliacao != null) {
                    oldIddepartOfAvaliacaoListAvaliacao.getAvaliacaoList().remove(avaliacaoListAvaliacao);
                    oldIddepartOfAvaliacaoListAvaliacao = em.merge(oldIddepartOfAvaliacaoListAvaliacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamento departamento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getIddeparta());
            List<Funcionario> funcionarioListOld = persistentDepartamento.getFuncionarioList();
            List<Funcionario> funcionarioListNew = departamento.getFuncionarioList();
            List<Avaliacao> avaliacaoListOld = persistentDepartamento.getAvaliacaoList();
            List<Avaliacao> avaliacaoListNew = departamento.getAvaliacaoList();
            List<Funcionario> attachedFuncionarioListNew = new ArrayList<Funcionario>();
            for (Funcionario funcionarioListNewFuncionarioToAttach : funcionarioListNew) {
                funcionarioListNewFuncionarioToAttach = em.getReference(funcionarioListNewFuncionarioToAttach.getClass(), funcionarioListNewFuncionarioToAttach.getIdfunc());
                attachedFuncionarioListNew.add(funcionarioListNewFuncionarioToAttach);
            }
            funcionarioListNew = attachedFuncionarioListNew;
            departamento.setFuncionarioList(funcionarioListNew);
            List<Avaliacao> attachedAvaliacaoListNew = new ArrayList<Avaliacao>();
            for (Avaliacao avaliacaoListNewAvaliacaoToAttach : avaliacaoListNew) {
                avaliacaoListNewAvaliacaoToAttach = em.getReference(avaliacaoListNewAvaliacaoToAttach.getClass(), avaliacaoListNewAvaliacaoToAttach.getIdaval());
                attachedAvaliacaoListNew.add(avaliacaoListNewAvaliacaoToAttach);
            }
            avaliacaoListNew = attachedAvaliacaoListNew;
            departamento.setAvaliacaoList(avaliacaoListNew);
            departamento = em.merge(departamento);
            for (Funcionario funcionarioListOldFuncionario : funcionarioListOld) {
                if (!funcionarioListNew.contains(funcionarioListOldFuncionario)) {
                    funcionarioListOldFuncionario.setIddepart(null);
                    funcionarioListOldFuncionario = em.merge(funcionarioListOldFuncionario);
                }
            }
            for (Funcionario funcionarioListNewFuncionario : funcionarioListNew) {
                if (!funcionarioListOld.contains(funcionarioListNewFuncionario)) {
                    Departamento oldIddepartOfFuncionarioListNewFuncionario = funcionarioListNewFuncionario.getIddepart();
                    funcionarioListNewFuncionario.setIddepart(departamento);
                    funcionarioListNewFuncionario = em.merge(funcionarioListNewFuncionario);
                    if (oldIddepartOfFuncionarioListNewFuncionario != null && !oldIddepartOfFuncionarioListNewFuncionario.equals(departamento)) {
                        oldIddepartOfFuncionarioListNewFuncionario.getFuncionarioList().remove(funcionarioListNewFuncionario);
                        oldIddepartOfFuncionarioListNewFuncionario = em.merge(oldIddepartOfFuncionarioListNewFuncionario);
                    }
                }
            }
            for (Avaliacao avaliacaoListOldAvaliacao : avaliacaoListOld) {
                if (!avaliacaoListNew.contains(avaliacaoListOldAvaliacao)) {
                    avaliacaoListOldAvaliacao.setIddepart(null);
                    avaliacaoListOldAvaliacao = em.merge(avaliacaoListOldAvaliacao);
                }
            }
            for (Avaliacao avaliacaoListNewAvaliacao : avaliacaoListNew) {
                if (!avaliacaoListOld.contains(avaliacaoListNewAvaliacao)) {
                    Departamento oldIddepartOfAvaliacaoListNewAvaliacao = avaliacaoListNewAvaliacao.getIddepart();
                    avaliacaoListNewAvaliacao.setIddepart(departamento);
                    avaliacaoListNewAvaliacao = em.merge(avaliacaoListNewAvaliacao);
                    if (oldIddepartOfAvaliacaoListNewAvaliacao != null && !oldIddepartOfAvaliacaoListNewAvaliacao.equals(departamento)) {
                        oldIddepartOfAvaliacaoListNewAvaliacao.getAvaliacaoList().remove(avaliacaoListNewAvaliacao);
                        oldIddepartOfAvaliacaoListNewAvaliacao = em.merge(oldIddepartOfAvaliacaoListNewAvaliacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = departamento.getIddeparta();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
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
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getIddeparta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            List<Funcionario> funcionarioList = departamento.getFuncionarioList();
            for (Funcionario funcionarioListFuncionario : funcionarioList) {
                funcionarioListFuncionario.setIddepart(null);
                funcionarioListFuncionario = em.merge(funcionarioListFuncionario);
            }
            List<Avaliacao> avaliacaoList = departamento.getAvaliacaoList();
            for (Avaliacao avaliacaoListAvaliacao : avaliacaoList) {
                avaliacaoListAvaliacao.setIddepart(null);
                avaliacaoListAvaliacao = em.merge(avaliacaoListAvaliacao);
            }
            em.remove(departamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
