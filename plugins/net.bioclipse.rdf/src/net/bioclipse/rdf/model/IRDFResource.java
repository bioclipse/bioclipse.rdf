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

import org.eclipse.core.runtime.IAdaptable;

/**
 * Generalization of an RDF Resource.
 */
public interface IRDFResource extends IAdaptable {

	/**
	 * Returns the rdfs:label value, or an empty String if not defined.
	 *
	 * @return A non-null, possibly zero-length String.
	 */
	public String getRDFSLabel();

	/**
	 * Returns the rdf:type value, or an empty String if not defined.
	 *
	 * @return A non-null, possibly zero-length String.
	 */
	public String getRDFType();
    
}