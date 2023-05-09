package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    /**
     * 스프링 데이터 JPA 테스트
     */
    @Autowired MemberRepository memberRepository;

    @Test
    public void testMember() {
        Member member = new Member("membera");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());

    }

    @Test
    public void paging() {
        int age = 10;
//        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
//        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //DTO 로 변환하는 팁
//        page.map(member -> new MemberDto(member.getId())...

        //then
//        List<Member> content = page.getContent(); // 데이터
//        long totalElements =  page.getTotalElements(); // 총 데이터 카운트
        }
    }

}