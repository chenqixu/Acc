package cqx.acc;

import cqx.acc.util.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

@SuppressLint("InflateParams") public class LeftFragment extends Fragment implements OnClickListener{
	private View exit_menu;
	private TextView usename_menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		exit_menu = view.findViewById(R.id.exit_menu);
		exit_menu.setOnClickListener(this);
		usename_menu = (TextView) view.findViewById(R.id.usename_menu);
		usename_menu.setText(Constants.USER_NAME);
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
		switch (v.getId()) {
		case R.id.exit_menu: // 退出
			if (getActivity() == null) {
				return;
			}
			if (getActivity() instanceof MainActivity) {
				MainActivity main_act = (MainActivity) getActivity();
				main_act.logoExit();
			}
			break;
		default:
			break;
		}
	}

}
