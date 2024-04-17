package com.zvz09.xiaochen.mcmp.controller;

import com.zvz09.xiaochen.common.web.controller.BaseController;
import com.zvz09.xiaochen.mcmp.domain.dto.RegionDTO;
import com.zvz09.xiaochen.mcmp.domain.entity.Region;
import com.zvz09.xiaochen.mcmp.service.IRegionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcmp/region")
@Tag(name = "地域管理")
public class RegionController extends BaseController<IRegionService, Region, RegionDTO> {
}
