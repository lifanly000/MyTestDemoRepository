package com.example.wireframe.protocal.protocalProcess.model;

public class PayResponseData {
	public ResponseCommonData commonData = new ResponseCommonData();
	
		// 1-微信支付，2-支付宝网页支付
		public String payType = ""; 
		//本系统的订单号
		public String orderid = ""; 
		/**
		 * 以下是微信支付的相关参数
		 */
		//appid
		public String appid = ""; 
		//服务器下发商户号id
		public String partnerid = ""; 
		//预支付交易会话标识
		public String prepay_id = ""; 
		//扩展字段 package
		public String myPackage = ""; 
		//随机字符串
		public String noncestr = ""; 
		//时间戳
		public String timestamp = ""; 
		//数据签名参数
		public String sign = ""; 
		/**
		 * 以下是支付宝网页支付相关参数,支付宝网页支付完成后，直接关闭网页即可，客户端可以查询订单状态。
		 */
		//支付宝网页支付url地址以及所有参数,客户端直接用这个url打开页面即可
		public String payUrl = ""; 
}
