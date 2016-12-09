package cqx.acc;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import cqx.acc.util.Constants;
import cqx.acc.util.Utils;
import cqx.acc.util.bean.Acc_my_card;
import cqx.acc.util.bean.Acc_use_type;
import cqx.acc.util.bean.DealCountsDailyRequestBean;
import cqx.acc.util.thread.AddThread;

@SuppressLint("HandlerLeak") @SuppressWarnings("deprecation")
public class AddActivity extends Activity implements OnClickListener{
	private ViewPager viewPager = null;
	private ArrayList<View> pageview = null;
	private Button cancelBtn = null;
	private Button saveBtn = null;
	private EditText consume = null;
	private EditText accusetype = null;
	private EditText accmycard = null;
	private EditText content = null;
	
	private TextView dateShowDialog = null;
    private Calendar cal = null;
    private int year = 2010,month = 1,day = 1;
    
    private int acc_type = 2; // 默认支出
	
	private ProgressDialog mDialog = null;
	private ImageView imageView = null;
	private ImageView[] imageViews = null;
	// 包裹点点的LinearLayout
	private ViewGroup group = null;
	// 管理多个activity
	private LocalActivityManager manager = null;	

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mDialog.cancel();
				Toast.makeText(getApplicationContext(), "保存成功！",
						Toast.LENGTH_SHORT).show();
				// 展现主窗口，关闭自身
				showMain();
				break;
			case 1:
				mDialog.cancel();
				Toast.makeText(getApplicationContext(), "保存失败！",
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
		setContentView(R.layout.activity_add);
		// 获得参数，支出还是收入，得到跳转到该Activity的Intent对象，默认支出2
		Intent intent = getIntent();
		acc_type = intent.getIntExtra("cqx.acc.AddActivity.acc_type", 2);
		Utils.LogInfo(" [cqx.acc.AddActivity.acc_type]"+acc_type);
		// 动态activity管理
		manager = new LocalActivityManager(this , true);
        manager.dispatchCreate(savedInstanceState);
        // 获得当前时间
        getDate();
        // 初始化
		initView();
		// 广播过滤器注册
		IntentFilter usetypefilter = new IntentFilter(UseTypeActivity.action);
		registerReceiver(usetype_broadcastReceiver, usetypefilter);
		IntentFilter mycardfilter = new IntentFilter(MyCardActivity.action);
		registerReceiver(mycard_broadcastReceiver, mycardfilter);
		// 屏幕高宽
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		Utils.LogInfo("[screenWidth]"+screenWidth);
		Utils.LogInfo("[screenHeigh]"+screenHeigh);
		TableLayout.LayoutParams vp_lp = (TableLayout.LayoutParams) viewPager.getLayoutParams();
		Utils.LogInfo("[vp_lp.height]"+vp_lp.height);
		switch (screenHeigh) {
			case 854:
				vp_lp.height = 400;
				break;
			case 1280:
				vp_lp.height = 500;
				break;
			case 1920:
				vp_lp.height = 800;
				break;
			default:
				break;
		}
		viewPager.setLayoutParams(vp_lp);
		// 红米2 1280x720 =500
		// 红米note2 1920x1080 =800
		// 测试sdk 854x480 =400
	}
	
