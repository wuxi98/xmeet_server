package nchu.wuxi.xmeet.servlce.impl;

import nchu.wuxi.xmeet.entity.*;
import nchu.wuxi.xmeet.mapper.TApplyResponseMapper;
import nchu.wuxi.xmeet.mapper.TCustomerMapper;
import nchu.wuxi.xmeet.mapper.TEnterpriseJoinApplyMapper;
import nchu.wuxi.xmeet.mapper.TEnterpriseMapper;
import nchu.wuxi.xmeet.servlce.CustomerService;
import nchu.wuxi.xmeet.utils.RestReturn;
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
 * @create 2020/4/21
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    TCustomerMapper tCustomerMapper;

    @Autowired
    TEnterpriseMapper tEnterpriseMapper;

    @Autowired
    TEnterpriseJoinApplyMapper joinApplyMapper;


    @Value("${headpic.url.prefix}")
    String headPrefix;

    @Value("${ftp.host}")
    String ftpHost;

    @Value("${ftp.port}")
    int ftpPort;

    @Override
    public RestReturn getByEnterprise(int enterpriseId) {
        TCustomerExample example = new TCustomerExample();
        example.createCriteria().andEnterpriseIdEqualTo(enterpriseId);

        try {
            List<TCustomer> list = tCustomerMapper.selectByExample(example);
            return new RestReturn().success(list,"获取成功",list.size());
        } catch (Exception e) {
            e.printStackTrace();
            return new RestReturn().error(0,null,"获取失败",0);
        }
    }

    @Override
    public RestReturn getEnterpriseByEnterpriseId(int enterpriseId) {
        try {
            TEnterpriseExample example = new TEnterpriseExample();
            example.createCriteria().andEnterpriseIdEqualTo(enterpriseId);
            List<TEnterprise> list = tEnterpriseMapper.selectByExample(example);
            return new RestReturn().success(list,"获取成功",list.size());
        } catch (Exception e) {
            e.printStackTrace();
            return new RestReturn().error(0,null,"获取失败",0);
        }
    }

    @Override
    public RestReturn uploadHead(File file, String phone) {
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
            System.out.println(file.getName());
            if (b){
                System.out.println("上传文件成功");
            }else {
                System.out.println("上传文件失败");
                return new RestReturn().success(null,"失败",0);
            }
            String fullName = headPrefix+file.getName();
            tCustomerMapper.updateHeadUrl(phone,fullName);
            ArrayList<String> list = new ArrayList<>();
            list.add(fullName);
            return new RestReturn().success(list,"成功",list.size());
        } catch (IOException e) {
            e.printStackTrace();
            return new RestReturn().success(null,"失败",0);
        }
    }

    @Override
    public RestReturn createEnterprise(String enterpriseName, String phone) {

        try {
            TEnterprise enterprise = new TEnterprise();
            enterprise.setEnterpriseName(enterpriseName);
            enterprise.setManagerPhone(phone);
            int insert = tEnterpriseMapper.insert(enterprise);
            tCustomerMapper.updateManageEnterprise(phone,enterprise.getEnterpriseId());
            List<TEnterprise> list = new ArrayList<>();
            if (insert > 0){
                list.add(enterprise);
                return new RestReturn().success(list,"创建成功",1);
            }
            return new RestReturn().error(0,null,"创建失败",0);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestReturn().error(0,null,"创建失败",0);
        }

    }

    @Override
    public RestReturn getEnterprises() {
        List<TEnterprise> tEnterprises = tEnterpriseMapper.selectByExample(null);
        return new RestReturn().success(tEnterprises,"成功",tEnterprises.size());
    }

    @Override
    public RestReturn searchUser(String targetPhone) {
        TCustomerExample example = new TCustomerExample();
        example.createCriteria().andPhoneEqualTo(targetPhone);
        List<TCustomer> list = tCustomerMapper.selectByExample(example);
        return new RestReturn().success(list,"成功",list.size());
    }

    @Override
    public RestReturn applyJoinEnterprise(int enterpriseId, String phone,String name) {
        TEnterpriseJoinApply record = new TEnterpriseJoinApply();
        record.setApplyName(name);
        record.setEnterpriseId(enterpriseId);
        record.setPhone(phone);
        record.setState(0);
        joinApplyMapper.insert(record);
        return null;
    }

    @Override
    public RestReturn processEnterpriseApply(int enterpriseId, String phone, int code) {
        TApplyResponseExample example = new TApplyResponseExample();
        example.createCriteria().andEnterpriseIdEqualTo(enterpriseId).andApplyPhoneEqualTo(phone);
        joinApplyMapper.processEnterpriseApply(enterpriseId,phone,code);
        tCustomerMapper.joinEnterprise(phone,enterpriseId);
        return null;
    }

    @Override
    public RestReturn notices(String phone) {
        List<TEnterpriseJoinApply> list = joinApplyMapper.selestByManagerId(phone);
        return new RestReturn().success(list,"success",list.size());
    }

    @Override
    public void updateName(String phone, String name) {
        tCustomerMapper.updateName(phone,name);
    }

    @Override
    public RestReturn getHeadUrl(String phone) {
    TCustomerExample example = new TCustomerExample();
        example.createCriteria().andPhoneEqualTo(phone);
    List<TCustomer> tCustomers = tCustomerMapper.selectByExample(example);
    List<String> list = new ArrayList<>();
        list.add(tCustomers.get(0).getHeadUrl());
        return new RestReturn().success(list,"success",list.size());
}
}
