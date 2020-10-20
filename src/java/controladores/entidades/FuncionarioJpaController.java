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
import entidades.Cargo;
import entidades.Categoria;
import entidades.Departamento;
import entidades.Avaliacao;
import entidades.Funcionario;
import java.util.ArrayList;
import java.util.List;
import entidades.Users;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PROGRAMADOR
 */
public class FuncionarioJpaController implements Serializable {

    public FuncionarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Funcionario funcionario) {
        if (funcionario.getAvaliacaoList() == null) {
            funcionario.setAvaliacaoList(new ArrayList<Avaliacao>());
        }
        if (funcionario.getUsersList() == null) {
            funcionario.setUsersList(new ArrayList<Users>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cargo idcargo = funcionario.getIdcargo();
            if (idcargo != null) {
                idcargo = em.getReference(idcargo.getClass(), idcargo.getIdcargo());
                funcionario.setIdcargo(idcargo);
            }
            Categoria idcateg = funcionario.getIdcateg();
            if (idcateg != null) {
                idcateg = em.getReference(idcateg.getClass(), idcateg.getIdcategoria());
                funcionario.setIdcateg(idcateg);
            }
            Departamento iddepart = funcionario.getIddepart();
            if (iddepart != null) {
                iddepart = em.getReference(iddepart.getClass(), iddepart.getIddeparta());
                funcionario.setIddepart(iddepart);
            }
            List<Avaliacao> attachedAvaliacaoList = new ArrayList<Avaliacao>();
            for (Avaliacao avaliacaoListAvaliacaoToAttach : funcionario.getAvaliacaoList()) {
                avaliacaoListAvaliacaoToAttach = em.getReference(avaliacaoListAvaliacaoToAttach.getClass(), avaliacaoListAvaliacaoToAttach.getIdaval());
                attachedAvaliacaoList.add(avaliacaoListAvaliacaoToAttach);
            }
            funcionario.setAvaliacaoList(attachedAvaliacaoList);
            List<Users> attachedUsersList = new ArrayList<Users>();
            for (Users usersListUsersToAttach : funcionario.getUsersList()) {
                usersListUsersToAttach = em.getReference(usersListUsersToAttach.getClass(), usersListUsersToAttach.getUtilizador());
                attachedUsersList.add(usersListUsersToAttach);
            }
            funcionario.setUsersList(attachedUsersList);
            em.persist(funcionario);
            if (idcargo != null) {
                idcargo.getFuncionarioList().add(funcionario);
                idcargo = em.merge(idcargo);
            }
            if (idcateg != null) {
                idcateg.getFuncionarioList().add(funcionario);
                idcateg = em.merge(idcateg);
            }
            if (iddepart != null) {
                iddepart.getFuncionarioList().add(funcionario);
                iddepart = em.merge(iddepart);
            }
            for (Avaliacao avaliacaoListAvaliacao : funcionario.getAvaliacaoList()) {
                Funcionario oldIdfuncOfAvaliacaoListAvaliacao = avaliacaoListAvaliacao.getIdfunc();
                avaliacaoListAvaliacao.setIdfunc(funcionario);
                avaliacaoListAvaliacao = em.merge(avaliacaoListAvaliacao);
                if (oldIdfuncOfAvaliacaoListAvaliacao != null) {
                    oldIdfuncOfAvaliacaoListAvaliacao.getAvaliacaoList().remove(avaliacaoListAvaliacao);
                    oldIdfuncOfAvaliacaoListAvaliacao = em.merge(oldIdfuncOfAvaliacaoListAvaliacao);
                }
            }
            for (Users usersListUsers : funcionario.getUsersList()) {
                Funcionario oldIdfuncOfUsersListUsers = usersListUsers.getIdfunc();
                usersListUsers.setIdfunc(funcionario);
                usersListUsers = em.merge(usersListUsers);
                if (oldIdfuncOfUsersListUsers != null) {
                    oldIdfuncOfUsersListUsers.getUsersList().remove(usersListUsers);
                    oldIdfuncOfUsersListUsers = em.merge(oldIdfuncOfUsersListUsers);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Funcionario funcionario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario persistentFuncionario = em.find(Funcionario.class, funcionario.getIdfunc());
            Cargo idcargoOld = persistentFuncionario.getIdcargo();
            Cargo idcargoNew = funcionario.getIdcargo();
            Categoria idcategOld = persistentFuncionario.getIdcateg();
            Categoria idcategNew = funcionario.getIdcateg();
            Departamento iddepartOld = persistentFuncionario.getIddepart();
            Departamento iddepartNew = funcionario.getIddepart();
            List<Avaliacao> avaliacaoListOld = persistentFuncionario.getAvaliacaoList();
            List<Avaliacao> avaliacaoListNew = funcionario.getAvaliacaoList();
            List<Users> usersListOld = persistentFuncionario.getUsersList();
            List<Users> usersListNew = funcionario.getUsersList();
            if (idcargoNew != null) {
                idcargoNew = em.getReference(idcargoNew.getClass(), idcargoNew.getIdcargo());
                funcionario.setIdcargo(idcargoNew);
            }
            if (idcategNew != null) {
                idcategNew = em.getReference(idcategNew.getClass(), idcategNew.getIdcategoria());
                funcionario.setIdcateg(idcategNew);
            }
            if (iddepartNew != null) {
                iddepartNew = em.getReference(iddepartNew.getClass(), iddepartNew.getIddeparta());
                funcionario.setIddepart(iddepartNew);
            }
            List<Avaliacao> attachedAvaliacaoListNew = new ArrayList<Avaliacao>();
            for (Avaliacao avaliacaoListNewAvaliacaoToAttach : avaliacaoListNew) {
                avaliacaoListNewAvaliacaoToAttach = em.getReference(avaliacaoListNewAvaliacaoToAttach.getClass(), avaliacaoListNewAvaliacaoToAttach.getIdaval());
                attachedAvaliacaoListNew.add(avaliacaoListNewAvaliacaoToAttach);
            }
            avaliacaoListNew = attachedAvaliacaoListNew;
            funcionario.setAvaliacaoList(avaliacaoListNew);
            List<Users> attachedUsersListNew = new ArrayList<Users>();
            for (Users usersListNewUsersToAttach : usersListNew) {
                usersListNewUsersToAttach = em.getReference(usersListNewUsersToAttach.getClass(), usersListNewUsersToAttach.getUtilizador());
                attachedUsersListNew.add(usersListNewUsersToAttach);
            }
            usersListNew = attachedUsersListNew;
            funcionario.setUsersList(usersListNew);
            funcionario = em.merge(funcionario);
            if (idcargoOld != null && !idcargoOld.equals(idcargoNew)) {
                idcargoOld.getFuncionarioList().remove(funcionario);
                idcargoOld = em.merge(idcargoOld);
            }
            if (idcargoNew != null && !idcargoNew.equals(idcargoOld)) {
                idcargoNew.getFuncionarioList().add(funcionario);
                idcargoNew = em.merge(idcargoNew);
            }
            if (idcategOld != null && !idcategOld.equals(idcategNew)) {
                idcategOld.getFuncionarioList().remove(funcionario);
                idcategOld = em.merge(idcategOld);
            }
            if (idcategNew != null && !idcategNew.equals(idcategOld)) {
                idcategNew.getFuncionarioList().add(funcionario);
                idcategNew = em.merge(idcategNew);
            }
            if (iddepartOld != null && !iddepartOld.equals(iddepartNew)) {
                iddepartOld.getFuncionarioList().remove(funcionario);
                iddepartOld = em.merge(iddepartOld);
            }
            if (iddepartNew != null && !iddepartNew.equals(iddepartOld)) {
                iddepartNew.getFuncionarioList().add(funcionario);
                iddepartNew = em.merge(iddepartNew);
            }
            for (Avaliacao avaliacaoListOldAvaliacao : avaliacaoListOld) {
                if (!avaliacaoListNew.contains(avaliacaoListOldAvaliacao)) {
                    avaliacaoListOldAvaliacao.setIdfunc(null);
                    avaliacaoListOldAvaliacao = em.merge(avaliacaoListOldAvaliacao);
                }
            }
            for (Avaliacao avaliacaoListNewAvaliacao : avaliacaoListNew) {
                if (!avaliacaoListOld.contains(avaliacaoListNewAvaliacao)) {
                    Funcionario oldIdfuncOfAvaliacaoListNewAvaliacao = avaliacaoListNewAvaliacao.getIdfunc();
                    avaliacaoListNewAvaliacao.setIdfunc(funcionario);
                    avaliacaoListNewAvaliacao = em.merge(avaliacaoListNewAvaliacao);
                    if (oldIdfuncOfAvaliacaoListNewAvaliacao != null && !oldIdfuncOfAvaliacaoListNewAvaliacao.equals(funcionario)) {
                        oldIdfuncOfAvaliacaoListNewAvaliacao.getAvaliacaoList().remove(avaliacaoListNewAvaliacao);
                        oldIdfuncOfAvaliacaoListNewAvaliacao = em.merge(oldIdfuncOfAvaliacaoListNewAvaliacao);
                    }
                }
            }
            for (Users usersListOldUsers : usersListOld) {
                if (!usersListNew.contains(usersListOldUsers)) {
                    usersListOldUsers.setIdfunc(null);
                    usersListOldUsers = em.merge(usersListOldUsers);
                }
            }
            for (Users usersListNewUsers : usersListNew) {
                if (!usersListOld.contains(usersListNewUsers)) {
                    Funcionario oldIdfuncOfUsersListNewUsers = usersListNewUsers.getIdfunc();
                    usersListNewUsers.setIdfunc(funcionario);
                    usersListNewUsers = em.merge(usersListNewUsers);
                    if (oldIdfuncOfUsersListNewUsers != null && !oldIdfuncOfUsersListNewUsers.equals(funcionario)) {
                        oldIdfuncOfUsersListNewUsers.getUsersList().remove(usersListNewUsers);
                        oldIdfuncOfUsersListNewUsers = em.merge(oldIdfuncOfUsersListNewUsers);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = funcionario.getIdfunc();
                if (findFuncionario(id) == null) {
                    throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.");
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
            Funcionario funcionario;
            try {
                funcionario = em.getReference(Funcionario.class, id);
                funcionario.getIdfunc();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.", enfe);
            }
            Cargo idcargo = funcionario.getIdcargo();
            if (idcargo != null) {
                idcargo.getFuncionarioList().remove(funcionario);
                idcargo = em.merge(idcargo);
            }
            Categoria idcateg = funcionario.getIdcateg();
            if (idcateg != null) {
                idcateg.getFuncionarioList().remove(funcionario);
                idcateg = em.merge(idcateg);
            }
            Departamento iddepart = funcionario.getIddepart();
            if (iddepart != null) {
                iddepart.getFuncionarioList().remove(funcionario);
                iddepart = em.merge(iddepart);
            }
            List<Avaliacao> avaliacaoList = funcionario.getAvaliacaoList();
            for (Avaliacao avaliacaoListAvaliacao : avaliacaoList) {
                avaliacaoListAvaliacao.setIdfunc(null);
                avaliacaoListAvaliacao = em.merge(avaliacaoListAvaliacao);
            }
            List<Users> usersList = funcionario.getUsersList();
            for (Users usersListUsers : usersList) {
                usersListUsers.setIdfunc(null);
                usersListUsers = em.merge(usersListUsers);
            }
            em.remove(funcionario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Funcionario> findFuncionarioEntities() {
        return findFuncionarioEntities(true, -1, -1);
    }

    public List<Funcionario> findFuncionarioEntities(int maxResults, int firstResult) {
        return findFuncionarioEntities(false, maxResults, firstResult);
    }

    private List<Funcionario> findFuncionarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Funcionario.class));
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

    public Funcionario findFuncionario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Funcionario.class, id);
        } finally {
            em.close();
        }
    }

    public int getFuncionarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Funcionario> rt = cq.from(Funcionario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
