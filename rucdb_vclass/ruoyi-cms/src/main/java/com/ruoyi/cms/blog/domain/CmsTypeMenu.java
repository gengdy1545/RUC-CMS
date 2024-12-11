package com.ruoyi.cms.blog.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

@Alias("CmsTypeMenu") //防止mybatis识别不到
@Data
public class CmsTypeMenu {
    private Long typeId;
    private Long menuId;
}
