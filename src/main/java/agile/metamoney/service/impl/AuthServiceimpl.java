package agile.metamoney.service.impl;

import agile.metamoney.entity.*;
import agile.metamoney.entity.constant.ERole;
import agile.metamoney.model.request.LoginRequest;
import agile.metamoney.model.request.RegisterAdminRequest;
import agile.metamoney.model.request.RegisterCustomerRequest;
import agile.metamoney.model.response.LoginResponse;
import agile.metamoney.model.response.RegisterAdminResponse;
import agile.metamoney.model.response.RegisterCustomerResponse;
import agile.metamoney.repository.AdminRepository;
import agile.metamoney.repository.CustomerRepository;
import agile.metamoney.repository.UserCredentialRepository;
import agile.metamoney.security.BcryptUtils;
import agile.metamoney.security.JwtUtils;
import agile.metamoney.service.AuthService;
import agile.metamoney.service.RoleService;
import agile.metamoney.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceimpl implements AuthService {
    private final ValidationService validationService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final BcryptUtils bcryptUtils;
    private final RoleService roleService;

    private final UserCredentialRepository userCredentialRepository;

    @Transactional
    @Override
    public RegisterCustomerResponse registerNewCustomer(RegisterCustomerRequest request) {
        validationService.validate(request);
        try {
            Role role = roleService.getOrsave(ERole.ROLE_CUSTOMER);
            UserCredential userCredential = UserCredential.builder()
                    .username(request.getUsername())
                    .password(bcryptUtils.hashPassword(request.getPassword()))
                    .roles(List.of(role))
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);


            Customer newCustomer = Customer.builder()
                    .nama(request.getName())
                    .email(request.getEmail())
                    .nomerTelepon(request.getNomerTelepon())
                    .userCredential(userCredential)
                    .isActive(false)
                    .isActivePeminjaman(false)
                    .build();

            customerRepository.save(newCustomer);


            return RegisterCustomerResponse.builder()
                    .id(newCustomer.getId())
                    .username(userCredential.getUsername())
                    .nama(newCustomer.getNama())
                    .email(newCustomer.getEmail())
                    .nomerTelepon(newCustomer.getNomerTelepon())
                    .build();
        } catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist");
        }

    }

    @Transactional
    @Override
    public RegisterAdminResponse registerNewAdmin(RegisterAdminRequest request) {
        validationService.validate(request);
        try {
            Role role = roleService.getOrsave(ERole.ROLE_ADMIN);
            UserCredential credential = UserCredential.builder()
                    .username(request.getUsername())
                    .password(bcryptUtils.hashPassword(request.getPassword()))
                    .roles(List.of(role))
                    .build();
            userCredentialRepository.saveAndFlush(credential);

            Admin admin = Admin.builder()
                    .nama(request.getNama())
                    .userCredential(credential)
                    .build();
            adminRepository.save(admin);

            return RegisterAdminResponse.builder()
                    .id(admin.getId())
                    .nama(admin.getNama())
                    .username(credential.getUsername())
                    .build();
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "admin already exist");
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailImpl userDetails = (UserDetailImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String token = jwtUtils.generateToken(userDetails.getUsername());
        return LoginResponse.builder()
                .username(userDetails.getUsername())
                .roles(roles)
                .token(token)
                .build();
    }


}
