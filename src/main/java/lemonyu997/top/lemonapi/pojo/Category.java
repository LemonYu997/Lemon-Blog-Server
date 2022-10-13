package lemonyu997.top.lemonapi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

//分类表
@Data
public class Category {
    @TableId(type = IdType.AUTO)
    private Integer id;             //id
    private String avatar;          //分类图标
    private String categoryName;    //分类名称
    private String description;     //分类说明
}
