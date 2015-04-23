/*
 * SonarQube PDF Report
 * Copyright (C) 2010 klicap - ingenieria del puzle
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.report.pdf.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.report.pdf.batch.PDFPostJob;
import org.testng.annotations.Test;

public class PDFPostJobTest {

    @Test(groups = { "post-job" })
    public void doNotExecuteIfSkipParameter() {
        // PropertiesConfiguration conf = new PropertiesConfiguration();
        // conf.setProperty(PDFPostJob.SKIP_PDF_KEY, Boolean.TRUE);

        Project project = mock(Project.class);
        when(project.getSettings()).thenReturn(new Settings().setProperty(PDFPostJob.SKIP_PDF_KEY, Boolean.TRUE));
        
        assertFalse(new PDFPostJob().shouldExecuteOnProject(project));
    }

    @Test(groups = { "post-job" })
    public void shouldExecuteIfNoSkipParameter() {
        Project project = mock(Project.class);
        when(project.getSettings()).thenReturn(null);

        assertTrue(new PDFPostJob().shouldExecuteOnProject(project));
    }
}
