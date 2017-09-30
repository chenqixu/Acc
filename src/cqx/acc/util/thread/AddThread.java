package cqx.acc.util.thread;

import cqx.acc.util.CallWebService;
import cqx.acc.util.Constants;
import cqx.acc.util.KEYUtils;
import cqx.acc.util.ResultXML;
import cqx.acc.util.Utils;
import cqx.acc.util.XMLData;
import cqx.acc.util.bean.DealCountsDailyRequestBean;
import android.os.Handler;
import android.os.Message;

public class AddThread implements Runnable{
	private Handler mHandler;
	private Message msg; // what: 0 success 1 error
	private DealCountsDailyRequestBean requestBean;
	
	public AddThread(Handler _mHandler, DealCountsDailyRequestBean _requestBean){
		this.mHandler = _mHandler;
		this.requestBean = _requestBean;
		this.msg = this.mHandler.obtainMessage();
	}
	
	/**
	 * 执行保存动作
	 * */
	@Override
	public void run() {
		if(requestAdd()){
			this.msg.what = 0;
		}else{
			this.msg.what = 1;		
		}
		this.mHandler.sendMessage(this.msg);
	}
	
	/**
	 * 保存请求
	 * */
	private boolean requestAdd() {
    	try {
	    	String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.acc.cqx.com/\">"
	    			+"<soapenv:Header/>"
	    			+"<soapenv:Body>"
	    			+"<ser:insertCountsDaily>"
	    			+"<message>"
	    			+"<header>"
	    			+"<requestname>"+Constants.USER_NAME+"</requestname>"
	    			+"<key>"+KEYUtils.getKEYByNameAndId(Constants.USER_NAME)+"</key>"
	    			+"<requesttime></requesttime>"
	    			+"<requestip></requestip>"
	    			+"</header>"
	    			+"<request>"
	    			+"<seq_id></seq_id>"
	    			+"<acc_use_time1>"+requestBean.getAcc_use_time1()+"</acc_use_time1>"
	    			+"<acc_use_time2></acc_use_time2>"
	    			+"<acc_type>"+requestBean.getAcc_type()+"</acc_type>"
	    			+"<acc_value1>"+requestBean.getAcc_value1()+"</acc_value1>"
	    			+"<acc_value2></acc_value2>"
	    			+"<acc_sts>1</acc_sts>"
	    			+"<acc_use_type>"+requestBean.getAcc_use_type()+"</acc_use_type>"
	    			+"<acc_card>"+requestBean.getAcc_card()+"</acc_card>"
	    			+"<acc_desc>"+Utils.nvl(Utils.encode(requestBean.getAcc_desc()))+"</acc_desc>"
	    			+"<user_name>"+Constants.USER_NAME+"</user_name>"
	    			+"<startnum></startnum>"
	    			+"<pagenum></pagenum>"
	    			+"</request>"
	    			+"</message>"
	    			+"</ser:insertCountsDaily>"
	    			+"</soapenv:Body>"
	    			+"</soapenv:Envelope>";
	    	Utils.LogDebug("[cqx.acc.requestAdd.soap]"+soap);
	    	byte[] data = soap.getBytes();
	    	String sUrl = Constants.dealcountsdailyserviceURL;
	    	CallWebService cws = new CallWebService();
	    	String resultxml = cws.doAction(sUrl, data);
	        if (resultxml.length()>0) {
	            // 解析返回信息
	        	Utils.LogDebug("[cqx.acc.requestAdd.resultxml]"+resultxml);
	        	ResultXML rx = new ResultXML();
	    		StringBuffer xml = new StringBuffer();
	    		xml.append("<?xml version=\"1.0\"  encoding='UTF-8'?>");
	    		xml.append(resultxml);
	    		XMLData xd = new XMLData(xml.toString());
	    		rx.rtFlag = true;
	    		rx.bXmldata = true;
	    		rx.xmldata = xd;
	    		rx.setbFlag(false);		
	    		rx.resetParent().node("Body").node("insertCountsDailyResponse").node("message").setParentPointer();
	    		rx.setRowFlagInfo("response");
	    		rx.First();
	            if (rx.isEof()) { // 没有结果
	            	Utils.LogInfo("[cqx.acc.requestAdd.resultxml]no result");
	            } else { // 有结果
	            	String resultcode = rx.getRowValue();
	            	if (resultcode.equals("1")) { // 增加记录成功
	            		return true;
	            	}
	            }
	        }
    	} catch (Exception e) {
			Utils.LogErr(e.getMessage());
    	}		
		return false;
	}

}
