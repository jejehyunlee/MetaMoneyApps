package agile.metamoney.service.impl;

import agile.metamoney.entity.Customer;
import agile.metamoney.entity.Pembayaran;
import agile.metamoney.entity.Role;
import agile.metamoney.entity.UserCredential;
import agile.metamoney.entity.constant.ERole;
import agile.metamoney.model.request.RegisterCustomerRequest;
import agile.metamoney.model.response.RegisterCustomerResponse;
import agile.metamoney.repository.AdminRepository;
import agile.metamoney.repository.CustomerRepository;
import agile.metamoney.repository.ProductRepository;
import agile.metamoney.repository.UserCredentialRepository;
import agile.metamoney.security.BcryptUtils;
import agile.metamoney.security.JwtUtils;
import agile.metamoney.service.AuthService;
import agile.metamoney.service.RoleService;
import agile.metamoney.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;



import static org.mockito.Mockito.*;

@SpringBootTest
class AuthServiceimplTest {
    private final ValidationService validationService = mock(ValidationService.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final JwtUtils jwtUtils = mock(JwtUtils.class);
    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final AdminRepository adminRepository = mock(AdminRepository.class);
    private final BcryptUtils bcryptUtils = mock(BcryptUtils.class);
    private final RoleService roleService = mock(RoleService.class);
    private final UserCredentialRepository userCredentialRepository = mock(UserCredentialRepository.class);
    private final AuthService authService = new AuthServiceimpl(validationService, authenticationManager, jwtUtils, customerRepository, adminRepository, bcryptUtils, roleService, userCredentialRepository);

    @Test
    void registerNewCustomer() {
        Role role = Role.builder()
                .id("role1")
                .role(ERole.ROLE_CUSTOMER)
                .build();
        UserCredential userCredential = UserCredential.builder()
                .id("customer1")
                .username("root")
                .password("test")
                .build();

        RegisterCustomerRequest request = RegisterCustomerRequest.builder()
                .name("Awas")
                .email("sawas@gmail.com")
                .nomerTelepon("08212313213")
                .username("user")
                .password("root")
                .build();

        Customer customer = Customer.builder()
                .id("1")
                .nama("Sawas")
                .email("dsdawsd@gmail.com")
                .build();

        when(roleService.getOrsave(any(ERole.class))).thenReturn(role);
        when(bcryptUtils.hashPassword(anyString())).thenReturn("password");
        when(userCredentialRepository.saveAndFlush(any(UserCredential.class))).thenReturn(userCredential);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        RegisterCustomerResponse customerResponse = authService.registerNewCustomer(request);
        assertNotNull(customerResponse);
        verify(customerRepository, times(1)).save(any(Customer.class));

    }

}