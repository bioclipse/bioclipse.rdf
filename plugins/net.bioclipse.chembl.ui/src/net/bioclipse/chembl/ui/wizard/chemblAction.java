package net.bioclipse.chembl.ui.wizard;


import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ChemblAction implements IObjectActionDelegate {

	private IWorkbenchPart part;
    private ISelection selection;
    private IResource parent;
    
    public ChemblAction() {
        super();
    }
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		part = targetPart;
		
	}

	@Override
	public void run(IAction action) {
		ChemblWizard wizard = new ChemblWizard();
		 WizardDialog dialog = new WizardDialog(part.getSite().getShell(),
	                wizard);

	        dialog.create();
	        dialog.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

}
