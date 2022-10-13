package lemonyu997.top.lemonapi.vo.params;

import lombok.Data;

//用来接收前端传过来的分页参数
@Data
public class PageParams {
    private Integer page = 1;       //当前页，默认为1
    private Integer pageSize = 10;  //分页大小，默认为10

    //用于文章分类展示
    private Integer categoryId;
    private Long tagId;

    //用于文章归档
    private String year;
    private String month;

    //自定义month的获得方式
    public String getMonth() {
        if (this.month != null && this.month.length() == 1) {
            return "0" + this.month;
        }
        return this.month;
    }
}
