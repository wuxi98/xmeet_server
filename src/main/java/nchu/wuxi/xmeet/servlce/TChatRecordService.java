package nchu.wuxi.xmeet.servlce;

import nchu.wuxi.xmeet.utils.RestReturn;

public interface TChatRecordService {
    RestReturn loadChatRecord(String phone);
    void deleteChatRecord(String phone);
}
