package repo;

import lombok.Data;
import model.User;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserRepository {
    private Map<String, User> users;

    public UserRepository() {
        users = new HashMap<>();
    }
}
