package nchu.wuxi.xmeet.controller;

import nchu.wuxi.xmeet.servlce.CustomerService;
import nchu.wuxi.xmeet.utils.RestReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @auther WuXi
 * @create 2020/4/21
 */
@RequestMapping("/customer")
@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RequestMapping(value = "/enterpriseId/{enterpriseId}",method = RequestMethod.GET)
    public RestReturn getCustomersByEnterpriseId(@PathVariable int enterpriseId){
        System.out.println("getCustomersByEnterpriseId:"+enterpriseId);
        return customerService.getByEnterprise(enterpriseId);
    }
    @RequestMapping(value = "/enterprise/{enterpriseId}",method = RequestMethod.GET)
    public RestReturn getEnterpriseByEnterpriseId(@PathVariable int enterpriseId){
        System.out.println("getEnterpriseByEnterpriseId:"+enterpriseId);
        return customerService.getEnterpriseByEnterpriseId(enterpriseId);
    }

    /**
     * 上传头像
     * @param file
     * @param phone
     * @return
     */
    @RequestMapping(value = "/uploadHeadPic",method = RequestMethod.POST)
    public RestReturn uploadHeadPic(@RequestParam("file") MultipartFile file,
                                    @RequestParam("phone") String phone){
        System.out.println("uploadHeadPic: phone="+phone);
        System.out.println(file);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        //构建文件上传所要保存的"文件夹路径"--这里是相对路径，保存到项目根路径的文件夹下
        String realPath = "src/main/resources/uploadHeadPic/";
        String format = sdf.format(new Date());
        //存放上传文件的文件夹
        File newFile = new File(realPath + format);
        if(!newFile.isDirectory()){
            //递归生成文件夹
            newFile.mkdirs();
        }
//        File dir = new File(newFile);
//
//        //判断目录是否存在，不存在则创建目录
//        if (!dir.exists()){
//            dir.mkdirs();
//        }

        //生成新文件名，防止文件名重复而导致文件覆盖
        //1、获取原文件后缀名 .img .jpg ....
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf('.'));
        //2、使用UUID生成新文件名
        String newFileName = UUID.randomUUID() + suffix;

        //生成文件
        //        C:\ftpfile\img  sdasdasd.jpg
        File fFile = new File(newFile.getAbsolutePath() + File.separator + newFileName);

        //传输内容
        try {
            file.transferTo(fFile);
        } catch (IOException e) {
            System.out.println("上传文件失败！");
            e.printStackTrace();
            return new RestReturn().success(null,"失败",0);
        }
        return customerService.uploadHead(fFile,phone);

    }

    /**
     * 创建企业
     * @param enterpriseName
     * @param phone
     * @return
     */
    @RequestMapping(value = "/createEnterprise",method = RequestMethod.POST)
    public RestReturn createEnterprise( String enterpriseName,
                                        String phone){
        System.out.println("createEnterprise: name:"+enterpriseName+"| phone="+phone);
        return customerService.createEnterprise(enterpriseName,phone);
    }

    /**
     * 获取企业信息
     * @return
     */
    @RequestMapping(value = "/enterprises",method = RequestMethod.GET)
    public RestReturn getEnterprises( ){
        System.out.println("getEnterprises");
        return customerService.getEnterprises();
    }

    /**
     * 加入企业
     * @param enterpriseId
     * @param phone
     * @param userName
     * @return
     */
    @RequestMapping(value = "/joinEnterprise",method = RequestMethod.POST)
    public RestReturn joinEnterprise(int enterpriseId,String phone, String userName ){
        System.out.println("joinEnterprise enterpriseId="+enterpriseId+" phone="+phone);
        return customerService.applyJoinEnterprise(enterpriseId,phone,userName);
    }


    /**
     * 查找用户
     * @param targetPhone
     * @return
     */
    @RequestMapping(value = "/targetPhone/{targetPhone}",method = RequestMethod.GET)
    public RestReturn searchUser(@PathVariable String targetPhone ){
        System.out.println("searchUser targetPhone="+targetPhone);
        return customerService.searchUser(targetPhone);
    }

    /**
     * 处理加入申请
     * @param enterpriseId
     * @param phone
     * @param code
     * @return
     */
    @RequestMapping(value = "/processEnterpriseApply/{enterpriseId}/{phone}/{code}",method = RequestMethod.POST)
    public RestReturn processEnterpriseApply(@PathVariable int enterpriseId,
                                             @PathVariable String phone,
                                             @PathVariable int code){
        System.out.println("processEnterpriseApply phone="+phone+",enterpriseId="+enterpriseId+",code="+code);
        return customerService.processEnterpriseApply(enterpriseId,phone,code);
    }

    /**
     * 企业管理员获取申请消息
     * @param phone
     * @return
     */
    @RequestMapping(value = "/notices/{phone}",method = RequestMethod.GET)
    public RestReturn notices(@PathVariable String phone){
        System.out.println("notices phone="+phone);
        return customerService.notices(phone);
    }

    @RequestMapping(value = "/updateName/{phone}/{name}",method = RequestMethod.POST)
    public void updateName(@PathVariable String phone,@PathVariable String name){
        System.out.println("updateName phone="+phone+",name="+name);
        customerService.updateName(phone,name);
    }
}
