/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.autocode.service.impl
 * @className com.zvz09.xiaochen.autocode.service.impl.GenCodeServiceImpl
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.autocode.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zvz09.xiaochen.autocode.convert.impl.MySqlTypeConvert;
import com.zvz09.xiaochen.autocode.domain.dto.gencode.Column;
import com.zvz09.xiaochen.autocode.domain.dto.gencode.CreateSqlDto;
import com.zvz09.xiaochen.autocode.domain.dto.gencode.GenConfig;
import com.zvz09.xiaochen.autocode.domain.dto.template.AutoCodeTemplateDto;
import com.zvz09.xiaochen.autocode.engine.FreemarkerTemplateEngine;
import com.zvz09.xiaochen.autocode.engine.TemplateEngineService;
import com.zvz09.xiaochen.autocode.domain.entity.SysAutoCodeTemplate;
import com.zvz09.xiaochen.autocode.domain.entity.SysGenCodeHistory;
import com.zvz09.xiaochen.autocode.mapper.SysGenCodeHistoryMapper;
import com.zvz09.xiaochen.autocode.service.IGenCodeService;
import com.zvz09.xiaochen.autocode.service.ISysAutoCodeTemplateService;
import com.zvz09.xiaochen.autocode.domain.vo.PreviewCodeVo;
import com.zvz09.xiaochen.common.core.util.JacksonUtil;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * GenCodeServiceImpl
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/20 15:38
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenCodeServiceImpl implements IGenCodeService {

    private final MySqlTypeConvert mySqlTypeConvert;
    private final ISysAutoCodeTemplateService sysAutoCodeTemplateService;
    private final TemplateEngineService templateEngineService;
    private final FreemarkerTemplateEngine freemarkerTemplateEngine;
    private final SysGenCodeHistoryMapper sysGenCodeHistoryMapper;

    private final Pattern pathPattern = Pattern.compile("^package(.*);");
    private final Pattern fileNamePattern = Pattern.compile("(public\\s+(interface|class)\\s+(\\w+))\\s");

    public static String removePunctuation(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '_') {
                sb.append(c);
            } else if (Character.isLetter(c) || Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Override
    public GenConfig parseCreateSql(CreateSqlDto createSqlDto) {
        GenConfig genConfig = new GenConfig();
        try {
            Statement stmt = CCJSqlParserUtil.parse(createSqlDto.getSql());
            if (stmt instanceof CreateTable createTableStmt) {
                genConfig.setTableName(removePunctuation(createTableStmt.getTable().getName()));
                genConfig.setClassName(StrUtil.toCamelCase(genConfig.getTableName(), '_'));

                List<Column> columns = new ArrayList<>();

                for (ColumnDefinition columnDefinition : createTableStmt.getColumnDefinitions()) {
                    String description = "";
                    for (int i = 0; i < columnDefinition.getColumnSpecs().size(); i++) {
                        if ("comment".equalsIgnoreCase(columnDefinition.getColumnSpecs().get(i))
                                && i + 1 < columnDefinition.getColumnSpecs().size()) {
                            description = columnDefinition.getColumnSpecs().get(i + 1);
                            break;
                        }
                    }
                    Column column = new Column(StrUtil.toCamelCase(columnDefinition.getColumnName(), '_'),
                            removePunctuation(description), mySqlTypeConvert.processTypeConvert(columnDefinition.getColDataType()).getType());

                    columns.add(column);
                }
                genConfig.setColumns(columns);
            }
        } catch (Exception e) {
            log.error("parseCreateSql error:{}", e.getMessage());
            throw new BusinessException("SQL 解析异常");
        }

        return genConfig;
    }

    @Override
    public List<PreviewCodeVo> previewCode(GenConfig genConfig) {

        List<SysAutoCodeTemplate> templates =
                sysAutoCodeTemplateService.list(new LambdaQueryWrapper<SysAutoCodeTemplate>().in(SysAutoCodeTemplate::getId, genConfig.getTemplateIds()));
        if (templates.size() != genConfig.getTemplateIds().size()) {
            throw new BusinessException("模板不存在");
        }
        genConfig.setDatetime(DateUtil.now());
        List<PreviewCodeVo> vos = new ArrayList<>();
        for (SysAutoCodeTemplate template : templates) {
            StringWriter sw = templateEngineService.execute(template.getTemplateEngine(), genConfig, template);
            String fileName;
            Matcher fileNameMatcher = fileNamePattern.matcher(sw.toString());
            if (fileNameMatcher.find()) {
                fileName = fileNameMatcher.group(3) + ".java";
            } else {
                fileName = formatTheDefaultFileName(template.getDefaultFileName(), genConfig);
            }
            vos.add(new PreviewCodeVo(fileName, sw.toString()));
        }

        try {

            SysGenCodeHistory genCodeHistory = SysGenCodeHistory.builder()
                    .tableName(genConfig.getTableName())
                    .description(genConfig.getDescription())
                    .genConfig(JacksonUtil.writeValueAsString(genConfig))
                    .build();
            sysGenCodeHistoryMapper.insert(genCodeHistory);
        } catch (Exception e) {
            log.error("历史保存错误", e);
        }
        return vos;
    }

    @Override
    public byte[] downloadCode(GenConfig genConfig) {

        List<SysAutoCodeTemplate> templates =
                sysAutoCodeTemplateService.list(new LambdaQueryWrapper<SysAutoCodeTemplate>().in(SysAutoCodeTemplate::getId, genConfig.getTemplateIds()));
        if (templates.size() != genConfig.getTemplateIds().size()) {
            throw new BusinessException("模板不存在");
        }

        genConfig.setDatetime(DateUtil.now());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (SysAutoCodeTemplate template : templates) {
            // 渲染模板
            StringWriter sw = templateEngineService.execute(template.getTemplateEngine(), genConfig, template);
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(getFilePath(genConfig, template, sw.toString())));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败", e);
            }
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    @Override
    public String verifyAutoCodeTemplate(AutoCodeTemplateDto autoCodeTemplateDto) {
        GenConfig genConfig = new GenConfig();
        List<Column> columns = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            columns.add(new Column("column" + i, "column" + i, "String"));
            if (i % 2 == 0) {
                columns.get(i).setDto(true);
            }
            if (i % 3 == 0) {
                columns.get(i).setQuery(true);
            }
        }
        columns.get(0).setSole(true);
        genConfig.setColumns(columns);

        // 渲染模板
        StringWriter sw = templateEngineService.execute(autoCodeTemplateDto.getTemplateEngine(), genConfig, autoCodeTemplateDto.getName(), autoCodeTemplateDto.getContent());
        return sw.toString();
    }

    private String getFilePath(GenConfig genConfig, SysAutoCodeTemplate template, String content) {
        String path;
        Matcher pathMatcher = pathPattern.matcher(content);
        Matcher fileNameMatcher = fileNamePattern.matcher(content);
        if (pathMatcher.find()) {
            path = StringUtils.replace(pathMatcher.group(1) + "."
                    , ".", "/");
            if (fileNameMatcher.find()) {
                path += fileNameMatcher.group(1).replaceAll("public class ", "")
                        .replaceAll("public interface ", "") + ".java";
            } else {
                path += formatTheDefaultFileName(template.getDefaultFileName(), genConfig);
            }
        } else {
            path = formatTheDefaultFileName(template.getDefaultFileName(), genConfig);
            if (path.endsWith("Mapper.xml")) {
                path = "resources/mapper/" + path;
            }
        }
        return path;
    }

    private String formatTheDefaultFileName(String defaultFileName, GenConfig genConfig) {
        StringWriter stringWriter = freemarkerTemplateEngine.writer(genConfig, UUID.randomUUID().toString(), defaultFileName);
        return stringWriter.toString();
    }
}
 