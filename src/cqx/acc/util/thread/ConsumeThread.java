package cqx.acc.util.thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import cqx.acc.util.CallWebService;
import cqx.acc.util.Constants;
import cqx.acc.util.KEYUtils;
import cqx.acc.util.ResultXML;
import cqx.acc.util.Utils;
import cqx.acc.util.XMLData;
import cqx.acc.util.bean.AccCountsDailyBean;

@SuppressLint("SimpleDateFormat") public class ConsumeThread implements Runnable{
	private Handler mHandler;
	private Message msg; // what: 0 success 1 error
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
	private String query_month = ""; // 查询月份
	
	public ConsumeThread(Handler _mHandler, String _query_month){
		this.mHandler = _mHandler;
		this.query_month = _query_month;
		this.msg = this.mHandler.obtainMessage();
	}
	
	@Override
	public void run() {
		Constants.acccountsdailyList = requestDetail(detailQuery());
		this.msg.what = 0;
		this.mHandler.sendMessage(this.msg);
	}
	
	private List<AccCountsDailyBean> requestDetail(String soap){
		List<AccCountsDailyBean> resultList = new ArrayList<AccCountsDailyBean>();
		try{
        	Utils.LogDebug("[cqx.acc.requestDetail.soap]"+soap);
	    	byte[] data = soap.getBytes();
	    	String sUrl = Constants.dealcountsdailyserviceURL;
	    	CallWebService cws = new CallWebService();
	    	String resultxml = cws.doAction(sUrl, data);
	        if (resultxml.length()>0){
	            // 解析返回信息
	        	Utils.LogDebug("[cqx.acc.requestDetail.resultxml]"+resultxml);
	        	ResultXML rx = new ResultXML();
	    		StringBuffer xml = new StringBuffer();
	    		xml.append("<?xml version=\"1.0\"  encoding='UTF-8'?>");
	    		xml.append(resultxml);
	    		XMLData xd = new XMLData(xml.toString());
	    		rx.rtFlag = true;
	    		rx.bXmldata = true;
	    		rx.xmldata = xd;
	    		rx.setbFlag(false);		
	    		rx.resetParent().node("Body").node("qryCountsDailyResponse").node("message").setParentPointer();
	    		rx.setRowFlagInfo("response");
	    		rx.First();
	            if(rx.isEof()){// 没有结果
	            	Utils.LogInfo("[cqx.acc.requestDetail.resultxml]no result");
	            }else{// 有结果
			        while (!rx.isEof()) {
		            	resultList.add(AccCountsDailyBean.strToBean(rx));
			        	rx.Next();
			        }
	            }
	        }
		}catch(Exception e){
			Utils.LogErr(e.getMessage());
		}
		return resultList;
	}

	private String requestSum(String soap){
    	String resultvalue = "";
    	try{
        	Utils.LogDebug("[cqx.acc.requestSum.soap]"+soap);
	    	byte[] data = soap.getBytes();
	    	String sUrl = Constants.dealcountsdailyserviceURL;
	    	CallWebService cws = new CallWebService();
	    	String resultxml = cws.doAction(sUrl, data);
	        if (resultxml.length()>0){
	            // 解析返回信息
	        	Utils.LogDebug("[cqx.acc.requestSum.resultxml]"+resultxml);
	        	ResultXML rx = new ResultXML();
	    		StringBuffer xml = new StringBuffer();
	    		xml.append("<?xml version=\"1.0\"  encoding='UTF-8'?>");
	    		xml.append(resultxml);
	    		XMLData xd = new XMLData(xml.toString());
	    		rx.rtFlag = true;
	    		rx.bXmldata = true;
	    		rx.xmldata = xd;
	    		rx.setbFlag(false);		
	    		rx.resetParent().node("Body").node("sumCountsDailyResponse").node("message").setParentPointer();
	    		rx.setRowFlagInfo("response");
	    		rx.First();
	            if(rx.isEof()){// 没有结果
	            	Utils.LogInfo("[cqx.acc.requestSum.resultxml]no result");
	            }else{// 有结果
	            	resultvalue = rx.getRowValue();	  
	            	Utils.LogDebug("[cqx.acc.requestSum.resultvalue]"+resultvalue);          	
	            }
	        }
    	}catch(Exception e){
			Utils.LogErr(e.getMessage());
    	}		
		return resultvalue;
	}
	
