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
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    @Value("${headpic.url.prefix}")
    String headPrefix;

    @Value("${ftp.host}")
    String ftpHost;

    @Value("${ftp.port}")
    int ftpPort;

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

    @Override
    public void updateNotice(int roomId, String msg) {
        tLiveRomeMapper.updateNotice(roomId,msg);
    }

    @Override
    public RestReturn uploadPage(File file, int roomId) {
        FTPClient ftpClient = new FTPClient();
        try {
            //连接ftp服务器 参数填服务器的ip
            ftpClient.connect(ftpHost,ftpPort);

            //进行登录 参数分别为账号 密码
            ftpClient.login("vsftpuser","0.0..0.0");

            int replyCode = ftpClient.getReplyCode(); // 是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("connect failed...ftp服务器:" + "39.106.64.220" + ":" + 21);
            }
            //改变工作目录（按自己需要是否改变）
            //只能选择local_root下已存在的目录
            ftpClient.changeWorkingDirectory("image");

            //设置文件类型为二进制文件
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

//开启被动模式（按自己如何配置的ftp服务器来决定是否开启）
            ftpClient.enterLocalPassiveMode();

            //上传文件 参数：上传后的文件名，输入流
            boolean b = ftpClient.storeFile(file.getName(), new FileInputStream(file));
            if (b){
                System.out.println("上传文件成功");
            }else {
                System.out.println("上传文件失败");
                return new RestReturn().success(null,"失败",0);
            }
            String fullName = headPrefix+file.getName();
            tLiveRomeMapper.updatePageUrl(roomId,fullName);
            ArrayList<String> list = new ArrayList<>();
            list.add(fullName);
            return new RestReturn().success(list,"成功",list.size());
        } catch (IOException e) {
            e.printStackTrace();
            return new RestReturn().success(null,"失败",0);
        }
    }
}
