package agile.metamoney.controller;

import agile.metamoney.model.request.LoginRequest;
import agile.metamoney.model.request.RegisterAdminRequest;
import agile.metamoney.model.request.RegisterCustomerRequest;
import agile.metamoney.model.response.CommonResponse;
import agile.metamoney.model.response.LoginResponse;
import agile.metamoney.model.response.RegisterAdminResponse;
import agile.metamoney.model.response.RegisterCustomerResponse;
import agile.metamoney.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> register(@RequestBody RegisterCustomerRequest request){
        RegisterCustomerResponse newCustomer = authService.registerNewCustomer(request);

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully registered")
                .data(newCustomer)
                .build();
        return ResponseEntity.status((HttpStatus.CREATED)).body(commonResponse);
    }

    @PostMapping(
            path = "/register-admin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterAdminRequest request) {
        RegisterAdminResponse register = authService.registerNewAdmin(request);

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully registered")
                .data(register)
                .build();

        return ResponseEntity.status((HttpStatus.CREATED)).body(commonResponse);
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        LoginResponse login = authService.login(request);

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully Login")
                .data(login)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
    }
}
