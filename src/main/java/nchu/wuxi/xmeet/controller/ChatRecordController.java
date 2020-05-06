package nchu.wuxi.xmeet.controller;

import nchu.wuxi.xmeet.entity.TChatRecord;
import nchu.wuxi.xmeet.servlce.TChatRecordService;
import nchu.wuxi.xmeet.utils.RestReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther WuXi
 * @create 2020/5/4
 */
@RestController
@RequestMapping("/chatRecord")
public class ChatRecordController {

    @Autowired
    TChatRecordService chatRecordService;

    @RequestMapping(value = "/loadChatRecord/{phone}",method = RequestMethod.GET)
    public RestReturn loadChatRecord(@PathVariable String phone){
        return chatRecordService.loadChatRecord(phone);
    }
    @RequestMapping(value = "deleteChatRecord/{phone}",method = RequestMethod.DELETE)
    public void deleteChatRecord(@PathVariable String phone){
        chatRecordService.deleteChatRecord(phone);
    }
}
