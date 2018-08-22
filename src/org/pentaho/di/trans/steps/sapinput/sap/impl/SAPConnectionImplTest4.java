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
import org.pentaho.di.trans.steps.sapinput.sap.SAPConnectionParams;
import org.pentaho.di.trans.steps.sapinput.sap.SAPException;
import org.pentaho.di.trans.steps.sapinput.sap.SAPField;
import org.pentaho.di.trans.steps.sapinput.sap.SAPFunction;
import org.pentaho.di.trans.steps.sapinput.sap.SAPResultSet;
import org.pentaho.di.trans.steps.sapinput.sap.SAPRow;

public class SAPConnectionImplTest4 {

	/**
	 * How to use a SAPConnection
	 * 
	 * @throws KettleException
	 */
	public static void main(String[] args) throws SAPException {

		// how to obtain a connection
		SAPConnectionImpl sc = new SAPConnectionImpl();
		// SAPConnection sc = SAPConnectionFactory.create();

		// how to open a connection
		// @Matt:
		// please show us how to retrieve the connection params from the
		// pentaho environment

		SAPConnectionParams cp = new SAPConnectionParams("TestConnection",
				"192.168.9.50", "00", "100", "EXT_KUBICKAM", "asdfasdf", "DE");
		sc.open(cp);

		// how to execute a function
		System.out
				.println("how to execute a function: BAPI_ACC_CO_DOCUMENT_FIND");
		SAPFunction sf3 = sc.getFunction("BAPI_ACC_CO_DOCUMENT_FIND");
		Collection<SAPField> input3 = new Vector<SAPField>();
		input3.add(new SAPField("RETURN_ITEMS", "", "input_single", "100"));
		input3.add(new SAPField("CO_AREA", "DOCUMENT", "input_structure", "A"));
		input3.add(new SAPField("PERIOD", "DOCUMENT", "input_structure", "10"));
		input3.add(new SAPField("FISC_YEAR", "DOCUMENT", "input_structure",
				"2010"));
		Collection<SAPField> output3 = new Vector<SAPField>();
		output3.add(new SAPField("CO_AREA", "DOC_HEADERS", "output_table"));
		output3.add(new SAPField("DOC_NO", "DOC_HEADERS", "output_table"));
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
