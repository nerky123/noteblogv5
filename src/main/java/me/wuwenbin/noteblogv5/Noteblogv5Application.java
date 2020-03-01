package me.wuwenbin.noteblogv5;

import me.wuwenbin.noteblogv5.annotation.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wuwenbin
 */
@SpringBootApplication
@MapperScan(basePackages = "me.wuwenbin.noteblogv5.mapper", annotationClass = Mapper.class)
@EnableScheduling
@EnableCaching
public class Noteblogv5Application  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Noteblogv5Application.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
        return applicationBuilder.sources(Noteblogv5Application.class);
    }
}
