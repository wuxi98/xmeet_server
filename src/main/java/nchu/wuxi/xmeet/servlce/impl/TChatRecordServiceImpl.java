package nchu.wuxi.xmeet.servlce.impl;

import nchu.wuxi.xmeet.entity.TChatRecord;
import nchu.wuxi.xmeet.entity.TChatRecordExample;
import nchu.wuxi.xmeet.mapper.TChatRecordMapper;
import nchu.wuxi.xmeet.servlce.TChatRecordService;
import nchu.wuxi.xmeet.utils.RestReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther WuXi
 * @create 2020/5/4
 */
@Service
public class TChatRecordServiceImpl implements TChatRecordService {

    @Autowired
    TChatRecordMapper chatRecordMapper;

    @Override
    public RestReturn loadChatRecord(String phone) {
        TChatRecordExample example = new TChatRecordExample();
        example.createCriteria().andToIdEqualTo(phone);
        try {
            List<TChatRecord> records = chatRecordMapper.selectByExample(example);
            return new RestReturn().success(records,"success",records.size());
        } catch (Exception e) {
            e.printStackTrace();
            return new RestReturn().error(0,null,"",0);
        }

    }

    @Override
    public void deleteChatRecord(String phone) {
        TChatRecordExample example = new TChatRecordExample();
        example.createCriteria().andToIdEqualTo(phone);
        chatRecordMapper.deleteByExample(example);
    }
}
