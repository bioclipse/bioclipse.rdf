/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
 |  Copyright (c) 2010  Jonathan Alvarsson <jonalv@users.sourceforge.net>    |
 *                                                                           *
 |  All rights reserved. This program and the accompanying materials         |
 *  are made available under the terms of the Eclipse Public License v1.0    *
 |  which accompanies this distribution, and is available at                 |
 *  www.eclipse.org�epl-v10.html <http://www.eclipse.org/legal/epl-v10.html> *
 |                                                                           |
 *  Contact: http://www.bioclipse.net/                                       *
 |                                                                           |
 *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
package net.bioclipse.rdf.ui;

import net.bioclipse.core.domain.IBioObject;
import net.bioclipse.core.domain.SMILESMolecule;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;


/**
 * @author jonalv
 *
 */
public class RDFToCDKMoleculeFactory
       implements IRDFToBioObjectFactory,
                  IExecutableExtension,
                  IExecutableExtensionFactory {

    public static final String URI
        = "http://www.bioclipse.net/structuredb/#Molecule";
    private final Logger logger
        = Logger.getLogger( RDFToCDKMoleculeFactory.class );

    private static RDFToCDKMoleculeFactory instance;

    public IBioObject rdfToBioObject( Model model, Resource res ) {

        StmtIterator iter
            = model.listStatements( res,
                                    RDF.type,
                                    model.createResource(URI)
        );
        if (iter.hasNext()) {
            // it's a molecule :)
            NodeIterator smileses
                = model.listObjectsOfProperty(
                      res,
                      model.createProperty(
                           "http://rdf.openmolecules.net/?smiles" ) );
            if (smileses.hasNext()) {
                Literal smiles = (Literal)smileses.next();
                // just take the first
                return new SMILESMolecule(smiles.getString());
            }
        }
        return null;
    }

    public void setInitializationData( IConfigurationElement config,
                                       String propertyName,
                                       Object data ) throws CoreException {
        if ( instance == null ) {
            instance = new RDFToCDKMoleculeFactory();
        }
    }

    public Object create() throws CoreException {
        return instance;
    }

    public ImageDescriptor getImageDescriptor() {
        return AbstractUIPlugin.imageDescriptorFromPlugin( 
                   "net.bioclipse.chemoinformatics.rdf", 
                   "icons/chemistry_32.jpeg" );
    }
}
