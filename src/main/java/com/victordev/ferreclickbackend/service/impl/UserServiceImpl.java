package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.dto.api.RegistrationBody;
import com.victordev.ferreclickbackend.dto.security.LoginRequest;
import com.victordev.ferreclickbackend.dto.security.LoginResponse;
import com.victordev.ferreclickbackend.exception.UserAlreadyExistsException;
import com.victordev.ferreclickbackend.persistence.entity.Role;
import com.victordev.ferreclickbackend.persistence.entity.User;
import com.victordev.ferreclickbackend.persistence.repository.RoleRepository;
import com.victordev.ferreclickbackend.persistence.repository.UserRepository;
import com.victordev.ferreclickbackend.service.IUserService;
import com.victordev.ferreclickbackend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private HttpSecurity http;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;


    @Override
    public User registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {

        if (userRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()
        || userRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        String encodedPassword = passwordEncoder.encode(registrationBody.getPassword());

        Role role = roleRepository.findByName(Role.RoleEnum.valueOf(registrationBody.getRole()))
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setName(registrationBody.getName());
        user.setUsername(registrationBody.getUsername());
        user.setEmail(registrationBody.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setEnabled(true);
        user.setAccountExpired(false);
        user.setAccountLocked(false);
        user.setCredentialsExpired(false);

        return userRepository.save(user);
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {

        Optional<User> OpUser = userRepository.findByEmailIgnoreCase(loginRequest.email());
        UserDetails user = userDetailsService.loadUserByUsername(OpUser.get().getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user,
                loginRequest.password(), user.getAuthorities());

        authenticationManager.authenticate(authentication);

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        return new LoginResponse(jwt);
    }

    private Map<String, Object> generateExtraClaims(UserDetails userDetails){
        Map<String, Object> extraClaims = new HashMap<>();

        User user = (User) userDetails;
        String roleName = user.getRole().getName().name();

        extraClaims.put("role", roleName);
        extraClaims.put("authorities",
                user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));
        return extraClaims;
    }

    public void logout(){
        try{
            http.logout(httpSecurityLogoutConfigurer -> {
                httpSecurityLogoutConfigurer.deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true);
            });
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
