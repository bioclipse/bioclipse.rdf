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

import net.bioclipse.core.domain.IMolecule;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

/**
 * 2D Rendering widget using the new Java2D based JChemPaint renderer.
 */
public class RDFView extends ViewPart implements ISelectionListener {

    private GraphViewer viewer;
    private RDFContentProvider contentProvider;
    
    public void createPartControl( Composite parent ) {
        viewer = new GraphViewer(parent, SWT.NONE);
        contentProvider = new RDFContentProvider();
        viewer.setContentProvider(contentProvider);
        viewer.setLabelProvider(new RDFLabelProvider());
        viewer.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        getViewSite().getPage().addSelectionListener(this);
    }

    public void setFocus() {
        // TODO Auto-generated method stub
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        System.out.println("Selection changed...");
        if (!(selection instanceof IStructuredSelection)) {
            System.out.println(" not IStructuredSelection...");
            return;
        }
        
        IStructuredSelection structuredSel = (IStructuredSelection)selection;
        Object firstElement = structuredSel.getFirstElement();

        if (firstElement instanceof IMolecule) {
            IMolecule mol = (IMolecule)firstElement;
            contentProvider.setMolecule(mol);
            viewer.setInput(new Object());
        } else {
            System.out.println("  not CDKMolecule, but: " + firstElement.getClass().getName());
        }
    }
    
}