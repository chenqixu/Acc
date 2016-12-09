package cqx.acc.util;

import java.util.ArrayList;
import java.util.List;

import cqx.acc.util.bean.Acc_use_type;

public class AccusetypeUtils {
	private static AccusetypeUtils accusetypeUtils = new AccusetypeUtils();
	private AccusetypeUtils(){		
	}
	public static AccusetypeUtils getInstance(){
		if(accusetypeUtils==null)accusetypeUtils = new AccusetypeUtils();
		return accusetypeUtils;
	}
	public List<Acc_use_type> queryTypeListByUsername(String user_name){
		List<Acc_use_type> resultList = new ArrayList<Acc_use_type>();
		String nodeName = "qryResponse";
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
	    			+" <request>{\"packagestr\":\"com.cqx.acc.service.bean.dim\",\"requestObjbean\":{\"acc_use_desc\":\"\",\"acc_use_name\":\"\",\"acc_use_type\":\"\",\"acc_sts\":\"\",\"pagenum\":\"\",\"startnum\":\"\",\"user_name\":\""
	    			+user_name+"\"},\"tablecolum\":\"acc_use_type,acc_use_name,acc_use_desc\",\"tablename\":\"acc_use_type\"}</request>"
	    			+"</message>"
	    			+"</ser:qry>"
	    			+"</soapenv:Body>"
	    			+"</soapenv:Envelope>";
        	Utils.LogDebug("[cqx.acc.queryTypeListByUsername.soap]"+soap);
	    	byte[] data = soap.getBytes();
	    	String sUrl = Constants.jsonserverURL;
	    	CallWebService cws = new CallWebService();
	    	String resultxml = cws.doAction(sUrl, data);
	        if (resultxml.length()>0){
	            // 解析返回信息
	        	Utils.LogDebug("[cqx.acc.queryTypeListByUsername.resultxml]"+resultxml);
	        	ResultXML rx = new ResultXML();
	    		StringBuffer xml = new StringBuffer();
	    		xml.append("<?xml version=\"1.0\"  encoding='UTF-8'?>");
	    		xml.append(resultxml);
	    		XMLData xd = new XMLData(xml.toString());
	    		rx.rtFlag = true;
	    		rx.bXmldata = true;
	    		rx.xmldata = xd;
	    		rx.setbFlag(false);		
	    		rx.resetParent().node("Body").node(nodeName).node("message").setParentPointer();
	    		// 鉴权失败及未知原因需要重试
	    		if(!MessageHelper.isStatusOk(rx, nodeName)){
	    			
	    		}
	    		rx.setRowFlagInfo("response");
	    		rx.First();
	            if(rx.isEof()){// 没有结果
	            	Utils.LogInfo("[cqx.acc.queryTypeListByUsername.resultxml]no result");
	            }else{// 有结果
			        while (!rx.isEof()) {
			        	String rowvalue = rx.getRowValue();
		            	Utils.LogDebug("[cqx.acc.queryTypeListByUsername.rowvalue]"+rowvalue);
		            	resultList.add(new Acc_use_type().jsonToBean(rowvalue));
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
