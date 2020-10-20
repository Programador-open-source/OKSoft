/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author PROGRAMADOR
 */
@Entity
@Table(name = "users", catalog = "customer_care", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")})
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "utilizador", nullable = false, length = 255)
    private String utilizador;
    @Column(name = "email", length = 255)
    private String email;
    @Column(name = "pasword", length = 255)
    private String pasword;
    @Column(name = "last_access")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccess;
    @Column(name = "nome", length = 255)
    private String nome;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users", fetch = FetchType.LAZY)
    private List<Usergrupo> usergrupoList;
    @JoinColumn(name = "idfunc", referencedColumnName = "idfunc")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario idfunc;

    public Users() {
    }

    public Users(String utilizador) {
        this.utilizador = utilizador;
    }

    public String getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(String utilizador) {
        this.utilizador = utilizador;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasword() {
        return pasword;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @XmlTransient
    public List<Usergrupo> getUsergrupoList() {
        return usergrupoList;
    }

    public void setUsergrupoList(List<Usergrupo> usergrupoList) {
        this.usergrupoList = usergrupoList;
    }

    public Funcionario getIdfunc() {
        return idfunc;
    }

    public void setIdfunc(Funcionario idfunc) {
        this.idfunc = idfunc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (utilizador != null ? utilizador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.utilizador == null && other.utilizador != null) || (this.utilizador != null && !this.utilizador.equals(other.utilizador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Users[ utilizador=" + utilizador + " ]";
    }
    
}
