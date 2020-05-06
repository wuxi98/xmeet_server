package nchu.wuxi.xmeet.servlce.impl;

import nchu.wuxi.xmeet.entity.TCustomer;
import nchu.wuxi.xmeet.entity.TCustomerExample;
import nchu.wuxi.xmeet.mapper.TCustomerMapper;
import nchu.wuxi.xmeet.servlce.AccountService;
import nchu.wuxi.xmeet.utils.RestReturn;
import nchu.wuxi.xmeet.utils.StringReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @auther WuXi
 * @create 2020/4/4
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    TCustomerMapper tCustomerMapper;

    /**
     * 登录
     *      登录成功：返回该用户信息
     *      登录失败 返回 {code：0，data："账号或密码错误"}
     * @param user
     * @return
     */
    @Override
    public Object login(TCustomer user) {
        RestReturn restReturn = new RestReturn();
        TCustomerExample example = new TCustomerExample();
        example.createCriteria().andPhoneEqualTo(user.getPhone()).andPasswordEqualTo(user.getPassword());
        List<TCustomer> tCustomers = tCustomerMapper.selectByExample(example);
        if(tCustomers.size() > 0){
           // return restReturn.success(new StringReturn(1,"登录成功",tCustomers,1),"登录成功");
            return restReturn.success(tCustomers,"登录成功",1);
        }
        return restReturn.error(200,new ArrayList<>(),"登录失败",0);
    }

    /**
     * 注册
     * code：
     *      1 注册成功
     *      0 注册失败
     * @param user
     * @return
     */
    @Override
    public RestReturn register(TCustomer user) {
        int insert = 0;
        RestReturn restReturn = new RestReturn();
        try {
            insert = tCustomerMapper.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
            return restReturn.error(200,null,"注册失败",0);
        }
        if(insert == 0){
            return restReturn.error(200,null,"注册失败",0);
        }
        ArrayList<TCustomer> tCustomers = new ArrayList<>();
        tCustomers.add(user);

        return restReturn.success(tCustomers,"注册成功",0);
    }

    @Override
    public TCustomer find(String phone) {
        TCustomerExample example = new TCustomerExample();
        example.createCriteria().andPhoneEqualTo(phone);
        return tCustomerMapper.selectByExample(example).get(0);
    }

    /**
     * 登录
     *      登录成功：返回该用户信息
     *      登录失败 返回 {code：0，data："不存在该账户"}
     * @param phone
     * @return
     */
    @Override
    public RestReturn loginWithoutPassword(String phone) {
        RestReturn restReturn = new RestReturn();
        TCustomerExample example = new TCustomerExample();
        example.createCriteria().andPhoneEqualTo(phone);
        List<TCustomer> tCustomers = tCustomerMapper.selectByExample(example);
        if(tCustomers.size() > 0){
            // return restReturn.success(new StringReturn(1,"登录成功",tCustomers,1),"登录成功");
            return restReturn.success(tCustomers,"登录成功",1);
        }
        return restReturn.error(200,new ArrayList<>(),"不存在该账户",0);
    }
}
