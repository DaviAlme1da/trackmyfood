package br.com.davi.trackmyfood.api.common.filters;

import br.com.davi.trackmyfood.core.exceptions.JwtServiceException;
import br.com.davi.trackmyfood.core.services.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUHORIZATION_HEADER = "Authorization";
    private static final String TOKE_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try{
            var token = "";
            var email = "";
            var header = request.getHeader(AUHORIZATION_HEADER);

            if(isTokenPresent(header)){
                token = header.substring(TOKE_PREFIX.length());
                email = jwtService.getSubFromAccessToken(token);
            }

            if(isEmailValid(email) && isNotAuthenticated()){
                setAuthentication(request, email);
            }

            filterChain.doFilter(request, response);

        }catch (JwtServiceException e ){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getLocalizedMessage());
        }
    }

    private void setAuthentication(HttpServletRequest request, String email){

        var userDetails = userDetailsService.loadUserByUsername(email);
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isEmailValid(String email){
        return email != null && !email.isEmpty();
    }

    private boolean isTokenPresent(String header){
        return header != null && header.startsWith(TOKE_PREFIX);
    }

    private boolean isNotAuthenticated(){
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }
}
