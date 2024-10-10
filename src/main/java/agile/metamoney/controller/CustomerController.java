package agile.metamoney.controller;

import agile.metamoney.entity.Customer;
import agile.metamoney.model.request.UpdateCustomerRequest;
import agile.metamoney.model.response.CommonResponse;
import agile.metamoney.model.response.CustomerResponse;
import agile.metamoney.model.response.ImageResponse;
import agile.metamoney.model.response.PagingResponse;
import agile.metamoney.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCustomer(@RequestParam("update-customer") String jsonObject, @RequestParam("ftKtp") MultipartFile ftKtp, @RequestParam("ftDiri") MultipartFile ftDiri) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        UpdateCustomerRequest request =objectMapper.readValue(jsonObject, UpdateCustomerRequest.class);

        CustomerResponse updateCustomer = customerService.updateCustomer(request,ftKtp,ftDiri);


        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully updated customer")
                .data(updateCustomer)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id){
        Customer customer = customerService.getById(id);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get id customer")
                .data(customer)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
    }

    @GetMapping("/{idAdmin}/all-customer")
    public ResponseEntity<?> getAllCustomer(@PathVariable("idAdmin") String idAdmin){
        List<Customer> customer = customerService.getAll(idAdmin);

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get all customer")
                .data(customer)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
    }

}
