package com.microforum.gestorusuarios.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class EstiloSesion {
	private String style ="normalSize";

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	public void setLargeSize(ActionEvent e) {
		style ="largeSize";
		System.out.println(style);
		//return "largeSizeTest";
	}
	
	public String setNormalSize() {
		style ="normalSize";
		System.out.println(style);
		return "normalSizeTest";
	}
}
