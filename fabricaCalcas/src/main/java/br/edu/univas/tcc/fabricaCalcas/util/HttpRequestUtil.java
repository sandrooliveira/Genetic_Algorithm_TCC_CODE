package br.edu.univas.tcc.fabricaCalcas.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtil {
	public static Object getParameterValueInRequest(String parameterName) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		
		Object value = null;
		if (request != null) {
			value = request.getParameter(parameterName);
		}
		
		return value;
	}
}

