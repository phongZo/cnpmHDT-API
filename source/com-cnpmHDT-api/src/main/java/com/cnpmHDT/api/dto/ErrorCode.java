package com.cnpmHDT.api.dto;

public class ErrorCode {

    /**
     * General error code
     */
    public static final String GENERAL_ERROR_UNAUTHORIZED = "ERROR-GENERAL-000";
    public static final String GENERAL_ERROR_NOT_FOUND = "ERROR-GENERAL-001";
    public static final String GENERAL_ERROR_BAD_REQUEST = "ERROR-GENERAL-002";
    public static final String GENERAL_ERROR_LOGIN_FAILED = "ERROR-GENERAL-003";
    public static final String GENERAL_ERROR_NOT_MATCH = "ERROR-GENERAL-004";
    public static final String GENERAL_ERROR_WRONG_HASH = "ERROR-GENERAL-005";
    public static final String GENERAL_ERROR_LOCKED = "ERROR-GENERAL-006";
    public static final String GENERAL_ERROR_INVALID = "ERROR-GENERAL-007";

    /**
     * Category error code
     */
    public static final String CATEGORY_ERROR_UNAUTHORIZED = "ERROR-CATEGORY-000";
    public static final String CATEGORY_ERROR_NOT_FOUND = "ERROR-CATEGORY-001";

    /**
     * Account error code
     */
    public static final String ACCOUNT_ERROR_EXIST = "ERROR-ACCOUNT-000";

    /**
     * Group error code
     */
    public static final String GROUP_ERROR_UNAUTHORIZED = "ERROR-GROUP-000";
    public static final String GROUP_ERROR_NOT_FOUND = "ERROR-GROUP-001";
    public static final String GROUP_ERROR_EXIST = "ERROR-GROUP-002";
    public static final String GROUP_ERROR_CAN_NOT_DELETED = "ERROR-GROUP-003";

    /**
     * Permission error code
     */
    public static final String PERMISSION_ERROR_UNAUTHORIZED = "ERROR-PERMISSION-000";
    public static final String PERMISSION_ERROR_NOT_FOUND = "ERROR-PERMISSION-001";

    /**
     * News error code
     */
    public static final String NEWS_ERROR_UNAUTHORIZED = "ERROR-NEWS-000";
    public static final String NEWS_ERROR_NOT_FOUND = "ERROR-NEWS-001";

    /**
     * Customer error code
     */
    public static final String CUSTOMER_ERROR_UNAUTHORIZED = "ERROR-CUSTOMER-000";
    public static final String CUSTOMER_ERROR_NOT_FOUND = "ERROR-CUSTOMER-001";
    public static final String CUSTOMER_ERROR_BAD_REQUEST = "ERROR-CUSTOMER-002";

    /**
     * Province error code
     */
    public static final String PROVINCE_ERROR_UNAUTHORIZED = "ERROR-PROVINCE-000";
    public static final String PROVINCE_ERROR_NOT_FOUND = "ERROR-PROVINCE-001";
    public static final String PROVINCE_ERROR_BAD_REQUEST = "ERROR-PROVINCE-002";

    public static final String PRODUCT_ERROR_UNAUTHORIZED = "ERROR-PRODUCT-000";
    public static final String PRODUCT_ERROR_NOT_FOUND = "ERROR-PRODUCT-001";
    public static final String PRODUCT_ERROR_BAD_REQUEST = "ERROR-PRODUCT-002";


    public static final String ORDERS_ERROR_UNAUTHORIZED = "ERROR-ORDERS-000";
    public static final String ORDERS_ERROR_NOT_FOUND = "ERROR-ORDERS-001";
    public static final String ORDERS_ERROR_BAD_REQUEST = "ERROR-ORDERS-002";

    public static final String ORDERS_DETAIL_ERROR_UNAUTHORIZED = "ERROR-ORDERS-DETAIL-000";
    public static final String ORDERS_DETAIL_ERROR_NOT_FOUND = "ERROR-ORDERS-DETAIL-001";
    public static final String ORDERS_DETAIL_ERROR_BAD_REQUEST = "ERROR-ORDERS-DETAIL-002";

    private ErrorCode() { throw new IllegalStateException("Utility class"); }
}
