package com.bxly.toursts.model.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: kevin
 * @data: 2018-08-21 14:19
 * @desc:   实体类基类
 */
@Data
@MappedSuperclass   //不加这个子类将不会生成id
public abstract class BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")     //uuid2主键生成策略：生成36位带-的字符串，不能限定@Column(column<36)
    @GeneratedValue(generator = "uuid")
    @Column(nullable = false, length = 32)  //此处不设置length会报错：Caused by: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Specified key was too long; max key length is 1000 bytes
    private String id;

    @CreationTimestamp//能将时间写入数据库,不加不会自动写入
    @Temporal(TemporalType.TIMESTAMP)//精确到年月日时分秒
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")//在将实体转为json格式时转换字段时间格式
    private Date createTime;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)//精确到年月日时分秒
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    //重写hashCode方法用于Set<实体类>去重：详见blog:https://www.cnblogs.com/zhangzongle/p/5479726.html
    @Override
    public int hashCode() {
//        return id.hashCode();
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        } else if (obj == null) {
//            return false;
//        } else if (obj.getClass() != this.getClass()) {
//            return false;
//        }
//        if (obj instanceof BaseEntity) {
//            BaseEntity baseEntity = (BaseEntity) obj;
//            if (this.getId() == null) {
//                if (baseEntity.getId() != null) {
//                    return false;
//                }
//            } else if (!this.getId().equals(baseEntity.getId())) {
//                return false;
//            }
//            return id.equals(baseEntity.getId());
//        }
//        return super.equals(obj);
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
