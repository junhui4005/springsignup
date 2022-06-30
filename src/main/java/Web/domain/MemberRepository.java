package Web.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Integer> {
    Optional<MemberEntity> findBymid(String mid);
}
