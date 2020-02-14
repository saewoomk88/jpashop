package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpashopApplication.class, args);
    }

    //==하이버네이트5 모듈이 프록시인건 출력을 안시켜줌
    @Bean
    Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }


}

