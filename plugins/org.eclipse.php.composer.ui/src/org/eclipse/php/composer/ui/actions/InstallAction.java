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
package org.eclipse.php.composer.ui.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.php.composer.ui.ComposerUIPluginImages;
import org.eclipse.php.composer.ui.job.InstallJob;
import org.eclipse.ui.IWorkbenchPartSite;

public class InstallAction extends ComposerAction {

	public InstallAction(IProject project, IWorkbenchPartSite site) {
		super(project, site, "org.eclipse.php.composer.ui.command.install");
	}

	@Override
	public void run() {
		ensureSaved();

		InstallJob job = new InstallJob(project);
		job.setUser(true);
		job.schedule();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ComposerUIPluginImages.INSTALL;
	}

}
