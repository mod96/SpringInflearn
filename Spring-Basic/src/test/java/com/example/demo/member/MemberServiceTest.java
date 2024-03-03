package com.example.demo.member;

import com.example.demo.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberServiceTest {
    MemberService memberService;

    @BeforeEach
    public void beforeAll() {
//        AppConfig appConfig = new AppConfig();
//        memberService = appConfig.memberService();
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        memberService = applicationContext.getBean("memberService", MemberService.class);
    }

    @Test
    void join() {
        // given
        Member member = new Member(1L, "memberA", Grade.VIP);
        // when
        memberService.join(member);
        Member findMember = memberService.findMember(1L);
        // then
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
