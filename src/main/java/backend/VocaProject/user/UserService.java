package backend.VocaProject.user;

import backend.VocaProject.domain.User;
import backend.VocaProject.jwt.dto.GeneratedToken;
import backend.VocaProject.user.dto.JoinRequest;
import backend.VocaProject.user.dto.LoginRequest;

public interface UserService {

    void userJoin(JoinRequest dto);

    boolean checkLoginIdDuplicate(String loginId);

    GeneratedToken login(LoginRequest loginRequest);
}
