package com.example.wireframe.protocal.protocalProcess;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParserUtil {
	public static String parseStr(JSONObject json, String name) {
		return parseStr(json, name, null);
	}

	public static String parseStr(JSONObject json, String name,
			String defaultStr) {
		String result = defaultStr;
		if (json.has(name)) {
			try {
				result = json.getString(name);
			} catch (Exception e) {

			}
		}
		return result;
	}
	
	public static int parseInteger(JSONObject json, String name,
			int defaultInteger) {
		int result = defaultInteger;
		if (json.has(name)) {
			try {
				result = json.getInt(name);
			} catch (Exception e) {

			}
		}
		return result;
	}
	
	public static long parseLong(JSONObject json, String name,
			long defaultInteger) {
		long result = defaultInteger;
		if (json.has(name)) {
			try {
				result = json.getLong(name);
			} catch (Exception e) {

			}
		}
		return result;
	}
	
	public static float parseFloat(JSONObject json, String name,
			float defaultFloat) {
		float result = defaultFloat;
		if (json.has(name)) {
			try {
				result = Float.parseFloat(json.getString(name));
			} catch (Exception e) {

			}
		}
		return result;
	}

	public static void parseStrArray(JSONObject json, String[] result,
			String name) {
		if (json.has(name)) {
			try {
				JSONArray jArray = json.getJSONArray(name);
				if (jArray != null) {
					if (result.length != jArray.length())
						return;
					for (int j01 = 0; j01 < jArray.length(); j01++) {
						result[j01] = jArray.getString(j01);
					}
				}
			} catch (Exception e) {}
		}
	}

	public static void parseStrArray(JSONObject json, List<String> result, String name) {
		if (json.has(name)) {
			try {
				JSONArray jArray = json.getJSONArray(name);
				if (jArray != null) {
					if (result == null)
						result = new ArrayList<String>(2);
					else
						result.clear();
					for (int j01 = 0; j01 < jArray.length(); j01++) {
						result.add(jArray.getString(j01));
					}
				}
			} catch (Exception e) {}
		}
	}

	public static List<String> parseStrArray(JSONObject json, String name) {
		List<String> result = new ArrayList<String>(2);
		
		if (json.has(name)) {
			try {
				JSONArray jArray = json.getJSONArray(name);
				if (jArray != null) {
					for (int j01 = 0; j01 < jArray.length(); j01++) {
						result.add(jArray.getString(j01));
					}
				}
			} catch (Exception e) {
			}
		}
		return result;
	}
	
	
	
	
}
