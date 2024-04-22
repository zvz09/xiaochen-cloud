package com.zvz09.xiaochen.mc.controller;

import com.zvz09.xiaochen.common.web.controller.BaseController;
import com.zvz09.xiaochen.mc.domain.dto.RegionDTO;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.service.IRegionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mc/region")
@Tag(name = "地域管理")
public class RegionController extends BaseController<IRegionService, Region, RegionDTO> {

    public RegionController(IRegionService baseService) {
        super(baseService);
    }
}
