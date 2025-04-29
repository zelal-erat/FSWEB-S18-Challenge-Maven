package com.workintech.fswebs18challengemaven;

import com.workintech.fswebs18challengemaven.entity.Card;
import com.workintech.fswebs18challengemaven.entity.Color;
import com.workintech.fswebs18challengemaven.entity.Type;
import com.workintech.fswebs18challengemaven.exceptions.CardErrorResponse;
import com.workintech.fswebs18challengemaven.exceptions.CardException;
import com.workintech.fswebs18challengemaven.repository.CardRepository;
import com.workintech.fswebs18challengemaven.repository.CardRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(ResultAnalyzer2.class)
public class MainTest {
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CardRepositoryImpl cardRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEnumConstants() {
        assertTrue(Color.valueOf("SPADE") == Color.SPADE);
        assertTrue(Color.valueOf("CLUB") == Color.CLUB);
        assertTrue(Color.valueOf("HEARTH") == Color.HEARTH);
        assertTrue(Color.valueOf("DIAMOND") == Color.DIAMOND);

        assertTrue(Type.valueOf("JACK") == Type.JACK);
        assertTrue(Type.valueOf("QUEEN") == Type.QUEEN);
        assertTrue(Type.valueOf("KING") == Type.KING);
        assertTrue(Type.valueOf("ACE") == Type.ACE);
        assertTrue(Type.valueOf("JOKER") == Type.JOKER);

    }

    @Test
    void testEnumValues() {
        assertEquals(4, Color.values().length);
        assertEquals(5, Type.values().length);
    }

    @Test
    void testCardSetAndGet() {
        Card card = new Card();
        card.setId(1L);
        card.setColor(Color.SPADE);
        card.setType(Type.KING);
        card.setValue(null);

        assertEquals(1L, card.getId());
        assertEquals(null, card.getValue());
        assertEquals(Color.SPADE, card.getColor());
        assertEquals(Type.KING, card.getType());
    }

    @Test
    void testNoArgsConstructor() {
        Card card = new Card();
        assertNull(card.getType());
    }


    @Test
    void testSave() {
        Card card = new Card();
        card.setId(1L);
        card.setType(Type.ACE);
        card.setColor(Color.HEARTH);
        cardRepository.save(card);
        verify(entityManager).persist(card);
    }

    @Test
    void testFindAll() {
        TypedQuery<Card> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Card.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(new Card(), new Card()));
        List<Card> cards = cardRepository.findAll();
        assertEquals(2, cards.size());
    }

    @Test
    void testFindByColor_Exists() {
        TypedQuery<Card> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Card.class))).thenReturn(query);

        Card card1 = new Card();
        card1.setColor(Color.HEARTH);
        card1.setType(Type.ACE);

        Card card2 = new Card();
        card2.setColor(Color.HEARTH);
        card2.setType(Type.ACE);

        when(query.getResultList()).thenReturn(Arrays.asList(card1, card2));

        List<Card> foundCards = cardRepository.findByColor(Color.valueOf("HEART"));  // Color enum'ına dönüştürdük

        assertEquals(2, foundCards.size());
    }

    @Test
    void testFindByColor_NotExists() {
        TypedQuery<Card> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Card.class))).thenReturn(query);

        // Mock veritabanı sorgusu dönmüyor
        when(query.getResultList()).thenReturn(new ArrayList<>());

        // "HEARTH" String değeri Color enum'ına dönüştürülüyor
        assertThrows(CardException.class, () -> cardRepository.findByColor(Color.HEARTH));
    }


    @Test
    void testUpdate() {
        Card card = new Card();
        card.setId(1L);
        card.setType(Type.ACE);
        card.setColor(Color.HEARTH);
        when(entityManager.merge(card)).thenReturn(card);
        Card updated = cardRepository.update(card);
        assertEquals(1L, updated.getId());
    }

    @Test
    void testRemove() {
        // Silinecek kartı oluşturuyoruz
        Card card = new Card();
        card.setId(1L);

        // EntityManager mocklama işlemi
        when(entityManager.find(Card.class, 1L)).thenReturn(card);
        doNothing().when(entityManager).remove(card);  // remove() void döndürür

        // Silme işlemi
        cardRepository.remove(1L);

        // Silinen kartı kontrol ediyoruz
        verify(entityManager, times(1)).remove(card);
        assertEquals(1L, card.getId()); // Kartın id'sinin 1 olması gerektiğini kontrol ediyoruz
    }


    @Test
    void testFindByValue() {
        TypedQuery<Card> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Card.class))).thenReturn(query);

        Card card1 = new Card();
        card1.setColor(Color.HEARTH);
        card1.setValue(10);

        Card card2 = new Card();
        card2.setColor(Color.DIAMOND);
        card2.setValue(10);

        when(query.getResultList()).thenReturn(Arrays.asList(card1, card2));
        List<Card> cards = cardRepository.findByValue(10);
        assertEquals(2, cards.size());
    }

    @Test
    void testFindByType() {
        TypedQuery<Card> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Card.class))).thenReturn(query);

        // Kartları oluşturuyoruz
        Card card1 = new Card();
        card1.setColor(Color.HEARTH);
        card1.setType(Type.ACE);  // Type enum değeri

        Card card2 = new Card();
        card2.setColor(Color.HEARTH);
        card2.setType(Type.ACE);  // Type enum değeri

        // Mock'lanan query'nin sonucu olarak kartları döndürüyoruz
        when(query.getResultList()).thenReturn(Arrays.asList(card1, card2));

        // "ACE" String yerine Type.ACE kullanmalıyız
        List<Card> cards = cardRepository.findByType(Type.ACE);  // String yerine enum kullanıyoruz
        assertEquals(2, cards.size());  // Sonucun boyutunun 2 olduğunu kontrol ediyoruz
    }



    @Test
    void testImplementsCardRepositoryInterface() {
        CardRepository burgerDaoImpl = new CardRepositoryImpl(null);
        assertTrue(burgerDaoImpl instanceof CardRepository, "CardRepositoryImpl should implement CardRepository interface");
    }

    @Test
    void testCardRepositoryErrorResponse() {
        String expectedMessage = "An error occurred";
        CardErrorResponse errorResponse = new CardErrorResponse(expectedMessage);
        assertEquals(expectedMessage, errorResponse.getMessage(), "The retrieved message should match the expected message.");
    }

    @Test
    void testCardExceptionCreation() {
        String expectedMessage = "Test exception message";
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        CardException exception = new CardException(expectedMessage, expectedStatus);

        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the expected value.");
        assertEquals(expectedStatus, exception.getHttpStatus(), "The HttpStatus should match the expected value.");
    }

    @Test
    void testCardExceptionIsRuntimeException() {
        CardException exception = new CardException("Test", HttpStatus.BAD_REQUEST);
        assertTrue(exception instanceof RuntimeException, "CardException should be an instance of RuntimeException.");
    }
}
