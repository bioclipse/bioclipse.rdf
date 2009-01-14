/*******************************************************************************
 * Copyright (c) 2008  Egon Willighagen <egonw@users.sf.net>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org�epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 * 
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.rdf.views;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMolecule;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphContentProvider;

public class RDFContentProvider implements IGraphContentProvider {

    IMolecule molecule;
    
    public void setMolecule(IMolecule molecule) {
        this.molecule = molecule;
    }
    
    public Object getDestination( Object rel ) {
        System.out.println("getDestination");
        if ("SMILES".equals(rel)) {
            try {
                return molecule.getSMILES();
            } catch ( BioclipseException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if ("Mass".equals(rel)) {
            return "-1.0";
        } else if ("Foo".equals(rel)) {
            return "Bar";
        }
        return null;
    }

    public Object[] getElements( Object input ) {
        System.out.println("getElements");
        Object[] elements = new Object[]{ "SMILES", "Mass", "Foo" };
        return elements;
    }

    public Object getSource( Object rel ) {
        System.out.println("getSource");
        return "Molecule";
    }

    public void dispose() {
        molecule = null;
    }

    public void inputChanged( Viewer viewer, Object oldInput, Object newInput ) {
        // TODO Auto-generated method stub
    }

}
