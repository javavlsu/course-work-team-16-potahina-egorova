package ru.vlsu.ispi.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vlsu.ispi.beans.FriendRequest;
import ru.vlsu.ispi.beans.Notification;
import ru.vlsu.ispi.beans.UserSearchCriteria;
import ru.vlsu.ispi.repositories.FriendRequestRepository;
import ru.vlsu.ispi.repositories.NotificationRepository;
import ru.vlsu.ispi.repositories.UserRepository;
import ru.vlsu.ispi.beans.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       NotificationRepository notificationRepository,
                       FriendRequestRepository friendRequestRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    public List<User> searchUsers(UserSearchCriteria criteria) {
        return userRepository.findByCriteria(
                criteria.getUserId(),
                criteria.getName(),
                criteria.getEmail()
        );
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isPhoneExists(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    public User createUser(String name, String password,
                           String email, String phoneNumber, int totalPoints) {
        User user = new User(name, password, email, phoneNumber, totalPoints);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public User updateUser(int id, String name, String password,
                           String email, String phoneNumber, int totalPoints) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setTotalPoints(totalPoints);
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String name) {
        return userRepository.findByName(name);
    }

    public User getTestUser() {
        return userRepository.findByName("testuser")
                .orElseGet(() -> {
                    User testUser = new User();
                    testUser.setName("testuser");
                    testUser.setEmail("test@example.com");
                    testUser.setPhoneNumber("1234567890");
                    testUser.setTotalPoints(0);
                    return userRepository.save(testUser);
                });
    }

    public User registerUser(String name, String password, String email, String phoneNumber) {
        if (isEmailExists(email)) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        if (isPhoneExists(phoneNumber)) {
            throw new IllegalArgumentException("Пользователь с таким номером телефона уже существует");
        }

        User user = new User(name, password, email, phoneNumber, 0);
        return userRepository.save(user);
    }

    public Optional<User> authenticate(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }


    public User save(User user) {
        // Валидация обязательных полей
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Некорректный email");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 6 символов");
        }

        // Установка значений по умолчанию
        if (user.getTotalPoints() < 0) {
            user.setTotalPoints(0);
        }

        return userRepository.save(user);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }


    public User findById(Integer id) {
        return userRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void sendFriendRequest(int senderId, int receiverId) {
        User sender = getUserById(senderId)
                .orElseThrow(() -> new RuntimeException("Отправитель с ID " + senderId + " не найден"));
        User receiver = getUserById(receiverId)
                .orElseThrow(() -> new RuntimeException("Получатель с ID " + receiverId + " не найден"));;

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(FriendRequest.RequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        friendRequestRepository.save(request);

        // Создаём уведомление
        Notification notification = new Notification();
        notification.setUser(receiver);
        notification.setText("Пользователь " + sender.getName() + " хочет добавить вас в друзья");
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public void acceptFriendRequest(int requestId, int currentUserId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Запрос не найден"));

        if (request.getReceiver().getId() != currentUserId) {
            throw new RuntimeException("Недостаточно прав");
        }

        request.setStatus(FriendRequest.RequestStatus.ACCEPTED);
        friendRequestRepository.save(request);

        // Добавляем в друзья обоих пользователей
        User sender = request.getSender();
        User receiver = request.getReceiver();
        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);

        userRepository.save(sender);
        userRepository.save(receiver);

        // Уведомление об принятии
        Notification notification = new Notification();
        notification.setUser(sender);
        notification.setText("Пользователь " + receiver.getName() + " принял ваш запрос в друзья");
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public List<FriendRequest> getPendingRequests(int userId) {
        User user = getUserById(userId).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return friendRequestRepository.findByReceiverAndStatus(user, FriendRequest.RequestStatus.PENDING);
    }

    public List<Notification> getUnreadNotifications(int userId) {
        User user = getUserById(userId).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return notificationRepository.findByUserAndIsReadFalse(user);
    }
}
