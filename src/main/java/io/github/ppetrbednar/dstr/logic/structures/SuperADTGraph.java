package io.github.ppetrbednar.dstr.logic.structures;

public interface SuperADTGraph<K, V, E> {
    void clear();

    boolean isEmpty();

    int size();

    void addVertex(K key, Vertex<K, V, E> vertex);

    void addEdge(K edgeKey, Edge<K, V, E> edge, K vertexKeyLeft, K vertexKeyRight);

    Vertex<K, V, E> removeVertex(K key);

    Edge<K, V, E> removeEdge(K key);

    Vertex<K, V, E> getVertex(K key);

    Edge<K, V, E> getEdge(K key);
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

