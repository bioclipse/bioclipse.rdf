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

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.business.IBioclipseManager;

@PublishedClass("Contains RDF related methods")
public interface IRDFManager extends IBioclipseManager {

    @Recorded
    @PublishedMethod(
        params = "String path of file to load", 
        methodSummary = "Loads a RDF/XML file"
    )
    public void loadCompound(String target)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    public void loadCompound(IFile target, IProgressMonitor monitor )
        throws IOException, BioclipseException, CoreException;

}