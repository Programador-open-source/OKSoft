/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Funcionario;
import entidades.Usergrupo;
import entidades.Users;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PROGRAMADOR
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) throws PreexistingEntityException, Exception {
        if (users.getUsergrupoList() == null) {
            users.setUsergrupoList(new ArrayList<Usergrupo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario idfunc = users.getIdfunc();
            if (idfunc != null) {
                idfunc = em.getReference(idfunc.getClass(), idfunc.getIdfunc());
                users.setIdfunc(idfunc);
            }
            List<Usergrupo> attachedUsergrupoList = new ArrayList<Usergrupo>();
            for (Usergrupo usergrupoListUsergrupoToAttach : users.getUsergrupoList()) {
                usergrupoListUsergrupoToAttach = em.getReference(usergrupoListUsergrupoToAttach.getClass(), usergrupoListUsergrupoToAttach.getUsergrupoPK());
                attachedUsergrupoList.add(usergrupoListUsergrupoToAttach);
            }
            users.setUsergrupoList(attachedUsergrupoList);
            em.persist(users);
            if (idfunc != null) {
                idfunc.getUsersList().add(users);
                idfunc = em.merge(idfunc);
            }
            for (Usergrupo usergrupoListUsergrupo : users.getUsergrupoList()) {
                Users oldUsersOfUsergrupoListUsergrupo = usergrupoListUsergrupo.getUsers();
                usergrupoListUsergrupo.setUsers(users);
                usergrupoListUsergrupo = em.merge(usergrupoListUsergrupo);
                if (oldUsersOfUsergrupoListUsergrupo != null) {
                    oldUsersOfUsergrupoListUsergrupo.getUsergrupoList().remove(usergrupoListUsergrupo);
                    oldUsersOfUsergrupoListUsergrupo = em.merge(oldUsersOfUsergrupoListUsergrupo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsers(users.getUtilizador()) != null) {
                throw new PreexistingEntityException("Users " + users + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getUtilizador());
            Funcionario idfuncOld = persistentUsers.getIdfunc();
            Funcionario idfuncNew = users.getIdfunc();
            List<Usergrupo> usergrupoListOld = persistentUsers.getUsergrupoList();
            List<Usergrupo> usergrupoListNew = users.getUsergrupoList();
            List<String> illegalOrphanMessages = null;
            for (Usergrupo usergrupoListOldUsergrupo : usergrupoListOld) {
                if (!usergrupoListNew.contains(usergrupoListOldUsergrupo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usergrupo " + usergrupoListOldUsergrupo + " since its users field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idfuncNew != null) {
                idfuncNew = em.getReference(idfuncNew.getClass(), idfuncNew.getIdfunc());
                users.setIdfunc(idfuncNew);
            }
            List<Usergrupo> attachedUsergrupoListNew = new ArrayList<Usergrupo>();
            for (Usergrupo usergrupoListNewUsergrupoToAttach : usergrupoListNew) {
                usergrupoListNewUsergrupoToAttach = em.getReference(usergrupoListNewUsergrupoToAttach.getClass(), usergrupoListNewUsergrupoToAttach.getUsergrupoPK());
                attachedUsergrupoListNew.add(usergrupoListNewUsergrupoToAttach);
            }
            usergrupoListNew = attachedUsergrupoListNew;
            users.setUsergrupoList(usergrupoListNew);
            users = em.merge(users);
            if (idfuncOld != null && !idfuncOld.equals(idfuncNew)) {
                idfuncOld.getUsersList().remove(users);
                idfuncOld = em.merge(idfuncOld);
            }
            if (idfuncNew != null && !idfuncNew.equals(idfuncOld)) {
                idfuncNew.getUsersList().add(users);
                idfuncNew = em.merge(idfuncNew);
            }
            for (Usergrupo usergrupoListNewUsergrupo : usergrupoListNew) {
                if (!usergrupoListOld.contains(usergrupoListNewUsergrupo)) {
                    Users oldUsersOfUsergrupoListNewUsergrupo = usergrupoListNewUsergrupo.getUsers();
                    usergrupoListNewUsergrupo.setUsers(users);
                    usergrupoListNewUsergrupo = em.merge(usergrupoListNewUsergrupo);
                    if (oldUsersOfUsergrupoListNewUsergrupo != null && !oldUsersOfUsergrupoListNewUsergrupo.equals(users)) {
                        oldUsersOfUsergrupoListNewUsergrupo.getUsergrupoList().remove(usergrupoListNewUsergrupo);
                        oldUsersOfUsergrupoListNewUsergrupo = em.merge(oldUsersOfUsergrupoListNewUsergrupo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = users.getUtilizador();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getUtilizador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Usergrupo> usergrupoListOrphanCheck = users.getUsergrupoList();
            for (Usergrupo usergrupoListOrphanCheckUsergrupo : usergrupoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Usergrupo " + usergrupoListOrphanCheckUsergrupo + " in its usergrupoList field has a non-nullable users field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Funcionario idfunc = users.getIdfunc();
            if (idfunc != null) {
                idfunc.getUsersList().remove(users);
                idfunc = em.merge(idfunc);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
