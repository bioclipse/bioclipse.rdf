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
package net.bioclipse.owl.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.rdf.business.IRDFManager;
import net.bioclipse.rdf.business.IRDFStore;
import net.bioclipse.rdf.business.RDFManager;

public class OWLManager implements IBioclipseManager {

    IRDFManager rdf = new RDFManager(); 
    
    public String getNamespace() {
        return "owl";
    }
    
    private final String SPARQL_GET_ALL_OWL_CLASSES =
        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
        "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
        "SELECT ?o WHERE { "+
        "  ?o rdf:type owl:Class." +
        "}";

    public List<String> listClasses(IRDFStore store)
        throws IOException, BioclipseException, CoreException {
        List<String> classes = new ArrayList<String>();
        List<List<String>> sparqlResults = rdf.reason(
            store, SPARQL_GET_ALL_OWL_CLASSES
        );
        for (List<String> sparqlRows : sparqlResults) {
            classes.add(sparqlRows.get(0));
        }
        return classes;
    }
    
}
