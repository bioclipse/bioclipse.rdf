/*******************************************************************************
 * Copyright (c) 2009  Jonathan Alvarsson <jonalv@users.sourceforge.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.rdf.ui;

import net.bioclipse.core.api.domain.IBioObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;


/**
 * @author jonalv
 *
 */
public interface IRDFToBioObjectFactory {

    /**
     * @param model
     * @param res 
     * @return
     */
    public IBioObject rdfToBioObject( Model model, Resource res );
    
    public ImageDescriptor getImageDescriptor();
}