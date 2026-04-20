package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.MusicMedia;

@Repository
public interface MusicMediaRepository extends JpaRepository<MusicMedia, Integer> {
}
