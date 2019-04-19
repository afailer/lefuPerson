package com.lefuyun.bean;

import java.io.Serializable;

public class OldPeople implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id; // 老人ID
	private long agency_id; // 所属机构ID
	private String agency_name; // 所属机构名称
	private String elderly_name; // 姓名
	private int gender; // 性别      14为男,15为女
	private int age; // 年龄
	private long birthday; // 生日
	private String account; // 户口性质: 如城镇户口
	private String document_number; // 身份证号码
	private String icon; // 头像uri
	private String mobile; // 手机号
	private String status; // 当前老人与用户的关联状态         0 为审核中
	private long mapId; // 老人mapId 用于与老人解除绑定时使用
	
	private boolean select; // 当前是否被选中, 在解除绑定中使用
	// 老人综合评估字段
	private long assessId; // 当前评估ID
	private int score; // 老人当前评分   优秀>=95-<=100、良好>=80-<95、一般>=60-<80、较差 <60
	private int signData; // 健康状况
	private int dailyLife; // 日常生活
	private int dailyNurs; // 护理服务
	private int drinkMeal; // 饮食情况
	private String config;//老人的体征数据的配置
	// 天气的相关字段
	private String tem; // 最低气温和最高气温
	private String img; // 天气图片
	private int code; //
	private String txt; // 天气状况;比如 多云
	private String city; // 老人所在的城市
	
	public OldPeople(){
		
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}
	public String getAgency_name() {
		return agency_name;
	}
	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}
	public String getElderly_name() {
		return elderly_name;
	}
	public void setElderly_name(String elderly_name) {
		this.elderly_name = elderly_name;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getBirthday() {
		return birthday;
	}
	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getDocument_number() {
		return document_number;
	}
	public void setDocument_number(String document_number) {
		this.document_number = document_number;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getMapId() {
		return mapId;
	}
	public void setMapId(long mapId) {
		this.mapId = mapId;
	}
	public boolean isSelect() {
		return select;
	}
	public void setSelect(boolean select) {
		this.select = select;
	}
	public long getAssessId() {
		return assessId;
	}
	public void setAssessId(long assessId) {
		this.assessId = assessId;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getSignData() {
		return signData;
	}
	public void setSignData(int signData) {
		this.signData = signData;
	}
	public int getDailyLife() {
		return dailyLife;
	}
	public void setDailyLife(int dailyLife) {
		this.dailyLife = dailyLife;
	}
	public int getDailyNurs() {
		return dailyNurs;
	}
	public void setDailyNurs(int dailyNurs) {
		this.dailyNurs = dailyNurs;
	}
	public int getDrinkMeal() {
		return drinkMeal;
	}
	public void setDrinkMeal(int drinkMeal) {
		this.drinkMeal = drinkMeal;
	}
	public String getTem() {
		return tem;
	}
	public void setTem(String tem) {
		this.tem = tem;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public String toString() {
		return "OldPeople [id=" + id + ", agency_id=" + agency_id
				+ ", agency_name=" + agency_name + ", elderly_name="
				+ elderly_name + ", gender=" + gender + ", age=" + age
				+ ", birthday=" + birthday + ", account=" + account
				+ ", document_number=" + document_number + ", icon=" + icon
				+ ", mobile=" + mobile + ", status=" + status + ", mapId="
				+ mapId + ", select=" + select + ", assessId=" + assessId
				+ ", score=" + score + ", signData=" + signData
				+ ", dailyLife=" + dailyLife + ", dailyNurs=" + dailyNurs
				+ ", drinkMeal=" + drinkMeal + ", config=" + config + ", tem="
				+ tem + ", img=" + img + ", code=" + code + ", txt=" + txt
				+ ", city=" + city + "]";
	}
}
