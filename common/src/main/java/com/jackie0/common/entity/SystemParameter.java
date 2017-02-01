package com.jackie0.common.entity;

import com.jackie0.common.constant.Constant;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 系统参数实体，与数据字典{@link DataDict}的区别是，系统参数通常需要在不同环境作调整，控制系统状态的，是面向开发人员的，如：是否开启缓存开关
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-12 16:23
 */
@Entity
@Table(name = "system＿parameter", schema = Constant.COMMON_SCHEMA)
public class SystemParameter extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1112924222733736263L;

    /**
     * 系统参数缓存前缀
     */
    public static final String DATA_SYSTEM_PARAMETER_KEY_PREFIX = Constant.DATA_CACHE_PREFIX + ".SystemParameter.";

    /**
     * 主键
     */
    private String systemParameterId;

    /**
     * 参数键值，其命名规则：包名+有意义的业务名称
     * 如:com.jackie0.common.bizArchiveBaseDate业务表归档时间
     */
    @NotBlank(message = "{jackie0.common.systemParameter.parameterKey.NotBlank}")
    @Length(min = 1, max = 512, message = "{jackie0.common.systemParameter.parameterKey.length}")
    private String parameterKey;

    /**
     * 参数值
     */
    @NotBlank(message = "{jackie0.common.systemParameter.parameterValue.NotBlank}")
    @Length(min = 1, max = 512, message = "{jackie0.common.systemParameter.parameterValue.length}")
    private String parameterValue;

    /**
     * 参数描述
     */
    @Length(min = 1, max = 1024, message = "{jackie0.common.systemParameter.description.length}")
    private String description;

    @Id
    @Column(name = "system_parameter_id", unique = true, length = 36, nullable = false)
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "idGenerator")
    public String getSystemParameterId() {
        return systemParameterId;
    }

    public void setSystemParameterId(String systemParameterId) {
        this.systemParameterId = systemParameterId;
    }

    @Basic
    @Column(name = "parameter_key", length = 512, nullable = false)
    public String getParameterKey() {
        return parameterKey;
    }

    public void setParameterKey(String parameterKey) {
        this.parameterKey = parameterKey;
    }

    @Basic
    @Column(name = "parameter_value", length = 512, nullable = false)
    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    @Basic
    @Column(name = "description", length = 1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SystemParameter{" +
                "systemParameterId='" + systemParameterId + '\'' +
                ", parameterKey='" + parameterKey + '\'' +
                ", parameterValue='" + parameterValue + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
