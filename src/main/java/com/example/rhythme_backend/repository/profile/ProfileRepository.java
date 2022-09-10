package com.example.rhythme_backend.repository.profile;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Profile findByMember(Member member);
}
