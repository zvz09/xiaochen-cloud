package com.zvz09.xiaochen.blog.controller;


import com.zvz09.xiaochen.blog.service.RabbitMqService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 爬取数据类 前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-12-13
 */
@RestController
@RequestMapping("/reptile")
@RequiredArgsConstructor
@Tag(name = "爬虫类管理")
public class BlogReptileClassController {

    private final RabbitMqService rabbitMqService;


    @GetMapping(value = "/test")
    public void test() {
        rabbitMqService.removeDataParserStrategy("cloudTencentParser");
    }
}

