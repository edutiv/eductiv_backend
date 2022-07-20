package com.projectlms.projectlms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.projectlms.projectlms.domain.common.ApiResponse;
import com.projectlms.projectlms.domain.dao.Faq;
import com.projectlms.projectlms.domain.dto.FaqDto;
import com.projectlms.projectlms.repository.FaqRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FaqService.class)
public class FaqServiceTest {

    @MockBean
    private FaqRepository faqRepository;

    @Autowired
    private FaqService faqService;
    
    @Test
    void addFaq_Success_Test() {

        when(faqRepository.save(any())).thenReturn(Faq.builder()
            .id(1L)
            .question("Bagaimana cara saya melakukan register?")
            .answer("Untuk melakukan register, dapat menghubungi admin Edutiv untuk didaftarkan sebagai member. Setelah mendapatkan akun yang sudah terdaftar sebagai member, user dapat melakukan login dan mengakses fitur-fitur pada website Edutiv")
            .build()
        );

        ResponseEntity<Object> responseEntity = faqService.addFaq(FaqDto.builder()
            .question("Bagaimana cara saya melakukan register?")
            .build()
        );

        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Faq faq = (Faq) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, faq.getId());
        assertEquals("Bagaimana cara saya melakukan register?", faq.getQuestion());
        assertEquals("Untuk melakukan register, dapat menghubungi admin Edutiv untuk didaftarkan sebagai member. Setelah mendapatkan akun yang sudah terdaftar sebagai member, user dapat melakukan login dan mengakses fitur-fitur pada website Edutiv", faq.getAnswer());
    }

    @Test
    void addFaq_Exception_Test() {
        when(faqRepository.save(any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = faqService.addFaq(FaqDto.builder()
            .id(1L)
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllFaq_Success_Test() {
        when(faqRepository.findAll()).thenReturn(List.of(Faq.builder()
            .id(1L)
            .question("Bagaimana cara saya melakukan register?")
            .answer("Untuk melakukan register, dapat menghubungi admin Edutiv untuk didaftarkan sebagai member. Setelah mendapatkan akun yang sudah terdaftar sebagai member, user dapat melakukan login dan mengakses fitur-fitur pada website Edutiv")
            .build()
        ));

        ResponseEntity<Object> responseEntity = faqService.getAllFaq();
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        List<Faq> faqs = (List<Faq>) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1, faqs.size());
    }

    @Test
    void getAllFaq_Exception_Test() {
        when(faqRepository.findAll()).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = faqService.getAllFaq ();
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getFaqDetail_Success_Test() {
        when(faqRepository.findById(anyLong())).thenReturn(Optional.of(Faq.builder()
            .id(1L)
            .question("Bagaimana cara saya melakukan register?")
            .answer("Untuk melakukan register, dapat menghubungi admin Edutiv untuk didaftarkan sebagai member. Setelah mendapatkan akun yang sudah terdaftar sebagai member, user dapat melakukan login dan mengakses fitur-fitur pada website Edutiv")
            .build()
        ));

        ResponseEntity<Object> responseEntity = faqService.getFaqDetail(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Faq faq = (Faq) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, faq.getId());
        assertEquals("Bagaimana cara saya melakukan register?", faq.getQuestion());
        assertEquals("Untuk melakukan register, dapat menghubungi admin Edutiv untuk didaftarkan sebagai member. Setelah mendapatkan akun yang sudah terdaftar sebagai member, user dapat melakukan login dan mengakses fitur-fitur pada website Edutiv", faq.getAnswer());
    }

    @Test
    void getFaqDetail_NotFound_Test() {
        when(faqRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object>  responseEntity = faqService.getFaqDetail(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getFaqDetail_Exception_Test() {
        when(faqRepository.findById(1L)).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = faqService.getFaqDetail(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteFaq_Success_Test() {
        when(faqRepository.findById(anyLong()))
            .thenReturn(Optional.of(Faq.builder()
                .id(1L)
                .question("Bagaimana cara saya melakukan register?")
                .answer("Untuk melakukan register, dapat menghubungi admin Edutiv untuk didaftarkan sebagai member. Setelah mendapatkan akun yang sudah terdaftar sebagai member, user dapat melakukan login dan mengakses fitur-fitur pada website Edutiv")
                .build()));
            doNothing().when(faqRepository).deleteById(anyLong());

        ApiResponse response = (ApiResponse) faqService.deleteFaq(1L).getBody();
        assertEquals("SUCCESS", response.getMessage());
        verify(faqRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteFaq_NotFound_Test() {
        when(faqRepository.findById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(faqRepository).deleteById(anyLong()); 
        
        ResponseEntity<Object> responseEntity = faqService.deleteFaq(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteFaq_Exception_Test() {
        when(faqRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        doNothing().when(faqRepository).deleteById(anyLong()); 
        
        ResponseEntity<Object> responseEntity = faqService.deleteFaq(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }
}
