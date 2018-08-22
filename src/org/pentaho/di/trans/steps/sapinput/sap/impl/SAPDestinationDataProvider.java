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

import java.util.Properties;

import org.pentaho.di.trans.steps.sapinput.sap.SAPConnectionParams;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

	public class SAPDestinationDataProvider implements DestinationDataProvider {
		
		static String SAP_SERVER = "SAP_SERVER";
		private Properties ABAP_AS_properties;

		public SAPDestinationDataProvider() {
			ABAP_AS_properties = null;
		}

		public SAPDestinationDataProvider(SAPConnectionParams system) {
			Properties properties = new Properties();
			properties.setProperty(DestinationDataProvider.JCO_ASHOST, system
					.getHost());
			properties.setProperty(DestinationDataProvider.JCO_SYSNR, system
					.getSysnr());
			properties.setProperty(DestinationDataProvider.JCO_CLIENT, system
					.getClient());
			
			properties.setProperty(DestinationDataProvider.JCO_USER, system
					.getUser());
			properties.setProperty(DestinationDataProvider.JCO_PASSWD, system
					.getPassword());
		
			ABAP_AS_properties = properties;
		}

		public void setDestinationProperties(SAPConnectionParams system) {
			Properties properties = new Properties();
			properties.setProperty(DestinationDataProvider.JCO_ASHOST, system
					.getHost());
			properties.setProperty(DestinationDataProvider.JCO_SYSNR, system
					.getSysnr());
			properties.setProperty(DestinationDataProvider.JCO_CLIENT, system
					.getClient());
			
			properties.setProperty(DestinationDataProvider.JCO_USER, system
					.getUser());
			properties.setProperty(DestinationDataProvider.JCO_PASSWD, system
					.getPassword());
		
			ABAP_AS_properties = properties;
		}
		
		public Properties getDestinationProperties(String system) {
			return ABAP_AS_properties;
		}

		public void setDestinationDataEventListener(
				DestinationDataEventListener eventListener) {
			// do nothing
		}

		public boolean supportsEvents() {
			return false;
		}

	}

