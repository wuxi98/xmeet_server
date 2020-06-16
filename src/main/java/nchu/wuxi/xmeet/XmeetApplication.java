package nchu.wuxi.xmeet;

import nchu.wuxi.xmeet.danmu.netty.server.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@MapperScan("nchu.wuxi.xmeet.mapper")
@SpringBootApplication
public class XmeetApplication implements CommandLineRunner {

    @Autowired
    MyServer myServer;

    public static void main(String[] args) {
        SpringApplication.run(XmeetApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        myServer.start(8855,new MyChatInitializer());

    }
}
