package org.javahub.submarine.modules.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.javahub.submarine.base.BaseEntity;
import org.javahub.submarine.common.dto.XPage;
import org.javahub.submarine.modules.system.entity.Dept;
import org.javahub.submarine.modules.system.mapper.DeptMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeptService extends ServiceImpl<DeptMapper, Dept> {

    @Resource
    private DeptMapper deptMapper;

    @Resource
    private ApplicationContext applicationContext;

    @Transactional(readOnly = true)
    public XPage<Dept> findDeptList(Dept dept, XPage xPage) {
        XPage<Dept> deptXPage = deptMapper.findPage(xPage, dept);
        return deptXPage;
    }

    @Transactional(readOnly = true)
    public List<Dept> findDeptList(Dept dept) {
        return deptMapper.findList(dept);
    }

    @Transactional
    public void saveDept(Dept dept) {
        Dept originalDept = null;
        if(Objects.nonNull(dept.getId())) {
            originalDept = this.getDeptById(dept.getId());
        }
        Dept parent = super.getById(dept.getPid());
        if(parent != null) {
            dept.setPids(String.format("%s%s,", Optional.ofNullable(parent.getPids()).orElse(""), parent.getId()));
        }
        if(Objects.nonNull(dept.getId()) && !StringUtils.equals(dept.getName(), originalDept.getName())) {
            applicationContext.publishEvent(dept.updateName());
        }
        super.saveOrUpdate(dept);
    }

    @Transactional
    public Dept getDeptById(Long id) {
        return deptMapper.selectById(id);
    }

    @Transactional
    public void deleteDept(Long id) {
        // 删除子级和自身
        List<Dept> deptList = super.lambdaQuery().like(id != null, Dept::getPids, id).list();
        List<Long> ids = deptList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        ids.add(id);
        super.removeByIds(ids);
    }
}
