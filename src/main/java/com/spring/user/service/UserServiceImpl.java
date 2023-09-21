package com.spring.user.service;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.user.DTO.*;
import com.spring.user.config.jwt.TokenProvider;
import com.spring.user.entity.Authority;
import com.spring.user.entity.RefreshToken;
import com.spring.user.entity.User;
import com.spring.user.repository.RefreshTokenRepository;
import com.spring.user.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final TokenProvider tokenProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
    }



    @Override
    public void signup(AddUserRequestDTO dto) { // 회원 email, password를 저장하고 password는 암호화

        boolean existEmail = userRepository.existsByEmail(dto.getEmail()); // 존재하는 이메일인지 확인
        User userNickname = userRepository.findByNickname(dto.getNickname());

        String password = dto.getPassword();
        String passwordCheck = dto.getConfirmPassword();

        dto.setAuthority(Authority.valueOf("USER")); // 회원가입은 기본 USER

        if(!password.equals(passwordCheck)){
            throw new CustomException(ExceptionCode.PASSWORD_WRONG);
        }

        if(existEmail){
            throw new CustomException(ExceptionCode.EXIST_EMAIL);
        }

        if(userNickname != null){
            throw new CustomException(ExceptionCode.EXIST_NICKNAME);
        }


        userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .imageUrl(dto.getImageUrl())
                .authority(dto.getAuthority())
                .build()
        );

    }

    //  persist, merge, remove와 같은 JPA 연산을 트랜잭션이 활성화된 상태가 아닌 곳에서 호출할 때
    //  TransactionRequiredException이 발생합니다. if문 내부에 토큰 삭제구문이 있으므로 transactional 걸어줘야함.
    @Transactional
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // 폼에서 입력한 로그인 아이디를 이용해 DB에 저장된 전체 정보 얻어오기
        User userInfo = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()
                -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        // 유저가 폼에서 날려주는 정보는 id랑 비번인데, 먼저 아이디를 통해 위에서 정보를 얻어오고
        // 비밀번호는 암호화 구문끼리 비교해야 하므로, 이 경우 bCryptEncoder의 .matchs(평문, 암호문) 를 이용하면
        // 같은 암호화 구문끼리 비교하는 효과가 생깁니다.
        // 상단에 bCryptPasswordEncoder 의존성을 생성한 후, if문 내부에서 비교합니다.
                                            // 폼에서 날려준 평문       // 디비에 들어있던 암호문
        if(bCryptPasswordEncoder.matches(loginRequest.getPassword(), userInfo.getPassword())){

            String accessToken = tokenProvider.generateAccessToken(userInfo, Duration.ofHours(2));//2시간동안 유효한 엑세스토큰 발급
            String refreshToken = tokenProvider.generateRefreshToken(userInfo, Duration.ofDays(7));//7일동안 유효한 리프레시토큰 발급


            //새로운 리프레시토큰 refreshToken 엔터티 객체에 담기
            RefreshToken newRefreshToken = new RefreshToken(userInfo.getUserId(), refreshToken);
            // DB에서 userId에 해당하는 refresh토큰 검색
            Optional<RefreshToken> existingToken = Optional.ofNullable(refreshTokenRepository.findByUserId(userInfo.getUserId()));

            // 기존 토근이 있든 없든 갱신하거나 새로 저장합니다.
            refreshTokenRepository.save(existingToken.orElse(newRefreshToken).update(newRefreshToken.getRefreshToken()));
                                        // null이라면 new토큰 저장             // 이미 있다면 업데이트후 저장


            LoginResponseDTO loginResponseDTO = new LoginResponseDTO().builder()
                                                    .accessToken(accessToken)
                                                    .refreshToken(refreshToken)
                                                    .authority(String.valueOf(userInfo.getAuthority()))
                                                    .userId(userInfo.getUserId())
                                                    .email(userInfo.getEmail())
                                                    .nickname(userInfo.getNickname())
                                                    .build();



            // accessToken과 refreshToken 둘다 저장, user정보도 넘김
            return loginResponseDTO;
        } else {
            throw new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요.");
        }
    }

    @Transactional
    public void logout(String accessToken, String refreshToken) {
        if(!tokenProvider.validToken(accessToken)){
            throw new IllegalArgumentException("로그아웃 : 유효하지 않은 토큰입니다.");
        }

        RefreshToken deleteRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);

        if (deleteRefreshToken == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }else {
            refreshTokenRepository.deleteByRefreshToken(deleteRefreshToken.getRefreshToken());
        }


    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }


    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ExceptionCode.USER_NOT_FOUND));
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        return user;
    }

    // 유저 정보 수정
    @Override
    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) {

        User user = userRepository.findById(userUpdateDTO.getUserId())

                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        User updatingUser = User.builder()
                .userId(userUpdateDTO.getUserId())
                .email(user.getEmail())
                .nickname(userUpdateDTO.getNickname())
                .password(user.getPassword())
                .imageUrl(userUpdateDTO.getImageUrl())
                .point(user.getPoint())
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(updatingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = this.userRepository.findById(id).orElseThrow(
                () -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        this.userRepository.delete(user);
    }

    @Override
    public boolean authenticateUser(LoginRequestDTO loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        if (user.isPresent() && bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            // 인증 성공
            return true;
        }
        // 인증 실패
        return false;
    }

    // 중복 닉네임 검증
    @Override
    public boolean isNicknameAvailable(String nickname) {
        // 닉네임으로 회원 조회
        User existingUser = userRepository.findByNickname(nickname);
        // 중복되지 않으면 true, 중복되면 false를 반환합니다.
        return existingUser == null;
    }

    // 유저 포인트 조회
    @Override
    public UserPointResponseDTO getUserPointById(Long userId) {
        // 유저 ID로 유저 정보를 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        // 조회한 유저 정보에서 포인트를 가져와서 DTO에 담아 반환
        return new UserPointResponseDTO(user.getUserId(), user.getPoint());
    }

    // 유저 포인트 사용 시 포인트 수정
    @Override
    public boolean usePoints(User user, int usedPoints) {
        int currentPoints = user.getPoint();

        if(currentPoints < usedPoints) {
            throw new CustomException(ExceptionCode.INSUFFICIENT_POINT_EXCEPTION);
        }

        int updatedPoints = currentPoints - usedPoints;

        // 엔티티 객체 복제
        User updatingUser = User.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .imageUrl(user.getImageUrl())
                .point(updatedPoints) // 포인트만 업데이트
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(updatingUser); // 수정된 유저 정보 저장
        return true;

    }



}