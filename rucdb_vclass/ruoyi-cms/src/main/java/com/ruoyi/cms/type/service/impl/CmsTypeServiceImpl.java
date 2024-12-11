package com.ruoyi.cms.type.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ruoyi.cms.blog.domain.CmsBlogTag;
import com.ruoyi.cms.blog.mapper.CmsBlogTypeMapper;
import com.ruoyi.cms.blog.service.impl.CmsBlogServiceImpl;
import com.ruoyi.cms.fileInfo.service.ISysFileInfoService;
import com.ruoyi.cms.tag.domain.CmsTag;
import com.ruoyi.cms.tag.service.ICmsTagService;
import com.ruoyi.cms.utils.BatchInsertService;
import com.ruoyi.cms.utils.BatchType;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.type.mapper.CmsTypeMapper;
import com.ruoyi.cms.type.domain.CmsType;
import com.ruoyi.cms.type.service.ICmsTypeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分类管理Service业务层处理
 *
 * @author ning
 * @date 2022-01-02
 */
@Service
public class CmsTypeServiceImpl implements ICmsTypeService {

    private static final Logger log = LoggerFactory.getLogger(CmsTypeServiceImpl.class);
    @Autowired
    private CmsTypeMapper cmsTypeMapper;

    @Autowired
    private ISysFileInfoService sysFileInfoService;

    @Autowired
    private CmsBlogTypeMapper cmsBlogTypeMapper;
    @Autowired
    private ICmsTagService cmsTagMapper;
    @Autowired
    private CmsBlogServiceImpl cmsBlogServiceImpl;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private BatchInsertService batchInsertService;

    /**
     * 查询分类管理
     *
     * @param typeId 分类管理主键
     * @return 分类管理
     */
    @Override
    public CmsType selectCmsTypeByTypeId(Long typeId) {
        CmsType cmsBlog = cmsTypeMapper.selectCmsTypeByTypeId(typeId);
        //查询标签列表
        List<CmsBlogTag> blogTagList = cmsTypeMapper.selectTypeTagList(typeId);
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
        cmsBlog.setTagIds(tagIds);
        cmsBlog.setTags(tags);
        return cmsBlog;
    }


    @Override
    public List<Long> selectMenuListByType(Long typeId) {
        CmsType type = cmsTypeMapper.selectCmsTypeByTypeId(typeId);
        return cmsTypeMapper.selectMenuListByTypeId(typeId, type.isMenuCheckStrictly());
    }
    /**
     * 查询分类管理列表
     *
     * @param cmsType 分类管理
     * @return 分类管理
     */
    @Override
    public List<CmsType> selectCmsTypeList(CmsType cmsType) {
        List<CmsType> cmsTypeList = cmsTypeMapper.selectCmsTypeList(cmsType);
        for (CmsType type : cmsTypeList) {
//            int blogNum = cmsBlogTypeMapper.countBlogByTypeId(type.getTypeId());
//            type.setBlogNum(blogNum);
            if (redisCache.getCacheObject(Constants.getTypeBlogCountKey(type.getTypeId())) != null) {
                type.setBlogNum((Integer) redisCache.getCacheObject(Constants.getTypeBlogCountKey(type.getTypeId())));
            } else {
                type.setBlogNum(cmsBlogServiceImpl.selectCmsBlogListByTypeId(type.getTypeId()).size());
            }
        }
        return cmsTypeList;
    }

    /**
     * 新增分类管理
     *
     * @param cmsType 分类管理
     * @return 结果
     */
    @Override
    @Transactional
    public int insertCmsType(CmsType cmsType) {
        List<CmsType> cmsTypeList = cmsTypeMapper.selectCmsTypeListByTypeName(cmsType.getTypeName());
        if (cmsTypeList.size() > 0) {
            throw new ServiceException("分类名称已存在");
        }
        cmsType.setCreateTime(DateUtils.getNowDate());
        int i = cmsTypeMapper.insertCmsType(cmsType);
        Long typeId = cmsType.getTypeId();
        Long[] tagIds = cmsType.getTagIds();
        int successCount = batchInsertService.batchInsertTag(tagIds, typeId, BatchType.TYPE_TAG);
        if(successCount != tagIds.length){
           log.error("分类标签绑定不全:"+ Arrays.toString(tagIds) + "成功绑定"+successCount+"个");
        }
        bathInsertTypeMenu(cmsType);
        return i;
    }



