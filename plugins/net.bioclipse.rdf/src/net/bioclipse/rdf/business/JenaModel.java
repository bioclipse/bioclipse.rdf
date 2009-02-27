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

public class JenaModel implements IRDFStore {

    private Model model;
    
    protected JenaModel() {
        model = ModelFactory.createMemModelMaker().createFreshModel();
    }
    
    protected Model getModel() {
        return this.model;
    }
    
    public String toString() {
        return "RDFStore: " + model.size() + " triples";
    }
    
}
