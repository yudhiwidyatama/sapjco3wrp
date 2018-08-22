/* Copyright (c) 2010 Aschauer EDV GmbH.  All rights reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This software was developed by Aschauer EDV GmbH and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 * 
 * Please contact Aschauer EDV GmbH www.aschauer-edv.at if you need additional
 * information or have any questions.
 * 
 * @author  Robert Wintner robert.wintner@aschauer-edv.at
 * @since   PDI 4.0
 */

package org.pentaho.di.trans.steps.sapinput.sap.impl;

import java.util.Collection;
import java.util.Vector;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.steps.sapinput.sap.SAPConnection;
import org.pentaho.di.trans.steps.sapinput.sap.SAPConnectionFactory;
import org.pentaho.di.trans.steps.sapinput.sap.SAPConnectionParams;
import org.pentaho.di.trans.steps.sapinput.sap.SAPException;
import org.pentaho.di.trans.steps.sapinput.sap.SAPField;
import org.pentaho.di.trans.steps.sapinput.sap.SAPFunction;
import org.pentaho.di.trans.steps.sapinput.sap.SAPFunctionSignature;
import org.pentaho.di.trans.steps.sapinput.sap.SAPResultSet;
import org.pentaho.di.trans.steps.sapinput.sap.SAPRow;

public class SAPConnectionImplTest {

