package com.ruoyi.cms.type.domain;

import com.ruoyi.cms.tag.domain.CmsTag;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Alias("CmsType")
/**
 * 分类管理对象 cms_type
 * 
 * @author ning
 * @date 2022-01-02
 */
public class CmsType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    private Long typeId;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String typeName;
    @Excel
    private Long menuId;
    @Excel
    private String menuName;
    private List<Long> menuIds;
    private Long[] tagIds;
    private List<CmsTag> tags;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
    private boolean menuCheckStrictly;
    /**
     * 分类图像类型（0地址 1上传）
     */
    @Excel(name = "分类图像类型（0地址 1上传）", readConverterExp = "0=地址,1=上传")
    private String typePicType;

    /** 分类图像（ 1上传） */
    @Excel(name = "分类图像（ 1上传）")
    private String typePic;

    /** 分类图像（ 0地址） */
    @Excel(name = "分类图像（ 0地址）")
    private String typePicLink;

    /** 文章数量 */
    private int blogNum;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("typeId", getTypeId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("typeName", getTypeName())
            .append("typePic", getTypePic())
                .append("typePicType", getTypePicType())
                .append("typePicLink", getTypePicLink())
                .append("blogNum", getBlogNum())
            .toString();
    }
}
