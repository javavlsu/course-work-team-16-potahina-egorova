package ru.vlsu.ispi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.ispi.repositories.UserRepository;
import ru.vlsu.ispi.beans.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        // Проверка, существует ли пользователь с таким email
        if (userRepository.findByEmailAndPassword(email, password).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
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
}
