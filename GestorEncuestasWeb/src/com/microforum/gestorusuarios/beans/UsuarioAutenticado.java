package com.microforum.gestorusuarios.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.microforum.gestorusuarios.entities.DatosLogin;
import com.microforum.gestorusuarios.entities.Usuario;


@ManagedBean(name="userBean")
@SessionScoped
public class UsuarioAutenticado {
	private String loginName= "LoginName";

	private Usuario user;
	
	public UsuarioAutenticado(){
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ext = fc.getExternalContext();
		String loginUserName = ext.getRemoteUser();
		
		if(loginUserName!=null){
			Configuration conf= new Configuration();
			SessionFactory sf= conf.configure().buildSessionFactory();
			Session session = sf.openSession();
			Transaction tr = session.beginTransaction();
			
			DatosLogin dl = (DatosLogin) session.get(DatosLogin.class, loginUserName);
			Usuario user = dl.getUsuario();
			this.user=user;
			tr.commit();
			session.close();
		}
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}
	
}
