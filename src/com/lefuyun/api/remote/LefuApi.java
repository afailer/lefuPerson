package com.lefuyun.api.remote;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.map.MapStatus;
import com.lefuyun.AppContext;
import com.lefuyun.api.ApiOkHttp;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.common.UserInfo;
import com.lefuyun.api.http.request.FileApiRequest;
import com.lefuyun.api.http.request.JsonApiRequest;
import com.lefuyun.api.http.request.UploadApiRequest;
import com.lefuyun.bean.City;
import com.lefuyun.bean.ContentTotal;
import com.lefuyun.bean.DailyLifeRecord;
import com.lefuyun.bean.HelpInfo;
import com.lefuyun.bean.ImportantMsg;
import com.lefuyun.bean.News;
import com.lefuyun.bean.NurseMedia;
import com.lefuyun.bean.NurseMediaSum;
import com.lefuyun.bean.NurseRecord;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.OldPeopleAssess;
import com.lefuyun.bean.OlderEgress;
import com.lefuyun.bean.OrgActive;
import com.lefuyun.bean.Organization;
import com.lefuyun.bean.Portrait;
import com.lefuyun.bean.Reserve;
import com.lefuyun.bean.SignDataBean;
import com.lefuyun.bean.Tourism;
import com.lefuyun.bean.TourismReserve;
import com.lefuyun.bean.User;
import com.lefuyun.bean.Version;
import com.lefuyun.bean.Weather;
import com.lefuyun.bean.WeekFood;
import com.lefuyun.bean.WeekRecipeBean;
import com.lefuyun.ui.ShareActivity;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.SPUtils;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.Utils;

public class LefuApi {
    public static final String IMG_URL ="http://www.lefukj.cn"; 
    private static String API_URL = "http://www.lefukj.cn/%s";
    public static final String nurseCareUrl=IMG_URL+"/lefuyun/dailyNursingRecordCtr/toInfoPage";
    public static final String LEFU_IMG_URL = "/lefuyun/resource/images/lefuyun.png";
    
