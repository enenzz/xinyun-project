package com.yunbian.constant;

/**
 * 系统常量
 */
public class SystemConstants {

    private SystemConstants() {}

    /**
     * 项目名称
     */
    public static final String PROJECT_NAME = "xinyun";

    /**
     * 项目根路径
     */
    public static final String PROJECT_ROOT = "xinyun-service/";

    /**
     * 静态资源目录
     */
    public static final String STATIC_RESOURCES = PROJECT_ROOT + "src/main/resources/static/";

    /**
     * 图片存储目录
     */
    public static final String IMAGES_PATH = STATIC_RESOURCES + "images/";

    /**
     * 默认头像
     */
    public static final String DEFAULT_AVATAR = " https://enenzz.oss-cn-shenzhen.aliyuncs.com/xinyun/images/default_avatar.jpg";

    /**
     * OSS 上传基础路径
     */
    public static final String OSS_BASE_PATH = "xinyun/images/";

    public static final Integer USER_STATUS_NORMAL = 1;

    public static final Integer DEFAULT_GENDER = 0;
}
