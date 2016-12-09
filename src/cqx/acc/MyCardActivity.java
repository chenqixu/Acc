package cqx.acc;

import cqx.acc.util.Constants;
import cqx.acc.util.MyViewGroup;
import cqx.acc.util.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//import android.view.ViewGroup;
import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
//import android.widget.TableLayout;
//import android.widget.TableRow;

public class MyCardActivity extends Activity implements OnClickListener{
	public static final String action = "accmycard.broadcast.action";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_card);
		initView();
	}
	
	/**
	 * 初始化
	 * */
	public void initView() {
		if(Constants.accmycardList!=null && Constants.accmycardList.size()>0){
			int listsize = Constants.accmycardList.size();
			
			MyViewGroup myViewGroup = (MyViewGroup) this.findViewById(R.id.myViewGroup_mycard);
			for(int i=0;i<listsize;i++){
				Button button = new Button(this);
				button.setId(2000+i);
				button.setText(Constants.accmycardList.get(i).getAcc_card_name());
				button.setOnClickListener(this);
				myViewGroup.addView(button);
			}
			
//			TableLayout tl = new TableLayout(this);
//			tl.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
//						,ViewGroup.LayoutParams.WRAP_CONTENT));
//			tl.setOrientation(TableLayout.HORIZONTAL);
//			Button btn[] = new Button[listsize];
//			// 每行5个按钮，计算需要几行
//			int line_count = (listsize-1)/Constants.LINE_SIZE+1;
//			int all_count = 0;
//			for(int i=0;i<line_count;i++){
//				TableRow tr = new TableRow(this);
//				tr.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
//						,ViewGroup.LayoutParams.WRAP_CONTENT));
//				tr.setOrientation(TableRow.VERTICAL);
//				while(all_count<listsize){
//					btn[all_count] = new Button(this);
//					btn[all_count].setId(2000+all_count);
//					btn[all_count].setText(Constants.accmycardList.get(all_count).getAcc_card_name());
//					
//					tr.addView(btn[all_count]);
//
//					all_count++;
//					if(all_count>0 && all_count%Constants.LINE_SIZE==0)break;
//				}
//				tl.addView(tr);
//			}
//			this.setContentView(tl);
		}		
	}

	@Override
	public void onClick(View v) {
		if(v.getId()>=2000 || v.getId()<(2000+Constants.accmycardList.size())){
			Utils.LogDebug("[cqx.acc.id]"+v.getId()+
					"[cqx.acc.id.text]"+((Button) findViewById(v.getId())).getText());
			Intent intent = new Intent(action);
			intent.putExtra("mycard", ((Button) findViewById(v.getId())).getText());
			sendBroadcast(intent);
		}
	}
}
