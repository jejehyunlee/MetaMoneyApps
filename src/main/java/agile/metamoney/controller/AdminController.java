package agile.metamoney.controller;


import agile.metamoney.entity.Admin;
import agile.metamoney.entity.Customer;
import agile.metamoney.model.response.CommonResponse;
import agile.metamoney.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable String id){
        Admin admin = adminService.getById(id);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get id admin")
                .data(admin)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
    }

    @GetMapping("/{idAdmin}/all-admin")
    public ResponseEntity<?> getAllCustomer(@PathVariable("idAdmin") String idAdmin){
        List<Admin> admins = adminService.getAll(idAdmin);

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get all admin")
                .data(admins)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
    }

    @DeleteMapping("/id-{id}/current-id-{idAdmin}")
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") String id, @PathVariable("idAdmin") String idAdmin){
        adminService.deleteById(id, idAdmin);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully delete admin")
                        .data("Successfully delete admin")
                        .build());
    }

}
