/*******************************************************************************
 * Copyright (c) 2009-2010  Egon Willighagen <egonw@users.sf.net>

 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.orgâ€”epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
 |  Copyright (c) 2010  Jonathan Alvarsson <jonalv@users.sourceforge.net>    |
 *                                                                           *
 |  All rights reserved. This program and the accompanying materials         |
 *  are made available under the terms of the Eclipse Public License v1.0    *
 |  which accompanies this distribution, and is available at                 |
 *  www.eclipse.orgÑepl-v10.html <http://www.eclipse.org/legal/epl-v10.html> *
 |                                                                           |
 *  Contact: http://www.bioclipse.net/                                       *
 |                                                                           |
 *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
package net.bioclipse.rdf.ui.editors;

import java.io.ByteArrayInputStream;

import net.bioclipse.core.domain.IBioObject;
import net.bioclipse.core.util.LogUtils;
import net.bioclipse.rdf.business.IRDFStore;
import net.bioclipse.rdf.business.JenaModel;
import net.bioclipse.rdf.business.RDFManager;
import net.bioclipse.rdf.model.IRDFClass;
import net.bioclipse.rdf.model.IStringMatrix;
import net.bioclipse.rdf.model.JenaRDFLiteral;
import net.bioclipse.rdf.model.JenaRDFResource;
import net.bioclipse.rdf.ui.IRDFToBioObjectFactory;
import net.bioclipse.ui.business.UIManager;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
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
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class RDFEditor
extends EditorPart implements ISelectionListener ,
	IResourceChangeListener, ISelectionProvider
	{

    private Logger logger = Logger.getLogger( RDFEditor.class );

    private final class ResourceSelectedListener
                  implements ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			if (selection instanceof StructuredSelection) {
				Object firstElem = ((StructuredSelection)selection).getFirstElement();
				Model model = ((JenaModel)store).getModel();

				if (firstElem instanceof Resource) {
				    Resource res = (Resource)firstElem;
				    IBioObject o = selectionToBioObject( selection );
				    if ( o != null ) {
				        setSelection(new StructuredSelection(o));
				    }
					else {
						// ok, we didn't recognize it.
					    // Send around a general IRDFResource selection
						IRDFClass resource = new JenaRDFResource(model, res);
						setSelection(new StructuredSelection(resource));
					}
				}
				else if (firstElem instanceof Literal) {
					Literal literal = (Literal)firstElem;
					IRDFClass literalSelection = new JenaRDFLiteral(
						literal.getString(),
						literal.getLanguage(),
						literal.getDatatypeURI()
					);
					setSelection(new StructuredSelection(literalSelection));
				} else if (firstElem instanceof Statement) {
					Statement statement = (Statement)firstElem;
					Property property = statement.getPredicate();
					IRDFClass resource = new JenaRDFResource(model, property);
					setSelection(new StructuredSelection(resource));
				} else {
					setSelection(StructuredSelection.EMPTY);
				}
			}
		}
	}

    private final class ResourceDoubleClickedListener
                  implements IDoubleClickListener {

		@Override
		public void doubleClick(DoubleClickEvent event) {
			IBioObject o = selectionToBioObject( event.getSelection() );
            try {
                if ( o != null ) {
                    ui.open( o );
                }
            }
            catch ( Exception e ) {
                LogUtils.handleException( e,
                                          logger,
                                          "net.bioclipse.rdf.ui" );
            }
		}
    }

    /**
     * @param selection
     * @return
     */
    private IBioObject selectionToBioObject( ISelection selection ) {

        if (selection instanceof StructuredSelection) {
            Object firstElem = ((StructuredSelection)selection)
                               .getFirstElement();
            if (firstElem instanceof Resource) {
                Resource res = (Resource)firstElem;
                Model model = ((JenaModel)store).getModel();

                IExtensionPoint serviceObjectExtensionPoint
                    = Platform.getExtensionRegistry().getExtensionPoint(
                          "net.bioclipse.rdf.rdf2bioobjectfactory" );

                IExtension[] serviceObjectExtensions
                    = serviceObjectExtensionPoint.getExtensions();
                for ( IExtension extension : serviceObjectExtensions ) {
                    for ( IConfigurationElement element
                              : extension.getConfigurationElements() ) {
                        /*
                         * TODO: Egon give this string the right value.
                         */
                        String uri
                         = "http://www.bioclipse.net/structuredb/#Molecule";

                        if ( !element.getAttribute("uri").equals( uri ) ) {
                            continue;
                        }

                        Object service = null;
                        try {
                            service
                             = element.createExecutableExtension("instance");
                        }
                        catch (CoreException e) {
                            throw new IllegalStateException(
                                          "Failed to get a service: "
                                              + e.getMessage(),
                                          e );
                        }
                        if ( service != null &&
                            !(service instanceof IRDFToBioObjectFactory) ) {
                            throw new IllegalStateException(
                                 "Service object: " + service + " does not "
                                 + "implement IRDFToBioObjectFactory" );
                        }
                        IRDFToBioObjectFactory factory
                            = (IRDFToBioObjectFactory)service;
                        return factory.rdfToBioObject( model,
                                                       res );
                    }
                }
            }
        }
        return null;
    }

    private GraphViewer viewer;
    private RDFContentProvider contentProvider;
    private RDFLabelProvider labelProvider;

    private IRDFStore store = null;

    RDFManager rdf = new RDFManager();
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
        labelProvider = new RDFLabelProvider();
        viewer.setContentProvider(contentProvider);
        viewer.setLabelProvider(labelProvider);
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
				labelProvider.setModel(((JenaModel)store).getModel());
				IStringMatrix results = rdf.sparql(store,
					"SELECT ?s WHERE {?s a [] } LIMIT 1"
				);
				viewer.setInput(results.get(1,1));
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
				MessageDialog.openError(null, "Error", e.getMessage());
				e.printStackTrace();
			}
			setPartName(file.getName());
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
