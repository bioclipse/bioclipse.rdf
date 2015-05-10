/*******************************************************************************
 * Copyright (c) 2014  Egon Willighagen <egonw@users.sourceforge.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.owlapi.business;

import java.io.IOException;
import java.util.List;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

import org.eclipse.core.runtime.CoreException;
import org.semanticweb.owlapi.model.OWLOntology;

@PublishedClass(
    value="Provide functionality to handle OWL files, using the OWLAPI library.",
    doi="10.3233/SW-2011-0025"
)
public interface IOWLAPIManager extends IBioclipseManager {

	@Recorded
    @PublishedMethod(
        params = "IRIMapper mapper, String ontologyURI, String file",
        methodSummary = "Defines where to locally find (in the Bioclipse workspace) the "
        		+ "instance of a particular ontology. If the mappers is null, it will "
        		+ "create a new one."
    )
    public IRIMapper addMapping(IRIMapper mapper, String ontologyURI, String file)
        throws IOException, BioclipseException, CoreException;

	@Recorded
    @PublishedMethod(
        params = "String target, IRIMapper iriMapper",
        methodSummary = "Loads an OWL ontology, possible using an IRI mapping to "
        		+ "resolve OWL ontology IRIs to IRIs where the OWL file can be physically "
        		+ "found."
    )
    public OWLOntology load(String target, IRIMapper iriMapper)
        throws IOException, BioclipseException, CoreException;

	@Recorded
    @PublishedMethod(
        params = "IRIMapper mapper",
        methodSummary = "Lists the mappings defined by this mapper."
    )
    public String listMappings(IRIMapper mapper)
        throws IOException, BioclipseException, CoreException;

	@Recorded
    @PublishedMethod(
        params = "OWLOntology ontology",
        methodSummary = "Checks for OWL DL profile violations."
    )
    public String checkVioloations(OWLOntology ontology)
        throws IOException, BioclipseException, CoreException;

	@Recorded
    @PublishedMethod(
        params = "OWLOntology ontology",
        methodSummary = "Shows the (non-asserted) OWL classes."
    )
    public String showClasses(OWLOntology ontology)
        throws IOException, BioclipseException, CoreException;

	@Recorded
    @PublishedMethod(
        params = "OWLOntology ontology",
        methodSummary = "Returns the OWL annotation properties as a List of String's."
    )
    public List<String> getAnnotationProperties(OWLOntology ontology)
        throws IOException, BioclipseException, CoreException;

	@Recorded
    @PublishedMethod(
        params = "OWLOntology ontology",
        methodSummary = "Returns the (non-asserted) OWL classes as a List of String's."
    )
    public List<String> getClasses(OWLOntology ontology)
        throws IOException, BioclipseException, CoreException;

	@Recorded
    @PublishedMethod(
        params = "OWLOntology ontology",
        methodSummary = "Shows the OWL annotationproperties."
    )
    public String showAnnotationProperties(OWLOntology ontology)
        throws IOException, BioclipseException, CoreException;

	@Recorded
    @PublishedMethod(
        params = "OWLOntology ontology",
        methodSummary = "Lists the imported ontologies."
    )
	public List<OWLOntology> getImportedOntologies(OWLOntology ontology)
        throws IOException, BioclipseException, CoreException;

}
