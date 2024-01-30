package com.zvz09;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.zvz09.xiaochen.common.core.util.Snowflake;

import java.util.Collections;

/**
 * @author zvz09
 * @version 1.0
 * @date 2023-01-03 18:06
 */
public class CodeGenerator {


    public static void main(String[] args) throws Exception {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/xiaochen-cloud?useUnicode=true&characterEncoding=utf-8", "xiaochen", "!QAZ2wsx#EDC")
                .globalConfig(builder -> {
                    builder.author("zvz09") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\code\\xiaochen-cloud\\xiaochen-modules\\xiaochen-system\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.zvz09.xiaochen.system") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                                    "D:\\code\\xiaochen-cloud\\xiaochen-modules\\xiaochen-system\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_micro"); // 设置需要生成的表名

                })
                .execute();

    }
}
