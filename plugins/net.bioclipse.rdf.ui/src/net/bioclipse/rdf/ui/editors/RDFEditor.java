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

import java.io.ByteArrayInputStream;
import java.util.List;

import net.bioclipse.rdf.business.IRDFStore;
import net.bioclipse.rdf.business.JenaModel;
import net.bioclipse.rdf.business.RDFManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

public class RDFEditor
extends EditorPart implements ISelectionListener ,
	IResourceChangeListener {

    private GraphViewer viewer;
    private RDFContentProvider contentProvider;

    private IRDFStore store = null; 
    
    RDFManager rdf = new RDFManager();

	public RDFEditor() {
		super();
	}

	public void dispose() {
		super.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {}
	
	@Override
	public void doSaveAs() {}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
        setInput(input);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
        viewer = new GraphViewer(parent, SWT.NONE);
        contentProvider = new RDFContentProvider();
        viewer.setContentProvider(contentProvider);
        viewer.setLabelProvider(new RDFLabelProvider());
        viewer.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        updateModel();
        getEditorSite().getPage().addSelectionListener(this);
	}

	private void updateModel() {
		if (store == null) {
			try {
				IRDFStore store = rdf.createInMemoryStore();
				readEditorInputIntoStore(store);
				contentProvider.setModel(((JenaModel)store).getModel());
				List<List<String>> results = rdf.sparql(store,
					"SELECT ?s WHERE {?s a [] } LIMIT 1"
				);
				viewer.setInput(results.get(0).get(0));
			} catch (Exception exception) {
				exception.printStackTrace();
				viewer.setInput(exception.getMessage());
			}
		}		
	}
	
	private void readEditorInputIntoStore(IRDFStore store) {
		IRDFStore rdfStore = (IRDFStore)getEditorInput().getAdapter(IRDFStore.class);
		if (rdfStore != null) {
			try {
				rdf.copy(store, rdfStore);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String data = (String)getEditorInput().getAdapter(String.class);
		if (data != null) {
			try {
				rdf.importFromStream(
					store,
					new ByteArrayInputStream(data.getBytes()),
					"N3", null
				);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		IFile file = (IFile)getEditorInput().getAdapter(IFile.class);
		if (file != null) {
			try {
				rdf.importFromStream(
					store,
					file.getContents(),
					"N3", null
				);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		updateModel();
	}

}
