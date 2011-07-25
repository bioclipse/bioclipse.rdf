/*******************************************************************************
 * Copyright (c) 2009-2010  Egon Willighagen <egonw@users.sf.net>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.rdf.ui.editors;

import net.bioclipse.rdf.ui.IRDFToBioObjectFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

public class RDFLabelProvider extends LabelProvider {

	private Model model;
    
    public void setModel(Model model) {
        this.model = model;
    }
	
    public String getText(Object element) {
    	if (element instanceof Statement) {
    		Property pred = ((Statement)element).getPredicate();
    		String prefix = model.getNsURIPrefix(pred.getNameSpace());
    		if (prefix != null)
    			return prefix + ":" + pred.getLocalName();
    	}

    	if (element instanceof Node)
    		return ((Node)element).getLocalName();

    	if (element instanceof Resource) {
    		Resource resource = (Resource)element;
    		String prefix = model.getNsURIPrefix(resource.getNameSpace());
    		if (prefix != null)
    			return prefix + ":" + resource.getLocalName();
    	}

    	if (element instanceof Literal) {
    		Literal literal = (Literal)element;
    		String litString = literal.getString();
    		if (litString.length() > 50)
    			litString = litString.substring(0, 47) + "...";
    		return litString;
    	}

    	return element.toString();
    }

    @Override
    public Image getImage( Object element ) {

        IExtensionPoint serviceObjectExtensionPoint
            = Platform.getExtensionRegistry().getExtensionPoint(
                  "net.bioclipse.rdf.rdf2bioobjectfactory" );

        IExtension[] serviceObjectExtensions
            = serviceObjectExtensionPoint.getExtensions();
        for ( IExtension extension : serviceObjectExtensions ) {
            for ( IConfigurationElement e
                            : extension.getConfigurationElements() ) {

                String serviceURI = e.getAttribute("uri");
    
                if ( !(element instanceof Resource) ) {
                    continue;
                }
                // check if this extension supports this Resource
                NodeIterator iter =
                    model.listObjectsOfProperty( (Resource) element, RDF.type );
                boolean match = false;
                while (iter.hasNext()) {
                    RDFNode node = iter.next();
                    if (node.isURIResource() &&
                        ((Resource)node).getURI().equals(serviceURI))
                        match = true;
                }
    
                if (!match) continue;
    
                Object service = null;
                try {
                    service
                     = e.createExecutableExtension("instance");
                }
                catch (CoreException ex) {
                    throw new IllegalStateException(
                                  "Failed to get a service: "
                                      + ex.getMessage(),
                                  ex );
                } 
                if ( service == null ) {
                    return super.getImage( element );
                }
                if ( !(service instanceof IRDFToBioObjectFactory) ) {
                    throw new IllegalStateException(
                         "Service object: " + service + " does not "
                         + "implement IRDFToBioObjectFactory" );
                }
                return ( (IRDFToBioObjectFactory)service ).getImageDescriptor()
                                                          .createImage();
            }
        }
        return super.getImage( element );
    }
    
    
}
