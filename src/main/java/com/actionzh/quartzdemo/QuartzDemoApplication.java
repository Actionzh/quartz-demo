package com.actionzh.quartzdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;

@SpringBootApplication(exclude = {
        SessionAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        RabbitAutoConfiguration.class,
        FreeMarkerAutoConfiguration.class,
        SpringDataWebAutoConfiguration.class,
        FlywayAutoConfiguration.class,
        RedisAutoConfiguration.class,
})
public class QuartzDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzDemoApplication.class, args);
    }

}
