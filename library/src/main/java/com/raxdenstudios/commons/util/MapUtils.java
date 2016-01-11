package com.raxdenstudios.commons.util;

import org.xml.sax.Attributes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Angel Gomez
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
