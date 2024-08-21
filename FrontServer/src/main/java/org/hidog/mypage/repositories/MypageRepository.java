package org.hidog.mypage.repositories;

import org.hidog.file.entities.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MypageRepository extends JpaRepository<FileInfo, Long> {
    Optional<FileInfo> findByGid(String gid);
}