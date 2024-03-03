package com.example.demo.autowired;

import com.example.demo.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredBeanTest {
    @Test
    public void AutowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    static class TestBean {
        @Autowired(required = false)
        public void setNoBean1(Member noBean) {
            System.out.println("setNoBean1 = " + noBean);
        }

        @Autowired
        public void setNoBean2(@Nullable Member noBean) {
            System.out.println("setNoBean2 = " + noBean);
        }

        @Autowired(required = false)
        public void setNoBean3(Optional<Member> noBean) {
            System.out.println("setNoBean3 = " + noBean);
        }
    }
}
