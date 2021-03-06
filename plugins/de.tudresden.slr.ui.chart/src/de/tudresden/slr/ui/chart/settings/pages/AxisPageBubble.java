package de.tudresden.slr.ui.chart.settings.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.ui.chart.settings.BubbleChartConfiguration;
import de.tudresden.slr.ui.chart.settings.parts.AxisSettings;

public class AxisPageBubble extends Composite implements Pages, SelectionListener{
	private Text xTitle;
	private Combo comboTitleSizeX;
	private Combo comboTitleSizeY;
	private Text yTitle;
	private Button btnAutoX, button0 , button30, button45 , button60, button90;
	
	private AxisSettings settings = BubbleChartConfiguration.get().getAxisSettings();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AxisPageBubble(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.VERTICAL));
		
		
		Group xAxisGroup = new Group(this, SWT.NONE);
		xAxisGroup.setText("X -Axis");
		xAxisGroup.setLayout(new GridLayout(2, false));
		
		Label lblTitle = new Label(xAxisGroup, SWT.NONE);
		GridData gd_lblTitle = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblTitle.widthHint = 150;
		lblTitle.setLayoutData(gd_lblTitle);
		lblTitle.setText("Title");
		
		xTitle = new Text(xAxisGroup, SWT.BORDER);
		xTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTitleSize = new Label(xAxisGroup, SWT.NONE);
		lblTitleSize.setText("Title Size");
		
		comboTitleSizeX = new Combo(xAxisGroup, SWT.READ_ONLY);
		comboTitleSizeX.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		comboTitleSizeX.add("12");
		comboTitleSizeX.add("14");
		comboTitleSizeX.add("16");
		comboTitleSizeX.add("18");
		comboTitleSizeX.add("20");
		comboTitleSizeX.add("22");
		comboTitleSizeX.add("24");
		comboTitleSizeX.add("26");
		comboTitleSizeX.add("28");
		comboTitleSizeX.add("36");
		comboTitleSizeX.add("48");
		comboTitleSizeX.add("72");
		comboTitleSizeX.select(0);
		
		Label lblRotation = new Label(xAxisGroup, SWT.NONE);
		lblRotation.setText("Rotation");
		
		Composite composite = new Composite(xAxisGroup, SWT.NONE);
		composite.setLayout(new GridLayout(6, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		btnAutoX = new Button(composite, SWT.RADIO);
		btnAutoX.setText("Auto");
		
		button0 = new Button(composite, SWT.RADIO);
		button0.setText("0\u00B0");
		
		button30 = new Button(composite, SWT.RADIO);
		button30.setText("30\u00B0");
		
		button45 = new Button(composite, SWT.RADIO);
		button45.setText("45\u00B0");
		
		button60 = new Button(composite, SWT.RADIO);
		button60.setText("60\u00B0");
		
		button90 = new Button(composite, SWT.RADIO);
		button90.setText("90\u00B0");
		
		Group yAxisGroup = new Group(this, SWT.NONE);
		yAxisGroup.setText("Y - Axis");
		yAxisGroup.setLayout(new GridLayout(2, false));
		
		Label lblTitle_1 = new Label(yAxisGroup, SWT.NONE);
		GridData gd_lblTitle_1 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblTitle_1.widthHint = 150;
		lblTitle_1.setLayoutData(gd_lblTitle_1);
		lblTitle_1.setText("Title");
		
		yTitle = new Text(yAxisGroup, SWT.BORDER);
		yTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTitleSize_1 = new Label(yAxisGroup, SWT.NONE);
		lblTitleSize_1.setText("Title Size");
		
		comboTitleSizeY = new Combo(yAxisGroup, SWT.READ_ONLY);
		comboTitleSizeY.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		comboTitleSizeY.add("12");
		comboTitleSizeY.add("14");
		comboTitleSizeY.add("16");
		comboTitleSizeY.add("18");
		comboTitleSizeY.add("20");
		comboTitleSizeY.add("22");
		comboTitleSizeY.add("24");
		comboTitleSizeY.add("26");
		comboTitleSizeY.add("28");
		comboTitleSizeY.add("36");
		comboTitleSizeY.add("48");
		comboTitleSizeY.add("72");
		comboTitleSizeY.select(0);
		
		loadSettings();
	}

	@Override
	protected void checkSubclass() {}

	@Override
	public void saveSettings() {
		settings.setxAxisTitle(getxTitle());
		settings.setyAxisTitle(getyTitle());
		settings.setxAxisTitleSize(getxSize());
		settings.setyAxisTitleSize(getySize());
		//settings.setxAxisGapWidth(((double) getScale()) / 100);
		if(btnAutoX.getSelection())
			settings.setxAxisAutoRotation(true);
		else {
			settings.setxAxisAutoRotation(false);
			settings.setxAxisRotation(getRotation());
		}
		
	}

	@Override
	public void loadSettings() {
		setxTitle(settings.getxAxisTitle());
		setyTitle(settings.getyAxisTitle());
		
		setxSize(settings.getxAxisTitleSize());
		setySize(settings.getyAxisTitleSize());
		
		//setScale(settings.getxAxisGapWidth());
		
		if(settings.isxAxisAutoRotation()){
			btnAutoX.setSelection(true);
		}
		else
			setRotation(settings.getxAxisRotation());
		
	}

	private String getxTitle() {return xTitle.getText();}
	private String getyTitle() {return yTitle.getText();}
	
	private void setxTitle(String xTitle) {this.xTitle.setText(xTitle);}
	private void setyTitle(String yTitle) {this.yTitle.setText(yTitle);}
	
	private int getxSize() {return Integer.valueOf(comboTitleSizeX.getItem(comboTitleSizeX.getSelectionIndex()));}
	private int getySize() {return Integer.valueOf(comboTitleSizeY.getItem(comboTitleSizeY.getSelectionIndex()));}
	
	private void setxSize(int size) {comboTitleSizeX.select(PageSupport.setFontSize(size));}
	private void setySize(int size) {comboTitleSizeY.select(PageSupport.setFontSize(size));}
	
	
	private int getRotation() {
		if(button0.getSelection())
			return 0;
		if(button30.getSelection())
			return 30;
		if(button45.getSelection())
			return 45;
		if(button60.getSelection())
			return 60;
		if(button90.getSelection())
			return 90;
		return 0;
	}
	
	private void setRotation(int rotation) {
		if(rotation == 0)
			button0.setSelection(true);
		if(rotation == 30)
			button30.setSelection(true);
		if(rotation == 45)
			button45.setSelection(true);
		if(rotation == 60)
			button60.setSelection(true);
		if(rotation == 90)
			button90.setSelection(true);
	}
	

	@Override
	public void widgetSelected(SelectionEvent e) {
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}	
}
