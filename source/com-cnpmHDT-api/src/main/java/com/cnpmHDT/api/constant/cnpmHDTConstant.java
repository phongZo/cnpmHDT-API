package com.cnpmHDT.api.constant;


import com.cnpmHDT.api.utils.ConfigurationService;

public class cnpmHDTConstant {
    public static final String ROOT_DIRECTORY =  ConfigurationService.getInstance().getString("file.upload-dir","/tmp/upload");

    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_CUSTOMER = 2;
    public static final Integer USER_KIND_EMPLOYEE = 3;
    public static final Integer USER_KIND_COLLABORATOR = 4;

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;

    public static final Integer ORDER_VAT = 10;

    public static final Integer ORDERS_STATE_CREATED = 0;
    public static final Integer ORDERS_STATE_DONE = 1;
    public static final Integer ORDERS_STATE_CANCELED = 2;


    public static final Integer GROUP_KIND_CUSTOMER = 2;


    public static final Integer MAX_ATTEMPT_FORGET_PWD = 5;
    public static final Integer MAX_TIME_FORGET_PWD = 5 * 60 * 1000; //5 minutes
    public static final Integer MAX_ATTEMPT_LOGIN = 5;
    public static final Integer MAX_TIME_VERIFY_ACCOUNT = 5 * 60 * 1000; //5 minutes

    public static final Integer CATEGORY_KIND = 1;

    public static final Integer PROVINCE_KIND_PROVINCE = 0;
    public static final Integer PROVINCE_KIND_DISTRICT = 1;
    public static final Integer PROVINCE_KIND_COMMUNE = 2;

    public static final Integer ORDERS_STATE_PENDING = 1;


    public static final Integer GENDER_MALE = 1;
    public static final Integer GENDER_FEMALE = 2;
    public static final Integer GENDER_OTHER = 3;

    private cnpmHDTConstant(){
        throw new IllegalStateException("Utility class");
    }

}
