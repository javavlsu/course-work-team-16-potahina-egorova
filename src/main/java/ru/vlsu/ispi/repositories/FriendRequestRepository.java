package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.FriendRequest;
import ru.vlsu.ispi.beans.Notification;
import ru.vlsu.ispi.beans.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    List<FriendRequest> findBySender(User sender);
    List<FriendRequest> findByReceiver(User receiver);
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequest.RequestStatus status);
    Optional<FriendRequest> findById(Integer id);
}
