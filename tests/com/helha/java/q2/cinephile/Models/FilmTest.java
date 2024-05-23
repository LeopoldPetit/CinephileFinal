package com.helha.java.q2.cinephile.Models;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class FilmTest {

    private Film film;

    @Before
    public void setUp() {
        film = new Film("Titre du film", "chemin/image.jpg", "Synopsis du film", "2h30");
    }

    @Test
    public void testGetTitre() {
        assertEquals("Titre du film", film.getTitre());
    }

    @Test
    public void testGetImage() {
        assertEquals("chemin/image.jpg", film.getImage());
    }

    @Test
    public void testGetTexte() {
        assertEquals("Synopsis du film", film.getTexte());
    }

    @Test
    public void testGetDuree() {
        assertEquals("2h30", film.getDuree());
    }
}


