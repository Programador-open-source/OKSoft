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
@Table(name = "parametro", catalog = "customer_care", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parametro.findAll", query = "SELECT p FROM Parametro p")})
public class Parametro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idparam", nullable = false)
    private Integer idparam;
    @Column(name = "descricao", length = 500)
    private String descricao;
    @OneToMany(mappedBy = "idparametro", fetch = FetchType.LAZY)
    private List<Pontuacao> pontuacaoList;
    @JoinColumn(name = "idindicador", referencedColumnName = "idindic")
    @ManyToOne(fetch = FetchType.LAZY)
    private Indicador idindicador;

    public Parametro() {
    }

    public Parametro(Integer idparam) {
        this.idparam = idparam;
    }

    public Integer getIdparam() {
        return idparam;
    }

    public void setIdparam(Integer idparam) {
        this.idparam = idparam;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public List<Pontuacao> getPontuacaoList() {
        return pontuacaoList;
    }

    public void setPontuacaoList(List<Pontuacao> pontuacaoList) {
        this.pontuacaoList = pontuacaoList;
    }

    public Indicador getIdindicador() {
        return idindicador;
    }

    public void setIdindicador(Indicador idindicador) {
        this.idindicador = idindicador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idparam != null ? idparam.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parametro)) {
            return false;
        }
        Parametro other = (Parametro) object;
        if ((this.idparam == null && other.idparam != null) || (this.idparam != null && !this.idparam.equals(other.idparam))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Parametro[ idparam=" + idparam + " ]";
    }
    
}
