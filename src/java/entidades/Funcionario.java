/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author PROGRAMADOR
 */
@Entity
@Table(name = "funcionario", catalog = "customer_care", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Funcionario.findAll", query = "SELECT f FROM Funcionario f")})
public class Funcionario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idfunc", nullable = false)
    private Integer idfunc;
    @Column(name = "nome", length = 255)
    private String nome;
    @Column(name = "apelido", length = 255)
    private String apelido;
    @Column(name = "telefone", length = 255)
    private String telefone;
    @JoinColumn(name = "idcargo", referencedColumnName = "idcargo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cargo idcargo;
    @JoinColumn(name = "idcateg", referencedColumnName = "idcategoria")
    @ManyToOne(fetch = FetchType.LAZY)
    private Categoria idcateg;
    @JoinColumn(name = "iddepart", referencedColumnName = "iddeparta")
    @ManyToOne(fetch = FetchType.LAZY)
    private Departamento iddepart;
    @OneToMany(mappedBy = "idfunc", fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacaoList;
    @OneToMany(mappedBy = "idfunc", fetch = FetchType.LAZY)
    private List<Users> usersList;

    public Funcionario() {
    }

    public Funcionario(Integer idfunc) {
        this.idfunc = idfunc;
    }

    public Integer getIdfunc() {
        return idfunc;
    }

    public void setIdfunc(Integer idfunc) {
        this.idfunc = idfunc;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Cargo getIdcargo() {
        return idcargo;
    }

    public void setIdcargo(Cargo idcargo) {
        this.idcargo = idcargo;
    }

    public Categoria getIdcateg() {
        return idcateg;
    }

    public void setIdcateg(Categoria idcateg) {
        this.idcateg = idcateg;
    }

    public Departamento getIddepart() {
        return iddepart;
    }

    public void setIddepart(Departamento iddepart) {
        this.iddepart = iddepart;
    }

    @XmlTransient
    public List<Avaliacao> getAvaliacaoList() {
        return avaliacaoList;
    }

    public void setAvaliacaoList(List<Avaliacao> avaliacaoList) {
        this.avaliacaoList = avaliacaoList;
    }

    @XmlTransient
    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idfunc != null ? idfunc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funcionario)) {
            return false;
        }
        Funcionario other = (Funcionario) object;
        if ((this.idfunc == null && other.idfunc != null) || (this.idfunc != null && !this.idfunc.equals(other.idfunc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Funcionario[ idfunc=" + idfunc + " ]";
    }
    
}
