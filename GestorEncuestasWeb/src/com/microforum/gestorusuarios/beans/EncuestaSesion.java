package com.microforum.gestorusuarios.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.microforum.gestorusuarios.entities.Administrador;
import com.microforum.gestorusuarios.entities.Encuesta;
import com.microforum.gestorusuarios.entities.Pregunta;


@ManagedBean
@SessionScoped
public class EncuestaSesion {
	
	private List<RegistroEncuesta> registrosEncuestas=new ArrayList<RegistroEncuesta>();
	private String nuevaPregunta="";
	private String visibilidadDetalle ="display: none;";
	private String proposito;
	
	private List<String> getReflist(List<RegistroEncuesta> regList){
		List<String> refList = new ArrayList<String>();
		for (RegistroEncuesta re : regList) {
			refList.add(re.getRef());
		}
		return refList;
	}
	
	public void validarNombre(FacesContext context , UIComponent componentToValidate, Object value)throws ValidatorException {
		String nombre = (String) value;
		if(!nombre.startsWith("Encuesta")){
			FacesMessage msg = new FacesMessage("El nombre debe empezar por Encuesta");
			throw new ValidatorException(msg);
		}
	}
	
	public String registrarEncuesta() {
		FacesContext fContext = FacesContext.getCurrentInstance();
		ExternalContext ec = fContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		HttpSession session = request.getSession();
		
		Object obj =session.getAttribute("userBean");
		
		if(obj ==null){
			session.invalidate();
			return "/index.jsp";
		}else{
			UsuarioAutenticado authUser = (UsuarioAutenticado)obj ;
			Administrador user = (Administrador) authUser.getUser();
			
			Configuration conf = new Configuration();
			SessionFactory sf = conf.configure().buildSessionFactory();
			Session hSession = sf.openSession();
			Transaction tr = hSession.beginTransaction();
			
			Query query = hSession.createQuery("from Pregunta where ref in (:preguntasRef)");
			List<String> refList = getReflist(registrosEncuestas);
			query.setParameterList("preguntasRef", refList);
			List<Pregunta> preguntas = query.list();
			
			hSession.merge(user);
			Encuesta encuesta = new Encuesta();
			encuesta.setAutor(user);
			encuesta.setProposito(proposito);
			for (Pregunta p : preguntas) {
				encuesta.getPreguntas().add(p);
			}
			
			hSession.save(encuesta);
			tr.commit();
			hSession.close();
			
			registrosEncuestas.clear();
			proposito = "";
			
			return "/encuestas/administracion/administracionIndex";
		}
	}
	
	public String iniciarEncuesta(){
		/*FacesMessage fc = new FacesMessage("ERROR");
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx.addMessage(null, fc);
		return null;*/
		return "DetalleEncuesta";
	}
	
	public String terminarEncuesta(){
		//agregar control de navegacion
		return "resumenEncuesta";
	}
	
	public String vueltaDetalle(){
		return "DetalleEncuesta";
	}
	
	public String cancelar(){
		Configuration conf = new Configuration();
		SessionFactory sf = conf.configure().buildSessionFactory();
		Session session = sf.openSession();
		session.close();
		
		return "index.jsp";
	}
	
	public String guardar(){
		Configuration conf = new Configuration();
		SessionFactory sf = conf.configure().buildSessionFactory();
		Session session = sf.openSession();
		Transaction tr =session.getTransaction();
		
		Encuesta en = new Encuesta();
		for (RegistroEncuesta re : registrosEncuestas) {
			Pregunta p = new Pregunta();
			p.setRef(re.getRef());
			p.setTexto(re.getTexto());
			
			
			
		}
		
		

		/*EventoEncuesta Event = EventoEncuesta;
		Event.setEncuesta(encuesta)*/
		//en.setAutor(autor);

		
		tr.commit();
		session.close();
		return "encuestas/administracion/administracionIndex.jsp";
		
	}

