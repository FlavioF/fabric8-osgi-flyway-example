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
package com.github.pires.example.dal.entities;




import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;




/**
 * This abstraction enables the user to persist a PostgresSQL native JSON column (9.2+)
 * without the need to set Hibernate specific annotations on the model.
 * The Hibernate custom UserType is implemented in JSONUserType class
 * The package where the class is used must have the custom mapping configured.
 * eg.: @org.hibernate.annotations.TypeDefs({@org.hibernate.annotations.TypeDef(name = "JSON", defaultForType = JSON.class, typeClass =  JSONUserType.class)})
 * Created by amarcos on 2/11/14.
 */
public class JSON implements Serializable
{
    //region members
    private String value;

    /**
     * Updates the instance value.<br/>
     * If <code>schema</code> is not provided (null), the schema is not enforced.<br/>
     * If any problem occurs when enforcing the <code>schema</code> on <code>newValue</code>, the instance value defaults to the <code>schema</code>.<br/>
     *
     *
     * @param schema        - the validation and default schema
     * @param newValue      - the new value intended for this instance
     */
    public void setValue(JSONObject schema, JSONObject newValue)
    {
        if (schema != null && newValue != null)
        {
            try
            {
                this.value = updateFromSchema(schema, newValue);
            }
            catch (JSONException e)
            {
                this.value = schema.toString();
            }
        }
        else if (schema == null && newValue != null)
        {
            this.value = newValue.toString();
        }

    }

    /**
     * Updates the instance value.<br/>
     * If <code>schema</code> is not provided (null), the schema is not enforced.<br/>
     * If any problem occurs when enforcing the <code>schema</code> on <code>newValue</code>, the instance value defaults to the <code>schema</code>.<br/>
     *
     *
     * @param schema        - the validation and default schema
     * @param newValue      - the new value intended for this instance
     */
    public void setValue(String schema, String newValue)
    {
        if (schema!=null && !schema.isEmpty() && newValue!=null && !newValue.isEmpty())
        {
            try
            {
                JSONObject schemaObject = new JSONObject(schema);
                JSONObject valueObject = new JSONObject(newValue);
                this.value = updateFromSchema(schemaObject, valueObject);
            }
            catch (JSONException e)
            {
                this.value = schema;
            }
        }
        else if ((schema==null || schema.isEmpty()) && newValue!=null && !newValue.isEmpty())
        {
            this.value = newValue;
        }
    }

    public JSONObject getValue() throws JSONException
    {
        return new JSONObject(this.value);
    }
    //endregion


    //region public API
    public JSON()
    {
    }

    /**
     * Creates a new instance with no schema enforcement.<br/>
     * @param value      - the value intended for this instance
     */
    public JSON(String value)
    {
        setValue(null, value);
    }

    /**
     * Creates a new instance with no schema enforcement.<br/>
     * @param value      - the value intended for this instance
     */
    public JSON(JSONObject value)
    {
        setValue(null, value);
    }


    /**
     * Creates a new instance.<br/>
     * If <code>schema</code> is not provided (null), the schema is not enforced.<br/>
     * If any problem occurs when enforcing the <code>schema</code> on <code>newValue</code>, the instance value defaults to the <code>schema</code>.<br/>
     *
     *
     * @param schema        - the validation and default schema
     * @param value         - the value intended for this instance
     */
    public JSON(String schema, String value)
    {
        setValue(schema, value);
    }

    /**
     * Creates a new instance.<br/>
     * If <code>schema</code> is not provided (null), the schema is not enforced.<br/>
     * If any problem occurs when enforcing the <code>schema</code> on <code>newValue</code>, the instance value defaults to the <code>schema</code>.<br/>
     *
     *
     * @param schema        - the validation and default schema
     * @param value         - the value intended for this instance
     */
    public JSON(JSONObject schema, JSONObject value)
    {
        setValue(schema, value);
    }

    @Override
    public String toString()
    {
        return this.value;
    }
    //endregion


    //region internal API
    private String updateFromSchema(JSONObject schema, JSONObject newValue) throws JSONException
    {
        return deepMerge(schema, newValue).toString();
    }

    private JSONObject deepMerge(JSONObject schemaObject, JSONObject newValueObject) throws JSONException
    {
        //add schema keys missing in  newValue
        for (String key : JSONObject.getNames(schemaObject))
        {
            Object schemaKeyValue = schemaObject.get(key);
            if (!newValueObject.has(key))
            {
                // new value for "key":
                newValueObject.put(key, schemaKeyValue);
            }
            else
            {
                // existing value for "key" - recursively deep merge:
                if (schemaKeyValue instanceof JSONObject)
                {
                    deepMerge((JSONObject) schemaKeyValue, newValueObject.getJSONObject(key));
                }
            }
        }

        //remove newValue keys missing in schema
        for (String key : JSONObject.getNames(newValueObject))
        {
            if (!schemaObject.has(key))
            {
                // remove "key":
                newValueObject.remove(key);
            }
            else
            {
                if (newValueObject.get(key) instanceof JSONObject)
                {
                    deepMerge(schemaObject.getJSONObject(key), newValueObject.getJSONObject(key));
                }
            }
        }

        return newValueObject;
    }
    //endregion
}
