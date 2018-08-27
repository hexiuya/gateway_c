package com.gateway.util;

public class StringUtil {

	public static String getRouteStr(String indexStr){
		
		if(indexStr == null){
			return "";
		}
		
		String[] arrayStr = indexStr.split("/");
		
		return arrayStr[1];
		
	}
	
//	public static void main(String[] args) {
//		String url = "/crm/crm-test/onlineManage/login?adfjlliieoi";
//		String subStr = getRouteStr(url,"/");
//		System.out.println(subStr);
//	}
}
