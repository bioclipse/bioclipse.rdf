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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.search.Searcher;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import net.bioclipse.business.BioclipsePlatformManager;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

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

	public String showAnnotationProperties(OWLOntology ontology, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

		StringBuffer list = new StringBuffer();
		for (OWLAnnotationProperty prop : ontology.getAnnotationPropertiesInSignature()) {
			list.append(prop.getIRI().toString()).append('\n');
		}
		return list.toString();
	}

	public Collection<String> getClasses(OWLOntology ontology, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

		List<String> list = new ArrayList<String>();
		for (OWLClass cls : ontology.getClassesInSignature()) {
			list.add(cls.getIRI().toString());
		}
		// recurse
		for (OWLOntology importedOntology : ontology.getImports()) {
			Collection<String> subList = getClasses(importedOntology, monitor);
			list.addAll(subList);
		}
		return list;
	}

	public Collection<String> getSuperClasses(OWLOntology ontology, String clazz, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

		Set<String> allSuperClasses = new HashSet<String>();
		Set<OWLEntity> entities = ontology.getEntitiesInSignature(IRI.create(clazz));
		if (entities.isEmpty()) {
			// recurse, maybe it's there
			for (OWLOntology importedOntology : ontology.getImports()) {
				Collection<String> subList = getSuperClasses(importedOntology, clazz, monitor);
				allSuperClasses.addAll(subList);
			}
		} else {
			if (entities.size() > 1)
				throw new BioclipseException("More than one entity found with this IRI.");
			OWLEntity entity = entities.iterator().next(); // there is exactly one
			if (!(entity instanceof OWLClass))
				throw new BioclipseException("The IRI is not of an OWLClass in this ontology, but an " + entity.getEntityType());
			OWLClass owlClazz = new OWLClassImpl(IRI.create(clazz));
			Collection<OWLClassExpression> superClasses = Searcher.sup(ontology.getSubClassAxiomsForSubClass(owlClazz));
			for (OWLClassExpression superClass : superClasses) {
				OWLClass superOwlClass = superClass.asOWLClass();
				String superIri = superOwlClass.getIRI().toString();
				allSuperClasses.add(superIri);
				// recurse
				allSuperClasses.addAll(getSuperClasses(ontology, superIri, monitor));
			}
		}
		return allSuperClasses;
	}

	public Collection<OWLAnnotationProperty> getAllAnnotationProperties(OWLOntology ontology, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

		List<OWLAnnotationProperty> list = new ArrayList<OWLAnnotationProperty>();
		for (OWLAnnotationProperty prop : ontology.getAnnotationPropertiesInSignature()) {
			list.add(prop);
		}
		// recurse
		for (OWLOntology importedOntology : ontology.getImports()) {
			Collection<OWLAnnotationProperty> subList = getAllAnnotationProperties(importedOntology, monitor);
			list.addAll(subList);
		}
		return list;
	}

	public Collection<String> getAnnotationProperties(OWLOntology ontology, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

		List<String> list = new ArrayList<String>();
		for (OWLAnnotationProperty prop : getAllAnnotationProperties(ontology, monitor)) {
			list.add(prop.getIRI().toString());
		}
		return list;
	}

	public Collection<String> getPropertyDeclarationAxioms(OWLOntology ontology, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

		List<String> list = new ArrayList<String>();
		for (OWLAxiom axiom : ontology.getAxioms()) {
			if (axiom instanceof OWLDeclarationAxiom) {
				OWLEntity entity = ((OWLDeclarationAxiom)axiom).getEntity();
				if (entity.isOWLObjectProperty() || entity.isOWLDataProperty()) {
					list.add(entity.getIRI().toString());
		        }
			}
		}
		// recurse
		for (OWLOntology importedOntology : ontology.getImports()) {
			Collection<String> subList = getPropertyDeclarationAxioms(importedOntology, monitor);
			list.addAll(subList);
		}
		return list;
	}

	public Collection<OWLOntology> getImportedOntologies(OWLOntology ontology, IProgressMonitor monitor)
	throws IOException, BioclipseException, CoreException {
		if (monitor == null) monitor = new NullProgressMonitor();

        Set<OWLOntology> list = new HashSet<OWLOntology>();
		for (OWLOntology importedOntology : ontology.getImports()) {
			list.add(importedOntology);
			// recurse
			Collection<OWLOntology> subList = getImportedOntologies(importedOntology, monitor);
			list.addAll(subList);
		}
		return list;
	}

	public String getLabel(OWLOntology ontology, String classIRI, IProgressMonitor monitor)
        throws IOException, BioclipseException, CoreException {
		IRI cIRI = IRI.create(classIRI);
		OWLClass owlClass = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(cIRI);
    	System.out.println("iri: " + cIRI);
    	System.out.println("owlClass: " + owlClass);
		Collection<OWLAnnotation> annos = EntitySearcher.getAnnotations(owlClass, ontology);
	    for (OWLAnnotation annotation : annos) {
	    	System.out.println("annotation: " + annotation.getProperty().getIRI());
        	if ("http://www.w3.org/2000/01/rdf-schema#label".equals(
        		annotation.getProperty().getIRI().toString())
            ) {
        	  return ((OWLLiteral)annotation.getValue()).getLiteral();
        	}
		}
		// recurse
		for (OWLOntology importedOntology : ontology.getImports()) {
			String label = getLabel(importedOntology, classIRI, monitor);
			if (label.length() > 0) return label;
		}
		return "";
	}

	public String getDescription(OWLOntology ontology, String classIRI, IProgressMonitor monitor)
        throws IOException, BioclipseException, CoreException {
		IRI cIRI = IRI.create(classIRI);
		OWLClass owlClass = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(cIRI);
    	System.out.println("iri: " + cIRI);
    	System.out.println("owlClass: " + owlClass);
		Collection<OWLAnnotation> annos = EntitySearcher.getAnnotations(owlClass, ontology);
	    for (OWLAnnotation annotation : annos) {
	    	System.out.println("annotation: " + annotation.getProperty().getIRI());
        	if ("http://purl.obolibrary.org/obo/IAO_0000115".equals(
        		annotation.getProperty().getIRI().toString())
            ) {
        	  return ((OWLLiteral)annotation.getValue()).getLiteral();
        	}
		}
		// recurse
		for (OWLOntology importedOntology : ontology.getImports()) {
			String desc = getDescription(importedOntology, classIRI, monitor);
			if (desc.length() > 0) return desc;
		}
		return "";		
	}
}
