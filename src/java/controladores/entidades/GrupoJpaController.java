/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import entidades.Grupo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Usergrupo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PROGRAMADOR
 */
public class GrupoJpaController implements Serializable {

    public GrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grupo grupo) throws PreexistingEntityException, Exception {
        if (grupo.getUsergrupoList() == null) {
            grupo.setUsergrupoList(new ArrayList<Usergrupo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usergrupo> attachedUsergrupoList = new ArrayList<Usergrupo>();
            for (Usergrupo usergrupoListUsergrupoToAttach : grupo.getUsergrupoList()) {
                usergrupoListUsergrupoToAttach = em.getReference(usergrupoListUsergrupoToAttach.getClass(), usergrupoListUsergrupoToAttach.getUsergrupoPK());
                attachedUsergrupoList.add(usergrupoListUsergrupoToAttach);
            }
            grupo.setUsergrupoList(attachedUsergrupoList);
            em.persist(grupo);
            for (Usergrupo usergrupoListUsergrupo : grupo.getUsergrupoList()) {
                Grupo oldGrupoOfUsergrupoListUsergrupo = usergrupoListUsergrupo.getGrupo();
                usergrupoListUsergrupo.setGrupo(grupo);
                usergrupoListUsergrupo = em.merge(usergrupoListUsergrupo);
                if (oldGrupoOfUsergrupoListUsergrupo != null) {
                    oldGrupoOfUsergrupoListUsergrupo.getUsergrupoList().remove(usergrupoListUsergrupo);
                    oldGrupoOfUsergrupoListUsergrupo = em.merge(oldGrupoOfUsergrupoListUsergrupo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrupo(grupo.getIdGrupo()) != null) {
                throw new PreexistingEntityException("Grupo " + grupo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grupo grupo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo persistentGrupo = em.find(Grupo.class, grupo.getIdGrupo());
            List<Usergrupo> usergrupoListOld = persistentGrupo.getUsergrupoList();
            List<Usergrupo> usergrupoListNew = grupo.getUsergrupoList();
            List<String> illegalOrphanMessages = null;
            for (Usergrupo usergrupoListOldUsergrupo : usergrupoListOld) {
                if (!usergrupoListNew.contains(usergrupoListOldUsergrupo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usergrupo " + usergrupoListOldUsergrupo + " since its grupo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Usergrupo> attachedUsergrupoListNew = new ArrayList<Usergrupo>();
            for (Usergrupo usergrupoListNewUsergrupoToAttach : usergrupoListNew) {
                usergrupoListNewUsergrupoToAttach = em.getReference(usergrupoListNewUsergrupoToAttach.getClass(), usergrupoListNewUsergrupoToAttach.getUsergrupoPK());
                attachedUsergrupoListNew.add(usergrupoListNewUsergrupoToAttach);
            }
            usergrupoListNew = attachedUsergrupoListNew;
            grupo.setUsergrupoList(usergrupoListNew);
            grupo = em.merge(grupo);
            for (Usergrupo usergrupoListNewUsergrupo : usergrupoListNew) {
                if (!usergrupoListOld.contains(usergrupoListNewUsergrupo)) {
                    Grupo oldGrupoOfUsergrupoListNewUsergrupo = usergrupoListNewUsergrupo.getGrupo();
                    usergrupoListNewUsergrupo.setGrupo(grupo);
                    usergrupoListNewUsergrupo = em.merge(usergrupoListNewUsergrupo);
                    if (oldGrupoOfUsergrupoListNewUsergrupo != null && !oldGrupoOfUsergrupoListNewUsergrupo.equals(grupo)) {
                        oldGrupoOfUsergrupoListNewUsergrupo.getUsergrupoList().remove(usergrupoListNewUsergrupo);
                        oldGrupoOfUsergrupoListNewUsergrupo = em.merge(oldGrupoOfUsergrupoListNewUsergrupo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = grupo.getIdGrupo();
                if (findGrupo(id) == null) {
                    throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.");
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
            Grupo grupo;
            try {
                grupo = em.getReference(Grupo.class, id);
                grupo.getIdGrupo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Usergrupo> usergrupoListOrphanCheck = grupo.getUsergrupoList();
            for (Usergrupo usergrupoListOrphanCheckUsergrupo : usergrupoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupo (" + grupo + ") cannot be destroyed since the Usergrupo " + usergrupoListOrphanCheckUsergrupo + " in its usergrupoList field has a non-nullable grupo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(grupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grupo> findGrupoEntities() {
        return findGrupoEntities(true, -1, -1);
    }

    public List<Grupo> findGrupoEntities(int maxResults, int firstResult) {
        return findGrupoEntities(false, maxResults, firstResult);
    }

    private List<Grupo> findGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupo.class));
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

    public Grupo findGrupo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupo> rt = cq.from(Grupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
