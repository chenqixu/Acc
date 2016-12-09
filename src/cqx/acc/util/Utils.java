package cqx.acc.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cqx.acc.util.bean.Acc_my_card;
import cqx.acc.util.bean.Acc_use_type;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class Utils {

	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getIDByName(String NAME, List<?> list, Class c){
		String result = "";
		if(c==Acc_use_type.class){
			for(Object aut : list){
				if(NAME.equals(((Acc_use_type)aut).getAcc_use_name())){
					result = ((Acc_use_type)aut).getAcc_use_type();
					break;
				}
			}
		}else if(c==Acc_my_card.class){
			for(Object aut : list){
				if(NAME.equals(((Acc_my_card)aut).getAcc_card_name())){
					result = ((Acc_my_card)aut).getAcc_my_card();
					break;
				}
			}
		}
		return result;
	}
	
	public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0;
	}
	
	public static String nvl(String str){
		String result = str;
		if(isEmpty(str)){
			result = "";
		}
		return result;
	}
	
	public static String encode(String str){
		try {
			return java.net.URLEncoder.encode(str, Constants.ENCODE);
		} catch (UnsupportedEncodingException e) {
			LogErr(e.getMessage());
		}
		return str;
	}
	
	/**
	 * 按照指定格式获得当前时间
	 * */
	@SuppressLint("SimpleDateFormat") public static String getNowDate(String formatStr){
		String result = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			result = sdf.format(new Date());
		}catch(Exception e){
			LogErr(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 获得当前月份最后一天
	 * */
	@SuppressLint("SimpleDateFormat") public static String getMonthLastday(String month){
		SimpleDateFormat df = new SimpleDateFormat(Constants.NOWDATE_FORMAT2);
		SimpleDateFormat sdf3 = new SimpleDateFormat(Constants.NOWDATE_FORMAT3);
		Calendar cale = Calendar.getInstance();
		try {
			cale.setTime(df.parse(month+"-01"));
			cale.add(Calendar.MONTH, 1);
			cale.add(Calendar.DAY_OF_MONTH, -1);
		} catch (Exception e) {
			LogErr(e.getMessage());
		}
		return sdf3.format(cale.getTime());
	}
	
	public static void LogInfo(String str){
		if(Constants.ISINFO){
			System.out.println("[cqx.acc.info]"+str);
		}
	}

	public static void LogDebug(String str){
		if(Constants.ISDEBUG){
			System.out.println("[cqx.acc.debug]"+str);
		}
	}

	public static void LogErr(String str){
		if(Constants.ISERR){
			System.out.println("[cqx.acc.err]"+str);
		}
	}	
}
