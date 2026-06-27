package br.com.davi.trackmyfood.api.auth.service;

import br.com.davi.trackmyfood.api.auth.mappers.AuthMapper;
import br.com.davi.trackmyfood.core.exceptions.EmailAlreadyExistsException;
import br.com.davi.trackmyfood.core.repository.UserRepository;
import br.com.davi.trackmyfood.core.services.jwt.JwtService;
import br.com.davi.trackmyfood.testutils.factories.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthMapper authMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("register: deve registrar Customer com sucesso")
    void register_shouldRegisterCustomer_successfully() {
        var request = RegisterRequestFactory.createCustomer();
        var customer = CustomerFactory.create();
        var response = AuthResponseFactory.create();

        given(userRepository.findByEmail(request.email())).willReturn(Optional.empty());
        given(passwordEncoder.encode(request.password())).willReturn("hashed_password");
        given(authMapper.toCustomer(request, "hashed_password")).willReturn(customer);
        given(jwtService.generateAccessToken(customer.getEmail())).willReturn("access_token_fake");
        given(jwtService.generateRefreshToken(customer.getEmail())).willReturn("refresh_token_fake");
        given(authMapper.toAuthResponse(customer, "access_token_fake", "refresh_token_fake")).willReturn(response);

        var result = authService.register(request);

        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("access_token_fake");
        assertThat(result.refreshToken()).isEqualTo("refresh_token_fake");
        assertThat(result.name()).isEqualTo("João Silva");
        assertThat(result.role()).isEqualTo("ROLE_CUSTOMER");

        verify(userRepository).save(customer);
        verify(passwordEncoder).encode(request.password());
        verify(jwtService).generateAccessToken(customer.getEmail());
        verify(jwtService).generateRefreshToken(customer.getEmail());
    }

    @Test
    @DisplayName("register: deve registrar DeliveryMan com sucesso")
    void register_shouldRegisterDeliveryMan_successfully() {
        var request = RegisterRequestFactory.createDeliveryMan();
        var deliveryMan = DeliverymanFactory.create();
        var response = AuthResponseFactory.create();

        given(userRepository.findByEmail(request.email())).willReturn(Optional.empty());
        given(passwordEncoder.encode(request.password())).willReturn("hashed_password");
        given(authMapper.toDeliveryMan(request, "hashed_password")).willReturn(deliveryMan);
        given(jwtService.generateAccessToken(deliveryMan.getEmail())).willReturn("access_token_fake");
        given(jwtService.generateRefreshToken(deliveryMan.getEmail())).willReturn("refresh_token_fake");
        given(authMapper.toAuthResponse(deliveryMan, "access_token_fake", "refresh_token_fake")).willReturn(response);

        var result = authService.register(request);

        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("access_token_fake");

        verify(userRepository).save(deliveryMan);
        verify(authMapper).toDeliveryMan(request, "hashed_password");
        verify(authMapper, never()).toCustomer(any(), anyString());
    }

    @Test
    @DisplayName("register: deve lançar EmailAlreadyExistsException quando email já existe")
    void register_shouldThrowEmailAlreadyExistsException_whenEmailAlreadyExists() {
        var request = RegisterRequestFactory.createCustomer();
        var customer = CustomerFactory.create();

        given(userRepository.findByEmail(request.email())).willReturn(Optional.of(customer));

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
        verify(jwtService, never()).generateAccessToken(anyString());
    }

    @Test
    @DisplayName("register: deve hashear a senha antes de salvar")
    void register_shouldHashPassword_beforeSaving() {
        var request = RegisterRequestFactory.createCustomer();
        var customer = CustomerFactory.create();
        var response = AuthResponseFactory.create();

        given(userRepository.findByEmail(request.email())).willReturn(Optional.empty());
        given(passwordEncoder.encode(request.password())).willReturn("hashed_password");
        given(authMapper.toCustomer(request, "hashed_password")).willReturn(customer);
        given(jwtService.generateAccessToken(anyString())).willReturn("access_token_fake");
        given(jwtService.generateRefreshToken(anyString())).willReturn("refresh_token_fake");
        given(authMapper.toAuthResponse(any(), anyString(), anyString())).willReturn(response);

        authService.register(request);

        verify(authMapper).toCustomer(request, "hashed_password");
        verify(authMapper, never()).toCustomer(request, "senha123");
    }


    @Test
    @DisplayName("login: deve retornar AuthResponse com tokens quando credenciais são válidas")
    void login_shouldReturnAuthResponse_whenCredentialsAreValid() {
        var request = AuthRequestFactory.create();
        var customer = CustomerFactory.create();
        var response = AuthResponseFactory.create();

        given(userRepository.findByEmail(request.email())).willReturn(Optional.of(customer));
        given(jwtService.generateAccessToken(customer.getEmail())).willReturn("access_token_fake");
        given(jwtService.generateRefreshToken(customer.getEmail())).willReturn("refresh_token_fake");
        given(authMapper.toAuthResponse(customer, "access_token_fake", "refresh_token_fake")).willReturn(response);

        var result = authService.login(request);

        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("access_token_fake");
        assertThat(result.refreshToken()).isEqualTo("refresh_token_fake");

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        verify(jwtService).generateAccessToken(customer.getEmail());
        verify(jwtService).generateRefreshToken(customer.getEmail());
    }

    @Test
    @DisplayName("login: deve lançar BadCredentialsException quando senha está errada")
    void login_shouldThrowBadCredentialsException_whenPasswordIsWrong() {

        var request = AuthRequestFactory.createWithWrongPassword();

        given(authenticationManager.authenticate(any()))
                .willThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);

        verify(jwtService, never()).generateAccessToken(anyString());
        verify(jwtService, never()).generateRefreshToken(anyString());
        verify(userRepository, never()).findByEmail(anyString());
    }
}