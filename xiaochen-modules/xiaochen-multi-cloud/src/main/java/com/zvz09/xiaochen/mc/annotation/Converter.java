package com.zvz09.xiaochen.mc.annotation;

import com.alibaba.fastjson.JSON;
import com.aliyun.sdk.service.vpc20160428.models.CreateVpcRequest;
import com.volcengine.vpc.model.SecurityGroupForDescribeSecurityGroupsOutput;
import com.zvz09.xiaochen.mc.domain.dto.SecurityGroupDTO;
import com.zvz09.xiaochen.mc.domain.dto.VpcDTO;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class Converter {

    private final CloudProviderEnum cloudProviderEnum;

    public Converter(CloudProviderEnum cloudProviderEnum) {
        this.cloudProviderEnum = cloudProviderEnum;
    }

    /**
     * 云厂商数据结构 转为 多云数据结构
     *
     * @param source   云厂商数据结构
     * @param target   多云数据结构
     * @param <S>
     * @param <T>
     * @return
     */
    public <S, T> T convertP2M(S source, T target) {

        Class<?> targetClass = target.getClass();

        for (Field targetField : targetClass.getDeclaredFields()) {
            targetField.setAccessible(true);
            FieldMapping mapping = targetField.getAnnotation(FieldMapping.class);
            BaseFieldMapping baseMapping;
            if (mapping != null) {
                try {
                    switch (this.cloudProviderEnum) {
                        case ALI_YUN:
                            baseMapping = mapping.aliyun();

                            targetField.set(target, getValue(baseMapping, source, targetField));
                            break;
                        case TENCENT_CLOUD:
                            baseMapping = mapping.tencentcloud();

                            targetField.set(target, getValue(baseMapping, source, targetField));
                            break;
                        case VOLCENGINE:
                            baseMapping = mapping.volcengine();
                            targetField.set(target, getValue(baseMapping, source, targetField));
                            break;
                    }
                } catch (IllegalAccessException e) {
                    log.error("Converter error: ", e);
                }
            }
        }
        return target;
    }

    /**
     * 多云数据结构 转为 云厂商数据结构
     *
     * @param source   多云数据结构
     * @param target   云厂商数据结构
     * @param <S>
     * @param <T>
     * @return
     */
    public <S, T> T convertM2P(S source, T target) {

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        for (Field sourceField : sourceClass.getDeclaredFields()) {
            sourceField.setAccessible(true);
            FieldMapping mapping = sourceField.getAnnotation(FieldMapping.class);
            BaseFieldMapping baseMapping = null;
            Field targetField;
            if (mapping != null) {
                try {
                    switch (this.cloudProviderEnum) {
                        case ALI_YUN:
                            baseMapping = mapping.aliyun();
                            break;
                        case TENCENT_CLOUD:
                            baseMapping = mapping.tencentcloud();
                            break;
                        case VOLCENGINE:
                            baseMapping = mapping.volcengine();
                            break;
                    }

                    if(baseMapping != null && StringUtils.isNotBlank(baseMapping.value())){
                        targetField = targetClass.getDeclaredField(baseMapping.value());
                        targetField.setAccessible(true);
                        targetField.set(target, convertValue(targetField.getType(), sourceField.get(source)));
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    log.error("Converter error: ", e);
                }
            }
        }
        return target;
    }


    private <S> Object getValue(BaseFieldMapping baseMapping, S source, Field targetField) {
        Class<?> sourceClass = source.getClass();
        try {
            Field sourceField = null;
            if (baseMapping != null && StringUtils.isNotBlank(baseMapping.value())) {
                sourceField = sourceClass.getDeclaredField(baseMapping.value());
                sourceField.setAccessible(true);
                if (sourceField.getType() == targetField.getType()) {
                    return sourceField.get(source);
                } else {
                    return convertValue(targetField.getType(), sourceField.get(source));
                }
            }
        } catch (Exception e) {
            log.error("Converter error: ", e);
        }
        return null;
    }

    private Object convertValue(Class<?> targetType, Object value) {
        if (value == null) {
            return null;
        }

        String strValue = value.toString().trim(); // Trim to handle leading/trailing spaces

        try {
            if (Byte.class.equals(targetType)) {
                return Byte.valueOf(strValue);
            } else if (Short.class.equals(targetType)) {
                return Short.valueOf(strValue);
            } else if (Integer.class.equals(targetType)) {
                return Integer.valueOf(strValue);
            } else if (Long.class.equals(targetType)) {
                return Long.valueOf(strValue);
            } else if (Float.class.equals(targetType)) {
                return Float.valueOf(strValue);
            } else if (Double.class.equals(targetType)) {
                return Double.valueOf(strValue);
            } else if (Character.class.equals(targetType)) {
                if (strValue.length() == 1) {
                    return Character.valueOf(strValue.charAt(0));
                }
            } else if (Boolean.class.equals(targetType)) {
                return Boolean.valueOf(strValue);
            }
        } catch (Exception e) {
           log.error("转换异常",e);
        }

        return strValue;
    }


    public static void main(String[] args) {
        Converter converter = new Converter(CloudProviderEnum.ALI_YUN);

        VpcDTO vpcDTO = new VpcDTO();
        vpcDTO.setRegion("cn-qingdao");
        vpcDTO.setVpcName("test-vpc");
        vpcDTO.setCidrBlock("192.168.0.0/16");
        vpcDTO.setIpv6CidrBlock("");
        vpcDTO.setEnableIpv6(true);

        CreateVpcRequest request = CreateVpcRequest.builder().build();
        request = converter.convertM2P(vpcDTO,request);
        System.out.println(JSON.toJSON(request));

       /* CreateVpcRequest obj = CreateVpcRequest.builder().build();

        // 获取类的Field对象
        try {
            Field field = obj.getClass().getDeclaredField("regionId");

            // 设置属性为可访问，即使是私有属性也可以访问和修改
            field.setAccessible(true);

            // 设置属性值
            field.set(obj, "New value");

            // 打印修改后的属性值
            System.out.println("Modified value: " + obj.getRegionId());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }*/

    }
}
