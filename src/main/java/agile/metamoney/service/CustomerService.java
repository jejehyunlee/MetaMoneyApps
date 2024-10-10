package agile.metamoney.service;

import agile.metamoney.entity.Customer;
import agile.metamoney.model.request.UpdateCustomerRequest;
import agile.metamoney.model.response.CustomerResponse;
import agile.metamoney.model.response.ImageResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CustomerService {
    Customer getById (String id);
    CustomerResponse updateCustomer(UpdateCustomerRequest request,MultipartFile ftKtp, MultipartFile ftDiri) throws IOException;
    List<Customer> getAll(String idAdmin);
}
