package agile.metamoney.service.impl;

import agile.metamoney.entity.UserCredential;
import agile.metamoney.entity.UserDetailImpl;
import agile.metamoney.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {
    private final UserCredentialRepository userCredentialRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("user not found")
        );

        List<SimpleGrantedAuthority> grantedAuthorities = userCredential.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name())).collect(Collectors.toList());

        return UserDetailImpl.builder()
                .username(userCredential.getUsername())
                .pasword(userCredential.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}
