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
import java.io.IOException;
import java.util.List;

import net.bioclipse.cdk.business.CDKManager;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.rdf.business.IRDFStore;
import net.bioclipse.rdf.business.JenaModel;
import net.bioclipse.rdf.business.RDFManager;
import net.bioclipse.ui.business.UIManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class RDFEditor
extends EditorPart implements ISelectionListener ,
	IResourceChangeListener, ISelectionProvider
	{

    private final class ResourceSelectedListener implements
			ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			if (selection instanceof StructuredSelection) {
				Object firstElem = ((StructuredSelection)selection).getFirstElement();
				if (firstElem instanceof Resource) {
					Resource res = (Resource)firstElem;
					Model model = ((JenaModel)store).getModel();
					StmtIterator iter = model.listStatements(
						res, RDF.type, model.createResource("http://www.bioclipse.net/structuredb/#Molecule")
					);
					if (iter.hasNext()) {
						// it's a molecule :)
						NodeIterator smileses = model.listObjectsOfProperty(
							res, model.createProperty("http://rdf.openmolecules.net/?smiles")
						);
						if (smileses.hasNext()) {
							Literal smiles = (Literal)smileses.next();
							// just take the first
							try {
								ICDKMolecule mol = cdk.fromSMILES(smiles.getString());
								setSelection(new StructuredSelection(mol));
							} catch (BioclipseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

    private final class ResourceDoubleClickedListener implements
    IDoubleClickListener {

		@Override
		public void doubleClick(DoubleClickEvent event) {
			ISelection selection = event.getSelection();
			if (selection instanceof StructuredSelection) {
				Object firstElem = ((StructuredSelection)selection).getFirstElement();
				if (firstElem instanceof Resource) {
					Resource res = (Resource)firstElem;
					Model model = ((JenaModel)store).getModel();
					StmtIterator iter = model.listStatements(
						res, RDF.type, model.createResource("http://www.bioclipse.net/structuredb/#Molecule")
					);
					if (iter.hasNext()) {
						// it's a molecule :)
						NodeIterator smileses = model.listObjectsOfProperty(
							res, model.createProperty("http://rdf.openmolecules.net/?smiles")
						);
						if (smileses.hasNext()) {
							Literal smiles = (Literal)smileses.next();
							// just take the first
							try {
								ICDKMolecule mol = cdk.fromSMILES(smiles.getString());
								ui.open(mol);
							} catch (BioclipseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CoreException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
    }

    private GraphViewer viewer;
    private RDFContentProvider contentProvider;

    private IRDFStore store = null; 
    
    RDFManager rdf = new RDFManager();
    CDKManager cdk = new CDKManager();
    UIManager  ui  = new UIManager();

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
        viewer.addSelectionChangedListener(
        	new ResourceSelectedListener()
        );
        viewer.addDoubleClickListener(
        	new ResourceDoubleClickedListener()
        );
        updateModel();
        getEditorSite().getPage().addSelectionListener(this);
        getSite().setSelectionProvider(this);
	}

	private void updateModel() {
		if (store == null) {
			try {
				this.store = rdf.createInMemoryStore();
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

	private ListenerList listeners = new ListenerList();

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	private ISelection selection = null;
	
	@Override
	public ISelection getSelection() {
		return this.selection;
	}

	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setSelection(ISelection selection) {
		this.selection = selection;
		final SelectionChangedEvent event = new SelectionChangedEvent(this, selection);

		for (Object listenerO : listeners.getListeners()) {
			final ISelectionChangedListener listener = (ISelectionChangedListener)listenerO;
            Display.getDefault().asyncExec( new Runnable() {
                public void run() {
                    listener.selectionChanged(event);
                }
            });
		}
	}

}
