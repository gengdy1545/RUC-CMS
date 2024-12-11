package com.ruoyi.cms.utils;

import com.ruoyi.cms.blog.domain.CmsBlogTag;
import com.ruoyi.cms.blog.mapper.CmsBlogMapper;
import com.ruoyi.cms.blog.mapper.CmsBlogTagMapper;
import com.ruoyi.cms.type.mapper.CmsTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatchInsertService {
    @Autowired
    CmsBlogTagMapper cmsBlogTagMapper;
    @Autowired
    CmsBlogMapper cmsBlogMapper;
    @Autowired
    CmsTypeMapper cmsTypeMapper;
    public int batchInsertTag(Long[] ids, Long id,BatchType batchType) {
        if (ids != null && ids.length > 0) {
            List<CmsBlogTag> blogTagList = new ArrayList<>();
            for (Long tagId : ids) {
                CmsBlogTag cmsBlogTag = new CmsBlogTag();
                cmsBlogTag.setBlogId(id);
                cmsBlogTag.setTagId(tagId);
                blogTagList.add(cmsBlogTag);
            }
            if(BatchType.BLOG_TAG.equals(batchType)){
                return cmsBlogTagMapper.batchBlogTag(blogTagList);
            }else if(BatchType.TYPE_TAG.equals(batchType)){
                return cmsTypeMapper.batchTypeTag(blogTagList);
            }else if(BatchType.BLOG_MENU.equals(batchType)){
                return cmsBlogMapper.batchBlogMenu(blogTagList);
            }
        }
        return 0;
    }
}
