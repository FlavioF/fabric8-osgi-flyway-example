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
package com.github.pires.example.dal.impl.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import com.github.pires.example.dal.entities.JSON;

@Entity
@org.hibernate.annotations.TypeDefs({
  @org.hibernate.annotations.TypeDef(name = "JSON", defaultForType =  com.github.pires.example.dal.entities.JSON.class, typeClass = com.github.pires.example.dal.impl.json.JSONUserType.class)})
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Version
  private Long version;
  private String name;
  private JSON properties;

  public UserEntity() {
    this.properties = new JSON();
  }

  public Long getId() {
    return id;
  }

  public Long getVersion() {
    return version;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the properties
   */
  public JSON getProperties() {
    return properties;
  }

  /**
   * @param properties the properties to set
   */
  public void setProperties(JSON properties) {
    this.properties = properties;
  }

  @Override
  public String toString() {
    return "Person{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", properties=" + properties.toString()
            + '}';
  }
}