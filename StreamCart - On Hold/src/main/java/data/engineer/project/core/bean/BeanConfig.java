package data.engineer.project.core.bean;

import data.engineer.project.core.catalog.Bar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BeanConfig {
    @Bean(name = "dawg")
    public Bar bar() {
        Bar bar = new Bar();
        log.info("Create a new bar");
        return bar;
    }
}
