package com.ruoyi.cms.blog.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ruoyi.cms.blog.domain.CmsBlogTag;
import com.ruoyi.cms.blog.domain.CmsBlogType;
import com.ruoyi.cms.blog.mapper.CmsBlogTagMapper;
import com.ruoyi.cms.blog.mapper.CmsBlogTypeMapper;
import com.ruoyi.cms.tag.domain.CmsTag;
import com.ruoyi.cms.tag.mapper.CmsTagMapper;
import com.ruoyi.cms.type.domain.CmsType;
import com.ruoyi.cms.type.mapper.CmsTypeMapper;
import com.ruoyi.cms.type.service.ICmsTypeService;
import com.ruoyi.cms.utils.BatchInsertService;
import com.ruoyi.cms.utils.BatchType;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.impl.SysConfigServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.blog.mapper.CmsBlogMapper;
import com.ruoyi.cms.blog.domain.CmsBlog;
import com.ruoyi.cms.blog.service.ICmsBlogService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章管理Service业务层处理
 *
 * @author ning
 * @date 2022-01-01
 */
@Service
public class CmsBlogServiceImpl implements ICmsBlogService {
    private static final Logger log = LoggerFactory.getLogger(CmsBlogServiceImpl.class);
    @Autowired
    private CmsBlogMapper cmsBlogMapper;

    @Autowired
    private CmsBlogTagMapper cmsBlogTagMapper;

    @Autowired
    private CmsTagMapper cmsTagMapper;

    @Autowired
    private CmsBlogTypeMapper cmsBlogTypeMapper;

    @Autowired
    private CmsTypeMapper cmsTypeMapper;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private BatchInsertService batchInsertService;
    @Autowired
    private ICmsTypeService cmsTypeService;
    @Autowired
    private ISysConfigService sysConfigServiceImpl;

    /**
     * 查询文章管理
     *
     * @param id 文章管理主键
     * @return 文章管理
     */
    @Override
    public CmsBlog selectCmsBlogById(Long id) {
        CmsBlog blog = cmsBlogMapper.selectCmsBlogById(id);
        //查询标签列表
        List<CmsBlogTag> blogTagList = cmsBlogTagMapper.selectBlogTagList(id);
        Long[] tagIds = new Long[blogTagList.size()];
        List<CmsTag> tags = new ArrayList<>();
        List<CmsType> types = new ArrayList<>();
        for (int i = 0; i < blogTagList.size(); i++) {
            CmsBlogTag cmsBlogTag = blogTagList.get(i);
            Long tagId = cmsBlogTag.getTagId();
            tagIds[i] = tagId;
            CmsTag cmsTag = cmsTagMapper.selectCmsTagByTagId(tagId);
            tags.add(cmsTag);
        }
        blog.setTagIds(tagIds);
        blog.setTags(tags);
        //查询分类列表
        List<CmsBlogType> blogTypeList = cmsBlogTypeMapper.selectBlogTypeList(id);
        Long[] typeIds = new Long[blogTypeList.size()];
        for (int i = 0; i < blogTypeList.size(); i++) {
            CmsBlogType cmsBlogType = blogTypeList.get(i);
            Long typeId = cmsBlogType.getTypeId();
            typeIds[i] = typeId;
            CmsType cmsType = cmsTypeMapper.selectCmsTypeByTypeId(typeId);
            types.add(cmsType);
        }
        // 查询菜单列表
        List<Long> menuList = cmsBlogMapper.selectMenuListByBlogId(id);
        blog.setMenuIds(menuList.toArray(new Long[0]));
        blog.setTypeIds(typeIds);
        blog.setTypes(types);
        return blog;
    }

    /**
     * 查询文章管理列表
     *
     * @param cmsBlog 文章管理
     * @return 文章管理
     */
    @Override
    public List<CmsBlog> selectCmsBlogList(CmsBlog cmsBlog) {
        List<CmsBlog> cmsBlogList = cmsBlogMapper.selectCmsBlogList(cmsBlog);
        List<CmsBlog> blogList = BlogListAddTypeAndTag(cmsBlogList);
        return blogList;
    }

    /**
     * 查询推荐文章列表
     */
    @Override
    public List<CmsBlog> selectCmsBlogListRecommend(CmsBlog cmsBlog) {
//        List<CmsBlog> cmsBlogList = cmsBlogMapper.selectCmsBlogListRecommend(cmsBlog);
        String s = sysConfigServiceImpl.selectConfigByKey("right.menuId");
        List<CmsBlog> cmsBlogList = this.selectCmsBlogListByMenuId(Long.parseLong(s));
        return cmsBlogList;
    }

