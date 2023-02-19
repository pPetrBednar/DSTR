package io.github.ppetrbednar.dstr.logic.structures;

import java.util.*;

public interface IGraph {
    void clear();

    boolean isEmpty();

    int size();

    void addVertex(Vertex vertex);

    void addEdge(Edge edge);

    void addEdge(Edge edge, String leftVertexKey, String rightVertexKey);

    Vertex removeVertex(String key);

    Edge removeEdge(String key);

    Vertex getVertex(String key);

    Edge getEdge(String key);

    Map<String, Vertex> getVertices();

    Map<String, Edge> getEdges();
}

   /* Vytvoř
            Zruš
    JePrázdný (­Boolean)
    Mohutnost (­PočetPrvků)
    Prohlídka(ØTyp, ØPočátek, ØAkce) vrcholová/hranová, do hloubky/šířky
    VložVrchol(ØVrchol)
    VložHranu(ØHrana)
    OdeberVrchol(ØKlíč, ­Vrchol)
    OdeberHranu(ØKlíč, ­Hrana)
    NajdiVrchol (ØKlíč, ­Vrchol)
    NajdiHranu(ØKlíč, ­Hrana)
    ZpřístupniNásledníky(ØKoho,­Prvky)
    ZpřístupniPředchůdce(ØKoho,­Prvky)
    ZpřístupniIncidenčníPrvky(ØKoho,­Prvky)
    DefinujBránu(ØPrvek)
    AnulujBránu(ØPrvek)
    ZpřístupniBrány(­Prvky)
B. Sjednocení (ØGrafA, ØGrafB, ­GrafC)+*/

