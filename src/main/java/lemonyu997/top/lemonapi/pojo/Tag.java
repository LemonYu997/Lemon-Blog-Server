package lemonyu997.top.lemonapi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

//标签表
@Data
public class Tag {
    @TableId(type = IdType.AUTO)
    private Long id;            //Id
    private String tagName;     //标签名
}
