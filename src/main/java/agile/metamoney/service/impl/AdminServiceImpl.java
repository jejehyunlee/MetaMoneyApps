package agile.metamoney.service.impl;


import agile.metamoney.entity.Admin;
import agile.metamoney.repository.AdminRepository;
import agile.metamoney.service.AdminService;
import agile.metamoney.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ValidationService validationService;

    @Override
    public Admin getById(String id) {
        return adminRepository.findById(id).get();
    }

    @Override
    public List<Admin> getAll(String idAdmin) {
        validationService.validate(idAdmin);
        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized Get All Admins, Id Admin Wrong"));

        return adminRepository.findAll();
    }

    @Override
    public void deleteById(String id, String idAdmin) {
        validationService.validate(idAdmin);
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Delete Admin Failed"));

        Admin currentAdmin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Delete Admin Failed"));

        adminRepository.deleteById(id);
    }
}
