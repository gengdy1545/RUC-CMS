package com.ruoyi.cms.blog.domain;
/**
 * @program: RuoYi-Vue
 * @Author: WangNing
 * @Description: 〈blog标签关联表 cms_blog_tag〉
 * @Date: 2022/1/2 22:54
 */

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

/**
 * @Author: WangNing
 * @Description:〈blog标签关联表 cms_blog_tag〉
 * @Date: 2022/1/2 22:54
 **/
@Alias("CmsBlogTag") //防止mybatis识别不到
public class CmsBlogTag {
    private Long tagId;
    private Long blogId;
    private Long[] tagIds;

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long[] getTagIds() {
        return tagIds;
    }

    public void setTagIds(Long[] tagIds) {
        this.tagIds = tagIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("tagId", getTagId())
                .append("blogId", getBlogId())
                .toString();
    }
}
