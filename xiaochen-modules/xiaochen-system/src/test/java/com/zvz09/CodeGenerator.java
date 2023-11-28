package com.zvz09;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/**
 * @author lizili-YF0033
 * @version 1.0
 * @date 2023-01-03 18:06
 */
public class CodeGenerator {


    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://192.168.191.1:3306/xiaochen-cloud?useUnicode=true&characterEncoding=utf-8", "xiaochen-cloud", "2T5kLTwTYWdsBASH")
                .globalConfig(builder -> {
                    builder.author("zvz09") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\Run\\Java\\xiaochen-cloud\\xiaochen-modules\\xiaochen-job-admin\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.zvz09.xiaochen.job.admin") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\Run\\Java\\xiaochen-cloud\\xiaochen-modules\\xiaochen-job-admin\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("job_info", "job_log"); // 设置需要生成的表名

                })
                .execute();

    }
}
