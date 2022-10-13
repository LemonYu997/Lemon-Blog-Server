package lemonyu997.top.lemonapi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Recommend {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String picture;
    private String nickname;
    private String src;
    private String content;
    private Long createDate;
}
