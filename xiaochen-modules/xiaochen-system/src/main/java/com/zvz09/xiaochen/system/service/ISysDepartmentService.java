package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.dto.dept.DepartmentDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysDepartment;
import com.zvz09.xiaochen.system.api.domain.vo.SysDepartmentVo;

import java.util.List;

/**
 * 部门管理 服务类
 *
 * @author zvz09
 * @date 2023-10-10 11:49:52
 */

public interface ISysDepartmentService extends IService<SysDepartment> {

    void createDepartment(DepartmentDto departmentDto);

    void deleteDepartmentByIds(List<Long> ids);

    void deleteDepartment(Long id);

    void updateDepartment(DepartmentDto departmentDto);

    List<SysDepartmentVo> getDepartmentTree();
}
