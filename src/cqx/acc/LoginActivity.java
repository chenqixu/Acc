package cqx.acc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import cqx.acc.util.Constants;
import cqx.acc.util.Utils;
import cqx.acc.util.thread.LoginThread;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity implements OnClickListener,OnCheckedChangeListener {
	private Button mBtnLogin = null;
	private ProgressDialog mDialog = null;
	private SharedPreferences sp = null;
	private EditText accounts = null;
	private EditText password = null;
	private CheckBox auto_save = null;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mDialog.cancel();
				Toast.makeText(getApplicationContext(), "登录成功！",
						Toast.LENGTH_SHORT).show();
				// 保存用户到静态变量
				Constants.USER_NAME = accounts.getText().toString();
				// 登录成功和记住密码框为选中状态才保存用户信息
				if(auto_save.isChecked()){
					// 记住用户名、密码
					Editor editor = sp.edit();
					editor.putString("USER_NAME", accounts.getText().toString());
					editor.putString("PASSWORD", password.getText().toString());
					editor.commit();
				}
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case 1:
				mDialog.cancel();
				Toast.makeText(getApplicationContext(), "用户密码错误",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}

	/**
	 * 初始化
	 * */
	public void initView() {
		mBtnLogin = (Button) findViewById(R.id.login);
		mBtnLogin.setOnClickListener(this);
		accounts = (EditText) findViewById(R.id.accounts);
		password = (EditText) findViewById(R.id.password);
		auto_save = (CheckBox) findViewById(R.id.auto_save_password);
		auto_save.setOnCheckedChangeListener(this);
		sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		// 判断是否记住密码
		if(sp.getBoolean("ISSAVE", false)){
			// 设置默认是记录密码状态
			auto_save.setChecked(true);
			// 读取用户和密码设置到界面
			accounts.setText(sp.getString("USER_NAME", ""));
			password.setText(sp.getString("PASSWORD", ""));
			// 自动登录
			loginEvent();
		}
	}

	/**
	 * 传参数进登陆线程进行验证，验证后发消息给handler<br>
	 * handler再通知界面
	 * */
	public void loginEvent() {
		String _username = accounts.getText().toString();
		String _password = password.getText().toString();
		if(isOK(_username, _password)){ // 验证是否有输入用户密码
			mDialog = new ProgressDialog(LoginActivity.this);
			mDialog.setTitle("登陆");
			mDialog.setMessage("正在登陆服务器，请稍后...");
			mDialog.show();
			if(mHandler!=null){
				Thread loginThread = new Thread(new LoginThread(mHandler, _username, _password));
				loginThread.start();			
			}
		}else{ // 没有输入用户密码
			Toast.makeText(getApplicationContext(), "请输入用户密码！",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 登录按钮点击事件
	 * */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			loginEvent();
			break;
		default:
			break;
		}
	}

	/**
	 * 记住密码按钮事件
	 * */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(auto_save.isChecked()){
			sp.edit().putBoolean("ISSAVE", true).commit();
		}else{
			sp.edit().putBoolean("ISSAVE", false).commit();
		}
	}
	
	/**
	 * 登陆验证，是否有输入用户密码
	 * */
	private boolean isOK(String usename, String password){
    	// 我的卡选择判断
    	if(Utils.isEmpty(usename))return false;
    	// 备注判断
    	if(Utils.isEmpty(password))return false;
    	return true;
	}
	
}
