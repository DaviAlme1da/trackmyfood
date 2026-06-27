package br.com.davi.trackmyfood.api.auth.service;

import br.com.davi.trackmyfood.api.auth.dtos.AuthRequest;
import br.com.davi.trackmyfood.api.auth.dtos.AuthResponse;
import br.com.davi.trackmyfood.api.auth.dtos.RegisterRequest;
import br.com.davi.trackmyfood.api.auth.mappers.AuthMapper;
import br.com.davi.trackmyfood.core.exceptions.EmailAlreadyExistsException;
import br.com.davi.trackmyfood.core.repository.UserRepository;
import br.com.davi.trackmyfood.core.services.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request){
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        var hashedPassword = passwordEncoder.encode(request.password());

        var user = switch (request.role()) {
            case ROLE_CUSTOMER -> authMapper.toCustomer(request,hashedPassword);
            case ROLE_DELIVERY_MAN -> authMapper.toDeliveryMan(request,hashedPassword);
        };

        userRepository.save(user);

        var accessToken = jwtService.generateAccessToken(user.getEmail());
        var refreshToken = jwtService.generateRefreshToken(user.getEmail());


        return authMapper.toAuthResponse(user,accessToken,refreshToken);
    }

    public AuthResponse login(AuthRequest request){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        ));

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var accessToken = jwtService.generateAccessToken(user.getEmail());
        var refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return authMapper.toAuthResponse(user, accessToken, refreshToken);
    }
}
