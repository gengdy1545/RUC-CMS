package com.ruoyi.cms.blog.domain;

import com.ruoyi.cms.tag.domain.CmsTag;
import com.ruoyi.cms.type.domain.CmsType;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 文章管理对象 cms_blog
 *
 * @author ning
 * @date 2022-01-01
 */
@Alias("CmsBlog") //防止mybatis识别不到
@Data
public class CmsBlog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 标题
     */
    @Excel(name = "标题")
    private String title;

    /**
     * 类型 1文章 2随笔
     */
    @Excel(name = "类型")
    private String type;

    /**
     * 内容
     */
    @Excel(name = "内容")
    private String content;

    /**
     * 文本编辑器类型 1 Quill富文本编辑器 2 CherryMarkdown
     */
    @Excel(name = "文本编辑器类型")
    private String contentType;

    /**
     * Markdown格式内容
     */
    @Excel(name = "Markdown格式内容")
    private String contentMarkdown;

    /**
     * 置顶（0否 1是）
     */
    @Excel(name = "置顶", readConverterExp = "0=否,1=是")
    private String top;

    /**
     * 阅读
     */
    @Excel(name = "阅读")
    private Long views;

    /**
     * 状态（0暂存 1发布）
     */
    @Excel(name = "状态", readConverterExp = "0=暂存,1=发布")
    private String status;

    /**
     * 首页图片类型（0地址 1上传）
     */
    @Excel(name = "首页图片类型", readConverterExp = "0=地址,1=上传")
    private String blogPicType;

    /** 首页图片（ 1上传） */
    @Excel(name = "首页图片（ 1上传）")
    private String blogPic;

    /** 首页图片（ 0地址） */
    @Excel(name = "首页图片（ 0地址）")
    private String blogPicLink;

    /** 简介 */
    @Excel(name = "简介")
    private String blogDesc;

    /** 附件列表 */
    private String blogFiles;

    /**
     * 分类
     */
    private Long[] typeIds;
    private Long[] menuIds;

    /**
     * 分类
     */
    private Long[] tagIds;

    private List<CmsTag> tags;

    private List<CmsType> types;

}