    /**
     * 按分类查询文章列表
     */
    @Override
    public List<CmsBlog> selectCmsBlogListByTypeId(Long id) {
        String typeBlogKey = Constants.getTypeBlogKey(id);
        List<CmsBlog> blogList = redisCache.getCacheObject(typeBlogKey);
        if (blogList == null) {
            CmsType cmsType = cmsTypeMapper.selectCmsTypeByTypeId(id);
            List<CmsBlogTag> typeTagList = cmsTypeMapper.selectTypeTagList(id);
            cmsType.setTagIds(typeTagList.stream().map(CmsBlogTag::getTagId).toArray(Long[]::new));
            Long menuId = cmsType.getMenuId();
//            List<Long> menuList = null;
//            if (menuId != null) {
//                menuList = sysMenuService.getAllChildMenuIdList(cmsType.getMenuId());
//                menuList.add(menuId);
//            }
//            cmsType.setMenuIds(menuList);
            cmsType.setMenuIds(cmsTypeService.selectMenuListByType(id));
            if (cmsType.getMenuIds() == null || cmsType.getMenuIds().isEmpty()) {
                blogList = new ArrayList<>();
            } else {
                List<CmsBlog> cmsBlogList = cmsBlogMapper.selectCmsBlogListByType(cmsType);
                blogList = BlogListAddTypeAndTag(cmsBlogList);
            }
            redisCache.setCacheObject(typeBlogKey, blogList);
            redisCache.setCacheObject(Constants.getTypeBlogCountKey(id), blogList.size());
        }
        return blogList;
    }

    @Override
    public List<CmsBlog> selectCmsBlogListByMenuId(Long id) {
        String menuBlogKey = Constants.getMenuBlogKey(id);
        List<CmsBlog> blogList = redisCache.getCacheObject(menuBlogKey);
        if (blogList == null) {
            List<Long> allChildMenuIdList = sysMenuService.getAllChildMenuIdList(id);
            allChildMenuIdList.add(id);
            CmsType cmsType = new CmsType();
            cmsType.setMenuIds(allChildMenuIdList);
            if (cmsType.getMenuIds() == null || cmsType.getMenuIds().isEmpty()) {
                blogList = new ArrayList<>();
            } else {
                List<CmsBlog> cmsBlogList = cmsBlogMapper.selectCmsBlogListByType(cmsType);
                blogList = BlogListAddTypeAndTag(cmsBlogList);
            }
            redisCache.setCacheObject(menuBlogKey, blogList);
            redisCache.setCacheObject(Constants.getMenuBlogCountKey(id), blogList);
        }
        return blogList;
    }

    /**
     * 按标签查询文章列表
     */
    @Override
    public List<CmsBlog> selectCmsBlogListByTagId(Long id) {
        List<CmsBlog> cmsBlogList = cmsBlogMapper.selectCmsBlogListByTagId(id);
        List<CmsBlog> blogList = BlogListAddTypeAndTag(cmsBlogList);
        return blogList;
    }

    /**
     * 新增文章管理
     *
     * @param cmsBlog 文章管理
     * @return 结果
     */
    @Override
    @Transactional
    public Long insertCmsBlog(CmsBlog cmsBlog) {
        cmsBlog.setCreateTime(DateUtils.getNowDate());
        cmsBlogMapper.insertCmsBlog(cmsBlog);
        Long blogId = cmsBlog.getId();
        //新增文章标签
        Long[] tagIds = cmsBlog.getTagIds();
        int successCount = 0;
        if (tagIds != null && tagIds.length > 0) {
            successCount = batchInsertService.batchInsertTag(tagIds, blogId, BatchType.BLOG_TAG);
            if (successCount != tagIds.length) {
                log.error("标签绑定不全:" + Arrays.toString(tagIds) + "成功绑定" + successCount + "个");
            }
        }
        //新增文章菜单
        Long[] menuIds = cmsBlog.getMenuIds();
        if (menuIds != null && menuIds.length > 0) {
            successCount = batchInsertService.batchInsertTag(menuIds, blogId, BatchType.BLOG_MENU);
            if (successCount != menuIds.length) {
                log.error("菜单绑定不全:" + Arrays.toString(menuIds) + "成功绑定" + successCount + "个");
            }
        }
        clearBlogCache();
        return blogId;
    }

