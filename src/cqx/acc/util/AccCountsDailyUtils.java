package cqx.acc.util;

import cqx.acc.util.bean.AccCountsDailyBean;

/**
 * 收入、支出、盈余统计
 * 2017-10-01增加信用卡统计，支出需要扣除信用卡
 * 2017-10-01储值卡只有缴费是支出，刷卡不算
 * */
public class AccCountsDailyUtils {
	private static AccCountsDailyUtils accCountsDailyUtils = new AccCountsDailyUtils();
	private AccCountsDailyUtils(){		
	}
	public static AccCountsDailyUtils getInstance(){
		if(accCountsDailyUtils==null)accCountsDailyUtils = new AccCountsDailyUtils();
		return accCountsDailyUtils;
	}
	/**
	 * 结果：收入、支出、盈余、信用卡
	 * */
	public String[] getSum(){
		String[] result = {"0", "0", "0","0"};
		double in = 0;
		double out = 0;
		double credit_out = 0;
		if(Constants.acccountsdailyList!=null && Constants.acccountsdailyList.size()>0){
			for(AccCountsDailyBean bean : Constants.acccountsdailyList){				
				switch (bean.getAcc_type()) {
				case 1: // 收入
					in += Double.valueOf(bean.getAcc_value());
					break;
				case 2: // 支出
					// 信用卡支出
					if (bean.getAcc_card_name().contains(Constants.CREDIT_CARD))
						credit_out += Double.valueOf(bean.getAcc_value());
					// 储值卡 且 不缴费，不算支出
					else if (bean.getAcc_card_name().contains(Constants.STORED_VALUE_CARD)
							&& !bean.getAcc_use_name().contains(Constants.PAY))
						break;
					// 其他都算支出
					else
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
		result[3] = String.valueOf(Constants.NUMBER_FORMAT.format(credit_out));
		return result;
	}
}
