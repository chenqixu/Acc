package cqx.acc.util.thread;

import android.os.Handler;
import android.os.Message;
import cqx.acc.util.AccmycardUtils;
import cqx.acc.util.AccusetypeUtils;
import cqx.acc.util.CallWebService;
import cqx.acc.util.Constants;
import cqx.acc.util.KEYUtils;
import cqx.acc.util.ResultXML;
import cqx.acc.util.Utils;
import cqx.acc.util.XMLData;

/**
 * 登陆线程
 * */
public class LoginThread implements Runnable{
	private Handler mHandler;
	private Message msg; // what: 0 success 1 error
	private String username;
	private String password;
	public LoginThread(Handler _mHandler, String _username, String _password){
		this.mHandler = _mHandler;
		this.username = _username;
		this.password = _password;
		this.msg = this.mHandler.obtainMessage();
	}
	
	/**
	 * 具体登陆动作
	 * */
	@Override
	public void run() {
		if(requestLogin()){
			this.msg.what = 0;
		}else{
			this.msg.what = 1;		
		}
		this.mHandler.sendMessage(this.msg);
	}
	
	/**
	 * 登陆请求
	 * */
	private boolean requestLogin(){
    	try{
	    	String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.acc.cqx.com/\"><soapenv:Header/><soapenv:Body><ser:LoginCheck><message><header>"
	    			+"<requestname>"+this.username+"</requestname>"
	    			+"<key>"+KEYUtils.getKEYByNameAndId(this.username)+"</key>"
	    			+"<requesttime></requesttime><requestip></requestip></header><request>"
	    			+"<username>"+this.username+"</username>"
	    			// 对密码进行md5加密
	    			+"<password>"+KEYUtils.stringToMD5(this.password)+"</password>"
	    			+"</request></message></ser:LoginCheck></soapenv:Body></soapenv:Envelope>";
        	Utils.LogDebug("[cqx.acc.requestLogin.soap]"+soap);
	    	byte[] data = soap.getBytes();
	    	String sUrl = Constants.loginserverURL;
	    	CallWebService cws = new CallWebService();
	    	String resultxml = cws.doAction(sUrl, data);
	        if (resultxml.length()>0){
	            // 解析返回信息
	        	Utils.LogDebug("[cqx.acc.requestLogin.resultxml]"+resultxml);
	        	ResultXML rx = new ResultXML();
	    		StringBuffer xml = new StringBuffer();
	    		xml.append("<?xml version=\"1.0\"  encoding='UTF-8'?>");
	    		xml.append(resultxml);
	    		XMLData xd = new XMLData(xml.toString());
	    		rx.rtFlag = true;
	    		rx.bXmldata = true;
	    		rx.xmldata = xd;
	    		rx.setbFlag(false);		
	    		rx.resetParent().node("Body").node("LoginCheckResponse").node("message").setParentPointer();
	    		rx.setRowFlagInfo("response");
	    		rx.First();
	            if(rx.isEof()){// 没有结果
	            	Utils.LogInfo("[cqx.acc.requestLogin.resultxml]no result");
	            }else{// 有结果
	            	String logincode = rx.getRowValue();
	            	if(logincode.equals("1")){// 验证密码正确
	            		// 查询分类列表
	            		Constants.accusetypeList = AccusetypeUtils.getInstance().queryTypeListByUsername(this.username);
	            		// 查询我的卡列表
	            		Constants.accmycardList = AccmycardUtils.getInstance().queryCradListByUsername(this.username);
	    	        	return true;
	            	}
	            }
	        }
    	}catch(Exception e){
			Utils.LogErr(e.getMessage());
    	}
    	return false;
	}
}
