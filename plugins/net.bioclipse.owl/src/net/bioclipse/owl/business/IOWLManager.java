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

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.StringMatrix;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.rdf.business.IRDFStore;

import org.eclipse.core.runtime.CoreException;

@PublishedClass("Contains OWL related methods")
public interface IOWLManager extends IBioclipseManager {

    @PublishedMethod(
         params = "IRDFStore store",
         methodSummary = "Returns a list of all OWL classes."
    )
    public StringMatrix listClasses(IRDFStore store)
        throws IOException, BioclipseException, CoreException;
    
    @PublishedMethod(
        params = "IRDFStore store",
        methodSummary = "Returns a list of all OWL object properties."
    )
    public StringMatrix listObjectProperties(IRDFStore store)
    throws IOException, BioclipseException, CoreException;
       
    @PublishedMethod(
        params = "IRDFStore store",
        methodSummary = "Returns a list of all OWL data type properties."
    )
    public StringMatrix listDatatypeProperties(IRDFStore store)
    throws IOException, BioclipseException, CoreException;
       
}