	public String getNuevaPregunta() {
		return nuevaPregunta;
	}

	public void setNuevaPregunta(String nuevaPregunta) {
		this.nuevaPregunta = nuevaPregunta;
	}
	
	public void addPregunta(ActionEvent e){
		System.out.println("ActionEvent");
	}
	
	/*public void borrarPregunta(ActionEvent e){ //Mio queda anticuado otra forma de hacerlo
		//System.out.println("Borrar " + e.getComponent().getId());
		//System.out.println("Borrar " + e.getComponent().getParent());
		UIComponent columna = e.getComponent().getParent();
		List<UIComponent> hijos =columna.getChildren();
		
		for (UIComponent uiComponent : hijos) {
			//System.out.println(uiComponent.getClass());
			//System.out.println(uiComponent.getClientId());
			if (uiComponent.getClientId().endsWith("EncuRef")){
				HtmlInputHidden hiden = (HtmlInputHidden) uiComponent;
				//System.out.println(hiden.getValue());
				String ref = (String) hiden.getValue();
				for (Iterator<RegistroEncuesta> iterator = registrosEncuestas.iterator(); iterator.hasNext();) {
					
					if (iterator.next().getRef().equals(ref)){
						iterator.remove();
					}
					
					//System.out.println(iterator.next());
					
				}
				for (Iterator<RegistroEncuesta> iterator = registrosEncuestas.iterator(); iterator.hasNext();) {

					System.out.println(iterator.next());
				}
			}
		}

	}*/
	
	public void eliminarPregunta(ActionEvent e) {
		UIComponent component = e.getComponent();
		component = component.findComponent("preguntaRef");
		if (component!= null){
			if(component instanceof UIParameter){
				UIParameter parameter = (UIParameter) component;
				RegistroEncuesta reg = (RegistroEncuesta) parameter.getValue();
				registrosEncuestas.remove(reg);
				if ( registrosEncuestas.isEmpty()){
					visibilidadDetalle="display: none;";
				}
			}
			
		}
	}
	
	public void selectPregunta(ValueChangeEvent e){
		System.out.println(e.getNewValue());
		System.out.println("ValueChangeEvent");
		
		String ref = (String) e.getNewValue();
		
		Configuration conf = new Configuration();
		SessionFactory sf = conf.configure().buildSessionFactory();
		Session session = sf.openSession();
		
		Pregunta p=(Pregunta) session.get(Pregunta.class, ref);
		if (p!=null){
			visibilidadDetalle ="display: inline-block;";
			RegistroEncuesta re= new RegistroEncuesta();
			re.setRef(p.getRef());
			re.setTexto(p.getTexto());
			re.setTipo(p.getTipo());
			registrosEncuestas.add(re);
		}
		session.close();
		//preguntasEncuesta.add((String) e.getNewValue());
	}

	public List<RegistroEncuesta> getRegistrosEncuestas() {
		return registrosEncuestas;
	}

	public String getVisibilidadDetalle() {
		return visibilidadDetalle;
	}

	public void setVisibilidadDetalle(String visibilidadDetalle) {
		this.visibilidadDetalle = visibilidadDetalle;
	}

	public String getProposito() {
		return proposito;
	}

	public void setProposito(String proposito) {
		this.proposito = proposito;
	}

	/*public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		if (nombre.startsWith("Encuesta")){
			this.nombre = nombre;
		}else{
			FacesMessage fc = new FacesMessage("El nombre debe ser encuesta<X>");
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.addMessage("nombreE", fc);
		}
		
	}*/

	/*public int getValoracion() {
		return valoracion;
	}

	public void setValoracion(int valoracion) {
		if (valoracion <0 || valoracion>10){
			this.valoracion = valoracion;
		}else{
			FacesMessage fc = new FacesMessage("Valoracion debe estar entre 0 y 10");
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.addMessage("importanciaE", fc);
		}
		
	}*/
	
	
}
