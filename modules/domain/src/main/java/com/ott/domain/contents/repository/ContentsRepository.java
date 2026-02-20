package com.ott.domain.contents.repository;

import com.ott.domain.contents.domain.Contents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsRepository extends JpaRepository<Contents, Long> {
}