    public static final String GOOD_IMG_URL = "/lefuyun/resource/images/score-good.png"; // 良好
    public static final String BAD_IMG_URL = "/lefuyun/resource/images/score-bad.png"; // 较差
    public static final String AVERAGE_IMG_URL = "/lefuyun/resource/images/score-average.png"; // 一般
    public static final String EXCELLENT_IMG_URL = "/lefuyun/resource/images/score-excellent.png"; // 优秀
	/**
	 * 登录
	 * @param mobile 用户手机号
	 * @param password 密码
	 * @param requestCallback
	 */
	public static void login(String mobile, String password, RequestCallback<User> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/userInfoCtr/queryUserInfo");
		JsonApiRequest<User> request = new JsonApiRequest<User>(url, User.class);
		request.addParam("user_name", mobile)
		.addParam("password", password)
		.addParam("typeList", "6").addParam("loginIMEI",Utils.getDeviceIMEI(AppContext.getInstance()));
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 短信验证码登录
	 * @param mobile 用户手机号
	 * @param code 验证码
	 * @param requestCallback
	 */
	public static void loginByCode(String mobile, String code, RequestCallback<User> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/userInfoCtr/loginByMobile");
		JsonApiRequest<User> request = new JsonApiRequest<User>(url, User.class);
		request.addParam("mobile", mobile)
		.addParam("code", code)
		.addParam("typeList", "6").addParam("loginIMEI",Utils.getDeviceIMEI(AppContext.getInstance()));
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 自动登录方法
	 * @param context
	 * @param requestCallback
	 */
	public static void autoLogin(Context context, RequestCallback<User> requestCallback) {
		if(context == null) {
			context = AppContext.sCotext;
		}
		String username = (String) SPUtils.get(context, "phone", "");
		String password = (String) SPUtils.get(context, "password", "");
		login(username, password, requestCallback);
	}
	/**
	 * 
	 * @param mobile
	 * @param password
	 * @param code
	 * @param requestCallback
	 */
	public static void resetPassword(String mobile, String password, String code,
			RequestCallback<String> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/userInfoCtr/mobileResetPasswordByMobileCode");
		JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
		request.addParam("mobile", mobile)
		.addParam("password", password)
		.addParam("mobileCode", code);
		ApiOkHttp.postAsync(request, requestCallback);
		
	}
	/**
	 * 判断当前号码是否已经注册
	 * @param mobile 需校验的手机号
	 * @param requestCallback
	 */
	public static void isRegister(String mobile, RequestCallback<User> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/userInfoCtr/queryUserInfoByMobile");
		JsonApiRequest<User> request = new JsonApiRequest<User>(url, User.class);
		request.addParam("mobile", mobile);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 获取当前用户绑定的所有老人, 已通过的
	 * @param requestCallback
	 */
	public static void getBindingElders(RequestCallback<List<OldPeople>> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/socialPeopleCtr/listVerifySuccess");
		JsonApiRequest<List<OldPeople>> request = new JsonApiRequest<List<OldPeople>>(url, List.class);
		request.addParam("typeList", "6");
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 解除指定绑定的老人
	 * @param mapId
	 * @param requestCallback
	 */
	public static void unBindingElders(long mapId, RequestCallback<String> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/socialPeopleCtr/deleteMap");
		JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
		request.addParam("id", mapId);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 获取所有绑定的老人,以及正在验证绑定的老人
	 * @param requestCallback
	 */
	public static void getAllElders(RequestCallback<List<OldPeople>> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/socialPeopleCtr/list");
		JsonApiRequest<List<OldPeople>> request = new JsonApiRequest<List<OldPeople>>(url, List.class);
		request.addParam("typeList", "6");
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 获取指定页面数的信息,获取养老院的信息
	 * @param id 查找的区域ID
	 * @param sort 排序方式                 "" 按名称 , "bed_total" 按总床位  "bed_surplus" 按剩余床位
	 * @param pageNo
	 * @param requestCallback
	 */
	public static void getAllOrganization(long id, String sort, int pageNo, RequestCallback<List<Organization>> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/agencyInfoCtr/getAgencyInfoByRegionId");
		JsonApiRequest<List<Organization>> request = new JsonApiRequest<List<Organization>>(url, List.class);
		request.addParam("region_id", id)
		.addParam("sortFile", sort)
		.addParam("pageNo", pageNo);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 获取养老院信息
	 * @param id 指定机构的ID
	 * @param requestCallback
	 */
	public static void getOrganization(long id, RequestCallback<Organization> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/agencyInfoCtr/getAgencyInfo");
		JsonApiRequest<Organization> request = new JsonApiRequest<Organization>(url, Organization.class);
		request.addParam("agency_id", id);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 通过区域位置获取养老院信息
	 * @param status 地图显示的区域范围
	 * @param requestCallback
	 */
	public static void getOrganizationByLocation(MapStatus status, RequestCallback<List<Organization>> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/agencyInfoCtr/getAgencyListWithCoordinates");
		JsonApiRequest<List<Organization>> request = new JsonApiRequest<List<Organization>>(url, List.class);
		request.addParam("startLongitude", status.bound.southwest.longitude)
		.addParam("startLatitude", status.bound.southwest.latitude)
		.addParam("endLongitude", status.bound.northeast.longitude)
		.addParam("endLatitude", status.bound.northeast.latitude);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	
	/**
	 * 获取所有的城市列表
	 * @param requestCallback
	 */
	public static void getCitys(RequestCallback<List<City>> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/regionInfoCtr/queryRegionInfo");
		JsonApiRequest<List<City>> request = new JsonApiRequest<List<City>>(url, List.class);
		request.addParam("typeList", "6");
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 获取养老院所有的图片
	 * @param id 指定机构的ID
	 * @param requestCallback
	 */
	public static void getOrganizationPicture(long id, RequestCallback<List<String>> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/agencyIntroCtr/queryAgencyIntroPictures");
		JsonApiRequest<List<String>> request = new JsonApiRequest<List<String>>(url, List.class);
		request.addParam("agency_id", id);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 获取当前新闻所有的图片
	 * @param id 指定新闻的ID
	 * @param requestCallback
	 */
	public static void getNewsPicture(long id, RequestCallback<List<String>> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/newsCenterCtr/queryNewsCenterPictures");
		JsonApiRequest<List<String>> request = new JsonApiRequest<List<String>>(url, List.class);
		request.addParam("id", id);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 根据id获取新闻详情
	 * @param id
	 */
	public static void getNewsDetailById(int id, RequestCallback<News> requestCallback){
		String url = getAbsoluteApiUrl("lefuyun/newsCenterCtr/queryNewsCenter");
		JsonApiRequest<News> request = new JsonApiRequest<News>(url, List.class);
		request.addParam("id", id);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 获取用户登录或注册验证码
	 * @param mobile 用户登录或注册手机号
	 * @param requestCallback
	 */
	public static void getMobileCode(String mobile, RequestCallback<String> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/userInfoCtr/getMobileCode");
		JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
		request.addParam("mobile", mobile);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 注册
	 * @param mobile 用户注册手机号
	 * @param password 用户设置的密码
	 * @param code 用户获取到的验证码
	 * @param requestCallback 接口回调
	 */
	public static void register(String mobile, String password, String code, RequestCallback<User> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/userInfoCtr/registerUserInfo");
		JsonApiRequest<User> request = new JsonApiRequest<User>(url, User.class);
		request.addParam("mobile", mobile)
		.addParam("password", password)
		.addParam("mobileCode", code);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 根据身份证号查找老人
	 * @param identityCard
	 * @param requestCallback
	 */
	public static void getOldPeople(String identityCard, RequestCallback<OldPeople> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/socialPeopleCtr/getOldPeople");
		JsonApiRequest<OldPeople> request = new JsonApiRequest<OldPeople>(url, OldPeople.class);
		request.addParam("oldPeopleNum", identityCard);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 绑定指定的老人,根据手机号进行绑定
	 * @param identityCard 当前老人身份证号
	 * @param phone 家属手机号
	 * @param name 家属姓名
	 * @param requestCallback
	 */
	public static void bindingElder(String identityCard, String phone, String name, RequestCallback<String> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/socialPeopleCtr/associateOldPeople");
		JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
		request.addParam("oldPeopleNum", identityCard)
		.addParam("familyPhone", phone)
		.addParam("familyName", name);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 绑定老人,根据邀请码或者二维码进行绑定
	 * @param UserId 用户id
	 * @param code 邀请码或者二维码
	 * @param requestCallback
	 */
	public static void bindingElder(long UserId, String code, RequestCallback<String> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/socialPeopleCtr/bingdingOldPeople");
		String id = code.substring(0, code.length() - 5);
		JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
		request.addParam("uid", UserId)
		.addParam("oid", id)
		.addParam("code", code);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 给当前老人所在的机构提交建议
	 * @param agencyId 当前老人所在机构ID
	 * @param content 建议内容
	 * @param requestCallback
	 */
	public static void addAdvice(long agencyId, String content, RequestCallback<String> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/adviceCtr/addAdvice");
		JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
		request.addParam("agency_id", agencyId)
		.addParam("content", content);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 分页查询新闻
	 * @param type 新闻类型      1：养老新闻，2：养生课堂
	 * @param pageNo 当前页数
	 * @param requestCallback
	 */
	public static void getAgencyNews(int type, int pageNo, RequestCallback<List<News>> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/newsCenterCtr/queryNewsCenterList");
		JsonApiRequest<List<News>> request = new JsonApiRequest<List<News>>(url, List.class);
		request.addParam("type", type)
		.addParam("pageNo", pageNo);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 查找指定老人的请假历史记录
	 * @param type 请假类型         1:进行中和驳回的状态     2:驳回状态     3:完成状态
	 * @param pageNo 当前页面
	 * @param id 老人ID
	 * @param requestCallback
	 */
	public static void getHistoryOfOBtainOut(int type, int pageNo, long id, 
			long time, RequestCallback<List<OlderEgress>> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/leaveOutCtr/queryLeaveOutListByOid");
		JsonApiRequest<List<OlderEgress>> request = new JsonApiRequest<List<OlderEgress>>(url, List.class);
		request.addParam("leave_state", type)
		.addParam("pageNo", pageNo)
		.addParam("old_people_id", id);
		if(time > 0) {
			request.addParam("create_dt", time);
		}
		ApiOkHttp.getAsync(request, requestCallback);
	}
	/**
	 * 添加一条请假记录
	 * @param egress 请假记录been
	 * @param requestCallback
	 */
	public static void addOBtainOut(OlderEgress egress, RequestCallback<String> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/leaveOutCtr/addLeaveOut");
		JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
		request.addParam("old_people_id", egress.getOld_people_id())
		.addParam("leave_reason", egress.getLeave_reason())
		.addParam("expected_return_dt", egress.getExpected_return_dt())
		.addParam("expected_leave_dt", egress.getExpected_leave_dt())
		.addParam("leave_state", egress.getLeave_state())
		.addParam("party_signature", egress.getParty_signature())
		.addParam("version", UserInfo.getVersion());
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 获取老人当前健康评估信息
	 * @param oldPeopleId 老人ID
	 * @param requestCallback
	 */
	public static void getElderAssess(long oldPeopleId, RequestCallback<OldPeopleAssess> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/oldPeopleScoreCtr/queryOldPeopleScore");
		JsonApiRequest<OldPeopleAssess> request = new JsonApiRequest<OldPeopleAssess>(url, OldPeopleAssess.class);
		request.addParam("old_people_id", oldPeopleId);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 修改个人用户信息
	 * @param user 用户信息
	 * @param isIcon 当前是否是只修改图片
	 * @param requestCallback
	 */
	public static void updateUserInfo(User user, boolean isIcon, RequestCallback<String> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/orgUserCtr/updateUserByIdForPersonal");
		JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
		if(isIcon) {
			request.addParam("icon", user.getIcon());
		}else {
			request.addParam("gender", user.getGender())
			.addParam("user_name", user.getUser_name())
			.addParam("birthday_dt", user.getBirthday_dt());
		}
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 上传用户头像
	 * @param file 用户头像
	 * @param requestCallback
	 */
	public static void updatePortrait(File file, RequestCallback<Portrait> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/dailyNursingRecordCtr/addOrUpdateMedias");
		UploadApiRequest<Portrait> request = new UploadApiRequest<Portrait>(url, file);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 上传首页截屏的图片
	 * @param file
	 * @param requestCallback
	 */
	public static void uploadScreenCutImg(File file, RequestCallback<Portrait> requestCallback){
		String url = getAbsoluteApiUrl("lefuyun/shiroPictureDetailCtr/uploadPictureAndaddShare");
		UploadApiRequest<Portrait> request = new UploadApiRequest<Portrait>(url, file);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 获取最新版本的信息
	 * @param requestCallback
	 */
	public static void updateApp(RequestCallback<Version> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/update");
		JsonApiRequest<Version> request = new JsonApiRequest<Version>(url, Version.class);
		request.addParam("token", "1e3500a4de");
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 文件下载
	 * @param url 文件下载接口
	 * @param fileDownloadPath 文件保存目录
	 * @param requestCallback
	 */
	public static void downloadApp(String url, String fileDownloadPath, RequestCallback<File> requestCallback) {
		FileApiRequest request = new FileApiRequest(url, fileDownloadPath);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 预约养老机构
	 * @param reserve 预约相关信息
	 * @param requestCallback
	 */
	public static void reserveOrganization(Reserve reserve, RequestCallback<String> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/reservation/add");
		JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
		request.addParam("contactPerson", reserve.getContactPerson())
		.addParam("mobile", reserve.getMobile())
		.addParam("mailbox", reserve.getMailbox())
		.addParam("age", reserve.getAge())
		.addParam("selfCareAbility", reserve.getSelfCareAbility())
		.addParam("orderbed", reserve.getOrderbed())
		.addParam("agencyId", reserve.getAgencyId());
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 获取天气
	 * @param id 老人id
	 * @param requestCallback
	 */
	public static void getWeather(long id, RequestCallback<Weather> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/weatherCtr/queryWeatherByOldPeopleId");
		JsonApiRequest<Weather> request = new JsonApiRequest<Weather>(url, Weather.class);
		request.addParam("old_people_id", id);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 获取旅游信息列表
	 * @param pageNo 
	 * @param requestCallback
	 */
	public static void getTourisms(int pageNo, RequestCallback<List<Tourism>> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/productCtr/listWithPage");
		JsonApiRequest<List<Tourism>> request = new JsonApiRequest<List<Tourism>>(url, List.class);
		request.addParam("pageNo", pageNo);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 预约指定的旅游产品
	 * @param reserve 预约相关信息
	 * @param requestCallback
	 */
	public static void reserveTourisms(TourismReserve reserve, RequestCallback<String> requestCallback) {
		String url = getAbsoluteApiUrl("lefuyun/bizreservationCtr/add");
		JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
		request.addParam("product_id", reserve.getProduct_id())
		.addParam("company_id", reserve.getCompany_id())
		.addParam("name", reserve.getName())
		.addParam("cellphone", reserve.getCellphone())
		.addParam("people_amount", reserve.getPeople_amount());
		ApiOkHttp.postAsync(request, requestCallback);
	}
	
	/**
	 * 获取绝对路径
	 * @param partUrl 如:lefuyun/userInfoCtr/queryUserInfo
	 * @return
	 */
	public static String getAbsoluteApiUrl(String partUrl) {
        String url = String.format(API_URL, partUrl);
        return url;
    }
	
	/**
	 * 获取未收到的通知列表
	 * @param user_id 用户ID
	 * @param old_people_id 老人ID
	 */
	public static void queryUnreceivedNoticeList(long user_id,long old_people_id,int pageNo,RequestCallback<List<ImportantMsg>> requestCallback){
		String url = getAbsoluteApiUrl("lefuyun/importantNoticeCtr/queryUnreceivedNoticeList");//?user_id=313&old_people_id=92
		JsonApiRequest<List<ImportantMsg>> request=new JsonApiRequest<List<ImportantMsg>>(url, List.class);
		LogUtil.e("old_people_id", old_people_id+"");
		request.addParam("user_id", user_id)
		.addParam("old_people_id", old_people_id)
		.addParam("pageNo", pageNo);
		ApiOkHttp.postAsync(request,requestCallback);
	}
	
	/**
	 * 获取已经收到的通知列表
	 */
	public static void queryReceivedNoticeList(long user_id,long old_people_id,long update_dt,int pageNo,RequestCallback<List<ImportantMsg>> requestCallback){
		LogUtil.e("user", user_id+" "+old_people_id+" "+update_dt+" "+pageNo+" ");
		String url=getAbsoluteApiUrl("lefuyun/importantNoticeCtr/queryAllNoticeList");//?user_id=313&old_people_id=92&update_dt=0
		JsonApiRequest<List<ImportantMsg>> request=new JsonApiRequest<List<ImportantMsg>>(url, List.class);
		request.addParam("user_id", user_id);
		request.addParam("old_people_id", old_people_id);
		request.addParam("update_dt", update_dt);
		request.addParam("pageNo", pageNo);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 根据通知id查找通知列表
	 * @param notice_id
	 * @param requestCallback
	 */
	public static void queryNoticeByNoticeid(int notice_id,RequestCallback<List<ImportantMsg>> requestCallback){
		String url=getAbsoluteApiUrl("lefuyun/importantNoticeCtr/queryNoticeListByIdAndUserId");//
		JsonApiRequest<List<ImportantMsg>> request=new JsonApiRequest<List<ImportantMsg>>(url, List.class);
		request.addParam("notice_id", notice_id);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 上传截图
	 * @param url
	 * @param fileDownloadPath
	 * @param requestCallback
	 */
	public static void upLoadImage(String url,String fileDownloadPath,RequestCallback<File> requestCallback){
		FileApiRequest request=new FileApiRequest(url, fileDownloadPath);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	
	/**
	 * 日常护理(护理服务)
	 * @param pageNo 页号，部分页时，值==0
	 * @param uid 老人ID
	 * @param nursing_dt 护理时间     不传值或传0时，查询所有数据。传时间戳，查询该时间戳之前的数据(包含该时间戳)
	 * @param nurs_items_id 护理项ID  不传值或传0时，查询所有数据。  传护理项ID时，查询该护理项相关数据
	 * @param requestCallback 回调类
	 */
	public static void queryDailyNursingRecordByUid(int pageNo,long uid,long nursing_dt,long nurs_items_id,RequestCallback<List<NurseRecord>> requestCallback){
		String url=getAbsoluteApiUrl("lefuyun/dailyNursingRecordCtr/queryDailyNursingRecordByUid");
		JsonApiRequest<List<NurseRecord>> request=new JsonApiRequest<List<NurseRecord>>(url);
		request.addParam("pageNo", pageNo)
		.addParam("uid", uid)
		.addParam("nursing_dt", nursing_dt)
		.addParam("nurs_items_id", nurs_items_id);
		ApiOkHttp.postAsync(request,requestCallback);
	}
	/**
	 * 获取日常生活列表
	 */
	public static void queryDailyLifeRecordByUid(int pageNo,long uid,long nursing_dt,RequestCallback<List<DailyLifeRecord>> requestCallback){
		String url=getAbsoluteApiUrl("lefuyun/dailyLifeRecordCtr/queryDailyLifeSameBeanByOldPeopleId");
		JsonApiRequest<List<DailyLifeRecord>> request=new JsonApiRequest<List<DailyLifeRecord>>(url);
		request.addParam("pageNo", pageNo)
		.addParam("old_people_id", uid)
		.addParam("nursing_dt", nursing_dt);
		ApiOkHttp.postAsync(request,requestCallback);
	}
	/**
	 * 查询某个老人的图片详情
	 * @param old_people_id 老人ID
	 * @param type 媒体类型 图片1，视频2 ，音频3
	 * @param pageNo 不传值，默认分页，每页10条
	 * @param requestCallback 回调类
	 */
	public static void queryMediaByOldPeopleId(long old_people_id,int type,int pageNo,RequestCallback<List<NurseMedia>> requestCallback){
		LogUtil.e("nurseMedia", "oldId"+old_people_id+" type"+type+" pageNO"+pageNo);
		String url=getAbsoluteApiUrl("lefuyun/dailyNursingRecordCtr/queryMediaByOldPeopleId");
		JsonApiRequest<List<NurseMedia>> request=new JsonApiRequest<List<NurseMedia>>(url);
		request.addParam("old_people_id", old_people_id)
		.addParam("type", type)
		.addParam("pageNo", pageNo);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 查询某个老人“日常生活”的图片详情
	 * @param old_people_id
	 * @param type
	 * @param pageNo
	 * @param requestCallback
	 */
	public static void queryDailyMediaByOldPeopleId(long old_people_id,int type,int pageNo,RequestCallback<List<NurseMedia>> requestCallback){
		String url=getAbsoluteApiUrl("lefuyun/dailyLifeRecordCtr/queryMediaByOldPeopleId");
		JsonApiRequest<List<NurseMedia>> request=new JsonApiRequest<List<NurseMedia>>(url);
		request.addParam("old_people_id", old_people_id)
		.addParam("type", type)
		.addParam("pageNo", pageNo);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 查询某个老人的图片详情
	 * @param old_people_id 老人ID
	 * @param type 媒体类型 图片1，视频2 ，音频3
	 * @param pageNo 不传值，默认分页，每页10条
	 * @param requestCallback 回调类
	 */
	public static void querydailyLifeMediaByOldPeopleId(long old_people_id,int type,int pageNo,RequestCallback<List<NurseMedia>> requestCallback){
		LogUtil.e("nurseMedia", "oldId"+old_people_id+" type"+type+" pageNO"+pageNo);
		String url=getAbsoluteApiUrl("lefuyun/dailyLifeRecordCtr/queryMediaByOldPeopleId");
		JsonApiRequest<List<NurseMedia>> request=new JsonApiRequest<List<NurseMedia>>(url);
		request.addParam("old_people_id", old_people_id)
		.addParam("type", type)
		.addParam("pageNo", pageNo);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 根据老人ID查询照护记录对应的媒体数量（图片，音频，视频）
	 */
	public static void queryMediaSumByOldPeopleId(long old_people_id,RequestCallback<NurseMediaSum> requestCallback){
		String url=getAbsoluteApiUrl("lefuyun/dailyNursingRecordCtr/queryMediaSumByOldPeopleId");
		JsonApiRequest<NurseMediaSum> request=new JsonApiRequest<NurseMediaSum>(url);
		request.addParam("old_people_id", old_people_id);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 根据老人ID查询“日常生活”对应的媒体数量
	 * @param old_people_id
	 * @param requestCallback
	 */
	public static void queryDailyLifeMediaSumByOldPeopleId(long old_people_id,RequestCallback<NurseMediaSum> requestCallback){
		String url=getAbsoluteApiUrl("lefuyun/dailyLifeRecordCtr/queryMediaSumByOldPeopleId");
		JsonApiRequest<NurseMediaSum> request=new JsonApiRequest<NurseMediaSum>(url);//只是请求地址不一样，bean的字段完全一样，所以就用护理的bean
		request.addParam("old_people_id", old_people_id);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 护理记录 点赞
	 * @param daily_id 护理记录id
	 * @param requestCallback
	 */
	public static void praiseNurseing(long daily_id,RequestCallback<String> requestCallback){
		String url=getAbsoluteApiUrl("lefuyun/dailyNursingRecordCtr/updatePraiseNumber");
		JsonApiRequest<String> request=new JsonApiRequest<String>(url);
		request.addParam("daily_id",daily_id);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 线下活动点赞
	 * @param id 线下活动ID
	 * @param requestCallback
	 */
	public static void parseOrgActive(long id,RequestCallback<String> requestCallback){
		String url=getAbsoluteApiUrl("lefuyun/agencyActivites/updateAgencyActivitePraiseNumber");
		JsonApiRequest<String> request=new JsonApiRequest<String>(url);
		request.addParam("id",id);
		ApiOkHttp.postAsync(request, requestCallback);
		
	}
	/**
	 * 根据机构ID获取护理项
	 * @param agency_id 老人所在的机构ID
	 */
	public static void getNurseTypeByAgencyId(long agency_id,RequestCallback<List<HelpInfo>> requestCallback){
		LogUtil.e("agency", agency_id+"");
		String url=getAbsoluteApiUrl("lefuyun/nursingItemsCtr/getNursingItemsByAgencyId");
		JsonApiRequest<List<HelpInfo>> request=new JsonApiRequest<List<HelpInfo>>(url);
		request.addParam("agency_id", agency_id);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 分享
	 * @param context
	 * @param title 分享页面标题
	 * @param des 分享条目描述内容
	 * @param imgUrl 分享条目图片地址
	 * @param url 分享内容详情地址
	 * @param isTitle 微信朋友圈不支持des显示内容,新浪微博不支持title内容, 如果为true则显示title内容,否则显示des内容,俩者只能选择一个
	 */
	public static void sharePage(Context context, String title, String des, String imgUrl, String url, boolean isTitle){
		Intent intent = new Intent(context,ShareActivity.class);
		intent.putExtra("title",title);
		intent.putExtra("des", des);
		intent.putExtra("imgUrl", imgUrl);
		intent.putExtra("url", url);
		intent.putExtra("isTitle", isTitle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	/**
	 * 根据区域获取活动列表
	 */
	public static void queryActivitesListWithArea(long region_id,long agencyId,long pageNo,RequestCallback<List<OrgActive>> callback){
		LogUtil.e("agencyId", agencyId+"");
		String url=getAbsoluteApiUrl("lefuyun/agencyActivites/toActivitesListWithArea");
		JsonApiRequest<List<OrgActive>> request=new JsonApiRequest<List<OrgActive>>(url);
		//request.addParam("region_id",region_id);
		request.addParam("agencyId", agencyId);
		request.addParam("pageNo", pageNo);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取机构活动中的所有图片
	 */
	public static void queryAgencyActivitePictures(long id,RequestCallback<List<String>> callback){
		String url=getAbsoluteApiUrl("lefuyun/agencyActivites/queryAgencyActivitePictures");
		JsonApiRequest<List<String>> request=new JsonApiRequest<List<String>>(url);
		request.addParam("id",id);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 根据重要通知（ImportantMsg）的id 获取重要通知中的所有图片
	 */
	public static void queryImportantNoticePictures(long id,RequestCallback<List<String>> callback){
		String url=getAbsoluteApiUrl("lefuyun/importantNoticeCtr/queryImportantNoticePictures");
		JsonApiRequest<List<String>> request=new JsonApiRequest<List<String>>(url);
		request.addParam("id",id);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取最新血糖的体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * @param callback
	 * old_people_id=92&inspect_dt=1
	 */
	public static void queryLastSignDataForSugar(long old_people_id,long inspect_dt,RequestCallback<SignDataBean> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/bloodSugar/queryForUserNewRecord");
		JsonApiRequest<SignDataBean> request=new JsonApiRequest<SignDataBean>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取最新血压的体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * @param callback
	 * old_people_id=92&inspect_dt=1
	 */
	public static void queryLastSignDataForPressure(long old_people_id,long inspect_dt,RequestCallback<SignDataBean> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/bloodPressure/queryForUserNewRecord");
		JsonApiRequest<SignDataBean> request=new JsonApiRequest<SignDataBean>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取最新呼吸的体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * @param callback
	 * old_people_id=92&inspect_dt=1
	 */
	public static void queryLastSignDataForBreath(long old_people_id,long inspect_dt,RequestCallback<SignDataBean> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/breathing/queryForUserNewRecord");
		JsonApiRequest<SignDataBean> request=new JsonApiRequest<SignDataBean>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取最新排便的体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * @param callback
	 * old_people_id=92&inspect_dt=1
	 */
	public static void queryLastSignDataForDefecation(long old_people_id,long inspect_dt,RequestCallback<SignDataBean> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/defecation/queryForUserNewRecord");
		JsonApiRequest<SignDataBean> request=new JsonApiRequest<SignDataBean>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取最新饮水的体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * @param callback
	 * old_people_id=92&inspect_dt=1
	 */
	public static void queryLastSignDataForDrink(long old_people_id,long inspect_dt,RequestCallback<SignDataBean> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/drinkwater/queryForUserNewRecord");
		JsonApiRequest<SignDataBean> request=new JsonApiRequest<SignDataBean>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取最新心率的体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * @param callback
	 * old_people_id=92&inspect_dt=1
	 */
	public static void queryLastSignDataForPulse(long old_people_id,long inspect_dt,RequestCallback<SignDataBean> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/pulse/queryForUserNewRecord");
		JsonApiRequest<SignDataBean> request=new JsonApiRequest<SignDataBean>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取最新睡眠的体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * @param callback
	 * old_people_id=92&inspect_dt=1
	 */
	public static void queryLastSignDataForSleep(long old_people_id,long inspect_dt,RequestCallback<SignDataBean> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/sleep/queryForUserNewRecord");
		JsonApiRequest<SignDataBean> request=new JsonApiRequest<SignDataBean>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取最新体温的体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * @param callback
	 * old_people_id=92&inspect_dt=1
	 */
	public static void queryLastSignDataForTemperature(long old_people_id,long inspect_dt,RequestCallback<SignDataBean> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/temperature/queryForUserNewRecord");
		JsonApiRequest <SignDataBean> request=new JsonApiRequest<SignDataBean>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取最新饮食的体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * @param callback
	 * old_people_id=92&inspect_dt=1
	 */
	public static void queryLastSignDataForMeal(long old_people_id,long inspect_dt,RequestCallback<SignDataBean> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/meal/queryForUserNewRecord");
		JsonApiRequest<SignDataBean> request=new JsonApiRequest<SignDataBean>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取所有体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * 分页查询所有的血糖数据
	 * @param old_people_id
	 * @param inspect_dt
	 * @param callback
	 */
	public static void queryAllSignDataForSugar(long old_people_id,long inspect_dt,int pageNo,int approval_status,RequestCallback<List<SignDataBean>> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/bloodSugar/queryForUserAll");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		request.addParam("pageNo", pageNo);
		request.addParam("approval_status", approval_status);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取所有体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * 分页查询所有的血压数据
	 * @param old_people_id
	 * @param inspect_dt
	 * @param callback
	 */
	public static void queryAllSignDataForPressure(long old_people_id,long inspect_dt,int pageNo,int approval_status,RequestCallback<List<SignDataBean>> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/bloodPressure/queryForUserAll");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		request.addParam("pageNo", pageNo);
		request.addParam("approval_status", approval_status);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取所有体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * 分页查询所有的心率数据
	 * @param old_people_id
	 * @param inspect_dt
	 * @param callback
	 */
	public static void queryAllSignDataForPulse(long old_people_id,long inspect_dt,int pageNo,int approval_status,RequestCallback<List<SignDataBean>> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/pulse/queryForUserAll");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		request.addParam("pageNo", pageNo);
		request.addParam("approval_status", approval_status);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取所有体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * 分页查询所有的体温数据
	 * @param old_people_id
	 * @param inspect_dt
	 * @param callback
	 * @param approval_status 0等待审核，1审核通过，2未通过
	 */
	public static void queryAllSignDataForTemperature(long old_people_id,long inspect_dt,int pageNo,int approval_status,RequestCallback<List<SignDataBean>> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/temperature/queryForUserAll");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		request.addParam("pageNo", pageNo);
		request.addParam("approval_status", approval_status);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取所有体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * 分页查询所有的饮水数据
	 * @param old_people_id
	 * @param inspect_dt
	 * @param callback
	 */
	public static void queryAllSignDataForDrink(long old_people_id,long inspect_dt,int pageNo,int approval_status,RequestCallback<List<SignDataBean>> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/drinkwater/queryForUserAll");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		request.addParam("pageNo", pageNo);
		request.addParam("approval_status", approval_status);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取所有体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * 分页查询所有的睡眠数据
	 * @param old_people_id
	 * @param inspect_dt
	 * @param callback
	 */
	public static void queryAllSignDataForSleep(long old_people_id,long inspect_dt,int pageNo,int approval_status,RequestCallback<List<SignDataBean>> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/sleep/queryForUserAll");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		request.addParam("pageNo", pageNo);
		request.addParam("approval_status", approval_status);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取所有体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * 分页查询所有的饮食数据
	 * @param old_people_id
	 * @param inspect_dt
	 * @param callback
	 */
	public static void queryAllSignDataForMeal(long old_people_id,long inspect_dt,int pageNo,int approval_status,RequestCallback<List<SignDataBean>> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/meal/queryForUserAll");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		request.addParam("pageNo", pageNo);
		request.addParam("approval_status", approval_status);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取所有体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * 分页查询所有的呼吸数据
	 * @param old_people_id
	 * @param inspect_dt
	 * @param callback
	 */
	public static void queryAllSignDataForBreath(long old_people_id,long inspect_dt,int pageNo,int approval_status,RequestCallback<List<SignDataBean>> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/breathing/queryForUserAll");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		request.addParam("pageNo", pageNo);
		request.addParam("approval_status", approval_status);
		ApiOkHttp.postAsync(request, callback);
	}
	/**
	 * 获取所有体征数据（如果想查询5.1号以前的数据 inspect_dt 传入5.1日23：59：59秒的long值）
	 * 分页查询所有的排便数据
	 * @param old_people_id
	 * @param inspect_dt
	 * @param callback
	 */
	public static void queryAllSignDataForDefecation(long old_people_id,long inspect_dt,int pageNo,int approval_status,RequestCallback<List<SignDataBean>> callback){
		String url=getAbsoluteApiUrl("lefuyun/singndata/defecation/queryForUserAll");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("inspect_dt", inspect_dt);
		request.addParam("pageNo", pageNo);
		request.addParam("approval_status", approval_status);
		ApiOkHttp.postAsync(request, callback);
	}
//	public static String GET_BODY_DATA_SUGAR=BaseUrl+"/lefuyun/singndata/bloodPressure/getBsrCharts";//血糖
//	public static String GET_BODY_DATA_Pressure=BaseUrl+"/lefuyun/singndata/bloodPressure/getBprCharts";//血压
//	//public static String GET_BODY_DATA_HeartBeat="";//心跳
//	public static String GET_BODY_DATA_Pluse=BaseUrl+"/lefuyun/singndata/bloodPressure/getPrCharts";//脉搏
//	public static String GET_BODY_DATA_Temprature=BaseUrl+"/lefuyun/singndata/bloodPressure/getTrCharts";//体温
//	dataParams.put("old_people_id", currentOldPeople.getId()+"");
//	dataParams.put("beginTime", beginTime+"");
//	dataParams.put("endTime", todayTimeLong+"");
//	dataParams.put("type", type);
//	dataParams.put("order", order);
	/**
	 * 查询图表中血压的体征数据
	 * @param old_people_id
	 * @param beginTime
	 * @param endTime
	 * @param type
	 * @param order
	 * @param callback
	 */
	public static void queryBodyDataForChartBressure(long old_people_id,String beginTime,String endTime,String type,String order,RequestCallback<List<SignDataBean>> callback){
    	String url=getAbsoluteApiUrl("lefuyun/singndata/bloodPressure/getBprCharts");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("beginTime", beginTime);
		request.addParam("endTime", endTime);
		request.addParam("type", type);
		request.addParam("order", order);
		ApiOkHttp.postAsync(request, callback);
    }
	/**
	 * 查询图表中血糖的体征数据
	 * @param old_people_id
	 * @param beginTime
	 * @param endTime
	 * @param type
	 * @param order
	 * @param callback
	 */
	public static void queryBodyDataForChartSugar(long old_people_id,String beginTime,String endTime,String type,String order,RequestCallback<List<SignDataBean>> callback){
    	String url=getAbsoluteApiUrl("lefuyun/singndata/bloodPressure/getBsrCharts");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("beginTime", beginTime);
		request.addParam("endTime", endTime);
		request.addParam("type", type);
		request.addParam("order", order);
		ApiOkHttp.postAsync(request, callback);
    }
	/**
	 * 查询图表中心率的体征数据
	 * @param old_people_id
	 * @param beginTime
	 * @param endTime
	 * @param type
	 * @param order
	 * @param callback
	 */
	public static void queryBodyDataForChartPulse(long old_people_id,String beginTime,String endTime,String type,String order,RequestCallback<List<SignDataBean>> callback){
    	String url=getAbsoluteApiUrl("lefuyun/singndata/bloodPressure/getPrCharts");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("beginTime", beginTime);
		request.addParam("endTime", endTime);
		request.addParam("type", type);
		request.addParam("order", order);
		ApiOkHttp.postAsync(request, callback);
    }
	/**
	 * 查询图表中体温的体征数据
	 * @param old_people_id
	 * @param beginTime
	 * @param endTime
	 * @param type
	 * @param order
	 * @param callback
	 */
	public static void queryBodyDataForChartTemperature(long old_people_id,String beginTime,String endTime,String type,String order,RequestCallback<List<SignDataBean>> callback){
    	String url=getAbsoluteApiUrl("lefuyun/singndata/bloodPressure/getTrCharts");
		JsonApiRequest<List<SignDataBean>> request=new JsonApiRequest<List<SignDataBean>>(url);
		request.addParam("old_people_id",old_people_id);
		request.addParam("beginTime", beginTime);
		request.addParam("endTime", endTime);
		request.addParam("type", type);
		request.addParam("order", order);
		ApiOkHttp.postAsync(request, callback);
    }
	/**
	 * 获取配置
	 * @param context
	 * @param agency_id
	 * @param tag
	 * @param callback
	 */
	public static String VERSION = "20151101";// 接口的版本号
	public static void queryConfig(Context context,String agency_id,String tag,RequestCallback<ContentTotal> callback){
    	String url=getAbsoluteApiUrl("lefuyun/config/getConfig");
		JsonApiRequest<ContentTotal> request=new JsonApiRequest<ContentTotal>(url);
		request.addParam("agency_id",SPUtils.get(context, SPUtils.ANGENCY_ID,""));
		request.addParam("version", VERSION);
		request.addParam("tag", tag);
		ApiOkHttp.postAsync(request, callback);
    }
	/**
	 * 根据一周食谱的id，查询一周食谱
	 * @param weekRecipeId
	 * @param requestCallback
	 */
	public static void queryWeekRecipeById(long weekRecipeId,OldPeople old,RequestCallback<WeekRecipeBean> requestCallback){
		String url=getAbsoluteApiUrl("lefuyun/weekrecipe/getInfoById");
		JsonApiRequest<WeekRecipeBean> request=new JsonApiRequest<WeekRecipeBean>(url);
		request.addParam("id", weekRecipeId);
		request.addParam("agency_id",old.getAgency_id());
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 根据周数查询一周食谱
	 * @param weekNo
	 * @param requestCallback
	 */
	public static void queryWeekRecipeListByWeekNum(OldPeople old,long time ,RequestCallback<List<WeekFood>> requestCallback){
		String url=getAbsoluteApiUrl("lefuyun/weekrecipe/list");
		JsonApiRequest<List<WeekFood>> request=new JsonApiRequest<List<WeekFood>>(url);
		request.addParam("inspect_dt", time);
		request.addParam("agency_id", old.getAgency_id());
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 查询月食谱id列表
	 * @param pageNo
	 * @param context
	 * @param requestCallback
	 */
	public static void queryWeekRecipeWithMonth(int pageNo,OldPeople old,Context context,RequestCallback<List<WeekFood>> requestCallback){
		String url=getAbsoluteApiUrl("lefuyun/weekrecipe/queryWeekRecipeWithMonth");
		JsonApiRequest<List<WeekFood>> request=new JsonApiRequest<List<WeekFood>>(url);
		request.addParam("pageNo", pageNo);
		request.addParam("pageSize", 3);
		request.addParam("agency_id",old.getAgency_id());
		ApiOkHttp.postAsync(request, requestCallback);
	}
	/**
	 * 为食谱点赞
	 * @param id
	 * @param requestCallback
	 */
	public static void parseWeekRecipe(long id,RequestCallback<String> requestCallback){
		String url=getAbsoluteApiUrl("lefuyun/weekrecipe/updatePraiseNumber");//id=1
		JsonApiRequest<String> request=new JsonApiRequest<String>(url);
		request.addParam("id",id);
		ApiOkHttp.postAsync(request, requestCallback);
	}
}
