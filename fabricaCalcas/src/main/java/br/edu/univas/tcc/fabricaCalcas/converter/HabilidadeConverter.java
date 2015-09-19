package br.edu.univas.tcc.fabricaCalcas.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.web.jsf.FacesContextUtils;

import br.edu.univas.tcc.fabricaCalcas.model.Habilidade;

@FacesConverter(value = "habilidadeConverter")
public class HabilidadeConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		if (value != null && !value.isEmpty()) {
			return (Habilidade) FacesContextUtils
					.getWebApplicationContext(FacesContext.getCurrentInstance())
					.getServletContext().getAttribute(value);
		} else {
			return null;
		}

	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null || value.equals("")) {
			return "";
		} else {
			FacesContextUtils
					.getWebApplicationContext(FacesContext.getCurrentInstance())
					.getServletContext()
					.setAttribute(((Habilidade) value).toString(),
							((Habilidade) value));
			return ((Habilidade) value).toString();

		}
	}

}
