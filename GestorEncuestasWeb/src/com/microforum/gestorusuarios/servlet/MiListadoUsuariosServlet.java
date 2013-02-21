package com.microforum.gestorusuarios.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.microforum.gestorusuarios.entities.Usuario;

/**
 * Servlet implementation class MiListadoUsuariosServlet
 */
@WebServlet("/MiListadoUsuariosServlet")
public class MiListadoUsuariosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MiListadoUsuariosServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Configuration conf= new Configuration();
		SessionFactory sf= conf.configure().buildSessionFactory();
		Session session = sf.openSession();
		Transaction tr = session.beginTransaction();
		
		Query query = session.createQuery("from Usuario order by TIPO_USUARIO Desc");
		List <Usuario> list= query.list();
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.write("<table><tr><th>Nombre Completo</th><th>Codigo Postal</th></tr>");
		for (Usuario user : list) {
			writer.write("<tr><td>");			
			writer.write(user.getNombreCompleto() +"</td>");
			writer.write("<td>");
			writer.write(user.getDomicilio().getCodPostal() +"</td>");
			writer.write("<td><tr>");
			
		}
		writer.write("</table>");	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
