package com.cdfg.custdept;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.cdfg.custdept.dao")
@SpringBootApplication
public class CustdeptApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustdeptApplication.class, args);
    }

}
