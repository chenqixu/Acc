package cqx.acc.util.bean;

import org.json.JSONObject;
import org.json.JSONTokener;

import cqx.acc.util.Utils;

public class Acc_my_card extends AccAllBean{
	private String acc_my_card;
	private String acc_card_name;
	private String acc_card_desc;
	private String user_name;	
	private String acc_sts;
	public String getAcc_sts() {
		return acc_sts;
	}
	public void setAcc_sts(String acc_sts) {
		this.acc_sts = acc_sts;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getAcc_my_card() {
		return acc_my_card;
	}
	public void setAcc_my_card(String acc_my_card) {
		this.acc_my_card = acc_my_card;
	}
	public String getAcc_card_name() {
		return acc_card_name;
	}
	public void setAcc_card_name(String acc_card_name) {
		this.acc_card_name = acc_card_name;
	}
	public String getAcc_card_desc() {
		return acc_card_desc;
	}
	public void setAcc_card_desc(String acc_card_desc) {
		this.acc_card_desc = acc_card_desc;
	}
	public Acc_my_card jsonToBean(String jsonStr){
//		JSONObject jsobj = JSONObject.fromObject(jsonStr);
//		return (Acc_my_card)JSONObject.toBean(jsobj, Acc_my_card.class);
		Acc_my_card result = new Acc_my_card();
		try{
			JSONTokener jsonParser = new JSONTokener(jsonStr);
			JSONObject accusetype = (JSONObject) jsonParser.nextValue();
			result.setAcc_my_card(accusetype.getString("acc_my_card"));
			result.setAcc_card_name(accusetype.getString("acc_card_name"));
			result.setAcc_card_desc(accusetype.getString("acc_card_desc"));
		}catch(Exception e){
			Utils.LogErr(e.getMessage());
		}
		return result;
	}
}
