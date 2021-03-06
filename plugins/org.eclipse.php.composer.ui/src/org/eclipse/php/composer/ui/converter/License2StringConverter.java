/*******************************************************************************
 * Copyright (c) 2012, 2016 PDT Extension Group and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     PDT Extension Group - initial API and implementation
 *******************************************************************************/
package org.eclipse.php.composer.ui.converter;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.conversion.Converter;

import org.eclipse.php.composer.api.collection.License;

public class License2StringConverter extends Converter {

	public License2StringConverter() {
		super(String[].class, String.class);
	}

	@Override
	public String convert(Object fromObject) {
		if (fromObject == null) {
			return "";
		}
		License licenses = (License) fromObject;
		return StringUtils.join((String[]) licenses.toArray(new String[] {}), ", ");
	}

}
