package com.helha.java.q2.cinephile.Models;

import com.helha.java.q2.cinephile.Models.Film;
import com.helha.java.q2.cinephile.Models.FilmDb;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmDbTest {
    private static FilmDb filmDb;

    @BeforeAll
    static void setUp() {
        filmDb = new FilmDb();
    }



    @Test
    void testGetAllFilms() {
        List<Film> films = filmDb.getAllFilms();
        // Vérifie si la liste de films n'est pas vide
        assertTrue(films.size() > 0);

        // Ajoutez d'autres assertions selon vos besoins pour tester les films récupérés
        // Par exemple, vous pouvez vérifier si les titres des films ne sont pas nuls
        for (Film film : films) {
            assertNotNull(film.getTitre());
        }
    }
}

