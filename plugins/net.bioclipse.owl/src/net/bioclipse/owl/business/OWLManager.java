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
import java.util.List;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.rdf.business.IRDFManager;
import net.bioclipse.rdf.business.IRDFStore;
import net.bioclipse.rdf.business.RDFManager;

import org.eclipse.core.runtime.CoreException;

public class OWLManager implements IBioclipseManager {

    IRDFManager rdf = new RDFManager(); 
    
    public String getNamespace() {
        return "owl";
    }
    
    public List<String> listClasses(IRDFStore store)
        throws IOException, BioclipseException, CoreException {
        return rdf.isRDFType(
            store, "http://www.w3.org/2002/07/owl#Class"
        );
    }
    
}
