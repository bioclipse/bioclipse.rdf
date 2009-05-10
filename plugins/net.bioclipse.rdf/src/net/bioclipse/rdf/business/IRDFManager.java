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
        params = "IRDFStore store, String target, String format",
        methodSummary = "Loads an RDF file in the given content format " +
        		"(\"RDF/XML\", \"N-TRIPLE\", \"TURTLE\" and \"N3\") into " +
        		"the given store"
    )
    public IRDFStore importFile(IRDFStore store, String target, String format)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    public IRDFStore importFile(IRDFStore store, IFile target, String format,
            IProgressMonitor monitor)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String url",
        methodSummary = "Loads a RDF/XML file from the URL into the given store"
    )
    public IRDFStore importURL(IRDFStore store, String url)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    public IRDFStore importURL(IRDFStore store, String url, IProgressMonitor monitor)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store",
        methodSummary = "Dumps the full model to the returned String"
    )
    public String dump(IRDFStore store);

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String query",
        methodSummary = "Returns the results matching the SPARQL query using" +
        		" the Pellet reasoner"
    )
    public List<List<String>> reason(IRDFStore store, String query)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String query",
        methodSummary = "Returns the results matching the SPARQL query"
    )
    public List<List<String>> sparql(IRDFStore store, String query)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store",
        methodSummary = "Validates the consistency of the RDF model in the store"
    )
    public void validate(IRDFStore store)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String subject, String predicate, " +
            "String object",
        methodSummary = "Adds a triple to the given store"
    )
    public void addObjectProperty(IRDFStore store,
        String subject, String predicate, String object)
        throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String subject, String predicate, " +
                "String value",
        methodSummary = "Adds a triple to the given store"
    )
    public void addDataProperty(IRDFStore store,
        String subject, String predicate, String value)
        throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store",
        methodSummary = "Returns the number of triples in the store"
    )
    public long size(IRDFStore store) throws BioclipseException;

}