	/**
	 * usetype广播接收处理
	 * */
	BroadcastReceiver usetype_broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			setUseType(intent.getExtras().getString("usetype"));
		}		
	};
	
	/**
	 * mycard广播接收处理
	 * */
	BroadcastReceiver mycard_broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			setMyCard(intent.getExtras().getString("mycard"));
		}		
	};
	
	/**
	 * 销毁
	 * */
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(usetype_broadcastReceiver);
		unregisterReceiver(mycard_broadcastReceiver);
	}

	/**
	 * 初始化
	 * */
	public void initView() {
		// 组件初始化
		cancelBtn = (Button) findViewById(R.id.add_btn_cancel);
		saveBtn = (Button) findViewById(R.id.add_btn_save);
		cancelBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		accusetype = (EditText) findViewById(R.id.add_editText_type);
		accmycard = (EditText) findViewById(R.id.add_editText_mycard);		
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		group = (ViewGroup) findViewById(R.id.viewGroup);
		dateShowDialog = (TextView) findViewById(R.id.add_textView_showdate);
		dateShowDialog.setOnClickListener(this);
		dateShowDialog.setText(year+"-"+(month+1)+"-"+day);
		consume = (EditText) findViewById(R.id.add_editText_consume);
		content = (EditText) findViewById(R.id.add_editText_content);
		// 动态activity初始化
		View view1 = getView("activity01", new Intent(AddActivity.this, UseTypeActivity.class));
		View view2 = getView("activity02", new Intent(AddActivity.this, MyCardActivity.class));		
		// 将view装入数组
		pageview = new ArrayList<View>();
		pageview.add(view1);
		pageview.add(view2);
		// 有多少张图就有多少个点点
		imageViews = new ImageView[pageview.size()];
		for (int i = 0; i < pageview.size(); i++) {
			imageView = new ImageView(AddActivity.this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(20, 0, 20, 0);
			imageViews[i] = imageView;
			// 默认第一张图显示为选中状态
			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			group.addView(imageViews[i]);
		}
		// 绑定适配器
		viewPager.setAdapter(mPagerAdapter);
		// 绑定监听事件
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	/**
	 * 数据适配器
	 * */
	PagerAdapter mPagerAdapter = new PagerAdapter() {
		@Override
		// 获取当前窗体界面数
		public int getCount() {
			return pageview.size();
		}
		@Override
		// 判断是否由对象生成界面
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		// 是从ViewGroup中移出当前View
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageview.get(arg1));
		}
		// 返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageview.get(arg1));
			return pageview.get(arg1);
		}
	};

	/**
	 * pageView监听器
	 * */
	class GuidePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		// 如果切换了，就把当前的点点设置为选中背景，其他设置未选中背景
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.page_indicator_focused);
				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
				}
			}
		}
	}	

	/**
	 * 按钮点击事件
	 * */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_btn_cancel: // 取消按钮			
			// 展现主窗口，关闭自身
	    	showMain();
			break;
		case R.id.add_btn_save: // 保存按钮
			// 先判断界面填写是否完整
			DealCountsDailyRequestBean request = getValueToBean();
			if(isSave(request)){ // 内容完整
				// 发送请求报文
				mDialog = new ProgressDialog(AddActivity.this);
				mDialog.setTitle("保存");
				mDialog.setMessage("正在保存记录，请稍后...");
				mDialog.show();
				if(mHandler!=null){
					Thread addThread = new Thread(new AddThread(mHandler, request));
					addThread.start();
				}
			}else{ // 内容不完整
				Toast.makeText(getApplicationContext(), "内容不完整，请填写金额、分类、我的卡、备注！",
						Toast.LENGTH_SHORT).show();				
			}
			break;
		case R.id.add_textView_showdate: // 时间选择
			// 后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
			DatePickerDialog dialog = new DatePickerDialog(AddActivity.this,listener,year,month,day);
			dialog.show();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 通过activity获取视图
	 * */
    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }
    
    /**
     * 设置usetype内容
     * */
    public void setUseType(String text){
    	accusetype.setText(text);
    }
    
    /**
     * 设置mycard内容
     * */
    public void setMyCard(String text){
    	accmycard.setText(text);
    }
    
    /**
     * 收集界面内容，转换成javabean
     * */
    private DealCountsDailyRequestBean getValueToBean(){
    	DealCountsDailyRequestBean request = new DealCountsDailyRequestBean();
    	// 金额
    	request.setAcc_value1(consume.getText().toString());
    	// 分类
    	request.setAcc_use_type(Utils.getIDByName(accusetype.getText().toString()
    			, Constants.accusetypeList, Acc_use_type.class));
    	// 我的卡
    	request.setAcc_card(Utils.getIDByName(accmycard.getText().toString()
    			, Constants.accmycardList, Acc_my_card.class));
    	// 时间
    	request.setAcc_use_time1(dateShowDialog.getText().toString());
    	// 备注
    	request.setAcc_desc(content.getText().toString());
    	// 账目类型，默认支出【帐目类型(0:平衡1:收入,2:支出;3:透支)】
    	request.setAcc_type(String.valueOf(acc_type));
    	return request;
    }
    
    /**
     * 判断内容是否完整
     * */
    private boolean isSave(DealCountsDailyRequestBean request){
    	Utils.LogDebug("[cqx.acc.bean.acc_value]"+request.getAcc_value1());
    	Utils.LogDebug("[cqx.acc.bean.acc_use_type]"+request.getAcc_use_type());
    	Utils.LogDebug("[cqx.acc.bean.acc_card]"+request.getAcc_card());
    	Utils.LogDebug("[cqx.acc.bean.acc_desc]"+request.getAcc_desc());
    	// 金额判断
    	if(Utils.isEmpty(request.getAcc_value1()))return false;
    	// 分类选择判断
    	if(Utils.isEmpty(request.getAcc_use_type()))return false;
    	// 我的卡选择判断
    	if(Utils.isEmpty(request.getAcc_card()))return false;
    	// 备注判断
    	if(Utils.isEmpty(request.getAcc_desc()))return false;
    	return true;
    }
    
    /**
     * 展现主窗口
     * */
    public void showMain(){
    	Intent intent = new Intent();
    	intent.setClass(AddActivity.this, MainActivity.class);
    	startActivity(intent);
		finish();
    }
    
    /**
     * 获取当前日期
     * */
    private void getDate() {
    	cal = Calendar.getInstance();
    	year = cal.get(Calendar.YEAR); // 获取年月日时分秒
    	month = cal.get(Calendar.MONTH); // 获取到的月份是从0开始计数
    	day = cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 时间监听事件
     * */
	OnDateSetListener listener=new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int myyear,
				int monthOfYear, int dayOfMonth) {
			// 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year = myyear;
            month = monthOfYear;
            day = dayOfMonth;
			// 将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
			dateShowDialog.setText(year+"-"+(month+1)+"-"+day);
		}				
	};
}
