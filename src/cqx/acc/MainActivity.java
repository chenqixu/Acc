package cqx.acc;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import cqx.acc.util.Constants;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends SlidingFragmentActivity implements OnClickListener{
	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;
	private SharedPreferences sp = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initSlidingMenu(savedInstanceState);
		initView();
	}
	
	/**
	 * 初始化
	 * */
    public void initView(){
		topButton = (ImageView) findViewById(R.id.topButton);
		topButton.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
		topTextView.setText("欢迎，"+Constants.USER_NAME+"登录！");
		sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }
    

	/**
	 * 初始化侧边栏
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}

		if (mContent == null) {
			mContent = new ConsumeFragment();
		}
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.content_frame, mContent).commit();
		getSlidingMenu().showContent();

		// 设置左侧滑动菜单
		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commit();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topButton:
			toggle();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 登出
	 * */
	public void logoExit(){
		sp.edit().putBoolean("ISSAVE", false).commit();
		Intent intent = new Intent();
		intent.setClass(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
    
    /**
     * 展现新增窗口
     * */
    public void showAdd(int acc_type){
    	Intent intent = new Intent();
    	intent.setClass(this, AddActivity.class);
    	intent.putExtra("cqx.acc.AddActivity.acc_type", acc_type);
    	startActivity(intent);
		finish();
    }
	
}