	private String detailQuery(){
		String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.acc.cqx.com/\">"
			+"<soapenv:Header/>"
			+"<soapenv:Body>"
			+"<ser:qryCountsDaily>"
			+"<message>"
			+"<header>"
			+"<requestname>"+Constants.USER_NAME+"</requestname>"
			+"<key>"+KEYUtils.getKEYByNameAndId(Constants.USER_NAME)+"</key>"
			+"<requesttime></requesttime>"
			+"<requestip></requestip>"
			+"</header>"
			+"<request>"
			+"<seq_id></seq_id>"
			+"<acc_use_time1>"+this.query_month+"-01</acc_use_time1>"
			+"<acc_use_time2>"+Utils.getMonthLastday(this.query_month)+"</acc_use_time2>"
			+"<acc_type></acc_type>"
			+"<acc_value1></acc_value1>"
			+"<acc_value2></acc_value2>"
			+"<acc_sts></acc_sts>"
			+"<acc_use_type></acc_use_type>"
			+"<acc_card></acc_card>"
			+"<acc_desc></acc_desc>"
			+"<user_name>"+Constants.USER_NAME+"</user_name>"
			+"<startnum></startnum>"
			+"<pagenum></pagenum>"
			+"</request>"
			+"</message>"
			+"</ser:qryCountsDaily>"
			+"</soapenv:Body>"
			+"</soapenv:Envelope>";
		return soap;
	}
	
	private String todaySum(){
    	String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.acc.cqx.com/\">"
			+"<soapenv:Header/>"
			+"<soapenv:Body>"
			+"<ser:sumCountsDaily>"
			+"<message>"
			+"<header>"
			+"<requestname>"+Constants.USER_NAME+"</requestname>"
			+"<key>"+KEYUtils.getKEYByNameAndId(Constants.USER_NAME)+"</key>"
			+"<requesttime></requesttime>"
			+"<requestip></requestip>"
			+"</header>"
			+"<request>"
			+"<seq_id></seq_id>"
			+"<acc_use_time1>"+sdf1.format(new Date())+"</acc_use_time1>"
			+"<acc_use_time2></acc_use_time2>"
			+"<acc_type>2</acc_type>"
			+"<acc_value1></acc_value1>"
			+"<acc_value2></acc_value2>"
			+"<acc_sts></acc_sts>"
			+"<acc_use_type></acc_use_type>"
			+"<acc_card></acc_card>"
			+"<acc_desc></acc_desc>"
			+"<user_name>"+Constants.USER_NAME+"</user_name>"
			+"<startnum></startnum>"
			+"<pagenum></pagenum>"
			+"</request>"
			+"</message>"
			+"</ser:sumCountsDaily>"
			+"</soapenv:Body>"
			+"</soapenv:Envelope>";
		return soap;
	}
	
	private String monthSum(){
		Calendar cale = Calendar.getInstance();
		try{
			cale.setTime(sdf1.parse(sdf2.format(new Date())+"-01"));
		}catch(Exception e){
			Utils.LogErr(e.getMessage());
		}
		cale.add(Calendar.MONTH, 1);
		cale.add(Calendar.DAY_OF_MONTH, -1);
    	String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.acc.cqx.com/\">"
			+"<soapenv:Header/>"
			+"<soapenv:Body>"
			+"<ser:sumCountsDaily>"
			+"<message>"
			+"<header>"
			+"<requestname>"+Constants.USER_NAME+"</requestname>"
			+"<key>"+KEYUtils.getKEYByNameAndId(Constants.USER_NAME)+"</key>"
			+"<requesttime></requesttime>"
			+"<requestip></requestip>"
			+"</header>"
			+"<request>"
			+"<seq_id></seq_id>"
			+"<acc_use_time1>"+sdf2.format(new Date())+"-01"+"</acc_use_time1>"
			+"<acc_use_time2>"+sdf1.format(cale.getTime())+"</acc_use_time2>"
			+"<acc_type>2</acc_type>"
			+"<acc_value1></acc_value1>"
			+"<acc_value2></acc_value2>"
			+"<acc_sts></acc_sts>"
			+"<acc_use_type></acc_use_type>"
			+"<acc_card></acc_card>"
			+"<acc_desc></acc_desc>"
			+"<user_name>"+Constants.USER_NAME+"</user_name>"
			+"<startnum></startnum>"
			+"<pagenum></pagenum>"
			+"</request>"
			+"</message>"
			+"</ser:sumCountsDaily>"
			+"</soapenv:Body>"
			+"</soapenv:Envelope>";
		return soap;
	}
}
