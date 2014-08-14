/* Copyright (c) 2014  Egon Willighagen <egonw@users.sourceforge.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 */
package net.bioclipse.owlapi.business;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.semanticweb.owlapi.model.IRI;

/**
 * Helper class for keeping track of where OWL ontologies can be really found.
 * 
 * @author egonw
 */
public class IRIMapper {

	private Map<IRI,IRI> iriMappings = new HashMap<>();
	
	public void addMapping(String owlIRI, String realIRI) {
		iriMappings.put(IRI.create(owlIRI), IRI.create(realIRI));
	}

	public Map<IRI,IRI> getMappings() {
		return Collections.unmodifiableMap(iriMappings);
	}
}
