package org.bboss.elasticsearchtest.parentchild;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.ESParentId;
import org.frameworkset.elasticsearch.entity.ESBaseData;

import java.util.Date;

/**
 * Created by Kay on 2018/11/1
 */
public class Exam extends ESBaseData {
    @ESParentId
    private String party_id;          //父id
    private String  hospital;           // 就诊医院
    public Exam() {
    }


    private String dept;                //就诊科室
    private String is_ok;               // 体检正常
    private String exam_result;         // 体检结果说明
    private String fld1;                // 身高
    private String fld2;                // 体重
    private String fld3;                // 胸围
    private String fld4;                // 腹围
    private String fld5;                // 血压-收缩压
    private String fld901;                // 血压-舒张压
    private String fld6;                // 视力-左
    private String fld902;                // 视力-右
    private String fld14;                // 脉搏
    private String fld20;                // 节律
    private String fld21;                // 心率
    private String fld23;                // 尿液检查-PH值
    private String fld24;                // 尿液检查-SG
    private String fld65;                // 红细胞计数(万)
    private String fld66;                // 血红蛋白
    private String fld67;                // 白细胞计数
    private String fld68;                // 血小板计数
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    private Date created_date;            // 体检日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    private Date last_modified_date;          //最后修改日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    private Date etl_date;    // etl 時間戳

    public String getParty_id() {
        return party_id;
    }

    public void setParty_id(String party_id) {
        this.party_id = party_id;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getIs_ok() {
        return is_ok;
    }

    public void setIs_ok(String is_ok) {
        this.is_ok = is_ok;
    }

    public String getExam_result() {
        return exam_result;
    }

    public void setExam_result(String exam_result) {
        this.exam_result = exam_result;
    }

    public String getFld1() {
        return fld1;
    }

    public void setFld1(String fld1) {
        this.fld1 = fld1;
    }

    public String getFld2() {
        return fld2;
    }

    public void setFld2(String fld2) {
        this.fld2 = fld2;
    }

    public String getFld3() {
        return fld3;
    }

    public void setFld3(String fld3) {
        this.fld3 = fld3;
    }

    public String getFld4() {
        return fld4;
    }

    public void setFld4(String fld4) {
        this.fld4 = fld4;
    }

    public String getFld5() {
        return fld5;
    }

    public void setFld5(String fld5) {
        this.fld5 = fld5;
    }

    public String getFld901() {
        return fld901;
    }

    public void setFld901(String fld901) {
        this.fld901 = fld901;
    }

    public String getFld6() {
        return fld6;
    }

    public void setFld6(String fld6) {
        this.fld6 = fld6;
    }

    public String getFld902() {
        return fld902;
    }

    public void setFld902(String fld902) {
        this.fld902 = fld902;
    }

    public String getFld14() {
        return fld14;
    }

    public void setFld14(String fld14) {
        this.fld14 = fld14;
    }

    public String getFld20() {
        return fld20;
    }

    public void setFld20(String fld20) {
        this.fld20 = fld20;
    }

    public String getFld21() {
        return fld21;
    }

    public void setFld21(String fld21) {
        this.fld21 = fld21;
    }

    public String getFld23() {
        return fld23;
    }

    public void setFld23(String fld23) {
        this.fld23 = fld23;
    }

    public String getFld24() {
        return fld24;
    }

    public void setFld24(String fld24) {
        this.fld24 = fld24;
    }

    public String getFld65() {
        return fld65;
    }

    public void setFld65(String fld65) {
        this.fld65 = fld65;
    }

    public String getFld66() {
        return fld66;
    }

    public void setFld66(String fld66) {
        this.fld66 = fld66;
    }

    public String getFld67() {
        return fld67;
    }

    public void setFld67(String fld67) {
        this.fld67 = fld67;
    }

    public String getFld68() {
        return fld68;
    }

    public void setFld68(String fld68) {
        this.fld68 = fld68;
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

    public Exam(String party_id, String hospital, String dept, String is_ok, String exam_result, String fld1, String fld2, String fld3, String fld4, String fld5, String fld901, String fld6, String fld902, String fld14, String fld20, String fld21, String fld23, String fld24, String fld65, String fld66, String fld67, String fld68, Date created_date, Date last_modified_date, Date etl_date) {
        this.party_id = party_id;
        this.hospital = hospital;
        this.dept = dept;
        this.is_ok = is_ok;
        this.exam_result = exam_result;
        this.fld1 = fld1;
        this.fld2 = fld2;
        this.fld3 = fld3;
        this.fld4 = fld4;
        this.fld5 = fld5;
        this.fld901 = fld901;
        this.fld6 = fld6;
        this.fld902 = fld902;
        this.fld14 = fld14;
        this.fld20 = fld20;
        this.fld21 = fld21;
        this.fld23 = fld23;
        this.fld24 = fld24;
        this.fld65 = fld65;
        this.fld66 = fld66;
        this.fld67 = fld67;
        this.fld68 = fld68;
        this.created_date = created_date;
        this.last_modified_date = last_modified_date;
        this.etl_date = etl_date;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "party_id='" + party_id + '\'' +
                ", hospital='" + hospital + '\'' +
                ", dept='" + dept + '\'' +
                ", is_ok='" + is_ok + '\'' +
                ", exam_result='" + exam_result + '\'' +
                ", fld1='" + fld1 + '\'' +
                ", fld2='" + fld2 + '\'' +
                ", fld3='" + fld3 + '\'' +
                ", fld4='" + fld4 + '\'' +
                ", fld5='" + fld5 + '\'' +
                ", fld901='" + fld901 + '\'' +
                ", fld6='" + fld6 + '\'' +
                ", fld902='" + fld902 + '\'' +
                ", fld14='" + fld14 + '\'' +
                ", fld20='" + fld20 + '\'' +
                ", fld21='" + fld21 + '\'' +
                ", fld23='" + fld23 + '\'' +
                ", fld24='" + fld24 + '\'' +
                ", fld65='" + fld65 + '\'' +
                ", fld66='" + fld66 + '\'' +
                ", fld67='" + fld67 + '\'' +
                ", fld68='" + fld68 + '\'' +
                ", created_date=" + created_date +
                ", last_modified_date=" + last_modified_date +
                ", etl_date=" + etl_date +
                '}';
    }
}
