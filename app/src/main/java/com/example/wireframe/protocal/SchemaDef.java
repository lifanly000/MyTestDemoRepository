package com.example.wireframe.protocal;


public class SchemaDef 
{
	// 首页数据  学习日志首页
    // 交互数据参见：
    // 请求数据结构：JournalHomeRequestData
    // 响应数据结构：JournalHomeResponseData
	public static final int JOURNAL_HOME_PAGE        = 100001;
	
	// 首页数据  学习日志报告详情
	// 交互数据参见：
	// 请求数据结构：JournalHomeDetailRequestData
	// 响应数据结构：JournalHomeDetailResponseData
	public static final int JOURNAL_HOME_DETAIL        = 100002;
	
	
	// 首页数据  学习日志提交作业语音
	// 交互数据参见：
	// 请求数据结构：JournalHomeWorkRequestData
	// 响应数据结构：JournalHomeWorkResponseData
	public static final int JOURNAL_HOMEWORK_ANSWER        = 100003;

	// 成长值
	// 交互数据参见：
	// 请求数据结构：GrowUpNumberRequestData
	// 响应数据结构：GrowUpNumberResponseData
	public static final int GROW_UP_NUMBER        = 100004;
	
	// 成长轨迹 
	// 交互数据参见：
	// 请求数据结构：GrowUpHomeRequestData
	// 响应数据结构：GrowUpHomeResponseData
	public static final int GROW_UP_HOME        = 100101;
	
	// 成长轨迹 报告详情
	// 交互数据参见：
	// 请求数据结构：GrowUpHomeDetailRequestData
	// 响应数据结构：GrowUpHomeDetailResponseData
	public static final int GROW_UP_HOME_DETAIL        = 100102;
	
	// 成长轨迹 成绩单详情
	// 交互数据参见：
	// 请求数据结构：GrowUpResultDetailRequestData
	// 响应数据结构：GrowUpResultDetailResponseData
	public static final int GROW_UP_RESULT_DETAIL       = 100103;
	
	// 成长轨迹 报告详情
	// 交互数据参见：
	// 请求数据结构：
	// 响应数据结构：EXiuxiuHomePageResponseData
	public static final int E_XIUXIU_HOMEPAGE        = 100201;
	
	// Exiuxiu报告详情
	// 交互数据参见：
	// 请求数据结构：EXiuxiuHomePageDetailRequestData
	// 响应数据结构：EXiuxiuHomePageDetailResponseData
	public static final int E_XIUXIU_HOMEPAGE_DETAIL        = 100202;
	
	// Exiuxiu报告详情
	// 交互数据参见：
	// 请求数据结构：ZanAndCommentRequestData
	// 响应数据结构：ZanAndCommentResponseData
	public static final int ZAN_COMMENT        = 100203;
	
	// 用户注册
	// 交互数据参见：
	// 请求数据结构：UserRegisterRequestData
	// 响应数据结构：UserRegisterResponseData
	public static final int USER_REGISTER       = 100301;
	
	// 用户登录
	// 交互数据参见：
	// 请求数据结构：UserLoginRequestData
	// 响应数据结构：UserLoginResponseData
	public static final int USER_LOGIN        = 100302;
	
	// 获取验证码
	// 交互数据参见：
	// 请求数据结构：GetVeriCodeRequestData
	// 响应数据结构：GetVeriCodeResponseData
	public static final int GET_VERI_CODE        = 100303;
	
	// 修改密码
	// 交互数据参见：
	// 请求数据结构：ResetPasswordRequestData
	// 响应数据结构：ResetPasswordResponseData
	public static final int RESET_PASSWORD        = 100304;
	
	// 账户-找回密码-设置新密码==
	// 账户-获取个人资料
	// 交互数据参见：
	// 请求数据结构：FindPasswordRequestData
	// 响应数据结构：FindPasswordResponseData
	public static final int FIND_PASSWORD        = 100305;
	
	// 账户-找回密码-设置新密码==
	// 交互数据参见：
	// 请求数据结构：
	// 响应数据结构：UserInfoResponseData
	public static final int USER_INFO        = 100307;
	
