package cqx.acc.util.thread.intf;

import cqx.acc.util.bean.CallServerBean;
import android.os.Handler;
import android.os.Message;

/**
 * <b>抽象的公共类</b><br>
 * <b>通用方法</b>=调用公共方法，判断类型，并进行重试操作<br>
 * <b>公共方法</b>=自身业务实现，公共方法一律返回int（即接口返回值），自身其他返回值设置到自身对象中
 * */
public abstract class AbstractCallServerThread {
	// 执行计数器
	protected int exec_cnt = 0;
	protected Handler mHandler;
	// 默认-1(网络失败等其他原因)，正常0，鉴权失败1
	protected Message msg;
	/**
	 * 真正调用服务，需要重写，返回值是状态
	 * */
	protected abstract CallServerBean callService();

	/**
	 * 调用callService，处理业务逻辑，包括失败重调
	 * 网络及其他原因需要重试3次，其他不重做
	 * */
	public void exec() {
		// 返回标志
		int flag=-1; // -1错误；0正常；1鉴权失败
		CallServerBean csb = callService(); // 服务返回结果
		flag = csb.getStatus(); // 服务状态
		// 网络及其他原因需要重试3次，其他不重做
		if (flag==-1) {
			// 判断网络及其他原因重做次数，没到达就继续
			if (exec_cnt<=2) {
				exec_cnt++;
				// 重做
				exec();
			} else { // 网络及其他原因重做次数，到达上限，返回
				this.msg.what = csb.getBusiness_code();
				this.mHandler.sendMessage(this.msg);
			}
		} else { // 非网络及其他原因，返回
			this.msg.what = csb.getBusiness_code();
			this.mHandler.sendMessage(this.msg);
		}
	}
}
