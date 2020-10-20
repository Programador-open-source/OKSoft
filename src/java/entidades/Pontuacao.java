/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PROGRAMADOR
 */
@Entity
@Table(name = "pontuacao", catalog = "customer_care", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pontuacao.findAll", query = "SELECT p FROM Pontuacao p")})
public class Pontuacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idpont", nullable = false)
    private Integer idpont;
    @Column(name = "descricao", length = 255)
    private String descricao;
    @JoinColumn(name = "idparametro", referencedColumnName = "idparam")
    @ManyToOne(fetch = FetchType.LAZY)
    private Parametro idparametro;
    @JoinColumn(name = "idaval", referencedColumnName = "idaval")
    @ManyToOne(fetch = FetchType.LAZY)
    private Avaliacao idaval;

    public Pontuacao() {
    }

    public Pontuacao(Integer idpont) {
        this.idpont = idpont;
    }

    public Integer getIdpont() {
        return idpont;
    }

    public void setIdpont(Integer idpont) {
        this.idpont = idpont;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Parametro getIdparametro() {
        return idparametro;
    }

    public void setIdparametro(Parametro idparametro) {
        this.idparametro = idparametro;
    }

    public Avaliacao getIdaval() {
        return idaval;
    }

    public void setIdaval(Avaliacao idaval) {
        this.idaval = idaval;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpont != null ? idpont.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pontuacao)) {
            return false;
        }
        Pontuacao other = (Pontuacao) object;
        if ((this.idpont == null && other.idpont != null) || (this.idpont != null && !this.idpont.equals(other.idpont))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Pontuacao[ idpont=" + idpont + " ]";
    }
    
}
