package agile.metamoney.service.impl;

import agile.metamoney.entity.Customer;
import agile.metamoney.entity.constant.EGender;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceImplTest {
//    @Test
//    void itShouldReturnCustomerWhenUpdateCustomer() {
//        Customer dummyCustomer = new Customer();
//        dummyCustomer.setNik("3273173505980002");
//        dummyCustomer.setNamaBank("BCA");
//        dummyCustomer.setNoRek("5xx-13xx-xxx");
//        dummyCustomer.setKota("Bandung");
//        dummyCustomer.setKecamatan("Bojongsoang");
//        dummyCustomer.setRt("01");
//        dummyCustomer.setRw("01");
//        dummyCustomer.setEmail("01");
//        dummyCustomer.setNomerTelepon(request.getNomerTelepon());
//        dummyCustomer.setAlamatLengkap("Jln Cadas No.XX");
//        dummyCustomer.setStatusHubungan(request.getStatusHubungan());
//        dummyCustomer.setNamaKondar(request.getNamaKondar());
//        dummyCustomer.setNamaKondar(request.getNamaKondar());
//        dummyCustomer.setNoTelpkondar(request.getNoTelpkondar());
//        dummyCustomer.setFotoKtp(fileFtKtp);
//        dummyCustomer.setFotoDiri(fileFtDiri);
//        dummyCustomer.setIsActive(true);
//    }

//    @Test
//    void itShouldReturnCustomersWhenGetAllCustomer() {
//        List<Customer> dummyStore = new ArrayList<>();
//
//        dummyStore.add(new Customer("1","Sadw", EGender.PRIA, LocalDate.of(1998,05,13),"082812829108","image.jpg","3273712823822910","image.jpg","Chr@gmail.com","sayand","rahasia","dwasdw","awdsad", "01"
//                ,"01", "jalan lama","dads","wadsdw","082812829108",true));
//        dummyStore.add(new Customer("2", "12345", "sukses", "jalan lama", "099897"));
//
//        when(storeRepository.findAll()).thenReturn(dummyStore);
//
//        List<Store> retrieved = storeService.getAll();
//
//        verify(storeRepository, times(1)).findAll();
//
//        assertEquals(dummyStore.size(), retrieved.size());
//
//        for (int i = 0 ; i < retrieved.size() ; i++){
//            assertEquals(dummyStore.get(i).getId(), retrieved.get(i).getId());
//            assertEquals(dummyStore.get(i).getName(), retrieved.get(i).getName());
//        }
//        for (Store store : retrieved) {
//            System.out.println("id"+ store.getId() + "name" + store.getName());
//        }
//
//    }
}