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
import org.pentaho.di.trans.steps.sapinput.sap.SAPResultSet;
import org.pentaho.di.trans.steps.sapinput.sap.SAPRow;

public class SAPConnectionImplTest2 {

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

		// how to get fieldlist from RFC_READ_TABLE
		System.out.println("how to get fieldlist from RFC_READ_TABLE");
		SAPFunction sf41 = sc.getFunction("RFC_READ_TABLE");
		Collection<SAPField> input41 = new Vector<SAPField>();
		input41.add(new SAPField("QUERY_TABLE", "", "input_single", "SKAT"));
		Collection<SAPField> output41 = new Vector<SAPField>();
		output41.add(new SAPField("FIELDNAME", "FIELDS", "output_table"));
		SAPResultSet sfr41 = sc.executeFunctionUncursored(sf41, input41,
				output41);
		for (SAPRow row : sfr41.getRows()) {
			System.out.println(row);
		}
		System.out.println(sfr41.getRows().size());
		if (7 != sfr41.getRows().size())
			System.out.println("ERROR: Size must be 7.");
		System.out.println();

		// how to get data via RFC_READ_TABLE from table SKAT
		System.out
				.println("how to get data via RFC_READ_TABLE from table SKAT");
		SAPFunction sf42 = sc.getFunction("RFC_READ_TABLE");
		Collection<SAPField> input42 = new Vector<SAPField>();
		input42.add(new SAPField("QUERY_TABLE", "", "input_single", "SKAT"));
		input42.add(new SAPField("ROWSKIPS", "", "input_single", "100"));
		input42.add(new SAPField("ROWCOUNT", "", "input_single", "100"));
		input42.add(new SAPField("DELIMITER", "", "input_single", ";"));
		input42.add(new SAPField("FIELDNAME", "FIELDS", "input_table",
				"MANDT;SPRAS;KTOPL;SAKNR;TXT20;TXT50;MCOD1"));
		Collection<SAPField> output42 = new Vector<SAPField>();
		output42.add(new SAPField("WA", "DATA", "output_table"));
		SAPResultSet sfr42 = sc.executeFunctionUncursored(sf42, input42,
				output42);
		for (SAPRow row : sfr42.getRows()) {
			System.out.println(row);
		}
		System.out.println(sfr42.getRows().size());
		if (100 != sfr42.getRows().size())
			System.out.println("ERROR: Size must be 100.");
		System.out.println();
	}

}
