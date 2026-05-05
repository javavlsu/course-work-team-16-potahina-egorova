package ru.vlsu.ispi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUser(User user);

    @Query("SELECT t FROM Task t WHERE t.user = :user " +
            "AND (:category IS NULL OR t.category = :category) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "ORDER BY t.id DESC")
    Page<Task> findByUserAndFilters(
            @Param("user") User user,
            @Param("category") Task.Category category,
            @Param("status") Task.Status status,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("SELECT t FROM Task t JOIN FETCH t.assignedUser WHERE t.id = :id")
    Optional<Task> findByIdWithAssignedUser(@Param("id") Integer id);

    List<Task> findByAssignedUser(User assignedUser);

    Page<Task> findByAssignedUserOrderByDeadlineAtAsc(User assignedUser, Pageable pageable);

    Page<Task> findByUser(User user, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.assignedUser = :assignedUser AND t.user != :assignedUser")
    Page<Task> findByAssignedUserAndUserNot(
            @Param("assignedUser") User assignedUser,
            @Param("assignedUser") User user,
            Pageable pageable
    );

    @Query("SELECT t FROM Task t WHERE t.assignedUser = :user " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:search IS NULL OR LOWER(t.title) LIKE CONCAT('%', LOWER(:search), '%')) " +
            "ORDER BY " +
            "CASE WHEN t.status = 'Completed' THEN 1 ELSE 0 END ASC, " +
            "t.id DESC")
    Page<Task> findByAssignedUserAndFilters(
            @Param("user") User user,
            @Param("status") Task.Status status,
            @Param("search") String search,
            Pageable pageable);
}
