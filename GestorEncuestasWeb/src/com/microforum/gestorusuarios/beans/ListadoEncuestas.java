package com.microforum.gestorusuarios.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.microforum.gestorusuarios.entities.Encuesta;
import com.microforum.gestorusuarios.entities.Pregunta;

@ManagedBean
@ApplicationScoped
public class ListadoEncuestas {
	private List<Encuesta> encuestas ;
	private List<SelectItem> encuestasItems;
	
	private String encuestaSeleccionada; 
	
	public ListadoEncuestas() {
		Configuration conf = new Configuration();
		SessionFactory sf = conf.configure().buildSessionFactory();
		Session session = sf.openSession();
		
		Query query = session.createQuery("from Encuesta");
		List<Encuesta> listadoEncuestas = query.list();
		int tam =listadoEncuestas.size();
		
		encuestasItems = new ArrayList<SelectItem>();
		encuestas = new ArrayList<Encuesta>();

		encuestasItems.add(new SelectItem("----Preguntas----"));
		
		for (int i = 0; i < tam; i++) {

			String ref = listadoEncuestas.get(i).getRef();
			String proposito = listadoEncuestas.get(i).getProposito();
			
			encuestasItems.add(new SelectItem(ref,proposito));
		}
		session.close();
	}

	public String RealizarEncuesta(ActionEvent e) {
		FacesContext fContext = FacesContext.getCurrentInstance();
		ExternalContext ec = fContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		
		HttpSession sesion = request.getSession();
		
		encuestaSeleccionada = (String) sesion.getAttribute("Encuesta");
		encuestaSeleccionada = (String) request.getParameter("Encuesta");
		
		while (request.getParameterNames().hasMoreElements()) {
			
			System.out.println(request.getParameterNames().nextElement());		
		}
		
		System.out.println(encuestaSeleccionada);
		return "ResponderEncuesta";
	}
	
	public List<Encuesta> getEncuestas() {
		return encuestas;
	}

	public void setEncuestas(List<Encuesta> encuestas) {
		this.encuestas = encuestas;
	}

	public List<SelectItem> getEncuestasItems() {
		return encuestasItems;
	}

	public void setEncuestasItems(List<SelectItem> encuestasItems) {
		this.encuestasItems = encuestasItems;
	}

	public String getEncuestaSeleccionada() {
		return encuestaSeleccionada;
	}

	public void setEncuestaSeleccionada(String encuestaSeleccionada) {
		this.encuestaSeleccionada = encuestaSeleccionada;
	}
}
