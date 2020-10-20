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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author PROGRAMADOR
 */
@Entity
@Table(name = "avaliacao", catalog = "customer_care", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Avaliacao.findAll", query = "SELECT a FROM Avaliacao a")})
public class Avaliacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idaval", nullable = false)
    private Integer idaval;
    @Column(name = "datavaliacao")
    @Temporal(TemporalType.DATE)
    private Date datavaliacao;
    @Column(name = "qtdpont")
    private Integer qtdpont;
    @OneToMany(mappedBy = "idaval", fetch = FetchType.LAZY)
    private List<Pontuacao> pontuacaoList;
    @JoinColumn(name = "iddepart", referencedColumnName = "iddeparta")
    @ManyToOne(fetch = FetchType.LAZY)
    private Departamento iddepart;
    @JoinColumn(name = "idfunc", referencedColumnName = "idfunc")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario idfunc;

    public Avaliacao() {
    }

    public Avaliacao(Integer idaval) {
        this.idaval = idaval;
    }

    public Integer getIdaval() {
        return idaval;
    }

    public void setIdaval(Integer idaval) {
        this.idaval = idaval;
    }

    public Date getDatavaliacao() {
        return datavaliacao;
    }

    public void setDatavaliacao(Date datavaliacao) {
        this.datavaliacao = datavaliacao;
    }

    public Integer getQtdpont() {
        return qtdpont;
    }

    public void setQtdpont(Integer qtdpont) {
        this.qtdpont = qtdpont;
    }

    @XmlTransient
    public List<Pontuacao> getPontuacaoList() {
        return pontuacaoList;
    }

    public void setPontuacaoList(List<Pontuacao> pontuacaoList) {
        this.pontuacaoList = pontuacaoList;
    }

    public Departamento getIddepart() {
        return iddepart;
    }

    public void setIddepart(Departamento iddepart) {
        this.iddepart = iddepart;
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
        hash += (idaval != null ? idaval.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Avaliacao)) {
            return false;
        }
        Avaliacao other = (Avaliacao) object;
        if ((this.idaval == null && other.idaval != null) || (this.idaval != null && !this.idaval.equals(other.idaval))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Avaliacao[ idaval=" + idaval + " ]";
    }
    
}
