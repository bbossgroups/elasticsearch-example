package org.bboss.elasticsearchtest.parentchild;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.ESParentId;
import org.frameworkset.elasticsearch.entity.ESBaseData;

import java.util.Date;

/**
 * Created by Kay on 2018/11/1
 */
public class Medical extends ESBaseData {
    @ESParentId
    private String party_id;          //父id
    private String hos_name_yb;         //就诊医院

    public Medical() {
    }

    private String eivisions_name;      //就诊科室
    private String medical_type;        //治疗类型
    private String medical_common_name;  //药品通用名
    private String medical_sale_name;       // 药品商品名
    private String medical_code;            // 药品编码(ATC)
    private String specification;           //规格
    private String usage_num;               //每次用量
    private String unit;                    //单位
    private String usage_times;             // 使用频次
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    private Date created_date;            // 处方开具日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    private Date last_modified_date;          //最后修改日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    private Date etl_date;                  // etl 日期

    public String getParty_id() {
        return party_id;
    }

    public void setParty_id(String party_id) {
        this.party_id = party_id;
    }

    public String getHos_name_yb() {
        return hos_name_yb;
    }

    public void setHos_name_yb(String hos_name_yb) {
        this.hos_name_yb = hos_name_yb;
    }

    public String getEivisions_name() {
        return eivisions_name;
    }

    public void setEivisions_name(String eivisions_name) {
        this.eivisions_name = eivisions_name;
    }

    public String getMedical_type() {
        return medical_type;
    }

    public void setMedical_type(String medical_type) {
        this.medical_type = medical_type;
    }

    public String getMedical_common_name() {
        return medical_common_name;
    }

    public void setMedical_common_name(String medical_common_name) {
        this.medical_common_name = medical_common_name;
    }

    public String getMedical_sale_name() {
        return medical_sale_name;
    }

    public void setMedical_sale_name(String medical_sale_name) {
        this.medical_sale_name = medical_sale_name;
    }

    public String getMedical_code() {
        return medical_code;
    }

    public void setMedical_code(String medical_code) {
        this.medical_code = medical_code;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUsage_num() {
        return usage_num;
    }

    public void setUsage_num(String usage_num) {
        this.usage_num = usage_num;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUsage_times() {
        return usage_times;
    }

    public void setUsage_times(String usage_times) {
        this.usage_times = usage_times;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(Date last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public Date getEtl_date() {
        return etl_date;
    }

    public void setEtl_date(Date etl_date) {
        this.etl_date = etl_date;
    }

    public Medical(String party_id, String hos_name_yb, String eivisions_name, String medical_type, String medical_common_name, String medical_sale_name, String medical_code, String specification, String usage_num, String unit, String usage_times, Date created_date, Date last_modified_date, Date etl_date) {
        this.party_id = party_id;
        this.hos_name_yb = hos_name_yb;
        this.eivisions_name = eivisions_name;
        this.medical_type = medical_type;
        this.medical_common_name = medical_common_name;
        this.medical_sale_name = medical_sale_name;
        this.medical_code = medical_code;
        this.specification = specification;
        this.usage_num = usage_num;
        this.unit = unit;
        this.usage_times = usage_times;
        this.created_date = created_date;
        this.last_modified_date = last_modified_date;
        this.etl_date = etl_date;
    }

    @Override
    public String toString() {
        return "Medical{" +
                "party_id='" + party_id + '\'' +
                ", hos_name_yb='" + hos_name_yb + '\'' +
                ", eivisions_name='" + eivisions_name + '\'' +
                ", medical_type='" + medical_type + '\'' +
                ", medical_common_name='" + medical_common_name + '\'' +
                ", medical_sale_name='" + medical_sale_name + '\'' +
                ", medical_code='" + medical_code + '\'' +
                ", specification='" + specification + '\'' +
                ", usage_num='" + usage_num + '\'' +
                ", unit='" + unit + '\'' +
                ", usage_times='" + usage_times + '\'' +
                ", created_date=" + created_date +
                ", last_modified_date=" + last_modified_date +
                ", etl_date=" + etl_date +
                '}';
    }
}
