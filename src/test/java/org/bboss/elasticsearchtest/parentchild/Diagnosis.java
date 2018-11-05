package org.bboss.elasticsearchtest.parentchild;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.ESParentId;
import org.frameworkset.elasticsearch.entity.ESBaseData;

import java.util.Date;

/**
 * Created by Kay on 2018/11/1
 */
public class Diagnosis extends ESBaseData {
    @ESParentId
    private String party_id;          //父id
    private String provider;            //诊断医院
    private String subject;             //科室

    public Diagnosis() {
    }

    private String diagnosis_type;      //诊断类别
    private String icd10_code;          //疾病编码(ICD-10)
    private String sd_disease_name;     // 疾病名称
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    private Date created_date;          // 诊断日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    private Date last_modified_date;          //最后修改日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    private Date etl_date;                      //etl时间戳

    public String getParty_id() {
        return party_id;
    }

    public void setParty_id(String party_id) {
        this.party_id = party_id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDiagnosis_type() {
        return diagnosis_type;
    }

    public void setDiagnosis_type(String diagnosis_type) {
        this.diagnosis_type = diagnosis_type;
    }

    public String getIcd10_code() {
        return icd10_code;
    }

    public void setIcd10_code(String icd10_code) {
        this.icd10_code = icd10_code;
    }

    public String getSd_disease_name() {
        return sd_disease_name;
    }

    public void setSd_disease_name(String sd_disease_name) {
        this.sd_disease_name = sd_disease_name;
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

    public Diagnosis(String party_id, String provider, String subject, String diagnosis_type, String icd10_code, String sd_disease_name, Date created_date, Date last_modified_date, Date etl_date) {
        this.party_id = party_id;
        this.provider = provider;
        this.subject = subject;
        this.diagnosis_type = diagnosis_type;
        this.icd10_code = icd10_code;
        this.sd_disease_name = sd_disease_name;
        this.created_date = created_date;
        this.last_modified_date = last_modified_date;
        this.etl_date = etl_date;
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "party_id='" + party_id + '\'' +
                ", provider='" + provider + '\'' +
                ", subject='" + subject + '\'' +
                ", diagnosis_type='" + diagnosis_type + '\'' +
                ", icd10_code='" + icd10_code + '\'' +
                ", sd_disease_name='" + sd_disease_name + '\'' +
                ", created_date=" + created_date +
                ", last_modified_date=" + last_modified_date +
                ", etl_date=" + etl_date +
                '}';
    }
}
