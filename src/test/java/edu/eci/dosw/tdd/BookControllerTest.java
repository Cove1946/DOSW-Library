package edu.eci.dosw.tdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.dosw.tdd.controller.BookController;
import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookMapper bookMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addBook_shouldReturn201() throws Exception {
        BookDTO bookDTO = new BookDTO("B1", "Clean Code", "Robert Martin", 3);
        Book book = new Book("B1", "Clean Code", "Robert Martin", true);

        when(bookMapper.toModel(any(BookDTO.class))).thenReturn(book);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isCreated());

        verify(bookService, times(1)).addBook(book, 3);
    }

    @Test
    void getAllBooks_shouldReturn200() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(
                new Book("B1", "Clean Code", "Robert Martin", true)
        ));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("B1"));
    }

    @Test
    void getBookById_shouldReturn200() throws Exception {
        when(bookService.getBookById("B1"))
                .thenReturn(new Book("B1", "Clean Code", "Robert Martin", true));

        mockMvc.perform(get("/books/B1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("B1"))
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void getBookById_shouldReturn500_whenNotFound() throws Exception {
        when(bookService.getBookById("B99"))
                .thenThrow(new RuntimeException("Book not found: B99"));

        mockMvc.perform(get("/books/B99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateAvailability_shouldReturn200() throws Exception {
        mockMvc.perform(put("/books/B1/availability")
                        .param("available", "false"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).updateBookAvailability("B1", false);
    }

    @Test
    void deleteBook_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/books/B1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook("B1");
    }
}