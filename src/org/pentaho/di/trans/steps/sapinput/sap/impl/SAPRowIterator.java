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
import java.util.Iterator;

import org.pentaho.di.trans.steps.sapinput.sap.SAPField;
import org.pentaho.di.trans.steps.sapinput.sap.SAPRow;

import com.sap.conn.jco.JCoRecord;
import com.sap.conn.jco.JCoTable;

public class SAPRowIterator implements Iterator<SAPRow> {

	private JCoRecord record;
	private Collection<SAPField> outputfields;
	private String tableorstructurename;
	private boolean BOF = true;

	public SAPRowIterator(JCoRecord record, Collection<SAPField> outputfields,
			String tableorstructurename) {
		super();
		this.record = record;
		this.outputfields = outputfields;
		this.tableorstructurename = tableorstructurename;
	}

	public boolean hasNext() {
		if (record instanceof JCoTable) {
			JCoTable t = (JCoTable) record;
			if (t.isEmpty())
				return false;
			if (BOF)
				return true;
			if (t.isLastRow())
				return false;
			return true;
		} else {
			return BOF;
		}
	}

	public SAPRow next() {
		if (record instanceof JCoTable) {
			JCoTable t = (JCoTable) record;
			if (t.isEmpty())
				return null;
			if (BOF) {
				BOF = false;
				return createRow();
			}
			if (t.isLastRow())
				return null;
			t.nextRow();
			return createRow();
		} else {
			if (BOF) {
				BOF = false;
				return createRow();
			}
			return null;
		}
	}

	public void remove() {
		// do nothing
	}

	private SAPRow createRow() {
		SAPRow sr = new SAPRow();
		for (SAPField field : outputfields) {
			String name = field.getName();
			Object value;
			if (name.contains("|"))
			{
				String[] namez =  name.split("\\|");
				String name1 = namez[0];
				String name2 = namez[1];
				value = record.getStructure(name1).getValue(name2);
			}else
			    value = record.getValue(name);
			String type = "Object";
			if (value != null) {
				if (value instanceof String) {
					type = "String";
				} else {
					type = value.getClass().getSimpleName();
				}
			}
			SAPField sapField = new SAPField(name, tableorstructurename, type,
					value);
			sapField.setTypePentaho(type);
			sr.addField(sapField);
		}
		return sr;
	}

}
