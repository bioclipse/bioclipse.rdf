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

import org.eclipse.core.runtime.CoreException;

import net.bioclipse.core.api.BioclipseException;
import net.bioclipse.core.api.managers.IBioclipseManager;
import net.bioclipse.core.api.managers.PublishedClass;
import net.bioclipse.core.api.managers.PublishedMethod;
import net.bioclipse.rdf.business.IRDFStore;

@PublishedClass("Contains OWL related methods")
public interface IOWLManager extends IBioclipseManager {

    @PublishedMethod(
         params = "IRDFStore store",
         methodSummary = "Returns a list of all OWL classes."
    )
    public List<String> listClasses(IRDFStore store)
        throws IOException, BioclipseException, CoreException;
    
}