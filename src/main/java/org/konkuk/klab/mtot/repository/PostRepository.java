package org.konkuk.klab.mtot.repository;

import org.konkuk.klab.mtot.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByPostId(Long PostId);
}