	// ==我-我的课程-线上课程列表==
	// 交互数据参见：
	// 请求数据结构：
	// 响应数据结构：MyCourseOnlineResponseData
	public static final int MY_COURSE_ONLINE        = 100311;
	
	// ==我-我的课程-线上课程购买信息==
	// 交互数据参见：
	// 请求数据结构：MyCourseOnlineBuyInfoRequestData
	// 响应数据结构：MyCourseOnlineBuyInfoResponseData
	public static final int MY_COURSE_ONLINE_BUY_INFO        = 100312;
	
	// ==我-我的课程-线下课程列表==
	// 交互数据参见：
	// 请求数据结构：
	// 响应数据结构：MyCourseOfflineResponseData
	public static final int MY_COURSE_OFFLINE        = 100313;
	
	// ==我-我的课程-线下课程购买信息==
	// 交互数据参见：
	// 请求数据结构：MyCourseOfflineBuyInfoRequestData
	// 响应数据结构：MyCourseOfflineBuyInfoResponseData
	public static final int MY_COURSE_OFFLINE_BUY_INFO        = 100314;
	
	// ==我-购买课程-线上课程列表==
	// 交互数据参见：
	// 请求数据结构：
	// 响应数据结构：MyBuyCourseOnlineResponseData
	public static final int MY_BUY_COURSE_ONLINE_LIST        = 100321;
	
	// ==我-购买课程-线下课程列表==
	// 交互数据参见：
	// 请求数据结构：
	// 响应数据结构：MyBuyCourseOfflineResponseData
	public static final int MY_BUY_COURSE_OFFLINE_LIST        = 100322;
	
	// ==我-购买课程-线下课程购买详情==
	// 交互数据参见：
	// 请求数据结构：MyCourseOfflineDetailRequestData
	// 响应数据结构：MyCourseOfflineDetailResponseData
	public static final int MY_COURSE_OFFLINE_DETAIL        = 100323;
	
	// ==我-购买课程-线上课程详情==
	// 交互数据参见：
	// 请求数据结构：MyCourseOnlineDetailRequestData
	// 响应数据结构：MyCourseOnlineDetailResponseData
	public static final int MY_COURSE_ONLINE_DETAIL        = 100324;
	
	// ==我-购买课程-线上课程 非会员试用==
	// 交互数据参见：
	// 请求数据结构：MyCourseOfflineNotMemberTryUseRequestData
	// 响应数据结构：MyCourseOfflineNotMemberTryUseResponseData
	public static final int MY_COURSE_OFFLINE_NOT_MEMBER_TYR_USE        = 100325;
		
	
	//我-购买课程-线上课程_发起试用==
	// 交互数据参见：
	// 请求数据结构：MyCourseOfflineNotMemberUseRequestData
	// 响应数据结构：MyCourseOfflineNotMemberUseResponseData
	public static final int MY_COURSE_OFFLINE_NOT_MEMBER_USE        = 100326;
	
	//购买课程
	// 交互数据参见：
	// 请求数据结构：PayRequestData
	// 响应数据结构：PayResponseData
	public static final int PAY_INFO        = 100330;
	
	//软件更新
	// 交互数据参见：
	// 请求数据结构：UpdateRequestData
	// 响应数据结构：UpdateResponseData
	public static final int UPDATE_INFO        = 150001;
	
	//意见反馈
	// 交互数据参见：
	// 请求数据结构：FeedBackRequestData
	// 响应数据结构：FeedBackResponseData
	public static final int FEED_BACK_INFO        = 150002;
	
	//排行榜
	// 交互数据参见：
	// 请求数据结构：RankInfoRequestData
	// 响应数据结构：RankInfoResponseData
	public static final int RANK_INFO        = 150003;

	// 分享反馈
	// 交互数据参见：
	// 请求数据结构：ShareResultRequestData
	// 响应数据结构：ShareResultResponseData
	public static final int SHARE_RESULT        = 150004;
	
}
