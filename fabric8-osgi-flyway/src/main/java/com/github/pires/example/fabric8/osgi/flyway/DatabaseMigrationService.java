/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.pires.example.fabric8.osgi.flyway;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseMigrationService {

  private static final Logger log = LoggerFactory
      .getLogger(DatabaseMigrationService.class);

  private DataSource datasource;
  private Flyway flyway;

  private void migrate(boolean factoryReset) {
    ClassLoader thisClassLoader = this.getClass().getClassLoader();
    getFlyway().setDataSource(datasource);
    getFlyway().setClassLoader(thisClassLoader);
    if (factoryReset) {
      getFlyway().clean();
    }
    // Creating init row in schema_version table in the case that there are
    // already information in database (e.g spatial_ref_sys)
    getFlyway().setInitOnMigrate(true);
    getFlyway().setInitVersion("0.0.0");
    getFlyway().setInitDescription("Init database versioning.");

    getFlyway().migrate();
  }

  public void reset() {
    log.info("Reseting..");
    migrate(true);
  }

  public void startUp() {
    log.info("Migrating..");
    migrate(false);
  }

  public DataSource getDatasource() {
    return datasource;
  }

  public void setDatasource(DataSource datasource) {
    this.datasource = datasource;
  }

  public Flyway getFlyway() {
    return flyway;
  }

  public void setFlyway(Flyway flyway) {
    this.flyway = flyway;
  }
}
