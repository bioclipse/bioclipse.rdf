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
 * Generalization of an RDF Literal.
 */
public interface IRDFLiteral extends IRDFResource {

	/**
	 * Language tag for plain literals.
	 */
	public String getLanguage();

	/**
	 * Datatype URI for types literals.
	 */
	public String getDatatype();
    
}