package nchu.wuxi.xmeet.mapper;

import java.util.List;
import nchu.wuxi.xmeet.entity.TEnterprise;
import nchu.wuxi.xmeet.entity.TEnterpriseExample;
import org.apache.ibatis.annotations.Param;

public interface TEnterpriseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_enterprise
     *
     * @mbg.generated Tue Apr 14 18:42:36 CST 2020
     */
    long countByExample(TEnterpriseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_enterprise
     *
     * @mbg.generated Tue Apr 14 18:42:36 CST 2020
     */
    int deleteByExample(TEnterpriseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_enterprise
     *
     * @mbg.generated Tue Apr 14 18:42:36 CST 2020
     */
    int insert(TEnterprise record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_enterprise
     *
     * @mbg.generated Tue Apr 14 18:42:36 CST 2020
     */
    int insertSelective(TEnterprise record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_enterprise
     *
     * @mbg.generated Tue Apr 14 18:42:36 CST 2020
     */
    List<TEnterprise> selectByExample(TEnterpriseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_enterprise
     *
     * @mbg.generated Tue Apr 14 18:42:36 CST 2020
     */
    int updateByExampleSelective(@Param("record") TEnterprise record, @Param("example") TEnterpriseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_enterprise
     *
     * @mbg.generated Tue Apr 14 18:42:36 CST 2020
     */
    int updateByExample(@Param("record") TEnterprise record, @Param("example") TEnterpriseExample example);
}