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
@Table(name = "departamento", catalog = "customer_care", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Departamento.findAll", query = "SELECT d FROM Departamento d")})
public class Departamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddeparta", nullable = false)
    private Integer iddeparta;
    @Column(name = "descricao", length = 255)
    private String descricao;
    @OneToMany(mappedBy = "iddepart", fetch = FetchType.LAZY)
    private List<Funcionario> funcionarioList;
    @OneToMany(mappedBy = "iddepart", fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacaoList;

    public Departamento() {
    }

    public Departamento(Integer iddeparta) {
        this.iddeparta = iddeparta;
    }

    public Integer getIddeparta() {
        return iddeparta;
    }

    public void setIddeparta(Integer iddeparta) {
        this.iddeparta = iddeparta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public List<Funcionario> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<Funcionario> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    @XmlTransient
    public List<Avaliacao> getAvaliacaoList() {
        return avaliacaoList;
    }

    public void setAvaliacaoList(List<Avaliacao> avaliacaoList) {
        this.avaliacaoList = avaliacaoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddeparta != null ? iddeparta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Departamento)) {
            return false;
        }
        Departamento other = (Departamento) object;
        if ((this.iddeparta == null && other.iddeparta != null) || (this.iddeparta != null && !this.iddeparta.equals(other.iddeparta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Departamento[ iddeparta=" + iddeparta + " ]";
    }
    
}
