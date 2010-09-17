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
package net.bioclipse.rdf.business;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.RDFReaderFImpl;

public class JenaModel implements IJenaStore {

    private Model model;
    
    public JenaModel() {
    	RDFReaderFImpl.setClassLoader(this.getClass().getClassLoader());
        model = ModelFactory.createOntologyModel();
    }

    public JenaModel( Model jenaTypeModel ) {
    	RDFReaderFImpl.setClassLoader(this.getClass().getClassLoader());
        model = ModelFactory.createOntologyModel();
        getModel().add( jenaTypeModel );
    }

    
    public Model getModel() {
        return this.model;
    }
    
    public String toString() {
        return "RDFStore: " + model.size() + " triples";
    }
    
}
