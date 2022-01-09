/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASES;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author franc
 */
@Entity
public class participa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int acumulado;
    private String estadoronda;

    
    @ManyToOne
    private jugador participante; 
    @OneToOne
    private rondas rondajugada;

    public int getAcumulado() {
        return acumulado;
    }

    public void setAcumulado(int acumulado) {
        this.acumulado = acumulado;
    }

    public String getEstadoronda() {
        return estadoronda;
    }

    public void setEstadoronda(String estadoronda) {
        this.estadoronda = estadoronda;
    }

    public jugador getParticipante() {
        return participante;
    }

    public void setParticipante(jugador participante) {
        this.participante = participante;
    }

    public rondas getRondajugada() {
        return rondajugada;
    }

    public void setRondajugada(rondas rondajugada) {
        this.rondajugada = rondajugada;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof participa)) {
            return false;
        }
        participa other = (participa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CLASES.juega[ id=" + id + " ]";
    }
    
}
