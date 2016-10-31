package com.jackie0.showcase.entity;

import com.jackie0.common.constant.Constant;
import com.jackie0.common.entity.BaseEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.io.Serializable;

/**
 * showcase演示用户实体
 *
 * @author jackie0
 * @since Java8
 * date 2016-10-26 15:37
 */
@Entity
@Table(name = "showcase_user", schema = Constant.COMMON_SCHEMA)
public class ShowcaseUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -279627191151697543L;

    /**
     * 主键
     */
    private String showcaseUserId;

    /**
     * 用户名称
     */
    @NotBlank(message = "{jackie0.showcase.showcaseUser.userName.NotBlank}")
    @Length(min = 1, max = 128, message = "{jackie0.showcase.showcaseUser.userName.length}")
    private String userName;

    @Digits(integer = 3, fraction = 0, message = "{jackie0.showcase.showcaseUser.age.digits}")
    private Integer age;

    @Length(min = 1, max = 32, message = "{jackie0.showcase.showcaseUser.mobilePhone.length}")
    private String mobilePhone;

    @Id
    @Column(name = "showcase_user_id", unique = true, length = 36, nullable = false)
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "idGenerator")
    public String getShowcaseUserId() {
        return showcaseUserId;
    }

    public void setShowcaseUserId(String showcaseUserId) {
        this.showcaseUserId = showcaseUserId;
    }

    @Basic
    @Column(name = "user_name", length = 128, nullable = false)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "age", length = 3)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Basic
    @Column(name = "mobile_phone", length = 32)
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
