package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.controller.LoanController;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @MockitoBean
    private LoanMapper loanMapper;


    private Loan buildLoan(Status status) {
        Book book = new Book("B1", "Clean Code", "Robert Martin", true);
        User user = new User("U1", "Lees");
        return new Loan(book, user, null, status, LocalDate.now());
    }

    @Test
    void createLoan_shouldReturn201() throws Exception {
        when(loanService.createLoan("B1", "U1")).thenReturn(buildLoan(Status.ACTIVE));

        mockMvc.perform(post("/loans")
                        .param("bookId", "B1")
                        .param("userId", "U1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void createLoan_shouldReturn409_whenNoCopiesAvailable() throws Exception {
        when(loanService.createLoan("B1", "U1"))
                .thenThrow(new BookNotAvailableException("No hay copias disponibles"));

        mockMvc.perform(post("/loans")
                        .param("bookId", "B1")
                        .param("userId", "U1"))
                .andExpect(status().isConflict());
    }

    @Test
    void returnBook_shouldReturn200() throws Exception {
        when(loanService.returnBook("B1", "U1")).thenReturn(buildLoan(Status.RETURNED));

        mockMvc.perform(put("/loans/return")
                        .param("bookId", "B1")
                        .param("userId", "U1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RETURNED"));
    }

    @Test
    void getAllLoans_shouldReturn200() throws Exception {
        when(loanService.getAllLoans()).thenReturn(List.of(buildLoan(Status.ACTIVE)));

        mockMvc.perform(get("/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getLoansByUser_shouldReturn200() throws Exception {
        when(loanService.getLoansByUser("U1")).thenReturn(List.of(buildLoan(Status.ACTIVE)));

        mockMvc.perform(get("/loans/user/U1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getLoansByBook_shouldReturn200() throws Exception {
        when(loanService.getLoansByBook("B1")).thenReturn(List.of(buildLoan(Status.ACTIVE)));

        mockMvc.perform(get("/loans/book/B1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void expireLoan_shouldReturn200() throws Exception {
        when(loanService.expireLoan("B1", "U1")).thenReturn(buildLoan(Status.EXPIRED));

        mockMvc.perform(put("/loans/expire")
                        .param("bookId", "B1")
                        .param("userId", "U1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EXPIRED"));
    }
}
