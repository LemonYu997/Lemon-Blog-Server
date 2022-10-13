package lemonyu997.top.lemonapi.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//统一返回的封装对象
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    //true成功，false失败
    private boolean success;
    //返回状态码，200表示成功
    private int code;
    //提示信息
    private String msg;
    //要封装的数据
    private Object data;

    //成功时封装的信息
    public static Result success(Object data) {
        return new Result(true, 200, "success", data);
    }

    //失败时封装的信息
    public static Result fail(int code, String msg) {
        return new Result(false, code, msg, null);
    }
}
