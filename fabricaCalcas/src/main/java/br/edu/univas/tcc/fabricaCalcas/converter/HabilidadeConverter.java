package br.edu.univas.tcc.fabricaCalcas.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.edu.univas.tcc.fabricaCalcas.model.Habilidade;

@FacesConverter(value = "habilidadeConverter")
public class HabilidadeConverter implements Converter {
	
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
        	Habilidade entity = (Habilidade) value;  
  
            this.addAttribute(component, entity);  
  
            Integer codigo = entity.getIdHabilidade();  
            if (codigo != null) {  
                return String.valueOf(codigo);  
            }  
        }  
  
        return (String) value; 		
	}
	
    protected void addAttribute(UIComponent component, Habilidade o) {  
        String key = o.getIdHabilidade().toString();  
        this.getAttributesFrom(component).put(key, o);  
    }  
    
    protected Map<String, Object> getAttributesFrom(UIComponent component) {  
        return component.getAttributes();  
    } 
}