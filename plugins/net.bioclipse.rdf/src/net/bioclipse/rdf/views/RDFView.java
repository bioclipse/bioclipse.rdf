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

import net.bioclipse.cdk.domain.CDKMolecule;
import net.bioclipse.core.business.BioclipseException;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

/**
 * 2D Rendering widget using the new Java2D based JChemPaint renderer.
 */
public class RDFView extends ViewPart implements ISelectionListener {

    public void createPartControl( Composite parent ) {
        Image image1 = Display.getDefault().getSystemImage(SWT.ICON_INFORMATION);
        Image image2 = Display.getDefault().getSystemImage(SWT.ICON_WARNING);
        Image image3 = Display.getDefault().getSystemImage(SWT.ICON_ERROR);

        Graph g = new Graph(parent, SWT.NONE);
        g.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
        GraphNode n1 = new GraphNode(g, SWT.NONE, "Information", image1);
        GraphNode n2 = new GraphNode(g, SWT.NONE, "Warning", image2);
        GraphNode n3 = new GraphNode(g, SWT.NONE, "Error", image3);

        new GraphConnection(g, SWT.NONE, n1, n2);
        new GraphConnection(g, SWT.NONE, n2, n3);
        new GraphConnection(g, SWT.NONE, n3, n3);

        g.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
    }

    public void setFocus() {
        // TODO Auto-generated method stub
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (!(selection instanceof IStructuredSelection))
            return;
        
        IStructuredSelection structuredSel = (IStructuredSelection)selection;
        Object firstElement = structuredSel.getFirstElement();

        if (firstElement instanceof CDKMolecule) {
            CDKMolecule mol = (CDKMolecule)firstElement;
            try {
                String SMILES = mol.getSMILES();
            } catch ( BioclipseException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
}