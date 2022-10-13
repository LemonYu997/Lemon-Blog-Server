package lemonyu997.top.lemonapi.vo;

import lombok.Data;

//用于首页展示归档信息的封装对象，不需要持久化
@Data
public class ArchiveVO {
    private String year;   //年份
    private String month;  //月份
    private Integer count; //篇数
}
