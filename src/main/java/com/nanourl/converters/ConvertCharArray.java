package com.nanourl.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="com.nanourl.converters.ConvertCharArray", managed=true)
public class ConvertCharArray implements Converter {
	public char[] getAsObject(FacesContext cxt, UIComponent comp, String value)
		throws ConverterException {
			if(value == null) {
				return null;
			}
			return value.toCharArray();
		}

	public String getAsString(FacesContext cxt, UIComponent comp, Object value)
		throws ConverterException {
			return new String((char [])value);
	}
}