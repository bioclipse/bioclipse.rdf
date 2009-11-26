/*******************************************************************************
 * Copyright (c) 2009  Egon Willighagen <egonw@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.pellet.business;

import net.bioclipse.rdf.business.IJenaStore;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class PelletInMemoryModel implements IJenaStore {

    private Model model;
    
    protected PelletInMemoryModel() {
        model = ModelFactory.createOntologyModel(
            PelletReasonerFactory.THE_SPEC
        );
    }
    
    public Model getModel() {
        return this.model;
    }
    
    public String toString() {
        return "RDFStore (Pellet in-memory): " + model.size() + " triples";
    }
    
}
