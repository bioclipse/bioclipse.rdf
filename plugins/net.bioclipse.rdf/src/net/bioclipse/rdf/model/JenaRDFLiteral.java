/*******************************************************************************
 * Copyright (c) 2010  Egon Willighagen <egonw.willighagen@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 *******************************************************************************/
package net.bioclipse.rdf.model;

import java.util.ArrayList;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;


/**
 * Jena-based implementation of {@link IRDFResource}.
 */
public class JenaRDFLiteral implements IRDFLiteral {

	private String value = null;
	private String language = null;
	private String datatype = null;

	public JenaRDFLiteral(String value, String language, String datatype) {
		this.value = value;
		this.language = language;
		this.datatype = datatype;
	}

	public String getDatatype() {
		return this.datatype;
	}

	public String getLanguage() {
		return this.language;
	}

	public String getRDFSLabel() {
		return "";
	}

	public String getRDFType() {
		return "";
	}

	@SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
		if ( adapter.isAssignableFrom( IPropertySource.class ) ) {
		    return new PropertySource( this );
		}
		return null;
	}

	public String getValue() {
		return this.value;
	}


	/**
	 * The PropertySource is used by the properties view.
	 *
	 * @author jonalv
	 */
	public static class PropertySource implements IPropertySource {

	    private JenaRDFLiteral literal;
	    private IPropertyDescriptor[] propertyDescriptors;

	    public static final String PROPERTY_LANGUAGE = "Language";
	    public static final String PROPERTY_DATATYPE = "Data type";
	    public static final String PROPERTY_VALUE    = "Value";

	    public PropertySource(JenaRDFLiteral literal) {
	        this.literal = literal;
	    }

        public Object getEditableValue() {
            return null;
        }

        public IPropertyDescriptor[] getPropertyDescriptors() {
            if ( propertyDescriptors == null ) {
                ArrayList<IPropertyDescriptor> properties
                    = new ArrayList<IPropertyDescriptor>();

                for ( String p : new String[] { PROPERTY_LANGUAGE,
                                                PROPERTY_DATATYPE,
                                                PROPERTY_VALUE } ) {
                    PropertyDescriptor descriptor
                        = new TextPropertyDescriptor(p, p);
                    descriptor.setCategory("RDF");
                    properties.add(descriptor);
                }

                propertyDescriptors = properties.toArray(
                                          new IPropertyDescriptor[2] );
            }
            return propertyDescriptors;
        }

        public Object getPropertyValue( Object id ) {
            if (PROPERTY_LANGUAGE.equals( id ) ) {
                return literal.language;
            }
            if ( PROPERTY_DATATYPE.equals( id ) ) {
                return literal.datatype;
            }
            if ( PROPERTY_VALUE.equals( id ) ) {
                return literal.value;
            }
            return null;
        }

        public boolean isPropertySet( Object id ) {
            return false;
        }

        public void resetPropertyValue( Object id ) {
        }

        public void setPropertyValue( Object id, Object value ) {
        }
	}
}