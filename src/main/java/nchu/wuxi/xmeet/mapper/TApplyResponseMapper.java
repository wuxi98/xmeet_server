package nchu.wuxi.xmeet.mapper;

import java.util.List;
import nchu.wuxi.xmeet.entity.TApplyResponse;
import nchu.wuxi.xmeet.entity.TApplyResponseExample;
import org.apache.ibatis.annotations.Param;

public interface TApplyResponseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_apply_response
     *
     * @mbg.generated Tue May 05 16:19:11 CST 2020
     */
    long countByExample(TApplyResponseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_apply_response
     *
     * @mbg.generated Tue May 05 16:19:11 CST 2020
     */
    int deleteByExample(TApplyResponseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_apply_response
     *
     * @mbg.generated Tue May 05 16:19:11 CST 2020
     */
    int insert(TApplyResponse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_apply_response
     *
     * @mbg.generated Tue May 05 16:19:11 CST 2020
     */
    int insertSelective(TApplyResponse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_apply_response
     *
     * @mbg.generated Tue May 05 16:19:11 CST 2020
     */
    List<TApplyResponse> selectByExample(TApplyResponseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_apply_response
     *
     * @mbg.generated Tue May 05 16:19:11 CST 2020
     */
    int updateByExampleSelective(@Param("record") TApplyResponse record, @Param("example") TApplyResponseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_apply_response
     *
     * @mbg.generated Tue May 05 16:19:11 CST 2020
     */
    int updateByExample(@Param("record") TApplyResponse record, @Param("example") TApplyResponseExample example);


}