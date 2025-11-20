package lunaytik.web.service;

import lunaytik.web.domain.User;

public interface UserService {
    void register(String username, String password);
    User findByUsername(String username);
}
