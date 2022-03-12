package service.impl;

import lombok.AllArgsConstructor;
import model.User;
import repo.UserRepository;
import service.UserService;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public void add(User user) {
        userRepository.getUsers().put(user.getUserId(), user);
    }

    @Override
    public User get(String userId) {
        if(!userRepository.getUsers().containsKey(userId)){
            throw new RuntimeException("Not a registered User: "+userId);
        }
        return userRepository.getUsers().get(userId);
    }

}
