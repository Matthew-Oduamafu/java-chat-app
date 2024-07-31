package io.nerdbyteslns.chatapp.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(User user) {
        // save user to database
        user.setStatus(Status.ONLINE);
        userRepository.save(user);
    }

    public void disconnect(User user) {
        // disconnect user
        var storedUser = userRepository.findById(user.getNickName());
        if (storedUser.isPresent()) {
            storedUser.get().setStatus(Status.OFFLINE);
            userRepository.save(storedUser.get());
        }
    }

    public List<User> findConnectedUsers() {
        // find connected users
        return userRepository.findAllByStatus(Status.ONLINE);
    }
}
