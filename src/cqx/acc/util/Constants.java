package cqx.acc.util;

import java.text.DecimalFormat;
import java.util.List;

import cqx.acc.util.bean.AccCountsDailyBean;
import cqx.acc.util.bean.Acc_my_card;
import cqx.acc.util.bean.Acc_use_type;

public class Constants {
	public static String USER_NAME = "";
	public static String CONSUME_D = "";
	public static String CONSUME_M = "";
	public static List<Acc_use_type> accusetypeList = null;
	public static List<Acc_my_card> accmycardList = null;
	public static List<AccCountsDailyBean> acccountsdailyList = null;
	public static final String ENCODE = "UTF-8";
	public static final int LINE_SIZE = 5;
	public static final boolean ISINFO = false;
	public static final boolean ISDEBUG = false;
	public static final boolean ISERR = false;
	
	// 关键字
	public static final String CREDIT_CARD = "信用卡";
	public static final String STORED_VALUE_CARD = "储值卡";
	public static final String PAY = "缴费";
	
	// DATE FORMAT
	public static final String NOWDATE_FORMAT2 = "yyyy-MM";
	public static final String NOWDATE_FORMAT3 = "yyyy-MM-dd";
	
	// NUMBER FORMAT
	public static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#0.00");
	
	// 10.0.2.2:8080
	// 192.168.1.10:8080
	// accfamily.duapp.com
	public static final String urlandport = "http://accfamily.duapp.com/ProjectAccSvc/";
	public static final String loginserverURL = urlandport+"LoginService";
	public static final String jsonserverURL = urlandport+"JsonService";
	public static final String dealcountsdailyserviceURL = urlandport+"DealCountsDailyService";
}
