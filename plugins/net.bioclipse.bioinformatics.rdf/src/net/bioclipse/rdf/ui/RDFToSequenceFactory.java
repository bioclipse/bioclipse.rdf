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

public class RDFToSequenceFactory
       implements IRDFToBioObjectFactory,
                  IExecutableExtension,
                  IExecutableExtensionFactory {

    public static final String URI
        = "http://rdf.farmbio.uu.se/chembl/onto/#Target";

    private static RDFToSequenceFactory instance;

    public IBioObject rdfToBioObject( Model model, Resource res ) {
    	NodeIterator sequences = model.listObjectsOfProperty(
    		res,
    		model.createProperty(
    			"http://rdf.farmbio.uu.se/chembl/onto/#sequence"
    		)
        );
    	if (sequences.hasNext()) {
    		Literal sequence = (Literal)sequences.next();
    		// just take the first
    		return net.bioclipse.biojava.business.Activator.getDefault()
    			.getJavaBiojavaManager().proteinFromPlainSequence(
    				sequence.getString()
    			);
    	}
    	return null;
    }

    public void setInitializationData( IConfigurationElement config,
                                       String propertyName,
                                       Object data ) throws CoreException {
        if ( instance == null ) {
            instance = new RDFToSequenceFactory();
        }
    }

    public Object create() throws CoreException {
        return instance;
    }

    public ImageDescriptor getImageDescriptor() {
        return AbstractUIPlugin.imageDescriptorFromPlugin( 
                   "net.bioclipse.bioinformatics.rdf", 
                   "icons/sequence4.gif" );
    }
}
