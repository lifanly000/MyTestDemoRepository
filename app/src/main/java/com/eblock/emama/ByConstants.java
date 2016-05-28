package com.eblock.emama;

import android.provider.SyncStateContract.Constants;

public class ByConstants extends Constants {

    public static final String APP_ID = "wx10a4efcf5536a66e";

    public static final String WIN_RETURN_SUCCESS = "0000";

    public static final int REQUEST_CONFIRM = 110;
    public static final int REQUEST_CONFIRM_BACK = 112;
    
    public static final int REQUEST_LOGIN = 120;
    public static final int RESULTCODE_LOGIN = 122;
    
    public static final int REQUEST_FINDPWD = 130;
    public static final int RESULTCODE_FINDPWD = 132;
    
    public static final int REQUEST_REGISTER = 140;
    public static final int RESULTCODE_REGISTER = 142;
    
    public static final int REQUEST_BINDPHONE = 150;
    public static final int RESULTCODE_BINDPHONE = 152;
    
    public static final int REQUEST_FILTER = 1;
    public static final int REQUEST_FILTER_OK = 100;
    public static final int REQUEST_MORE_OK = 1100;

 // tabhost --tab传递信息用
    public static int HANDLER_TAB_INDEX = 1;
    public static int HANDLER_TAB_INDEX_REFRESH = 101;
    public static int HANDLER_TAB_INDEX_TABINIT = 102;
    public static int HANDLER_TAB_USER = 2;
    public static int HANDLER_TAB_USER_REFRESH = 201;
    public static int HANDLER_TAB_USER_TABINIT = 202;
    public static int HANDLER_TAB_ME = 3;
    public static int HANDLER_TAB_SURVEY_REFRESH = 301;
    public static int HANDLER_TAB_ME_TABINIT = 302;
    public static int HANDLER_TAB_EXAM = 4;
    public static int HANDLER_TAB_EXAM_REFRESH = 401;
    public static int HANDLER_TAB_EXAM_TABINIT = 402;
    public static int HANDLER_TAB_PRODUCT = 5;
    public static int HANDLER_TAB_PRODUCT_REFRESH = 501;
    public static int HANDLER_TAB_PRODUCT_BACK = 502;
    public static int HANDLER_TAB_PRODUCT_TABINIT = 203;

    public static int HANDLER_TAB_CAI = 6;
    public static int HANDLER_TAB_CAI_REFRESH = 601;
    public static int HANDLER_TAB_CAI_BACK = 602;
    public static int HANDLER_TAB_CAI_TABINIT = 603;
}
