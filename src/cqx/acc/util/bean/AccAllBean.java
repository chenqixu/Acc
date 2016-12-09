package cqx.acc.util.bean;

public class AccAllBean {
	protected String startnum = "0"; // 默认从0开始查询
	protected String pagenum = "-1"; // 默认-1查询全部
	public String getStartnum() {
		return startnum;
	}
	public void setStartnum(String startnum) {
		this.startnum = startnum;
	}
	public String getPagenum() {
		return pagenum;
	}
	public void setPagenum(String pagenum) {
		this.pagenum = pagenum;
	}
}
