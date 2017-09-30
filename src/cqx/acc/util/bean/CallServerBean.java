package cqx.acc.util.bean;

public class CallServerBean {
	private int status = -1; // 服务状态 -1错误；0正常；1鉴权失败
	private int business_code = -1; // 业务结果 1成功；0失败；-1其他异常
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getBusiness_code() {
		return business_code;
	}
	public void setBusiness_code(int business_code) {
		this.business_code = business_code;
	}
}
