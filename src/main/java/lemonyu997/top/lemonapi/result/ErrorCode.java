package lemonyu997.top.lemonapi.result;

//统一错误码
public enum ErrorCode {
    PARAMS_ERROR(101, "参数有误"),
    ACCOUNT_PWD_NOT_EXIST(102, "用户名或密码不存在"),
    TOKEN_ERROR(103, "token不合法"),
    ACCOUNT_EXIST(104, "账号已存在"),
    PUBLISH_FAIL(105, "发布失败，请检查表单是否完整"),
    COMMENT_FAIL(201, "评论失败"),
    COMMENT_EMAIL_OR_NAME_NOT_EXIST(202, "昵称和姓名不能为空"),
    COMMENT_CONTENT_NOT_EXIST(203, "不能发送空的评论"),
    EMAIL_ERROR(204, "邮箱格式错误"),
    NO_PERMISSSION(901, "无访问权限"),
    SESSION_TIME_OUT(902, "会话超时"),
    NO_LOGIN(903, "未登录");

    private int code;
    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
