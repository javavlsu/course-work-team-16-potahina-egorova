package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.VisualMedia;

@Repository
public interface VisualMediaRepository extends JpaRepository<VisualMedia, Integer> {
}
