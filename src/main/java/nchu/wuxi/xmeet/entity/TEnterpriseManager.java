package nchu.wuxi.xmeet.entity;

import java.io.Serializable;

public class TEnterpriseManager implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_enterprise_manager.enterprise_id
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    private Integer enterpriseId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_enterprise_manager.phone
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    private String phone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_enterprise_manager.user_name
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    private String userName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_enterprise_manager.enterprise_name
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    private String enterpriseName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_enterprise_manager
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_enterprise_manager.enterprise_id
     *
     * @return the value of t_enterprise_manager.enterprise_id
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_enterprise_manager.enterprise_id
     *
     * @param enterpriseId the value for t_enterprise_manager.enterprise_id
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    public void setEnterpriseId(Integer enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_enterprise_manager.phone
     *
     * @return the value of t_enterprise_manager.phone
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_enterprise_manager.phone
     *
     * @param phone the value for t_enterprise_manager.phone
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_enterprise_manager.user_name
     *
     * @return the value of t_enterprise_manager.user_name
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_enterprise_manager.user_name
     *
     * @param userName the value for t_enterprise_manager.user_name
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_enterprise_manager.enterprise_name
     *
     * @return the value of t_enterprise_manager.enterprise_name
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    public String getEnterpriseName() {
        return enterpriseName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_enterprise_manager.enterprise_name
     *
     * @param enterpriseName the value for t_enterprise_manager.enterprise_name
     *
     * @mbg.generated Fri Apr 24 11:23:46 CST 2020
     */
    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
}