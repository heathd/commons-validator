/*
 * $Id: ValidatorUtils.java 153692 2005-02-14 01:33:31Z niallp $
 * $Rev$
 * $Date: 2005-02-13 18:33:31 -0700 (Sun, 13 Feb 2005) $
 *
 * ====================================================================
 * Copyright 2001-2005 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.validator.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Msg;
import org.apache.commons.validator.Var;

/**
 * Basic utility methods.
 * <p>
 * The use of FastHashMap is deprecated and will be replaced in a future
 * release.
 * </p>
 */
public class ValidatorUtils {

    private static final Log log = LogFactory.getLog(ValidatorUtils.class);

    /**
     * <p>Replace part of a <code>String</code> with another value.</p>
     *
     * @param value <code>String</code> to perform the replacement on.
     * @param key The name of the constant.
     * @param replaceValue The value of the constant.
     */
    public static String replace(String value, String key, String replaceValue) {

        if (value == null || key == null || replaceValue == null) {
            return value;
        }

        int pos = value.indexOf(key);

        if (pos < 0) {
            return value;
        }

        int length = value.length();
        int start = pos;
        int end = pos + key.length();

        if (length == key.length()) {
            value = replaceValue;

        } else if (end == length) {
            value = value.substring(0, start) + replaceValue;

        } else {
            value =
                    value.substring(0, start)
                    + replaceValue
                    + replace(value.substring(end), key, replaceValue);
        }

        return value;
    }

    /**
     * Convenience method for getting a value from a bean property as a
     * <code>String</code>.  If the property is a <code>String[]</code> or
     * <code>Collection</code> and it is empty, an empty <code>String</code>
     * "" is returned.  Otherwise, property.toString() is returned.  This method
     * may return <code>null</code> if there was an error retrieving the
     * property.
     */
    public static String getValueAsString(Object bean, String property) {
        Object value = null;

        try {
            value = PropertyUtils.getProperty(bean, property);

        } catch(IllegalAccessException e) {
            log.error(e.getMessage(), e);
        } catch(InvocationTargetException e) {
            log.error(e.getMessage(), e);
        } catch(NoSuchMethodException e) {
            log.error(e.getMessage(), e);
        }

        if (value == null) {
            return null;
        }

        if (value instanceof String[]) {
            return ((String[]) value).length > 0 ? value.toString() : "";

        } else if (value instanceof Collection) {
            return ((Collection) value).isEmpty() ? "" : value.toString();

        } else {
            return value.toString();
        }

    }

    /**
     * Makes a deep copy of a <code>FastHashMap</code> if the values
     * are <code>Msg</code>, <code>Arg</code>,
     * or <code>Var</code>.  Otherwise it is a shallow copy.
     *
     * @param map <code>FastHashMap</code> to copy.
     * @return FastHashMap A copy of the <code>FastHashMap</code> that was
     * passed in.
     * @deprecated This method is not part of Validator's public API.  Validator
     * will use it internally until FastHashMap references are removed.  Use
     * copyMap() instead.
     */
    public static FastHashMap copyFastHashMap(FastHashMap map) {
        FastHashMap results = new FastHashMap();

        Iterator i = map.keySet().iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            Object value = map.get(key);

            if (value instanceof Msg) {
                results.put(key, ((Msg) value).clone());
            } else if (value instanceof Arg) {
                results.put(key, ((Arg) value).clone());
            } else if (value instanceof Var) {
                results.put(key, ((Var) value).clone());
            } else {
                results.put(key, value);
            }
        }

        results.setFast(true);
        return results;
    }
    
    /**
     * Makes a deep copy of a <code>Map</code> if the values are 
     * <code>Msg</code>, <code>Arg</code>, or <code>Var</code>.  Otherwise, 
     * it is a shallow copy.
     *
     * @return A copy of the <code>Map</code> that was passed in.
     */
    public static Map copyMap(Map map) {
        Map results = new HashMap();

        Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Object value = map.get(key);

            if (value instanceof Msg) {
                results.put(key, ((Msg) value).clone());
            } else if (value instanceof Arg) {
                results.put(key, ((Arg) value).clone());
            } else if (value instanceof Var) {
                results.put(key, ((Var) value).clone());
            } else {
                results.put(key, value);
            }
        }
        return results;
    }

}
