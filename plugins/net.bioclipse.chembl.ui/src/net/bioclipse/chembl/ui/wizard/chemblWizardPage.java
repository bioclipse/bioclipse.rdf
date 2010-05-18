package net.bioclipse.chembl.ui.wizard;

import java.util.List;

import net.bioclipse.chembl.Activator;
import net.bioclipse.chembl.business.IChEMBLManager;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.moss.business.IMossManager;
import net.bioclipse.rdf.model.IStringMatrix;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class ChemblWizardPage extends WizardPage {

	private IChEMBLManager chembl;
	Label title, score, type, label,labelchebi, target, key;
	GridData gridData;
	private Table table;
	private TableColumn column1, column2, column3, column4, column5,column6, column7, column8,column9;
	TableColumn[] columnCollection = {column1, column2, column3, column4, column5, column6, column7, column8};
	private Button showTable, targetprot, compounds;
	private List<String> list;
	
	protected ChemblWizardPage(String pageName) {
		super(pageName);
		chembl = Activator.getDefault().getJavaChEMBLManager();
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		setControl(container);

		label = new Label(container, SWT.NONE);
		gridData = new GridData(GridData.FILL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		label.setLayoutData(gridData);
		label.setText("Search");

		compounds = new Button(container, SWT.CHECK);
		compounds.setText("Compound");
		compounds.setSelection(true);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		compounds.setLayoutData(gridData);
		compounds.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean selected = compounds.getSelection();
				if(selected == true){
					targetprot.setSelection(false);
				}
			}
		});
		targetprot = new Button(container, SWT.CHECK);
		targetprot.setText("Target");
		targetprot.setSelection(false);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		targetprot.setLayoutData(gridData);
		targetprot.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean selected = targetprot.getSelection();
				if(selected == true){
					compounds.setSelection(false);
				}
			}
		});
		Text compound = new Text(container, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		compound.setLayoutData(gridData);
		compound.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				if(targetprot.getSelection() == true && compounds.getSelection() == false){
					update2((Text)e.getSource());
				}
				else if(compounds.getSelection() == true && targetprot.getSelection() == false){
					update((Text)e.getSource());
				}
				else{
					showMessage("error","Selection Error","Both boxes can't be checked");
				}
			}
		});

		label = new Label(container, SWT.NONE);
		gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalSpan = 1;
		label.setLayoutData(gridData);
		label.setText("Title: ");
		title = new Label(container, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		title.setLayoutData(gridData);

		label = new Label(container, SWT.NONE);
		gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalSpan = 1;
		label.setLayoutData(gridData);
		label.setText("Type: ");
		type = new Label(container, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		type.setLayoutData(gridData);

		label = new Label(container, SWT.NONE);
		gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalSpan = 1;
		label.setLayoutData(gridData);
		label.setText("Score: ");
		score = new Label(container, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		score.setLayoutData(gridData);

		label = new Label(container, SWT.NONE);
		gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalSpan = 1;
		label.setLayoutData(gridData);
		label.setText("Target: ");
		target = new Label(container, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		target.setLayoutData(gridData);

		labelchebi = new Label(container, SWT.NONE);
		gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalSpan = 1;
		labelchebi.setLayoutData(gridData);
		labelchebi.setText("Key:      ");
		key = new Label(container, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		key.setLayoutData(gridData);

		showTable = new Button(container, SWT.CHECK);
		showTable.setText("Show table");
		showTable.setSelection(false);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		showTable.setLayoutData(gridData);
		showTable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean selected = showTable.getSelection();

				if (selected == true) {
					table.setVisible(true);
				} else {
					table.setVisible(false);
				}
			}
		});
		table = new Table(container, SWT.CHECK );
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setVisible(false);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.widthHint = 600;
		gridData.heightHint = 600;
		gridData.horizontalSpan = 2;
		table.setLayoutData(gridData);

		column1 = new TableColumn(table, SWT.CHECK);
		column1.setWidth(100);
		column2 = new TableColumn(table, SWT.NONE);
		column2.setWidth(100);
		column3 = new TableColumn(table, SWT.NONE);
		column3.setWidth(100);
		column4 = new TableColumn(table, SWT.NONE);
		column4.setWidth(100);
		column5 = new TableColumn(table, SWT.NONE);
		column5.setWidth(100);
		column6 = new TableColumn(table, SWT.NONE);
		column6.setWidth(100);
		column7 = new TableColumn(table, SWT.NONE);
		column7.setWidth(100);
		column8 = new TableColumn(table, SWT.NONE);
		column8.setWidth(100);

	}

	public boolean isInteger(String check){
		try{
			Integer.parseInt(check);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public void addToTable(IStringMatrix matrix){
		for(int r = 1; r < matrix.getRowCount()+1; r++){	
			TableItem item = new TableItem(table, SWT.NULL);
			for(int i = 0; i < matrix.getColumnCount(); i++){	
				item.setText(i, matrix.get(r, matrix.getColumnName(i+1)));
			}
		}
	}
	/**
	 * Updates the wizard when something happens
	 * */
	private void update(Text field){
		String text = field.getText();	
		IStringMatrix matrix =null;
		table.clearAll();
		table.removeAll();
		title.setText("");
		type.setText("");
		score.setText("");
		target.setText("");
		key.setText("");
		labelchebi.setText("Key:");
		
//		for(int i =0; i<columnCollection.length; i++)
//			columnCollection[i].setText(""); 
		
		
	column1.setText("");column2.setText("");column3.setText("");column4.setText("");column5.setText("");
	column6.setText("");column7.setText("");column8.setText("");	
	int id = 0;

		try{
			if(isInteger(text) == true){
				Integer cid = Integer.parseInt(text);
				matrix = chembl.getCompoundInfo(cid);
				id = 1;
			}
			else {
				if(text.endsWith("#")){
					matrix = chembl.getCompoundInfoWithSmiles(text.substring(0,text.length()-1));
					System.out.print(matrix.get(1,"chebi") +" "+ matrix.getRowCount());
					id=3;
				}else if(text.length()>0){
					matrix = chembl.getCompoundInfoWithKeyword(text);
					id =2;
				}
			}

			if (matrix != null && matrix.getRowCount() > 0) {
				if(id==1){
					title.setText(matrix.get(1, "title"));
					type.setText(matrix.get(1, "type"));
					score.setText(matrix.get(1, "score"));
					target.setText(matrix.get(1, "target"));
					column1.setText(matrix.getColumnName(1));
					column2.setText(matrix.getColumnName(2));
					column3.setText(matrix.getColumnName(3));
					column4.setText(matrix.getColumnName(4));
					column5.setText(matrix.getColumnName(5));
					column6.setText(matrix.getColumnName(6));

					addToTable(matrix);

					if(!table.isVisible() && matrix.getRowCount() > 1){
						table.setVisible(true);
						showTable.setSelection(true);
					}
				}
				else if(id == 2){
					target.setText(matrix.get(1, "target"));
					key.setText(matrix.get(1,"description"));
					column1.setText(matrix.getColumnName(1));
					column2.setText(matrix.getColumnName(2));
					addToTable(matrix);

					if(matrix.getRowCount() > 2 && !table.isVisible()){
						table.setVisible(true);
						showTable.setSelection(true);
					}
				}
				else if(id == 3){
					labelchebi.setText("chebi id:");
					if(matrix.getColumnCount() >1){
						title.setText(matrix.get(1,"title"));
					}
					key.setText(matrix.get(1,"chebi"));	
				}
			}else {
				target.setText("");
				key.setText("");
				title.setText("");
				type.setText("");
				score.setText("");
				setErrorMessage("Check .");
				setPageComplete( false );
				getWizard().getContainer().updateButtons();
			}
			setErrorMessage(null);
		} catch (BioclipseException e) {
			setErrorMessage("Could not update information.");
		}

		setPageComplete( true );
		getWizard().getContainer().updateButtons();
	}

	private void update2(Text field){
		String text = field.getText();
		IStringMatrix matrix = null;
		table.clearAll();
		table.removeAll();
		title.setText("");
		type.setText("");
		score.setText("");
		target.setText("");
		key.setText("");
		labelchebi.setText("Key:");
//		for(int i =0; i<columnCollection.length; i++){
//			columnCollection[i].setText(""); }
		column1.setText("");column2.setText("");column3.setText("");column4.setText("");column5.setText("");
		column6.setText("");column7.setText("");column8.setText("");	
		int id = 0;

		try{
			if(isInteger(text) == true){
				Integer cid = Integer.parseInt(text);
				matrix = chembl.getProteinData(cid);
				id = 1;
			}
			else if(text.length()>0){
				matrix = chembl.getTargetIDWithKeyword(text);
				id=2;
			}

			if (matrix != null && matrix.getRowCount() > 0) {
				
				if(id==1){			
						column1.setText(matrix.getColumnName(1));
						column2.setText(matrix.getColumnName(2));
						column3.setText(matrix.getColumnName(3));
						
						if(matrix.getColumnCount() == 4){
						column4.setText(matrix.getColumnName(4));
						}
						if(matrix.getColumnCount() == 5){
							column4.setText(matrix.getColumnName(4));
							column5.setText(matrix.getColumnName(5));
							}
						if(matrix.getColumnCount() == 6){
							column4.setText(matrix.getColumnName(4));
							column5.setText(matrix.getColumnName(5));
							column6.setText(matrix.getColumnName(6));
							}
						if(matrix.getColumnCount() == 7){
							column4.setText(matrix.getColumnName(4));
							column5.setText(matrix.getColumnName(5));
							column6.setText(matrix.getColumnName(6));
							column7.setText(matrix.getColumnName(7));
							}
						if(matrix.getColumnCount() == 8){
							column4.setText(matrix.getColumnName(4));
							column5.setText(matrix.getColumnName(5));
							column6.setText(matrix.getColumnName(6));
							column7.setText(matrix.getColumnName(7));
							column8.setText(matrix.getColumnName(8));
							}
						if(matrix.getColumnCount() == 9){
							column4.setText(matrix.getColumnName(4));
							column5.setText(matrix.getColumnName(5));
							column6.setText(matrix.getColumnName(6));
							column7.setText(matrix.getColumnName(7));
							column8.setText(matrix.getColumnName(8));
							//column9.setText(matrix.getColumnName(9));
							}
			
						addToTable(matrix);
						
						if(!table.isVisible()){
							table.setVisible(true);
							showTable.setSelection(true);
						}
				}			
				if(id==2){
					target.setText(matrix.get(1, "target"));
					key.setText(matrix.get(1, "key"));
					column1.setText(matrix.getColumnName(1));
					column2.setText(matrix.getColumnName(2));

					addToTable(matrix);
					
					if(!table.isVisible()){
						table.setVisible(true);
						showTable.setSelection(true);
					}
					
				}
			}else {
				target.setText("");
				key.setText("");
				title.setText("");
				type.setText("");
				score.setText("");
				setErrorMessage("Check .");
				setPageComplete( false );
				getWizard().getContainer().updateButtons();
			}
			setErrorMessage(null);
		} catch (BioclipseException e) {
			setErrorMessage("Could not update information.");
		}

		setPageComplete( true );
		getWizard().getContainer().updateButtons();
	}

	public void showMessage(String id, String title, String message) {
		if(id.equals("error")){
			MessageDialog.openError(getShell(), title, message);
		}else
		  MessageDialog.openQuestion(getShell(), title, message);
	}
}

