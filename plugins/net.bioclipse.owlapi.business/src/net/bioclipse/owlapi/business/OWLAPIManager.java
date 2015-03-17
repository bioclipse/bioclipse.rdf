/* Copyright (c) 2014  Egon Willighagen <egonw@users.sourceforge.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 */
package net.bioclipse.owlapi.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.bioclipse.business.BioclipsePlatformManager;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class OWLAPIManager implements IBioclipseManager {

	BioclipsePlatformManager bioclipse = new BioclipsePlatformManager();

    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "owlapi";
    }

    public IRIMapper addMapping(IRIMapper mapper, String ontologyURI, IFile file, IProgressMonitor monitor) {
    	if (monitor == null) monitor = new NullProgressMonitor();
    	if (mapper == null) mapper = new IRIMapper();

    	String filename = bioclipse.fullPath(file);
    	mapper.addMapping(ontologyURI, "file://" + filename);
    	
    	return mapper;
    }

	public OWLOntology load(IFile target, IRIMapper iriMapper,
            IProgressMonitor monitor)
    throws IOException, BioclipseException, CoreException {
    	if (monitor == null) monitor = new NullProgressMonitor();

    	OWLOntologyManager man = OWLManager.createOWLOntologyManager();
    	if (iriMapper != null) {
    		Map<IRI,IRI> mappings = iriMapper.getMappings();
    		for (IRI iri : mappings.keySet()) {
    			man.addIRIMapper(new SimpleIRIMapper(iri, mappings.get(iri)));
    		}
    	}
    	
    	// load the file
    	String filename = bioclipse.fullPath(target);
    	OWLOntology onto;
		try {
			onto = man.loadOntology(
				IRI.create("file://" + filename)
			);
		} catch (OWLOntologyCreationException exception) {
			throw new BioclipseException(
				"Error while reading the OWL file: " + exception.getMessage(),
				exception
			);
		}
    	
    	return onto;
    }

	public String listMappings(IRIMapper mapper, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (mapper == null) throw new BioclipseException("IRIMapper cannot be null");
		if (monitor == null) monitor = new NullProgressMonitor();

		StringBuffer list = new StringBuffer();
		Map<IRI,IRI> mappings = mapper.getMappings();
		for (IRI iri : mappings.keySet()) {
			list.append(iri.toString()).append(" -> ")
			    .append(mappings.get(iri).toString())
			    .append('\n');
		}
		return list.toString();
	}

	public String checkVioloations(OWLOntology ontology, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

		StringBuffer list = new StringBuffer();
		OWL2DLProfile profile = new OWL2DLProfile();
		OWLProfileReport report = profile.checkOntology(ontology);
		for(OWLProfileViolation v:report.getViolations()) {
			list.append(v).append('\n');
		}
		return list.toString();
	}

	public String showClasses(OWLOntology ontology, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

		StringBuffer list = new StringBuffer();
		for (OWLClass cls : ontology.getClassesInSignature()) {
			list.append(cls.getIRI().toString()).append('\n');
		}
		return list.toString();
	}

	public String showProperties(OWLOntology ontology, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

		StringBuffer list = new StringBuffer();
		for (OWLAnnotationProperty prop : ontology.getAnnotationPropertiesInSignature()) {
			list.append(prop.getIRI().toString()).append('\n');
		}
		return list.toString();
	}

	public List<String> getClasses(OWLOntology ontology, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

		List<String> list = new ArrayList<String>();
		for (OWLClass cls : ontology.getClassesInSignature()) {
			list.add(cls.getIRI().toString());
		}
		return list;
	}

	public List<String> getProperties(OWLOntology ontology, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

		List<String> list = new ArrayList<String>();
		for (OWLAnnotationProperty prop : ontology.getAnnotationPropertiesInSignature()) {
			list.add(prop.getIRI().toString());
		}
		return list;
	}

	public List<OWLOntology> getImportedOntologies(OWLOntology ontology, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

        List<OWLOntology> list = new ArrayList<OWLOntology>();
		for (OWLOntology importedOntology : ontology.getImports()) {
			list.add(importedOntology);
		}
		return list;
	}
}
