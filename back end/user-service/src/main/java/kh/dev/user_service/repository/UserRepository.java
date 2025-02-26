package kh.dev.user_service.repository;

import java.util.Optional;
import kh.dev.user_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByIdAndEmail(Long id, String email);

  boolean existsByEmail(String email);
}
