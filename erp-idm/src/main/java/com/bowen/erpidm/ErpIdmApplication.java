package com.bowen.erpidm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class ErpIdmApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErpIdmApplication.class, args);
        /*list.add(null);
        String kk = "1";
        int res = Integer.parseInt(kk);
        time1();*/
    }

    public static void time1() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 获取今天的起始时间
        LocalDateTime startOfDay = today.atStartOfDay();
        System.out.println("Start of today: " + startOfDay.format(formatter));

        // 获取今天的结束时间
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        System.out.println("End of today: " + endOfDay.format(formatter));
    }


}
