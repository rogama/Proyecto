package com.microforum.gestorusuarios.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="USUARIO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE) //esto se hace para poderla heredar
@DiscriminatorColumn(name="TIPO_USUARIO",discriminatorType=DiscriminatorType.INTEGER)
@NamedQuery(name="usuario.byCodPostal",query="from Usuario where domicilio.codPostal =?")
public class Usuario {
	@EmbeddedId
	private DocumentoId identificador;
	
	@Column(name="NOMBRE")
	private String nombre;
	@Column(name="APELLIDO_1")
	private String apellido1;
	@Column(name="APELLIDO_2")
	private String apellido2;
	
	@Embedded
	private DatosDomicilio domicilio;
	
	/*@Transient //Esta marca sirve para que este atributo no se sincronice con la BBDD
	private String nombreCompleto;*/
	@OneToOne
	@JoinColumn(name="LOGIN_USER")
	private DatosLogin datosLogin;
	
	@Transient
	private String alias;
	
	public Usuario(){
		
	}
	
	public Usuario(DocumentoId identificador, String nombre, String apellido1,
			String apellido2, DatosDomicilio domicilio) {
		super();
		this.identificador = identificador;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.domicilio = domicilio;
		//this.nombreCompleto = nombre + " " + apellido1 + " " + apellido2;
	}

	public DocumentoId getIdentificador() {
		return identificador;
	}

	public void setIdentificador(DocumentoId identificador) {
		this.identificador = identificador;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public DatosDomicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(DatosDomicilio domicilio) {
		this.domicilio = domicilio;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getNombreCompleto() {
		return nombre + " " + apellido1 + " " + apellido2;
	}

}
