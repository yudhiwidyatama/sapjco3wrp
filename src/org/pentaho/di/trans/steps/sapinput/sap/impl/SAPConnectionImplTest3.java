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

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.steps.sapinput.sap.SAPConnectionParams;
import org.pentaho.di.trans.steps.sapinput.sap.SAPException;
import org.pentaho.di.trans.steps.sapinput.sap.SAPField;
import org.pentaho.di.trans.steps.sapinput.sap.SAPFunction;
import org.pentaho.di.trans.steps.sapinput.sap.SAPFunctionSignature;
import org.pentaho.di.trans.steps.sapinput.sap.SAPResultSet;
import org.pentaho.di.trans.steps.sapinput.sap.SAPRow;

public class SAPConnectionImplTest3 {

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
		
		// how to get table fields
		System.out.println("how to get table fields: SKAT");
		Collection<SAPField> fields = sc.getTableFields("SKAT");
		for (SAPField field : fields) {
			System.out.println(field);
		}
		System.out.println();
		
		// how to read table
		System.out.println("how to read table: SKAT");
		SAPResultSet sfr9 = sc.readTable("SKAT", fields);
		for (SAPRow row : sfr9.getRows()) {
			System.out.println(row);
		}
		System.out.println(sfr9.getRows().size());
		System.out.println();

		// how to get function signature for table
		System.out.println("how to get function signature: TABLE:SKAT");
		SAPFunction sf11 = new SAPFunction("TABLE:SKAT");
		SAPFunctionSignature sfs11 = sc.getFunctionSignature(sf11);
		System.out.println("input:");
		for (SAPField field : sfs11.getInput()) {
			System.out.println(field);
		}
		System.out.println("output:");
		for (SAPField field : sfs11.getOutput()) {
			System.out.println(field);
		}
		System.out.println();
	}

}
