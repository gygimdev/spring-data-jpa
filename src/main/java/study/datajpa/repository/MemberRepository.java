package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

//    @Query(name = "Member.findByUsername") <-없어도된다.
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    /**
     * 모든 맴버의 이름을 가져오고 싶을때
     */
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    /**
     * DTO 로 반환하는 방법
     */
    @Query("select new study.datajpa.dto.MemberDto m.id, m.username, t.name from Member m join m.team")
    List<MemberDto> findMemberDto();

    /**
     * In 절과, 파라미터 바인딩
     */
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    /**
     * 반환 타입 1. 컬랙션, 2. 단건, 3. 옵셔널
     */
    List<Member> findList1ByUsername(String username); //컬랙션
    Member findList2ByUsername(String username); //단건
    Optional<Member> findList3ByUsername(String username); //컬랙션

    /**
     * 페이지네이션
     * 1. 기본 페이지네이션 쿼리
     * 2. 최적화: 카운트 쿼리 분리
     */
    @Query(value = "select m from Member m left join m.team t",
    countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    /**
     * 벌크 업데이트
     * 주의: 벌크 연산은 영속성 컨테이너를 거치지 않고 적용된다.
     * 벌크 연산이 끝나고 영속성 컨테이너를 제거 해야한다.
     * em.clear(); 추가 필수 혹은 @Modifying(clearAutomatically = true) 작성해주어야한다.
     */
    @Modifying(clearAutomatically = true) // <- 업데이트시 꼭 넣어줘야한다
    @Query(value = "update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    /**
     * 패치 조인
     * 지연로딩이였던 team 을 패치조인을 통해 N+1 문제를 해결할 수 있다.
     * 혹은 @EntityGraph(attributePaths = ("team") 을 통해서 해결할 수 있다.
     */
    @Query("select m from Member m left join fetch m.team ")
    List<Member> findMemberFetchJoin();

    @EntityGraph(attributePaths = ("team"))
    @Query("select m from Member m")
    List<Member> findMemberFetchJoin2();

    /**
     * ReadyOnly 설정하면 중간에 불필요한 dirty check 를 건너 뛰어 최적화가 가능하다
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    /**
     * 로직 수행시 디비 락을 걸어버린다.
     *
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}