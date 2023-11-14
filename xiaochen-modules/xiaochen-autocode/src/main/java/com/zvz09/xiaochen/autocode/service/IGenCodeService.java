/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.autocode.service
 * @className com.zvz09.xiaochen.autocode.service.IGenCodeService
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.autocode.service;

import com.zvz09.xiaochen.autocode.domain.dto.gencode.CreateSqlDto;
import com.zvz09.xiaochen.autocode.domain.dto.gencode.GenConfig;
import com.zvz09.xiaochen.autocode.domain.dto.template.AutoCodeTemplateDto;
import com.zvz09.xiaochen.autocode.domain.vo.PreviewCodeVo;

import java.util.List;

/**
 * IGenCodeService
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/20 15:28
 */
public interface IGenCodeService {


    GenConfig parseCreateSql(CreateSqlDto createSqlDto);

    List<PreviewCodeVo> previewCode(GenConfig genConfig);

    byte[] downloadCode(GenConfig genConfig);

    String verifyAutoCodeTemplate(AutoCodeTemplateDto autoCodeTemplateDto);
}