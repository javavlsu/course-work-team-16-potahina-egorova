package ru.vlsu.ispi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.ispi.beans.*;
import ru.vlsu.ispi.repositories.FriendRequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public FriendRequestService(FriendRequestRepository friendRequestRepository,
                                NotificationService notificationService,
                                UserService userService) {
        this.friendRequestRepository = friendRequestRepository;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    public FriendRequest createFriendRequest(User sender, User receiver) {
        FriendRequest request = new FriendRequest(
                sender,
                receiver,
                FriendRequest.RequestStatus.PENDING,
                LocalDateTime.now()
        );
        FriendRequest savedRequest = friendRequestRepository.save(request);

        // Создаём уведомление для получателя
        notificationService.createNotification(
                receiver,
                "Пользователь " + sender.getName() + " хочет добавить вас в друзья"
        );
        return savedRequest;
    }

    // Остальные методы остаются без изменений
    public Optional<FriendRequest> getFriendRequestById(Integer id) {
        return friendRequestRepository.findById(id);
    }

    public FriendRequest updateFriendRequestStatus(Integer id, FriendRequest.RequestStatus status) {
        FriendRequest request = friendRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));
        request.setStatus(status);
        return friendRequestRepository.save(request);
    }


    private void updateUserEntity(User user) {
        userService.updateUser(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getTotalPoints()
        );
    }

    public FriendRequest acceptFriendRequest(Integer id) {
        FriendRequest request = updateFriendRequestStatus(id, FriendRequest.RequestStatus.ACCEPTED);

        // Добавляем пользователей в друзья друг другу
        User sender = request.getSender();
        User receiver = request.getReceiver();
        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);

        updateUserEntity(sender);
        updateUserEntity(receiver);

        // Уведомление отправителю о принятии запроса
        notificationService.createNotification(
                sender,
                "Пользователь " + receiver.getName() + " принял ваш запрос в друзья"
        );
        return request;
    }

    public FriendRequest rejectFriendRequest(Integer id) {
        return updateFriendRequestStatus(id, FriendRequest.RequestStatus.REJECTED);
    }

    public void deleteFriendRequest(Integer id) {
        friendRequestRepository.deleteById(id);
    }

    public List<FriendRequest> getSentFriendRequests(User sender) {
        return friendRequestRepository.findBySender(sender);
    }

    public List<FriendRequest> getReceivedFriendRequests(User receiver) {
        return friendRequestRepository.findByReceiver(receiver);
    }

    public List<FriendRequest> getPendingFriendRequestsForUser(User user) {
        return friendRequestRepository.findByReceiverAndStatus(user, FriendRequest.RequestStatus.PENDING);
    }

    public List<FriendRequest> getAllFriendRequests() {
        return friendRequestRepository.findAll();
    }
}
