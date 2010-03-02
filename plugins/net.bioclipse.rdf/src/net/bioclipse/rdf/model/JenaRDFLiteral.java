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


/**
 * Jena-based implementation of {@link IRDFResource}.
 */
public class JenaRDFLiteral implements IRDFLiteral {

	private String language = null;
	private String datatype = null;

	public JenaRDFLiteral(String language, String datatype) {
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

	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}
    
}