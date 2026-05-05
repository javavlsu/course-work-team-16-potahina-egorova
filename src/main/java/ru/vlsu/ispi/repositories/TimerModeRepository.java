package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.MusicMedia;
import ru.vlsu.ispi.beans.TimerMode;

@Repository
public interface TimerModeRepository extends JpaRepository<TimerMode, Integer> {
}
