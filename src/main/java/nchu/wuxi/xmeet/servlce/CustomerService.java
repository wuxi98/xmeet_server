package nchu.wuxi.xmeet.servlce;

import nchu.wuxi.xmeet.utils.RestReturn;

import java.io.File;

/**
 * @auther WuXi
 * @create 2020/4/21
 */
public interface CustomerService {
    RestReturn getByEnterprise(int enterpriseId);
    RestReturn getEnterpriseByEnterpriseId(int enterpriseId);
    RestReturn uploadHead(File file, String phone);
    RestReturn createEnterprise(String enterpriseName, String phone);
    RestReturn getEnterprises();
    RestReturn searchUser(String targetPhone);
    RestReturn applyJoinEnterprise(int enterpriseId, String phone, String userName);
    RestReturn processEnterpriseApply(int enterpriseId, String phone, int code);
    RestReturn notices(String phone);
    void updateName(String phone, String name);
    RestReturn getHeadUrl(String phone);
}
