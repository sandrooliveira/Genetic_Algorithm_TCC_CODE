package br.edu.univas.tcc.fabricaCalcas.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import br.edu.univas.tcc.fabricaCalcas.model.Costureira;


@FacesConverter(value = "costureiraConverter")
public class CostureiraConverter implements Converter {
	
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
        	Costureira entity = (Costureira) value;  
  
            this.addAttribute(component, entity);  
  
            Integer codigo = entity.getIdCostureira();  
            if (codigo != null) {  
                return String.valueOf(codigo);  
            }  
        }  
  
        return (String) value; 		
	}
	
    protected void addAttribute(UIComponent component, Costureira o) {  
        String key = o.getIdCostureira().toString();  
        this.getAttributesFrom(component).put(key, o);  
    }  
    
    protected Map<String, Object> getAttributesFrom(UIComponent component) {  
        return component.getAttributes();  
    } 
}