    /**
     * 修改文章管理
     *
     * @param cmsBlog 文章管理
     * @return 结果
     */
    @Override
    @Transactional
    public int updateCmsBlog(CmsBlog cmsBlog) {
        cmsBlog.setUpdateTime(DateUtils.getNowDate());
        Long blogId = cmsBlog.getId();
        //清空文章标签关联
        cmsBlogTagMapper.deleteBlogTagByBlogId(blogId);
        //新增文章标签
        Long[] tagIds = cmsBlog.getTagIds();
        int successCount = 0;
        if (tagIds != null && tagIds.length > 0) {
            successCount = batchInsertService.batchInsertTag(tagIds, blogId, BatchType.BLOG_TAG);
            if (successCount != tagIds.length) {
                log.error("分类标签绑定不全:" + Arrays.toString(tagIds) + "成功绑定" + successCount + "个");
            }
        }
        Long[] menuIds = cmsBlog.getMenuIds();
        //清空文章菜单关联
        cmsBlogMapper.deleteMenuByBlogId(blogId);
        if (menuIds != null && menuIds.length > 0) {
            //新增文章菜单
            successCount = batchInsertService.batchInsertTag(menuIds, blogId, BatchType.BLOG_MENU);
            if (successCount != menuIds.length) {
                log.error("分类菜单绑定不全:" + Arrays.toString(menuIds) + "成功绑定" + successCount + "个");
            }
        }
        clearBlogCache();
        return cmsBlogMapper.updateCmsBlog(cmsBlog);
    }

    private void clearBlogCache() {
        redisCache.deleteKeysByPrefix(Constants.TYPE_BLOG_KEY);
        redisCache.deleteKeysByPrefix(Constants.TYPE_BLOG_COUNT_KEY);
        redisCache.deleteKeysByPrefix(Constants.MENU_BLOG_KEY);
        redisCache.deleteKeysByPrefix(Constants.MENU_BLOG_COUNT_KEY);
    }

    /**
     * 批量删除文章管理
     *
     * @param ids 需要删除的文章管理主键
     * @return 结果
     */
    @Override
    public int deleteCmsBlogByIds(Long[] ids) {
        for (Long id : ids) {
            //清空文章分类关联
            cmsBlogTypeMapper.deleteBlogTypeByBlogId(id);
            //清空文章标签关联
            cmsBlogTagMapper.deleteBlogTagByBlogId(id);
        }
        clearBlogCache();
        return cmsBlogMapper.deleteCmsBlogByIds(ids);
    }

    /**
     * 删除文章管理信息
     *
     * @param id 文章管理主键
     * @return 结果
     */
    @Override
    public int deleteCmsBlogById(Long id) {
        //清空文章分类关联
        cmsBlogTypeMapper.deleteBlogTypeByBlogId(id);
        //清空文章标签关联
        cmsBlogTagMapper.deleteBlogTagByBlogId(id);
        return cmsBlogMapper.deleteCmsBlogById(id);
    }

    @Override
    public List<Long> selectMenuListByBlog(Long blogId) {
        CmsBlog blog = cmsBlogMapper.selectCmsBlogById(blogId);
        return cmsBlogMapper.selectMenuListByBlogId(blog.getId());
    }

    @Override
    public void visitBlog(Long id) {
        CmsBlog cmsBlog = selectCmsBlogById(id);
        Long views = cmsBlog.getViews();
        views += Long.parseLong("0");
        cmsBlog.setViews(views);
        cmsBlogMapper.updateCmsBlog(cmsBlog);
    }

    @Override
    public int approveBlog(CmsBlog cmsBlog) {
        int i = cmsBlogMapper.updateCmsBlog(cmsBlog);
        clearBlogCache();
        return i;
    }

    private List<CmsBlog> BlogListAddTypeAndTag(List<CmsBlog> cmsBlogList) {
        if (cmsBlogList == null || cmsBlogList.size() < 0) {
            return cmsBlogList;
        }
        for (CmsBlog blog : cmsBlogList) {
            Long blogId = blog.getId();
            //查询标签列表
            List<CmsBlogTag> blogTagList = cmsBlogTagMapper.selectBlogTagList(blogId);
            List<CmsTag> cmsTagList = new ArrayList<>();
            blogTagList.forEach((CmsBlogTag cmsBlogTag) -> {
                Long tagId = cmsBlogTag.getTagId();
                CmsTag cmsTag = cmsTagMapper.selectCmsTagByTagId(tagId);
                cmsTagList.add(cmsTag);
            });
            blog.setTags(cmsTagList);
            //查询分类列表
            List<CmsBlogType> blogTypeList = cmsBlogTypeMapper.selectBlogTypeList(blogId);
            List<CmsType> cmsTypeList = new ArrayList<>();
            blogTypeList.forEach((CmsBlogType cmsBlogType) -> {
                Long typeId = cmsBlogType.getTypeId();
                CmsType cmsType = cmsTypeMapper.selectCmsTypeByTypeId(typeId);
                cmsTypeList.add(cmsType);
            });
            blog.setTypes(cmsTypeList);
        }
        return cmsBlogList;
    }
}
