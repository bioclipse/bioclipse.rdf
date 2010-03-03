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

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Jena-based implementation of {@link IRDFResource}.
 */
public class JenaRDFResource implements IRDFResource {

	private Model model;
	private Resource resource;

	public JenaRDFResource(Model model, Resource resource) {
		this.model = model;
		this.resource = resource;
	}

	/**
	 * Returns the rdfs:label value, or an empty String if not defined.
	 *
	 * @return A non-null, possibly zero-length String.
	 */
	public String getRDFSLabel() {
		if (model != null && resource != null) {
			Statement statement = model.getProperty(resource, RDFS.label);
			if (statement != null) {
				RDFNode object = statement.getObject();
				if (object.isLiteral()) return ((Literal)object).getString();
			}
		}
		return "";
	}

	/**
	 * Returns the rdf:type value, or an empty String if not defined.
	 *
	 * @return A non-null, possibly zero-length String.
	 */
	public String getRDFType() {
		if (model != null && resource != null) {
			Statement statement = model.getProperty(resource, RDF.type);
			if (statement != null) {
				RDFNode object = statement.getObject();
				if (object.isResource()) return ((Resource)object).getURI();
			}
		}
		return "";
	}

	@SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
	    if ( adapter.isAssignableFrom( IPropertySource.class ) ) {
	        return new PropertySource( this );
	    }
	    return null;
	}

	 /**
     * The PropertySource is used by the properties view.
     *
     * @author jonalv
     */
    public static class PropertySource implements IPropertySource {

        private JenaRDFResource resource;
        private IPropertyDescriptor[] propertyDescriptors;

        public static final String PROPERTY_TYPE  = "Type";
        public static final String PROPERTY_LABEL = "Label";

        public PropertySource(JenaRDFResource resource) {
            this.resource = resource;
        }

        public Object getEditableValue() {
            return null;
        }

        public IPropertyDescriptor[] getPropertyDescriptors() {
            if ( propertyDescriptors == null ) {
                ArrayList<IPropertyDescriptor> properties
                    = new ArrayList<IPropertyDescriptor>();

                for ( String p : new String[] { PROPERTY_TYPE,
                                                PROPERTY_LABEL } ) {
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
            if (PROPERTY_TYPE.equals( id ) ) {
                return resource.getRDFType();
            }
            if ( PROPERTY_LABEL.equals( id ) ) {
                return resource.getRDFSLabel();
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