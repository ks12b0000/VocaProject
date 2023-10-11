package backend.VocaProject.user;

import backend.VocaProject.domain.User;
import backend.VocaProject.user.dto.JoinRequest;
import backend.VocaProject.user.dto.LoginRequest;
import backend.VocaProject.user.dto.LoginResponse;

public interface UserService {

    void userJoin(JoinRequest dto);

    boolean checkLoginIdDuplicate(String loginId);

    User getLoginUserByLoginId(String loginId);

    LoginResponse login(LoginRequest loginRequest);
}
