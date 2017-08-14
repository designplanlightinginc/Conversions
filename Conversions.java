//Programmed by Aaron Weiss 11/10/2015

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import java.awt.event.*;
import java.text.DecimalFormat;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.*;


public class Conversions {
	private final int WINDOW_HEIGHT = 400;
	private final int WINDOW_WIDTH = 500;
	private final double LENGTH_FACTOR = 3.2808399;
	private boolean unitChecked = true;
	private String previousUnit;

	public Conversions() {

		JFrame window = new JFrame("Conversions");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Populate JFrame
		JTextField inputField = new JTextField(20);
		inputField.setText("0");
		inputField.setToolTipText("Input");
		JTextField outputField = new JTextField(20);
		outputField.setText("0");
		outputField.setToolTipText("Output");

		JComboBox inputList, outputList, roundingList;

		String[] unitChoice = {"Length","Weight","Light","Length Sq."};
		String[] unitStringLength = {"mm","cm","m","in","ft","yd"};
		String[] unitStringLength2 = {"m2","ft2"};
		String[] unitStringWeight = {"g","kg","oz","lbs"};
		String[] unitStringLight = {"lux","fc"};
		String[] roundingString = {"Thousandths","Hundredths","Tenths","Whole"};


		JButton switchButton = new JButton("Switch");


		JPanel lengthPanel = new JPanel();

		JComboBox unitList = new JComboBox(unitChoice);
		unitList.setSelectedIndex(0);

		inputList = new JComboBox(unitStringLength);
		inputList.setSelectedIndex(0);

		roundingList = new JComboBox(roundingString);
		roundingList.setSelectedIndex(1);

		outputList = new JComboBox(unitStringLength);
		outputList.setSelectedIndex(3);

		previousUnit = "Length";

		//Listen for unit changes
		unitList.addPropertyChangeListener(new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent e) {
			String selection = (String)unitList.getSelectedItem();
			if(!previousUnit.equals(selection)) {
				inputList.removeAllItems();
				outputList.removeAllItems();
				switch (selection) {
					case "Length":
						for(String unit : unitStringLength) {
							inputList.addItem(unit);
							outputList.addItem(unit);
						}
						inputList.setSelectedIndex(0);
						outputList.setSelectedIndex(3);
						lengthPanel.setBorder(BorderFactory.createTitledBorder("Length Conversions"));
						previousUnit = "Length";
						break;
					case "Weight":
						for(String unit : unitStringWeight) {
							inputList.addItem(unit);
							outputList.addItem(unit);
						}
						inputList.setSelectedIndex(1);
						outputList.setSelectedIndex(3);
						lengthPanel.setBorder(BorderFactory.createTitledBorder("Weight Conversions"));
						previousUnit = "Weight";
						break;
					case "Light":
						for(String unit : unitStringLight) {
							inputList.addItem(unit);
							outputList.addItem(unit);
						}
						inputList.setSelectedIndex(0);
						outputList.setSelectedIndex(1);
						lengthPanel.setBorder(BorderFactory.createTitledBorder("Light Conversions"));
						previousUnit = "Light";
						break;
					case "Length Sq.":
						for(String unit : unitStringLength2) {
							inputList.addItem(unit);
							outputList.addItem(unit);
						}
						inputList.setSelectedIndex(0);
						outputList.setSelectedIndex(1);
						lengthPanel.setBorder(BorderFactory.createTitledBorder("Length Sq. Conversions"));
						previousUnit = "Length Sq.";
						break;
					default:
						break;
				}
			}
			}
		});


		JButton updateButton = new JButton("Update & Send to Clipboard");
		JCheckBox unitBox = new JCheckBox("Units into Clipboard", true);

		unitBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(unitChecked)
					unitChecked = false;
				else
					unitChecked = true;
			}
		});



		JRadioButton lengthLabel = new JRadioButton("Focus: ",true);
		JLabel unitLabel = new JLabel("Select Unit:");
		JLabel spaceLabel = new JLabel(" ");

		lengthPanel.setLayout(new GridLayout(4,3));
		lengthPanel.add(unitLabel);
		lengthPanel.add(unitList);
		lengthPanel.add(spaceLabel);
		lengthPanel.add(inputField);
		lengthPanel.add(switchButton);
		lengthPanel.add(outputField);
		lengthPanel.add(inputList);
		lengthPanel.add(roundingList);
		lengthPanel.add(outputList);
		lengthPanel.add(lengthLabel);
		lengthPanel.add(updateButton);
		lengthPanel.add(unitBox);
		lengthPanel.setBorder(BorderFactory.createTitledBorder("Length Conversions"));


		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double inputVal = Double.parseDouble(inputField.getText());

				//Convert all inputs to base unit: meter/kg/lux/m2.
				String inputUnit = (String)inputList.getSelectedItem();
				switch (inputUnit) {
					case "mm":
						inputVal = inputVal/1000;
						break;
					case "cm":
						inputVal = inputVal/100;
						break;
					case "m":
						break;
					case "in":
						inputVal = (inputVal/12)/LENGTH_FACTOR;
						break;
					case "ft":
						inputVal = (inputVal)/LENGTH_FACTOR;
						break;
					case "yd":
						inputVal = (inputVal*3)/LENGTH_FACTOR;
						break;
					case "g":
						inputVal = inputVal/1000;
						break;
					case "kg":
						break;
					case "oz":
						inputVal = (inputVal/16)/2.204622;
						break;
					case "lbs":
						inputVal = inputVal/2.204622;
						break;
					case "lux":
						break;
					case "fc":
						inputVal = inputVal*10.76391;
						break;
					case "m2":
						break;
					case "ft2":
						inputVal = (inputVal/LENGTH_FACTOR)/LENGTH_FACTOR;
						break;
					default:
						break;
				}

				//Converts from base unit back to selected output.
				double outputVal = inputVal;
				String unit = "";
				String outputUnit = (String)outputList.getSelectedItem();
				switch (outputUnit) {
					case "in":
						outputVal = outputVal*LENGTH_FACTOR*12;
						unit = "\"";
						break;
					case "ft":
						outputVal = outputVal*LENGTH_FACTOR;
						unit = "\'";
						break;
					case "yd":
						outputVal = (outputVal*LENGTH_FACTOR)/3;
						unit = "yds.";
						break;
					case "mm":
						outputVal = outputVal*1000;
						unit = "mm";
						break;
					case "cm":
						outputVal = outputVal*100;
						unit = "cm";
						break;
					case "m":
						outputVal = outputVal;
						unit = "m";
						break;
					case "g":
						outputVal = outputVal*1000;
						unit = "g";
						break;
					case "kg":
						outputVal = outputVal;
						unit = "kg";
						break;
					case "oz":
						outputVal = outputVal*2.204622*16;
						unit = "oz";
						break;
					case "lbs":
						outputVal = outputVal*2.204622;
						unit = " lbs";
						break;
					case "lux":
						outputVal = outputVal;
						break;
					case "fc":
						outputVal = outputVal/10.76391;
						break;
					case "m2":
						unit = "m2";
						break;
					case "ft2":
						outputVal = (outputVal*LENGTH_FACTOR)*LENGTH_FACTOR;
						unit = "ft2";
						break;
					default:
						break;
				}
				double result = outputVal;

				//Do rounding based on choice
				String roundingUnit = (String)roundingList.getSelectedItem();
				switch (roundingUnit) {
					case "Whole":
						DecimalFormat df = new DecimalFormat("##0");
						outputField.setText(df.format(result));

						break;
					case "Tenths":
						DecimalFormat df1 = new DecimalFormat("##0.0");
						outputField.setText(df1.format(result));
						break;
					case "Hundredths":
						DecimalFormat df2 = new DecimalFormat("##0.00");
						outputField.setText(df2.format(result));

						break;
					case "Thousandths":
						DecimalFormat df3 = new DecimalFormat("##0.000");
						outputField.setText(df3.format(result));

						break;
					default:
						break;
				}

				//Include units?
				if(unitChecked) {
					StringSelection stringSelection = new StringSelection(outputField.getText()+unit);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, stringSelection);
				}
				else {
					StringSelection stringSelection = new StringSelection(outputField.getText());
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, stringSelection);
				}
				inputField.selectAll();
			}
		});

		switchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int temp = inputList.getSelectedIndex();
				inputList.setSelectedIndex(outputList.getSelectedIndex());
				outputList.setSelectedIndex(temp);
				String temp2 = inputField.getText();
				inputField.setText(outputField.getText());
				outputField.setText(temp2);
			}
		});

		window.getRootPane().setDefaultButton(updateButton);

		ButtonGroup focusGroup = new ButtonGroup();
		focusGroup.add(lengthLabel);

		window.setLayout(new BorderLayout());
		window.add(lengthPanel, BorderLayout.PAGE_START);
		window.pack();
		window.setVisible(true);
	}


	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(
			UIManager.getSystemLookAndFeelClassName());
		}
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
    	}
		new Conversions();
	}
}