    /**
     * 修改分类管理
     *
     * @param cmsType 分类管理
     * @return 结果
     */
    @Override
    @Transactional
    public int updateCmsType(CmsType cmsType) {
        List<CmsType> cmsTypeList = cmsTypeMapper.selectCmsTypeListByTypeName(cmsType.getTypeName());
        if (cmsTypeList.size() > 0) {
            for (CmsType type : cmsTypeList) {
                if (!type.getTypeId().equals(cmsType.getTypeId())) {
                    throw new ServiceException("分类名称已存在");
                }
            }
        }
        String typePic = cmsTypeMapper.selectCmsTypeByTypeId(cmsType.getTypeId()).getTypePic();
        if (typePic != null && !"".equals(typePic) && !typePic.equals(cmsType.getTypePic())) {
            int newFileNameSeparatorIndex = typePic.lastIndexOf("/");
            String FileName = typePic.substring(newFileNameSeparatorIndex + 1).toLowerCase();
            sysFileInfoService.deleteSysFileInfoByFileObjectName(FileName);
        }
        //清空文章标签关联
        cmsTypeMapper.deleteTypeTagByTypeId(cmsType.getTypeId());
        //新增文章标签
        Long[] tagIds = cmsType.getTagIds();
        if (tagIds != null && tagIds.length > 0) {
            int successCount = batchInsertService.batchInsertTag(tagIds, cmsType.getTypeId(), BatchType.TYPE_TAG);
            if(successCount != tagIds.length){
                log.error("分类标签绑定不全:"+ Arrays.toString(tagIds) + "成功绑定"+successCount+"个");
            }
        }

        bathInsertTypeMenu(cmsType);
        cmsType.setUpdateTime(DateUtils.getNowDate());
        redisCache.deleteObject(Constants.getTypeBlogCountKey(cmsType.getTypeId()));
        redisCache.deleteObject(Constants.getTypeBlogKey(cmsType.getTypeId()));
        return cmsTypeMapper.updateCmsType(cmsType);
    }

    private void bathInsertTypeMenu(CmsType cmsType) {
        cmsTypeMapper.deleteTypeMenuByTypeId(cmsType.getTypeId());
        if (cmsType.getMenuIds() != null && !cmsType.getMenuIds().isEmpty()) {
            List<Long> menuIds = cmsType.getMenuIds();
            List<CmsType> typeList = new ArrayList<>();
            for (Long menuId : menuIds) {
                CmsType type = new CmsType();
                type.setTypeId(cmsType.getTypeId());
                type.setMenuId(menuId);
                typeList.add(type);
            }
            cmsTypeMapper.batchTypeMenu(typeList);
        }
    }

    /**
     * 批量删除分类管理
     *
     * @param typeIds 需要删除的分类管理主键
     * @return 结果
     */
    @Override
    public int deleteCmsTypeByTypeIds(Long[] typeIds) {
        for (Long typeId : typeIds) {
            String typePic = cmsTypeMapper.selectCmsTypeByTypeId(typeId).getTypePic();
            if (typePic != null && !"".equals(typePic)) {
                int newFileNameSeparatorIndex = typePic.lastIndexOf("/");
                String FileName = typePic.substring(newFileNameSeparatorIndex + 1).toLowerCase();
                sysFileInfoService.deleteSysFileInfoByFileObjectName(FileName);
            }
            //删除分类文章关联表信息
            cmsBlogTypeMapper.deleteBlogTypeByTypeId(typeId);
        }
        return cmsTypeMapper.deleteCmsTypeByTypeIds(typeIds);
    }

    /**
     * 删除分类管理信息
     *
     * @param typeId 分类管理主键
     * @return 结果
     */
    @Override
    public int deleteCmsTypeByTypeId(Long typeId) {
        String typePic = cmsTypeMapper.selectCmsTypeByTypeId(typeId).getTypePic();
        if (typePic != null && !"".equals(typePic)) {
            int newFileNameSeparatorIndex = typePic.lastIndexOf("/");
            String FileName = typePic.substring(newFileNameSeparatorIndex + 1).toLowerCase();
            sysFileInfoService.deleteSysFileInfoByFileObjectName(FileName);
        }
        //删除分类文章关联表信息
        cmsBlogTypeMapper.deleteBlogTypeByTypeId(typeId);
        return cmsTypeMapper.deleteCmsTypeByTypeId(typeId);
    }

    /**
     * 取消按钮-删除分类图片
     *
     * @param cmsType 分类管理
     * @return 结果
     */
    @Override
    public int cancel(CmsType cmsType) {
        String typePic = cmsType.getTypePic();
        if (typePic != null && !"".equals(typePic)) {
            Long typeId = cmsType.getTypeId();
            if (typeId == null) {
                int newFileNameSeparatorIndex = typePic.lastIndexOf("/");
                String FileName = typePic.substring(newFileNameSeparatorIndex + 1).toLowerCase();
                sysFileInfoService.deleteSysFileInfoByFileObjectName(FileName);
            } else {
                String Pic = cmsTypeMapper.selectCmsTypeByTypeId(typeId).getTypePic();
                if (!typePic.equals(Pic)) {
                    int newFileNameSeparatorIndex = typePic.lastIndexOf("/");
                    String FileName = typePic.substring(newFileNameSeparatorIndex + 1).toLowerCase();
                    sysFileInfoService.deleteSysFileInfoByFileObjectName(FileName);
                }
            }
        }
        return 1;
    }
}
