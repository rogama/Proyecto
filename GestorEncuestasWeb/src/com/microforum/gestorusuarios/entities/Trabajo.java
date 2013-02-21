package com.microforum.gestorusuarios.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Table(name="TRABAJO_ADMINISTTRACION_SISTEMA")
@Entity
public class Trabajo {
	@Id
	@Column(name="NUMERO_REFERENCIA")
	//@GenericGenerator(name="UUID-GENERATOR",strategy="uuid")
	//@GeneratedValue(generator="UUID-GENERATOR")
	private String numReferencia;
	@Column(name="DESCRIPCION_TRABAJO")
	private String descripcion;
	@Column(name="FECHA_TRABAJO")
	@Temporal(TemporalType.DATE)
	private Date fecha;
	@ManyToMany(mappedBy="trabajos")
	private Collection<Administrador> autores = new ArrayList<Administrador>();
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Collection<Administrador> getAutores() {
		return autores;
	}
	public void setAutores(Collection<Administrador> autores) {
		this.autores = autores;
	}
	public String getNumReferencia() {
		return numReferencia;
	}
	public void setNumReferencia(String numReferencia) {
		this.numReferencia = numReferencia;
	}
	
	
}
