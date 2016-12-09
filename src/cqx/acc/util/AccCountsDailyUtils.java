package cqx.acc.util;

import cqx.acc.util.bean.AccCountsDailyBean;

public class AccCountsDailyUtils {
	private static AccCountsDailyUtils accCountsDailyUtils = new AccCountsDailyUtils();
	private AccCountsDailyUtils(){		
	}
	public static AccCountsDailyUtils getInstance(){
		if(accCountsDailyUtils==null)accCountsDailyUtils = new AccCountsDailyUtils();
		return accCountsDailyUtils;
	}
	public String[] getSum(){
		String[] result = {"0", "0", "0"};
		double in = 0;
		double out = 0;
		if(Constants.acccountsdailyList!=null && Constants.acccountsdailyList.size()>0){
			for(AccCountsDailyBean bean : Constants.acccountsdailyList){				
				switch (bean.getAcc_type()) {
				case 1: // 收入
					in += Double.valueOf(bean.getAcc_value());
					break;
				case 2: // 支出
					out += Double.valueOf(bean.getAcc_value());
					break;
				default:
					break;
				}
			}
		}
		result[0] = String.valueOf(Constants.NUMBER_FORMAT.format(in));
		result[1] = String.valueOf(Constants.NUMBER_FORMAT.format(out));
		result[2] = String.valueOf(Constants.NUMBER_FORMAT.format(in-out));
		return result;
	}
}
