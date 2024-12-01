package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.dto.api.RegistrationBody;
import com.victordev.ferreclickbackend.dto.api.UserDto;
import com.victordev.ferreclickbackend.dto.security.LoginRequest;
import com.victordev.ferreclickbackend.dto.security.LoginResponse;
import com.victordev.ferreclickbackend.exceptions.user.UserAlreadyExistsException;
import com.victordev.ferreclickbackend.persistence.entity.Cart;
import com.victordev.ferreclickbackend.persistence.entity.Role;
import com.victordev.ferreclickbackend.persistence.entity.User;
import com.victordev.ferreclickbackend.persistence.repository.CartRepository;
import com.victordev.ferreclickbackend.persistence.repository.RoleRepository;
import com.victordev.ferreclickbackend.persistence.repository.UserRepository;
import com.victordev.ferreclickbackend.service.ICartService;
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
    private CartRepository cartRepository;
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
    @Autowired
    private ICartService cartService;


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

        User userSaved = userRepository.save(user);
        Cart cart = cartService.createCart(userSaved);
        userSaved.setCart(cart);
        userRepository.save(userSaved);

        return userSaved;
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {

        Optional<User> OpUser = userRepository.findByEmailIgnoreCase(loginRequest.email());
        if (OpUser.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = OpUser.get();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                loginRequest.password(),
                userDetails.getAuthorities()
        );

        authenticationManager.authenticate(authentication);

        String jwt = jwtService.generateToken(userDetails, generateExtraClaims(userDetails));
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
        extraClaims.put("userId", user.getId());
        return extraClaims;
    }

    public UserDto getUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: "+ userId));

        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().getName().name());

        return userDto;
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
