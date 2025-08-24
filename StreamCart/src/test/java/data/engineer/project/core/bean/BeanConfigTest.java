package data.engineer.project.core.bean;

import data.engineer.project.core.catalog.Bar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
public class BeanConfigTest {
    ApplicationContext context;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(BeanConfig.class);
    }

    @Test
    void testCreateBean() {
        Assertions.assertNotNull(context);
    }

    @Test
    void testGetBean() {
        Bar bar1 = context.getBean(Bar.class);
        Bar bar2 = context.getBean(Bar.class);

        Assertions.assertSame(bar1, bar2);
    }
}
