package cqx.acc.util;

import java.util.ArrayList;
import java.util.List;

import cqx.acc.util.bean.Acc_my_card;

public class AccmycardUtils {
	private static AccmycardUtils accmycardUtils = new AccmycardUtils();
	private AccmycardUtils(){		
	}
	public static AccmycardUtils getInstance(){
		if(accmycardUtils==null)accmycardUtils = new AccmycardUtils();
		return accmycardUtils;
	}
	public List<Acc_my_card> queryCradListByUsername(String user_name){
		List<Acc_my_card> resultList = new ArrayList<Acc_my_card>();
		try{
	    	String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.acc.cqx.com/\">"
	    			+"<soapenv:Header/>"
	    			+"<soapenv:Body>"
	    			+"<ser:qry>"
	    			+"<message>"
	    			+"<header>"
	    			+"<requestname>"+user_name+"</requestname>"
	    			+"<key>"+KEYUtils.getKEYByNameAndId(user_name)+"</key>"
	    			+"<requesttime></requesttime>"
	    			+"<requestip></requestip>"
	    			+"</header>"
	    			+" <request>{\"packagestr\":\"com.cqx.acc.service.bean.dim\",\"requestObjbean\":{\"acc_card_desc\":\"\",\"acc_card_name\":\"\",\"acc_my_card\":\"\",\"acc_sts\":\"\",\"pagenum\":\"\",\"startnum\":\"\",\"user_name\":\""
	    			+user_name+"\"},\"tablecolum\":\"acc_my_card,acc_card_name,acc_card_desc\",\"tablename\":\"acc_my_card\"}</request>"
	    			+"</message>"
	    			+"</ser:qry>"
	    			+"</soapenv:Body>"
	    			+"</soapenv:Envelope>";
        	Utils.LogDebug("[cqx.acc.queryCradListByUsername.soap]"+soap);
	    	byte[] data = soap.getBytes();
	    	String sUrl = Constants.jsonserverURL;
	    	CallWebService cws = new CallWebService();
	    	String resultxml = cws.doAction(sUrl, data);
	        if (resultxml.length()>0){
	            // 解析返回信息
	        	Utils.LogDebug("[cqx.acc.queryCradListByUsername.resultxml]"+resultxml);
	        	ResultXML rx = new ResultXML();
	    		StringBuffer xml = new StringBuffer();
	    		xml.append("<?xml version=\"1.0\"  encoding='UTF-8'?>");
	    		xml.append(resultxml);
	    		XMLData xd = new XMLData(xml.toString());
	    		rx.rtFlag = true;
	    		rx.bXmldata = true;
	    		rx.xmldata = xd;
	    		rx.setbFlag(false);		
	    		rx.resetParent().node("Body").node("qryResponse").node("message").setParentPointer();
	    		rx.setRowFlagInfo("response");
	    		rx.First();
	            if(rx.isEof()){// 没有结果
	            	Utils.LogInfo("[cqx.acc.queryCradListByUsername.resultxml]no result");
	            }else{// 有结果
			        while (!rx.isEof()) {
			        	String rowvalue = rx.getRowValue();
		            	Utils.LogDebug("[cqx.acc.queryCradListByUsername.rowvalue]"+rowvalue);
		            	resultList.add(new Acc_my_card().jsonToBean(rowvalue));
			        	rx.Next();
			        }
	            }
	        }
		}catch(Exception e){
			Utils.LogErr(e.getMessage());
		}
		return resultList;
	}
}
