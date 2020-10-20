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
@Table(name = "indicador", catalog = "customer_care", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Indicador.findAll", query = "SELECT i FROM Indicador i")})
public class Indicador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idindic", nullable = false)
    private Integer idindic;
    @Column(name = "descricao", length = 255)
    private String descricao;
    @OneToMany(mappedBy = "idindicador", fetch = FetchType.LAZY)
    private List<Parametro> parametroList;

    public Indicador() {
    }

    public Indicador(Integer idindic) {
        this.idindic = idindic;
    }

    public Integer getIdindic() {
        return idindic;
    }

    public void setIdindic(Integer idindic) {
        this.idindic = idindic;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public List<Parametro> getParametroList() {
        return parametroList;
    }

    public void setParametroList(List<Parametro> parametroList) {
        this.parametroList = parametroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idindic != null ? idindic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Indicador)) {
            return false;
        }
        Indicador other = (Indicador) object;
        if ((this.idindic == null && other.idindic != null) || (this.idindic != null && !this.idindic.equals(other.idindic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Indicador[ idindic=" + idindic + " ]";
    }
    
}
