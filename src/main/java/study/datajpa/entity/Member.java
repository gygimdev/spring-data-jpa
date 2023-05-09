package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username"
)
public class Member {

    /**
     * 컬럼
     */
    @Id @GeneratedValue
    @Column(name = "memberId")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="team_id")
    private Team team;


    /**
     * 생성자
     */
    public Member(String memberName) {
        this.username = memberName;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null)  {
            changeTeam(team);
        }
    }

    /**
     * 팀변경 로직
     */
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }



}
