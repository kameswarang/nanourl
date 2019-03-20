package com.nanourl.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import java.sql.Timestamp;

@FacesConverter(value="com.nanourl.converters.ConvertTimestamp", managed=true)
public class ConvertTimestamp implements Converter {
	public Object getAsObject(FacesContext cxt, UIComponent comp, String value)
		throws ConverterException {
			if(value == null) {
				return null;
			}
			return null;
		}

	public String getAsString(FacesContext cxt, UIComponent comp, Object ts)
		throws ConverterException {
			return ts.toString();
	}

	public ConvertTimestamp() {
		
	}
}