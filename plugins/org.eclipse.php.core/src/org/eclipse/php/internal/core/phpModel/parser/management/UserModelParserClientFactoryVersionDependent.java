/*******************************************************************************
 * Copyright (c) 2006 Zend Corporation and IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Zend and IBM - Initial implementation
 *******************************************************************************/
package org.eclipse.php.internal.core.phpModel.parser.management;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.php.internal.core.phpModel.parser.*;
import org.eclipse.php.internal.core.preferences.IPreferencesPropagatorListener;
import org.eclipse.php.internal.core.preferences.PreferencesPropagatorEvent;
import org.eclipse.php.internal.core.project.properties.handlers.PhpVersionChangedHandler;
import org.eclipse.php.internal.core.project.properties.handlers.PhpVersionProjectPropertyHandler;
import org.eclipse.php.internal.core.util.project.observer.IProjectClosedObserver;
import org.eclipse.php.internal.core.util.project.observer.ProjectRemovedObserversAttacher;

public class UserModelParserClientFactoryVersionDependent implements IParserClientFactory {

	private String phpVersion;
	private PhpVersionListener phpVersionListener;
	private Map version2ParserClientMap = new HashMap();
	private PHPUserModelManager userModelManager;
	private IProjectClosedObserver projectChangeObserver;
	
	public void dispose() {
		Iterator i = version2ParserClientMap.values().iterator();
		while (i.hasNext()) {
			((ParserClient)i.next()).dispose();
		}
	}

	public UserModelParserClientFactoryVersionDependent(PHPUserModelManager userModelManager) {
		this.userModelManager = userModelManager;
		phpVersion = PhpVersionProjectPropertyHandler.getVersion(userModelManager.getProject());
		initVersionChangeListener();
	}

	private void initVersionChangeListener() {
		phpVersionListener = new PhpVersionListener();
		PhpVersionChangedHandler.getInstance().addPhpVersionChangedListener(phpVersionListener);
		ProjectRemovedObserversAttacher.getInstance().addProjectClosedObserver(userModelManager.getProject(), projectChangeObserver = new IProjectClosedObserver() {
			public void closed() {
				PhpVersionChangedHandler.getInstance().removePhpVersionChangedListener(phpVersionListener);
			}
		});
	}
	
	private class PhpVersionListener implements IPreferencesPropagatorListener {
		public void preferencesEventOccured(PreferencesPropagatorEvent event) {
			phpVersion = (String) event.getNewValue();
		}

		public IProject getProject() {
			return userModelManager.getProject();
		}
	}

	public ParserClient create() {
		Object object = version2ParserClientMap.get(phpVersion);
		if (object == null) {
			PHPLanguageManager languageManager = PHPLanguageManagerProvider.instance().getPHPLanguageManager(phpVersion);
			ParserClient parserClient = languageManager.createParserClient(userModelManager.getUserModel(), userModelManager.getProject());
			version2ParserClientMap.put(phpVersion, parserClient);
			return parserClient;
		}
		
		return (ParserClient)object;
	}

	public boolean isParsable(String fileName, int parsingReason) {
		return true;
	}
}