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
package com.github.pires.example.fabric8.osgi.flyway.db.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

/**
 * Flyway Java Migration
 */
public class V0_0_3__insert_new_user implements JdbcMigration {

  public void migrate(Connection connection) throws Exception {
    try (PreparedStatement statement = connection
        .prepareStatement("INSERT INTO userentity (id,name) VALUES ('5','JavaUser')")) {
      statement.execute();
    }

  }

}
