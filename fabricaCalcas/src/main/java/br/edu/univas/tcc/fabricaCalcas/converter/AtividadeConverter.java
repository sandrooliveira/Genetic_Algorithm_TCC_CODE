package br.edu.univas.tcc.fabricaCalcas.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.edu.univas.tcc.fabricaCalcas.model.Atividade;

@FacesConverter(value = "atividadeConverter")
public class AtividadeConverter implements Converter {
	
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {		
		if(value != null && !value.isEmpty()){
			return this.getAttributesFrom(component).get(value);
		}else{		
			return null;
		}
	}

	
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
        if (value != null && !"".equals(value)) {  
        	Atividade entity = (Atividade) value;  
  
            this.addAttribute(component, entity);  
  
            Integer codigo = entity.getIdAtividade();  
            if (codigo != null) {  
                return String.valueOf(codigo);  
            }  
        }  
  
        return (String) value; 		
	}
	
    protected void addAttribute(UIComponent component, Atividade o) {  
        String key = o.getIdAtividade().toString();  
        this.getAttributesFrom(component).put(key, o);  
    }  
    
    protected Map<String, Object> getAttributesFrom(UIComponent component) {  
        return component.getAttributes();  
    } 
}