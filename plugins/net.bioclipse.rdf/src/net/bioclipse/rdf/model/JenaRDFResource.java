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
    
}