package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.ApprovalUpdateRequest;
import backend.VocaProject.admin.dto.UserListResponse;
import backend.VocaProject.domain.User;
import backend.VocaProject.response.BaseException;
import backend.VocaProject.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static backend.VocaProject.response.BaseExceptionStatus.NON_EXISTENT_USER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    /**
     * 유저 목록 조회
     * @return
     */
    @Override
    public List<UserListResponse> userList(Long adminId, String className, String approval) {
        User user = userRepository.findById(adminId).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));

        // 유저의 클래스 이름이 master면(마스터 관리자)
        if (user.getClassName().equals("master")) {
            // 입력한 keyword가 all 이면 승인 여부에 맞는 전체 유저 목록 조회
            if (className.equals("all")) {
                List<UserListResponse> allList = userRepository.findByApproval(approval).stream().map(UserListResponse::new).collect(Collectors.toList());
                return allList;
            }
            else {
                // 입력한 keyword에 맞는 클래스의 승인 여부가 Y인 유저 목록
                List<UserListResponse> listByClass = userRepository.findByClassNameAndApproval(className, "Y").stream().map(UserListResponse::new).collect(Collectors.toList());
                return listByClass;
            }
        }
        // 중간 관리자가 요청했을 경우 자기가 맡은 클래스의 승인 여부가 Y인 유저 목록만 조회
        else {
            className = user.getClassName();
            List<UserListResponse> listByClass = userRepository.findByClassNameAndApproval(className, "Y").stream().map(UserListResponse::new).collect(Collectors.toList());
            return listByClass;
        }
    }

    /**
     * 유저 승인 여부 변경
     * @param request
     */
    @Override
    @Transactional
    public void userUpdateApproval(ApprovalUpdateRequest request) {
        User user = userRepository.findByLoginId(request.getUserLoginId()).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));

        user.updateApproval(request.getApproval());
    }
}
