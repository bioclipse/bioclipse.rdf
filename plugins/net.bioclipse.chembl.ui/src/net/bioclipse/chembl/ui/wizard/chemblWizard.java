package net.bioclipse.chembl.ui.wizard;


import net.bioclipse.chembl.ui.wizard.chemblWizardPage2;

import org.apache.log4j.Logger;
import org.eclipse.ui.IWorkbench;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;


public class chemblWizard extends Wizard {

	private static final Logger logger = Logger.getLogger(chemblWizard.class);


	private ISelection selection;
	//chemblWizardPageTabs firstpage;


	private chemblWizardPage firstpage;
	private chemblWizardPage2 secondpage;

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}


	public chemblWizard() {
		super();
		setWindowTitle("ChEMBL wizard");
	}

	public void addPages(){
//		firstpage = new chemblWizardPage("ChEMBL page");
//		addPage(firstpage);
		
		secondpage = new chemblWizardPage2("ChEMBL MoSS page");
		addPage(secondpage);
	}

}
