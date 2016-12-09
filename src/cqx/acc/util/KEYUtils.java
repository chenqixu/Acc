package cqx.acc.util;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat") public class KEYUtils {
	private static Map<String, String> keylist = new HashMap<String, String>();
	static {
		keylist.put("cqx", "61832587718812523095");
		keylist.put("cry", "81239571609091726356");
	}
	
	/**
	 * 将字符串转成MD5值
	 * 
	 * @param string
	 * @return
	 */
	public static String stringToMD5(String string) {
	    byte[] hash;
	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (Exception e) {
			Utils.LogErr(e.getMessage());
	        return null;
	    }
	    StringBuilder hex = new StringBuilder(hash.length * 2);
	    for (byte b : hash) {
	        if ((b & 0xFF) < 0x10)
	            hex.append("0");
	        hex.append(Integer.toHexString(b & 0xFF));
	    }
	    return hex.toString();
	}
	
	public static String getKEYByNameAndId(String name){
		String result = "";
		String sed = syncTime();
		String id = keylist.get(name);
		result = stringToMD5(sed.substring(0, sed.length()-1)+name+id);		
		return result;
	}
	
	private static String syncTime(){
		String resultcode = "";
    	try{
	    	String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.acc.cqx.com/\">"
				+"<soapenv:Header/>"
				+"<soapenv:Body>"
				+"<ser:syncTime>"
				+"<message>"
				+"<header>"
				+"<requestname></requestname>"
				+"<key></key>"
				+"<requesttime></requesttime>"
				+"<requestip></requestip>"
				+"</header>"
				+"<request>"
				+"<username></username>"
				+"<password></password>"
				+"</request>"
				+"</message>"
				+"</ser:syncTime>"
				+"</soapenv:Body>"
				+"</soapenv:Envelope>";
        	Utils.LogDebug("[cqx.acc.syncTime.soap]"+soap);
	    	byte[] data = soap.getBytes();
	    	String sUrl = Constants.loginserverURL;
	    	CallWebService cws = new CallWebService();
	    	String resultxml = cws.doAction(sUrl, data);
	        if (resultxml.length()>0){
	            // 解析返回信息
	        	Utils.LogDebug("[cqx.acc.syncTime.resultxml]"+resultxml);
	        	ResultXML rx = new ResultXML();
	    		StringBuffer xml = new StringBuffer();
	    		xml.append("<?xml version=\"1.0\"  encoding='UTF-8'?>");
	    		xml.append(resultxml);
	    		XMLData xd = new XMLData(xml.toString());
	    		rx.rtFlag = true;
	    		rx.bXmldata = true;
	    		rx.xmldata = xd;
	    		rx.setbFlag(false);		
	    		rx.resetParent().node("Body").node("syncTimeResponse").node("message").setParentPointer();
	    		rx.setRowFlagInfo("response");
	    		rx.First();
	            if(rx.isEof()){// 没有结果
	            	Utils.LogInfo("[cqx.acc.syncTime.resultxml]no result");
	            }else{// 有结果
	            	resultcode = rx.getRowValue();
	            }
	        }
    	}catch(Exception e){
			Utils.LogErr(e.getMessage());
    	}
    	return resultcode;
	}
}
