package visang.dataportal.test.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import visang.dataportal.test.dto.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
}