package com.zvz09.xiaochen.note.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.web.util.StringUtils;
import com.zvz09.xiaochen.note.constant.OperateAction;
import com.zvz09.xiaochen.note.domain.dto.ReptileClassDTO;
import com.zvz09.xiaochen.note.domain.entity.ReptileParseClass;
import com.zvz09.xiaochen.note.mapper.ReptileParseClassMapper;
import com.zvz09.xiaochen.note.service.IReptileParseClassService;
import com.zvz09.xiaochen.note.service.RabbitMqService;
import com.zvz09.xiaochen.note.strategy.ReptileDataParserStrategy;
import com.zvz09.xiaochen.note.strategy.ReptileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * <p>
 * 爬取数据类 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-13
 */
@Service
@RequiredArgsConstructor
public class ReptileParseClassServiceImpl extends ServiceImpl<ReptileParseClassMapper, ReptileParseClass> implements IReptileParseClassService {

    private final RabbitMqService rabbitMqService;

    @Override
    public IPage<ReptileParseClass> page(BasePage basePage) {
        return this.page(new Page<>(basePage.getPageNum(), basePage.getPageSize()));
    }

    @Override
    public void operate(Long id, OperateAction operate) {
        ReptileParseClass reptileParseClass = this.getById(id);
        if (reptileParseClass == null) {
            return;
        }
        if(operate.isStatus()){
            rabbitMqService.addDataParserStrategy(reptileParseClass);
        }else {
            rabbitMqService.removeDataParserStrategy(reptileParseClass.getClassName());
        }
        reptileParseClass.setStatus(operate.isStatus());
        this.updateById(reptileParseClass);
    }
}

