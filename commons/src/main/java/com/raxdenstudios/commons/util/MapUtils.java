/*
 * Copyright 2014 Ángel Gómez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.raxdenstudios.commons.util;

import org.xml.sax.Attributes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class MapUtils {

	public static Map<String, Object> attributesToHashMap(Attributes atts) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (int i=0; i<atts.getLength(); i++) {
			map.put(atts.getLocalName(i), atts.getValue(i));
		}
		return map;
	}
	
}
