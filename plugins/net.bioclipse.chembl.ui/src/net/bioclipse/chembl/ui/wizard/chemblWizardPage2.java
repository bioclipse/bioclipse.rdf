package net.bioclipse.chembl.ui.wizard;

import java.util.List;

import net.bioclipse.chembl.Activator;
import net.bioclipse.chembl.business.IChEMBLManager;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.rdf.model.IStringMatrix;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class chemblWizardPage2 extends WizardPage {

	private IChEMBLManager chembl;
	private Label label, title, type, target;
	private GridData gridData;
	private Combo cbox, cboxAct;
	private Table table;
	private TableColumn column1;
	private Spinner spinn;
	private Button button;

	public chemblWizardPage2(String pagename){
		super(pagename);
		chembl = Activator.getDefault().getJavaChEMBLManager();
	}	
	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);
		setControl(container);

		label = new Label(container, SWT.NONE);
		gridData = new GridData(GridData.FILL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		label.setLayoutData(gridData);
		label.setText("Choose Protein Familes");

		label = new Label(container, SWT.NONE);
		gridData = new GridData(GridData.FILL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		label.setLayoutData(gridData);
		label.setText("Available activities for chosen protein family");

		label = new Label(container, SWT.NONE);
		gridData = new GridData(GridData.FILL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		label.setLayoutData(gridData);
		label.setText("Limit");

		cbox = new Combo(container,SWT.NONE);
		gridData = new GridData(GridData.BEGINNING);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		cbox.setLayoutData(gridData);
		String[] items = { "TK", "TKL", "STE", "CK1","CMCG","AGC", "CAMK" };
		cbox.setItems(items);

		cboxAct = new Combo(container,SWT.NULL);
		gridData = new GridData(GridData.FILL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1	;
		cboxAct.setLayoutData(gridData);
		String[] item = { "No available activity" };
		cboxAct.setItems(item);
		cboxAct.setEnabled(false);

		cbox.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String selected = cbox.getItem(cbox.getSelectionIndex());

				try {
					table.clearAll();
					table.removeAll();
					cboxAct.removeAll();
					List<String> list = chembl.MossAvailableActivities(selected);
					String[] item = new String[list.size()];
					for(int i=0;i<list.size(); i++){
						item[i]= list.get(i);
					}
					cboxAct.setEnabled(true);

					//String[] item = {list.get(0)};
					cboxAct.setItems(item);

				} catch (BioclipseException e1) {
					e1.printStackTrace();
				}
			}
		});


		spinn = new Spinner(container, SWT.BORDER);
		gridData = new GridData();
		spinn.setLayoutData(gridData);
		spinn.setSelection(50);

		table = new Table(container, SWT.CHECK );
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setVisible(false);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.widthHint = 500;
		gridData.heightHint = 500;
		gridData.horizontalSpan = 2;
		table.setLayoutData(gridData);
		column1 = new TableColumn(table, SWT.CHECK);
		column1.setWidth(500);

		button = new Button(container, SWT.PUSH);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		button.setLayoutData(gridData);
		button.setText("Restore");

		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				table.clearAll();
				table.removeAll();
				cbox.clearSelection();
				cboxAct.clearSelection();
				cboxAct.removeAll();
				spinn.setSelection(50);

			}
		});


		cboxAct.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				String selected = cboxAct.getItem(cboxAct.getSelectionIndex());
				try{
					table.clearAll();
					table.removeAll();
					IStringMatrix matrix = chembl.MossProtFamilyCompounds(cbox.getItem(cbox.getSelectionIndex()), selected, spinn.getSelection());
					table.setVisible(true);
					addToTable(matrix);
				}catch(BioclipseException e1){
					e1.printStackTrace();
				}
			} 
		});

		spinn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selected = spinn.getSelection();
				try{
					table.clearAll();
					table.removeAll();
					IStringMatrix matrix = chembl.MossProtFamilyCompounds(cbox.getItem(cbox.getSelectionIndex()),cboxAct.getItem(cboxAct.getSelectionIndex()), selected);
					table.setVisible(true);
					addToTable(matrix);
				}catch(BioclipseException e1){
					e1.printStackTrace();
				}
			}});

	}

	public void addToTable(IStringMatrix matrix){
		for(int r = 1; r < matrix.getRowCount()+1; r++){	
			TableItem item = new TableItem(table, SWT.NULL);
			for(int i = 0; i < matrix.getColumnCount(); i++){	
				item.setText(i, matrix.get(r, matrix.getColumnName(i+1)));
			}
		}
	}
}
