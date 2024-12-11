package com.ruoyi.cms.fileInfo.domain;
/**
 * @program: RuoYi-Vue
 * @Author: WangNing
 * @Description: 〈blog文件关联 cms_blog_file〉
 * @Date: 2022/1/2 0:41
 */

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Author: WangNing
 * @Description:〈blog文件关联 cms_blog_file〉
 * @Date: 2022/1/2 0:41
 **/
@Data
public class FileBlogInfo {
    private Long fileId;
    private Long blogId;

    private Long[] fileIds;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fileId", getFileId())
                .append("blogId", getBlogId())
                .toString();
    }

}
