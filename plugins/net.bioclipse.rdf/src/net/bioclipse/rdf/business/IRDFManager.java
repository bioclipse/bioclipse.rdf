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
import java.util.List;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.business.IBioclipseManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

@PublishedClass("Contains RDF related methods")
public interface IRDFManager extends IBioclipseManager {

    @Recorded
    @PublishedMethod(
        methodSummary = "Creates a new RDF store"
    )
    public IRDFStore createStore();

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store to which loaded content is added," +
                 "String path of file to load",
        methodSummary = "Loads an RDF/XML file"
    )
    public IRDFStore importFile(IRDFStore store, String target)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    public IRDFStore importFile(IRDFStore store, IFile target,
            IProgressMonitor monitor)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store to which loaded content is added," +
                 "String URL to load from",
        methodSummary = "Loads a RDF/XML file"
    )
    public IRDFStore importURL(IRDFStore store, String url)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    public IRDFStore importURL(IRDFStore store, String url, IProgressMonitor monitor)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store to dump",
        methodSummary = "Dumps the full model to the console"
    )
    public void dump(IRDFStore store)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store to query," +
                 "String SPARQL query",
        methodSummary = "Returns the results matching the query"
    )
    public List<List<String>> sparql(IRDFStore store, String query)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store to validate",
        methodSummary = "Validates the consistency of the RDF model"
    )
    public void validate(IRDFStore store)
        throws IOException, BioclipseException, CoreException;

}