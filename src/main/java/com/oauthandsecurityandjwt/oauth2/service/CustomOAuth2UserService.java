package com.oauthandsecurityandjwt.oauth2.service;

import com.oauthandsecurityandjwt.oauth2.dto.KakaoResponse;
import com.oauthandsecurityandjwt.oauth2.dto.NaverResponse;
import com.oauthandsecurityandjwt.oauth2.dto.OAuth2Response;
import com.oauthandsecurityandjwt.oauth2.entity.UserEntity;
import com.oauthandsecurityandjwt.oauth2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    //DefaultOAuth2UserService OAuth2UserService의 구현체

    private final UserRepository userRepository;

    @Override                   // 인증 데이터가 넘어옴
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //"registraionId" 로 어떤 OAuth 로 로그인 했는지 확인 가능(google,naver 등)
        log.info("getClientRegistration: "+userRequest.getClientRegistration());
        log.info("getAccessToken: "+userRequest.getAccessToken().getTokenValue());
        log.info("getAttributes: "+ super.loadUser(userRequest).getAttributes());


        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());

        // 어디서 넘어온 것인지 확인(naver, google ..)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")){
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username);
        String role = "ROLE_USER";

        if (existData == null) {

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setRole(role);

            userRepository.save(userEntity);
        } else {

            existData.setUsername(username);
            existData.setEmail(oAuth2Response.getEmail());

            role = existData.getRole();

            userRepository.save(existData);
        }

        return new CustomOauth2User(oAuth2Response, role);
    }
}
