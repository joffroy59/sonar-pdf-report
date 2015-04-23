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

package org.sonar.report.pdf.batch;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.CheckProject;
import org.sonar.api.batch.PostJob;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.report.pdf.util.FileUploader;

public class PDFPostJob implements PostJob, CheckProject {

  private static final Logger LOG = LoggerFactory.getLogger(PDFPostJob.class);

  public static final String SKIP_PDF_KEY = "sonar.pdf.skip";
  public static final boolean SKIP_PDF_DEFAULT_VALUE = false;

  public static final String REPORT_TYPE = "report.type";
  public static final String REPORT_TYPE_DEFAULT_VALUE = "workbook";

  public static final String USERNAME = "sonar.pdf.username";
  public static final String USERNAME_DEFAULT_VALUE = "";

  public static final String PASSWORD = "sonar.pdf.password";
  public static final String PASSWORD_DEFAULT_VALUE = "";

  public static final String SONAR_HOST_URL = "sonar.host.url";
  public static final String SONAR_HOST_URL_DEFAULT_VALUE = "http://localhost:9000";

  public static final String SONAR_BRANCH = "sonar.branch";
  public static final String SONAR_BRANCH_DEFAULT_VALUE = null;

  @Override
  public boolean shouldExecuteOnProject(final Project project) {
      return !((project.getSettings() == null)?SKIP_PDF_DEFAULT_VALUE:project.getSettings().getBoolean(SKIP_PDF_KEY)); 
  }

  @Override
  public void executeOn(final Project project, final SensorContext context) {
    LOG.info("Executing decorator: PDF Report");
    String sonarHostUrl = project.getSettings().getString(SONAR_HOST_URL);
    String username = project.getSettings().getString(USERNAME);
    String password = project.getSettings().getString(PASSWORD);
    String branch = project.getSettings().getString(SONAR_BRANCH);
    String reportType = project.getSettings().getString(REPORT_TYPE);
    PDFGenerator generator = new PDFGenerator(project, sonarHostUrl, username, password, branch, reportType);

    generator.execute();

    String path = project.getFileSystem().getSonarWorkingDirectory().getAbsolutePath() + "/"
        + project.getEffectiveKey().replace(':', '-') + ".pdf";

    File pdf = new File(path);
    if (pdf.exists()) {
      FileUploader.upload(pdf, sonarHostUrl + "/pdf_report/store");
    } else {
      LOG.error("PDF file not found in local filesystem. Report could not be sent to server.");
    }
  }

}
