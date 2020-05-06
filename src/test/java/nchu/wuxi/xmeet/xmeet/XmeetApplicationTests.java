package nchu.wuxi.xmeet.xmeet;

import nchu.wuxi.xmeet.controller.AccountController;
import nchu.wuxi.xmeet.entity.TCustomer;
import nchu.wuxi.xmeet.servlce.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.ArrayList;

@SpringBootTest
class XmeetApplicationTests {

    @Autowired
    AccountService accountService;
    @Test
    void Test() {
        //ArrayList<T> objects = new ArrayList<>();
    }

}
