package nchu.wuxi.xmeet.controller;

import nchu.wuxi.xmeet.servlce.LiveRoomService;
import nchu.wuxi.xmeet.utils.RestReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @auther WuXi
 * @create 2020/4/13
 */
@RestController
@RequestMapping("/liveRoom")
public class LiveRoomController {

    @Autowired
    LiveRoomService liveRoomService;


    @RequestMapping("/{enterpriseId}")
    public RestReturn getRoomByEnterpriseId(@PathVariable int enterpriseId){
        System.out.println("getRoomByEnterpriseId"+enterpriseId);
        return liveRoomService.getRoomByEnterpriseId(enterpriseId);
    }
    @PostMapping(value = "/register")
    public RestReturn registerRoomByPhone(String phone){
        System.out.println("register"+phone);
        return liveRoomService.registerRoomByPhone(phone);
    }
    @RequestMapping("/getInfoById/{roomId}")
    public RestReturn getRoomInfoById(@PathVariable int roomId){
        System.out.println("getInfoById"+roomId);
        return liveRoomService.getRoomInfoById(roomId);
    }

    @PostMapping("/changeStatus")
    public void changeStatus(String roomId,String status){
        System.out.println("changeStatus"+roomId+" status:"+status);
        int id = 0;
        try {
            id = Integer.parseInt(roomId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        liveRoomService.changeStatus(id,status);
    }

    @PostMapping("/updateNotice")
    public void updateNotice(int roomId,String msg){
        System.out.println("updateNotice"+roomId+" msg:"+msg);
        liveRoomService.updateNotice(roomId,msg);
    }

    @RequestMapping(value = "/uploadPage",method = RequestMethod.POST)
    public RestReturn uploadPage(@RequestParam("file") MultipartFile file,
                                    @RequestParam("roomId") int roomId){
        System.out.println("uploadPage: roomId="+roomId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        //构建文件上传所要保存的"文件夹路径"--这里是相对路径，保存到项目根路径的文件夹下
        String realPath = "src/main/resources/uploadPage/";
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
            e.printStackTrace();
            return new RestReturn().success(null,"失败",0);
        }
        return liveRoomService.uploadPage(fFile,roomId);

    }
}
