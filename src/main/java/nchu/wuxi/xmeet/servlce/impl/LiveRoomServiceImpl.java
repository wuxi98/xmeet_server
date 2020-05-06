package nchu.wuxi.xmeet.servlce.impl;

import nchu.wuxi.xmeet.entity.TCustomer;
import nchu.wuxi.xmeet.entity.TCustomerExample;
import nchu.wuxi.xmeet.entity.TLiveRome;
import nchu.wuxi.xmeet.entity.TLiveRomeExample;
import nchu.wuxi.xmeet.mapper.TCustomerMapper;
import nchu.wuxi.xmeet.mapper.TLiveRomeMapper;
import nchu.wuxi.xmeet.servlce.LiveRoomService;
import nchu.wuxi.xmeet.utils.RestReturn;
import nchu.wuxi.xmeet.utils.StringReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther WuXi
 * @create 2020/4/13
 */
@Service
public class LiveRoomServiceImpl implements LiveRoomService {

    @Autowired
    TLiveRomeMapper tLiveRomeMapper;

    @Autowired
    TCustomerMapper customerMapper;

    @Override
    public RestReturn getRoomByEnterpriseId(int enterpriseId) {
        TLiveRomeExample example = new TLiveRomeExample();
        example.createCriteria().andEnterpriseIdEqualTo(enterpriseId).andStateEqualTo("online");
        List<TLiveRome> tLiveRomes = null;
        RestReturn restReturn = new RestReturn();
        try {
            tLiveRomes = tLiveRomeMapper.selectByExample(example);
            return restReturn.success(tLiveRomes,"",tLiveRomes.size());
        } catch (Exception e) {
            return restReturn.error(200,tLiveRomes,"读取数据失败",0);
        }

    }

    @Override
    public RestReturn registerRoomByPhone(String phone) {
        try {
            TCustomerExample example = new TCustomerExample();
            example.createCriteria().andPhoneEqualTo(phone);
            TCustomer customer = customerMapper.selectByExample(example).get(0);
            TLiveRome rome = new TLiveRome();
            rome.setOwnerPhone(customer.getPhone());
            rome.setEnterpriseId(customer.getEnterpriseId());
            rome.setOwnerName(customer.getNickName());
            rome.setOwnerName(customer.getNickName());

            int insert = tLiveRomeMapper.insert(rome);
            customer.setLiveRomeId(rome.getRomeId());
            customerMapper.updateByExample(customer,example);
            ArrayList<TCustomer> tCustomers = new ArrayList<>();
            tCustomers.add(customer);
            return new RestReturn().success(tCustomers,"注册直播间成功",1);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestReturn().error(200,null,"注册直播间失败",0);
        }
    }

    @Override
    public RestReturn getRoomInfoById(int roomId) {
        TLiveRomeExample example = new TLiveRomeExample();
        example.createCriteria().andRomeIdEqualTo(roomId);
        try {
            List<TLiveRome> romes = tLiveRomeMapper.selectByExample(example);

            return new RestReturn().success(romes,"获取直播间信息成功",romes.size());
        } catch (Exception e) {
            e.printStackTrace();
            return new RestReturn().error(200,null,"获取直播间信息失败",0);
        }
    }

    @Override
    public void changeStatus(int roomId, String status) {
        TLiveRomeExample example = new TLiveRomeExample();
        example.createCriteria().andRomeIdEqualTo(roomId);
        tLiveRomeMapper.updataStatusById(roomId,status);

    }
}