	/**
	 * How to use a SAPConnection
	 * 
	 * @throws KettleException
	 */
	public static void main(String[] args) throws SAPException {

		// how to obtain a connection
		// SAPConnectionImpl sc = new SAPConnectionImpl();
		SAPConnection sc = SAPConnectionFactory.create();

		// how to open a connection
		// @Matt:
		// please show us how to retrieve the connection params from the
		// pentaho environment

		SAPConnectionParams cp = new SAPConnectionParams("TestConnection",
				"192.168.9.50", "00", "100", "EXT_KUBICKAM", "asdfasdf", "DE");
		sc.open(cp);

		// how to query all functions
		System.out.println("how to query functions: BAPI_RES");
		Collection<SAPFunction> csf1 = sc.getFunctions("BAPI_RES");
		for (SAPFunction sapFunction : csf1) {
			System.out.println(sapFunction);
		}
		System.out.println();

		// how to query functions
		System.out.println("how to query functions: RFC_GET");
		Collection<SAPFunction> csf2 = sc.getFunctions("RFC_GET");
		for (SAPFunction sapFunction : csf2) {
			System.out.println(sapFunction);
		}
		System.out.println();

		// how to get a function
		System.out.println("how to get a function: RFC_FUNCTION_SEARCH");
		SAPFunction sf = sc.getFunction("RFC_FUNCTION_SEARCH");
		System.out.println(sf);
		System.out.println();

		// how to get function signature
		System.out
				.println("how to get function signature: RFC_FUNCTION_SEARCH");
		SAPFunctionSignature sfs = sc.getFunctionSignature(sf);
		System.out.println("input:");
		for (SAPField field : sfs.getInput()) {
			System.out.println(field);
		}
		System.out.println("output:");
		for (SAPField field : sfs.getOutput()) {
			System.out.println(field);
		}
		System.out.println();

		// how to get function signature
		System.out
				.println("how to get function signature: BAPI_BILLINGDOC_GETLIST");
		SAPFunction sf2 = sc.getFunction("BAPI_BILLINGDOC_GETLIST");
		SAPFunctionSignature sfs2 = sc.getFunctionSignature(sf2);
		System.out.println("input:");
		for (SAPField field : sfs2.getInput()) {
			System.out.println(field);
		}
		System.out.println("output:");
		for (SAPField field : sfs2.getOutput()) {
			System.out.println(field);
		}
		System.out.println();

		// how to get function signature
		System.out
				.println("how to get function signature: RFC_GET_TABLE_ENTRIES");
		SAPFunction sf7 = sc.getFunction("RFC_GET_TABLE_ENTRIES");
		SAPFunctionSignature sfs7 = sc.getFunctionSignature(sf7);
		System.out.println("input:");
		for (SAPField field : sfs7.getInput()) {
			System.out.println(field);
		}
		System.out.println("output:");
		for (SAPField field : sfs7.getOutput()) {
			System.out.println(field);
		}
		System.out.println();

		// how to get function signature
		System.out
				.println("how to get function signature: BAPI_ACC_CO_DOCUMENT_FIND");
		SAPFunction sf73 = sc.getFunction("BAPI_ACC_CO_DOCUMENT_FIND");
		SAPFunctionSignature sfs73 = sc.getFunctionSignature(sf73);
		System.out.println("input:");
		for (SAPField field : sfs73.getInput()) {
			System.out.println(field);
		}
		System.out.println("output:");
		for (SAPField field : sfs73.getOutput()) {
			System.out.println(field);
		}

		// how to get function signature
		System.out
				.println("how to get function signature: RFC_READ_TABLE");
		SAPFunction sf74 = sc.getFunction("RFC_READ_TABLE");
		SAPFunctionSignature sfs74 = sc.getFunctionSignature(sf74);
		System.out.println("input:");
		for (SAPField field : sfs74.getInput()) {
			System.out.println(field);
		}
		System.out.println("output:");
		for (SAPField field : sfs74.getOutput()) {
			System.out.println(field);
		}
		System.out.println();
		// how to get function signature
		System.out
				.println("how to get function signature: C100_RFC_ENTRY_INQUIERY");
		SAPFunction sf75 = sc.getFunction("C100_RFC_ENTRY_INQUIERY");
		SAPFunctionSignature sfs75 = sc.getFunctionSignature(sf75);
		System.out.println("input:");
		for (SAPField field : sfs75.getInput()) {
			System.out.println(field);
		}
		System.out.println("output:");
		for (SAPField field : sfs75.getOutput()) {
			System.out.println(field);
		}
		System.out.println();
		
		// how to execute a function
		System.out.println("how to execute a function: RFC_FUNCTION_SEARCH");
		Collection<SAPField> input = new Vector<SAPField>();
		input.add(new SAPField("FUNCNAME", "", "input_single", "BAPI_RES*"));
		Collection<SAPField> output = new Vector<SAPField>();
		output.add(new SAPField("FUNCNAME", "FUNCTIONS", "output_table"));
		output.add(new SAPField("STEXT", "FUNCTIONS", "output_table"));
		SAPResultSet sfr = sc.executeFunctionUncursored(sf, input, output);
		for (SAPRow row : sfr.getRows()) {
			System.out.println(row);
		}
		System.out.println(sfr.getRows().size());
		if (16 != sfr.getRows().size())
			System.out.println("ERROR: Size must be 16.");
		System.out.println();

		// how to execute a function
		System.out
				.println("how to execute a function: BAPI_BILLINGDOC_GETLIST");
		SAPFunction sf4 = sc.getFunction("BAPI_BILLINGDOC_GETLIST");
		Collection<SAPField> input4 = new Vector<SAPField>();
		input4.add(new SAPField("SIGN", "REFDOCRANGE", "input_structure", "I"));
		input4.add(new SAPField("OPTION", "REFDOCRANGE", "input_structure",
				"BT"));
		input4.add(new SAPField("REF_DOC_LOW", "REFDOCRANGE",
				"input_structure", "0010000002"));
		input4.add(new SAPField("REF_DOC_HIGH", "REFDOCRANGE",
				"input_structure", "0080000003"));
		Collection<SAPField> output4 = new Vector<SAPField>();
		output4.add(new SAPField("BILLINGDOC", "BILLINGDOCUMENTDETAIL",
				"output_table"));
		output4.add(new SAPField("BILL_TYPE", "BILLINGDOCUMENTDETAIL",
				"output_table"));
		output4.add(new SAPField("NET_VALUE", "BILLINGDOCUMENTDETAIL",
				"output_table"));
		output4.add(new SAPField("TAX_VALUE", "BILLINGDOCUMENTDETAIL",
				"output_table"));
		SAPResultSet sfr4 = sc.executeFunctionUncursored(sf4, input4, output4);
		for (SAPRow row : sfr4.getRows()) {
			System.out.println(row);
		}
		System.out.println(sfr4.getRows().size());
		if (66 != sfr4.getRows().size())
			System.out.println("ERROR: Size must be 66.");
		System.out.println();

		// how to execute a function
		System.out
				.println("how to execute a function: BAPI_BILLINGDOC_GETDETAIL");
		SAPFunction sf3 = sc.getFunction("BAPI_BILLINGDOC_GETDETAIL");
		Collection<SAPField> input3 = new Vector<SAPField>();
		input3.add(new SAPField("BILLINGDOCUMENT", "", "input_single",
				"0090000025"));
		Collection<SAPField> output3 = new Vector<SAPField>();
		output3.add(new SAPField("BILLINGDOC", "BILLINGDOCUMENTDETAIL",
				"output_structure"));
		output3.add(new SAPField("BILL_TYPE", "BILLINGDOCUMENTDETAIL",
				"output_structure"));
		output3.add(new SAPField("NET_VALUE", "BILLINGDOCUMENTDETAIL",
				"output_structure"));
		output3.add(new SAPField("TAX_VALUE", "BILLINGDOCUMENTDETAIL",
				"output_structure"));
		SAPResultSet sfr3 = sc.executeFunctionUncursored(sf3, input3, output3);
		for (SAPRow row : sfr3.getRows()) {
			System.out.println(row);
		}
		System.out.println(sfr3.getRows().size());
		if (1 != sfr3.getRows().size())
			System.out.println("ERROR: Size must be 1.");
		System.out.println();
	}

}
