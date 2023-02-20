package io.github.ppetrbednar.dstr.logic.railway.structures;

public record Rail(int length) implements Comparable<Rail> {

    @Override
    public int compareTo(Rail o) {
        return length - o.length();
    }
}
