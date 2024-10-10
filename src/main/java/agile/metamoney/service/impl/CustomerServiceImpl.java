package agile.metamoney.service.impl;

import agile.metamoney.entity.Admin;
import agile.metamoney.entity.Customer;
import agile.metamoney.entity.CustomerWork;
import agile.metamoney.entity.constant.EGender;
import agile.metamoney.model.request.UpdateCustomerRequest;
import agile.metamoney.model.response.CustomerResponse;
import agile.metamoney.model.response.CustomerWorkResponse;
import agile.metamoney.repository.AdminRepository;
import agile.metamoney.repository.CustomerRepository;
import agile.metamoney.repository.CustomerWorkRepository;
import agile.metamoney.service.CustomerService;
import agile.metamoney.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationService validationService;
    private final CustomerWorkRepository customerWorkRepository;
    private final AdminRepository adminRepository;

    @Value("${file.upload-dir}")
    private String filePath;
    @Transactional(rollbackOn = Exception.class)
    @Override
    public CustomerResponse updateCustomer(UpdateCustomerRequest request,MultipartFile ftKtp, MultipartFile ftDiri) throws IOException {
        validationService.validate(request);
        String randomNameFile= UUID.randomUUID().toString();
        String randomNameFile1= UUID.randomUUID().toString();
        String fileFtKtp= randomNameFile+ftKtp.getOriginalFilename();
        String fileFtDiri= randomNameFile1+ftDiri.getOriginalFilename();
        Customer customer = getById(request.getId());
        if (customer != null) {
            LocalDate localDate = LocalDate.parse(request.getTanggalLahir());
            if(LocalDate.now().toEpochDay()-localDate.toEpochDay()>=17) customer.setTanggalLahir(localDate);
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tanggal lahir tidak valid");

            if(request.getJenisKelamin().equalsIgnoreCase("PRIA")){
                customer.setJenisKelamin(EGender.PRIA);
            } else if (request.getJenisKelamin().equalsIgnoreCase("WANITA")) {
                customer.setJenisKelamin(EGender.WANITA);
            }else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Jenis Kelamin tidak ditemukan");
            }
            customer.setNik(request.getNik());
            customer.setNamaBank(request.getNamaBank());
            customer.setNoRek(request.getNoRek());
            customer.setKota(request.getKota());
            customer.setKecamatan(request.getKecamatan());
            customer.setRt(request.getRt());
            customer.setRw(request.getRw());
            customer.setEmail(request.getEmail());
            customer.setNomerTelepon(request.getNomerTelepon());
            customer.setAlamatLengkap(request.getAlamatLengkap());
            customer.setStatusHubungan(request.getStatusHubungan());
            customer.setNamaKondar(request.getNamaKondar());
            customer.setNamaKondar(request.getNamaKondar());
            customer.setNoTelpkondar(request.getNoTelpkondar());
            customer.setFotoKtp(fileFtKtp);
            customer.setFotoDiri(fileFtDiri);
            customer.setIsActive(true);

            Path pathFtKtp = Path.of(filePath+ fileFtKtp);
            ftKtp.transferTo(pathFtKtp);

            Path pathFtDiri = Path.of(filePath+ fileFtDiri);
            ftDiri.transferTo(pathFtDiri);

            if (customer.getCustomerWork() == null) {
                CustomerWork customerWork = new CustomerWork();
                customerWork.setTipePekerjaan(request.getCustomerWork().getTipePekerjaan());
                customerWork.setBidangPekerjaan(request.getCustomerWork().getBidangPekerjaan());
                customerWork.setNamaPekerjaan(request.getCustomerWork().getNamaPekerjaan());
                customerWork.setNamaPerusahaan(request.getCustomerWork().getNamaPerusahaan());
                customerWork.setNomerTelepon(request.getCustomerWork().getNomerTelepon());
                customerWorkRepository.save(customerWork);

                customer.setCustomerWork(customerWork);
                customerRepository.save(customer);

                return getCustomerResponse(customer, customerWork);
            } else {

                customer.getCustomerWork().setTipePekerjaan(request.getCustomerWork().getTipePekerjaan());
                customer.getCustomerWork().setBidangPekerjaan(request.getCustomerWork().getBidangPekerjaan());
                customer.getCustomerWork().setNamaPekerjaan(request.getCustomerWork().getNamaPekerjaan());
                customer.getCustomerWork().setNamaPerusahaan(request.getCustomerWork().getNamaPerusahaan());
                customer.getCustomerWork().setNomerTelepon(request.getCustomerWork().getNomerTelepon());
                customerWorkRepository.save(customer.getCustomerWork());
                return CustomerResponse.builder()
                        .id(customer.getId())
                        .name(customer.getNama())
                        .fotoDiri(customer.getFotoDiri())
                        .fotoKtp(customer.getFotoKtp())
                        .tanggalLahir(customer.getTanggalLahir())
                        .jenisKelamin(customer.getJenisKelamin())
                        .nik(customer.getNik())
                        .email(customer.getEmail())
                        .nomerTelepon(customer.getNomerTelepon())
                        .namaBank(customer.getNamaBank())
                        .noRek(customer.getNoRek())
                        .kota(customer.getKota())
                        .kecamatan(customer.getKecamatan())
                        .rw(customer.getRw())
                        .rt(customer.getRt())
                        .alamatLengkap(customer.getAlamatLengkap())
                        .statusHubungan(customer.getStatusHubungan())
                        .namaKondar(customer.getNamaKondar())
                        .noTelpkondar(customer.getNoTelpkondar())
                        .isActive(customer.getIsActive())
                        .workResponse(CustomerWorkResponse.builder()
                                .tipePekerjaan(customer.getCustomerWork().getTipePekerjaan())
                                .bidangPekerjaan(customer.getCustomerWork().getBidangPekerjaan())
                                .namaPekerjaan(customer.getCustomerWork().getNamaPekerjaan())
                                .namaPerusahaan(customer.getCustomerWork().getNamaPerusahaan())
                                .nomerTelepon(customer.getCustomerWork().getNomerTelepon())
                                .build())
                        .build();
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "customer not found");
    }


    private static CustomerResponse getCustomerResponse(Customer customer, CustomerWork customerWork) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getNama())
                .fotoDiri(customer.getFotoDiri())
                .fotoKtp(customer.getFotoKtp())
                .tanggalLahir(customer.getTanggalLahir())
                .jenisKelamin(customer.getJenisKelamin())
                .nik(customer.getNik())
                .namaBank(customer.getNamaBank())
                .noRek(customer.getNoRek())
                .kota(customer.getKota())
                .kecamatan(customer.getKecamatan())
                .email(customer.getEmail())
                .nomerTelepon(customer.getNomerTelepon())
                .rw(customer.getRw())
                .rt(customer.getRt())
                .alamatLengkap(customer.getAlamatLengkap())
                .statusHubungan(customer.getStatusHubungan())
                .namaKondar(customer.getNamaKondar())
                .noTelpkondar(customer.getNoTelpkondar())
                .isActive(customer.getIsActive())
                .workResponse(CustomerWorkResponse.builder()
                        .tipePekerjaan(customerWork.getTipePekerjaan())
                        .bidangPekerjaan(customerWork.getBidangPekerjaan())
                        .namaPekerjaan(customerWork.getNamaPekerjaan())
                        .namaPerusahaan(customerWork.getNamaPerusahaan())
                        .nomerTelepon(customerWork.getNomerTelepon())
                        .build())
                .build();
    }


    public Customer getById(String id) {
        return customerRepository.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")
        );
    }

    @Override
    public List<Customer> getAll(String idAdmin) {
        validationService.validate(idAdmin);
        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized Get Customer, Id Admin Wrong"));

        return customerRepository.findAll();
    }
}
