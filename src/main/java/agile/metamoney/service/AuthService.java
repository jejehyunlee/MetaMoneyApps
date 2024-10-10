package agile.metamoney.service;

import agile.metamoney.entity.Admin;
import agile.metamoney.model.request.LoginRequest;
import agile.metamoney.model.request.RegisterAdminRequest;
import agile.metamoney.model.request.RegisterCustomerRequest;
import agile.metamoney.model.response.LoginResponse;
import agile.metamoney.model.response.RegisterAdminResponse;
import agile.metamoney.model.response.RegisterCustomerResponse;

public interface AuthService {
    RegisterCustomerResponse registerNewCustomer (RegisterCustomerRequest request);
    RegisterAdminResponse registerNewAdmin (RegisterAdminRequest request);
    LoginResponse login(LoginRequest loginRequest);
}
