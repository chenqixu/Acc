package cqx.acc.util;

public class MessageHelper {
	/**
	 * 鉴权失败及未知原因需要重试
	 * */
	public static boolean isStatusOk(ResultXML rx, String nodeName){
		boolean flag = false;
		String reasonStr = ""; // 原因
		rx.resetParent().node("Body").node(nodeName).node("message").setParentPointer();
		rx.setRowFlagInfo("header");
		rx.First();
		if(rx.isEof()){
			reasonStr = "no header";
		}else{
			String status = rx.getColumnsValue("status");
			Utils.LogDebug("["+nodeName+".status]"+status);
			if(status.equals("")){
				reasonStr = "status error";
			}else if(status.equals("-1")){
				reasonStr = "[error]"+rx.getColumnsValue("desc");
				flag = true;
			}else if(status.equals("0")){
				reasonStr = "sucess";
				flag = true;
			}else if(status.equals("1")){
				reasonStr = "[authority fail]"+rx.getColumnsValue("desc");
			}
		}
		return flag;
	}
}
