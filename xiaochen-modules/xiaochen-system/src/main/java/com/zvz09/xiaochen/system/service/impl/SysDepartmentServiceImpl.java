package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.system.api.RemoteDepartmentService;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.dto.dept.DepartmentDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysDepartment;
import com.zvz09.xiaochen.system.api.domain.vo.SysDepartmentVo;
import com.zvz09.xiaochen.system.mapper.SysDepartmentMapper;
import com.zvz09.xiaochen.system.service.ISysDepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 部门管理 服务实现类
 *
 * @author zvz09
 * @date 2023-10-10 11:49:52
 */
@Slf4j
@Service
@RestController
@Tag(name = "feign-部门接口")
@RequestMapping(FeignPath.DEPARTMENT)
@RequiredArgsConstructor
public class SysDepartmentServiceImpl extends ServiceImpl<SysDepartmentMapper, SysDepartment> implements ISysDepartmentService, RemoteDepartmentService {

    @Override
    public void createDepartment(DepartmentDto departmentDto) {
        if (this.count(new LambdaQueryWrapper<SysDepartment>()
                .eq(SysDepartment::getDeptName, departmentDto.getDeptName()).or()
        ) > 0) {
            String msg = "部门名称已存在";
            throw new BusinessException(msg);
        }

        this.save(departmentDto.convertedToPo());
    }

    @Override
    public void deleteDepartmentByIds(List<Long> ids) {
        if (ids == null || !ids.isEmpty()) {
            return;
        }
        this.update(new LambdaUpdateWrapper<SysDepartment>().in(SysDepartment::getId, ids)
                .set(SysDepartment::getDeleted, true));
    }

    @Override
    public void deleteDepartment(Long id) {
        if (id == null) {
            return;
        }
        this.update(new LambdaUpdateWrapper<SysDepartment>().eq(SysDepartment::getId, id)
                .set(SysDepartment::getDeleted, true));
    }

    @Override
    public void updateDepartment(DepartmentDto departmentDto) {
        if (this.count(new LambdaQueryWrapper<SysDepartment>()
                .eq(SysDepartment::getDeptName, departmentDto.getDeptName())
                .ne(SysDepartment::getId, departmentDto.getId())
        ) > 0) {
            String msg = "部门名称已存在";
            throw new BusinessException(msg);
        }
        this.updateById(departmentDto.convertedToPo());
    }

    @Override
    public List<SysDepartmentVo> getDepartmentTree() {
        List<SysDepartment> departments = this.list();
        if (departments == null || departments.isEmpty()) {
            return null;
        }
        List<SysDepartmentVo> voList = new ArrayList<>();
        departments.stream().filter(t -> t.getParentId() == 0)
                .forEach((department) -> {
                    SysDepartmentVo sysDepartmentVo = new SysDepartmentVo(department);
                    sysDepartmentVo.setChildren(this.getChildren(department, departments));
                    voList.add(sysDepartmentVo);
                });
        return voList;
    }

    private List<SysDepartmentVo> getChildren(SysDepartment root, List<SysDepartment> departments) {
        List<SysDepartmentVo> voList = new ArrayList<>();
        departments.stream().filter(t -> Objects.equals(t.getParentId(), root.getId()))
                .forEach((department) -> {
                    SysDepartmentVo sysDepartmentVo = new SysDepartmentVo(department);
                    sysDepartmentVo.setChildren(this.getChildren(department, departments));
                    voList.add(sysDepartmentVo);
                });
        voList.sort(new Comparator<SysDepartmentVo>() {
            @Override
            public int compare(SysDepartmentVo o1, SysDepartmentVo o2) {
                return o1.getOrderNum().compareTo(o2.getOrderNum());
            }
        });
        return voList;
    }

    @Override
    public SysDepartment getById(Long deptId) {
        return super.getById(deptId);
    }
}
