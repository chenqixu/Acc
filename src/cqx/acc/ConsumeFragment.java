package cqx.acc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import cqx.acc.util.AccCountsDailyUtils;
import cqx.acc.util.Constants;
import cqx.acc.util.Utils;
import cqx.acc.util.bean.AccCountsDailyBean;
import cqx.acc.util.thread.ConsumeThread;

@SuppressLint({ "InflateParams", "HandlerLeak" }) public class ConsumeFragment extends Fragment implements OnClickListener{
	private Button button_out = null;
	private Button button_add = null;
	private Button button_flush = null;
	private Button button_fromto = null;
	private ProgressDialog mDialog = null;
	private ListView listView_detail= null;
	private TextView textView_month = null;
	private TextView textView_in = null;
	private TextView textView_out = null;
	private TextView textView_surplus = null;

    private Calendar cal = null;
    private int year = 2010,month = 1,day = 1;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 取消进度条
				mDialog.cancel();
				// 设置查询结果
				SimpleAdapter adapter = new SimpleAdapter(getActivity(), getData(), R.layout.vlist,
		                new String[]{"title","info","datevalue"},
		                new int[]{R.id.title,R.id.info,R.id.datevalue});
				listView_detail.setAdapter(adapter);
				String[] listSum = AccCountsDailyUtils.getInstance().getSum();
				textView_out.setText("支出："+listSum[1]);
				textView_in.setText("收入："+listSum[0]);
				textView_surplus.setText("盈余："+listSum[2]);
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_consume, null);
		// 支出
		button_out = (Button) view.findViewById(R.id.button_out);
		button_out.setOnClickListener(this);
		// 收入
		button_add = (Button) view.findViewById(R.id.button_add);
		button_add.setOnClickListener(this);
		// 刷卡
		button_flush = (Button) view.findViewById(R.id.button_flush);
		button_flush.setOnClickListener(this);
		// 转账
		button_fromto = (Button) view.findViewById(R.id.button_fromto);
		button_fromto.setOnClickListener(this);
		// 清单
		listView_detail = (ListView) view.findViewById(R.id.listView_detail);
		// 屏幕高宽
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		Utils.LogInfo("[screenWidth]"+screenWidth);
		Utils.LogInfo("[screenHeigh]"+screenHeigh);
		TableRow.LayoutParams lvd_lp = (TableRow.LayoutParams) listView_detail.getLayoutParams();
		Utils.LogInfo("[vp_lp.height]"+lvd_lp.height);
		switch (screenHeigh) {
			case 854:
				lvd_lp.height = 600;
				break;
			case 1280:
				lvd_lp.height = 800;
				break;
			case 1920:
				lvd_lp.height = 1200;
				break;
			default:
				break;
		}
		listView_detail.setLayoutParams(lvd_lp);
		// 红米2 1280x720 =800
		// 红米note2 1920x1080 =1200
		// 测试sdk 854x480 =600
		
		// 月份
		textView_month = (TextView) view.findViewById(R.id.textView_month);
		textView_month.setText(Utils.getNowDate("yyyy-MM"));
		getDate();
		textView_month.setOnClickListener(this);
		// 收入
		textView_in = (TextView) view.findViewById(R.id.textView_in);
		// 支出
		textView_out = (TextView) view.findViewById(R.id.textView_out);
		// 盈余
		textView_surplus = (TextView) view.findViewById(R.id.textView_surplus);

		// 查询
		query();
		
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		// 支出
		case R.id.button_out:
			if (getActivity() == null) {
				return;
			}
			if (getActivity() instanceof MainActivity) {
				MainActivity main_act = (MainActivity) getActivity();
				main_act.showAdd(2);
			}
			break;
		// 收入
		case R.id.button_add:
			if (getActivity() == null) {
				return;
			}
			if (getActivity() instanceof MainActivity) {
				MainActivity main_act = (MainActivity) getActivity();
				main_act.showAdd(1);
			}
			break;
		// 刷卡
		case R.id.button_flush:
			break;
		// 转账
		case R.id.button_fromto:
			break;
		// 时间条件
		case R.id.textView_month:
			// 后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
			DatePickerDialog dialog = new DatePickerDialog(getActivity(),listener,year,month,day);
			dialog.show();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 查询
	 * */
	private void query(){
		if (getActivity() != null && getActivity() instanceof MainActivity) {
			MainActivity main_act = (MainActivity) getActivity();
			// 发送请求报文
			mDialog = new ProgressDialog(main_act);
			mDialog.setTitle("查询");
			mDialog.setMessage("正在查询记录，请稍后...");
			mDialog.show();
			if(mHandler!=null){
				Thread consumeThread = new Thread(new ConsumeThread(mHandler, textView_month.getText().toString()));
				consumeThread.start();
			}
		}
	}
	
	/**
	 * 获得查询清单结果
	 * */
	private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
        for(AccCountsDailyBean bean : Constants.acccountsdailyList){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", bean.getAcc_type_desc()+" "+bean.getAcc_value()+" "+bean.getAcc_card_name());
            map.put("info", bean.getAcc_desc());
            map.put("datevalue", bean.getAcc_use_time());
            list.add(map);
        }         
        return list;
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
            textView_month.setText(year+"-"+(month+1));
            Utils.LogInfo(" onDateSet query...");
            // 查询
            query();
		}				
	};
}
