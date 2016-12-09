package cqx.acc.util.bean;

import org.json.JSONObject;
import org.json.JSONTokener;

import cqx.acc.util.Utils;

public class Acc_use_type extends AccAllBean{
	private String acc_use_type;
	private String acc_use_name;
	private String acc_use_desc;
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
	public String getAcc_use_type() {
		return acc_use_type;
	}
	public void setAcc_use_type(String acc_use_type) {
		this.acc_use_type = acc_use_type;
	}
	public String getAcc_use_name() {
		return acc_use_name;
	}
	public void setAcc_use_name(String acc_use_name) {
		this.acc_use_name = acc_use_name;
	}
	public String getAcc_use_desc() {
		return acc_use_desc;
	}
	public void setAcc_use_desc(String acc_use_desc) {
		this.acc_use_desc = acc_use_desc;
	}
	public Acc_use_type jsonToBean(String jsonStr){
//		JSONObject jsobj = JSONObject.fromObject(jsonStr);
//		return (Acc_use_type)JSONObject.toBean(jsobj, Acc_use_type.class);
		Acc_use_type result = new Acc_use_type();
		try{
			JSONTokener jsonParser = new JSONTokener(jsonStr);
			JSONObject accusetype = (JSONObject) jsonParser.nextValue();
			result.setAcc_use_type(accusetype.getString("acc_use_type"));
			result.setAcc_use_name(accusetype.getString("acc_use_name"));
			result.setAcc_use_desc(accusetype.getString("acc_use_desc"));
		}catch(Exception e){
			Utils.LogErr(e.getMessage());
		}
		return result;
	}
}
