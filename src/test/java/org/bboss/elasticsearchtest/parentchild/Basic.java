package org.bboss.elasticsearchtest.parentchild;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.ESId;
import org.frameworkset.elasticsearch.entity.ESBaseData;

import java.util.Date;

/**
 * Created by Kay on 2018/11/1
 */
public class Basic extends ESBaseData {
    /**
     *  作为 索引_id
     */
    @ESId
    private String party_id;
    private String sex;                     // 性别
    private String mari_sts;               //婚姻状况
    private String ethnic;                 //民族
    private String prof;                   //职业
    private String province;                 //省份
    private String city;                     //城市

    public Basic() {
    }

    private String client_type;             //当事人类型
    private String client_name;            //姓名
    private Integer age;                     //年龄
    private String id_type;                //证件类型
    private String idno;                    //证件号码
    private String education;               //学历
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    private Date created_date;                  //创建日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    private Date birth_date;                    //生日
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMari_sts() {
        return mari_sts;
    }

    public void setMari_sts(String mari_sts) {
        this.mari_sts = mari_sts;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getId_type() {
        return id_type;
    }

    public void setId_type(String id_type) {
        this.id_type = id_type;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
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

    public Basic(String party_id, String sex, String mari_sts, String ethnic, String prof, String province, String city, String client_type, String client_name, Integer age, String id_type, String idno, String education, Date created_date, Date birth_date, Date last_modified_date, Date etl_date) {
        this.party_id = party_id;
        this.sex = sex;
        this.mari_sts = mari_sts;
        this.ethnic = ethnic;
        this.prof = prof;
        this.province = province;
        this.city = city;
        this.client_type = client_type;
        this.client_name = client_name;
        this.age = age;
        this.id_type = id_type;
        this.idno = idno;
        this.education = education;
        this.created_date = created_date;
        this.birth_date = birth_date;
        this.last_modified_date = last_modified_date;
        this.etl_date = etl_date;
    }

    @Override
    public String toString() {
        return "Basic{" +
                "party_id='" + party_id + '\'' +
                ", sex='" + sex + '\'' +
                ", mari_sts='" + mari_sts + '\'' +
                ", ethnic='" + ethnic + '\'' +
                ", prof='" + prof + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", client_type='" + client_type + '\'' +
                ", client_name='" + client_name + '\'' +
                ", age='" + age + '\'' +
                ", id_type='" + id_type + '\'' +
                ", idno='" + idno + '\'' +
                ", education='" + education + '\'' +
                ", created_date=" + created_date +
                ", birth_date=" + birth_date +
                ", last_modified_date=" + last_modified_date +
                ", etl_date=" + etl_date +
                '}';
    }
}
