package agile.metamoney.service;

import agile.metamoney.entity.Admin;

import java.util.List;

public interface AdminService {

    Admin getById(String id);
    List<Admin> getAll(String idAdmin);
    void deleteById(String id, String idAdmin);
}
