package ru.etu.common;

import java.util.LinkedList;

public class LinkedListWrapper<E> extends LinkedList<E> {

    public void swap(int i1, int i2) {
        if ( ( i1 < 0 || i1 >= this.size() ) || ( i2 < 0 || i2 >= this.size() ) )
            throw new IndexOutOfBoundsException();

        if (i1 == i2) return;

        E temp = this.get(i1);
        this.set(i1, this.get(i2));
        this.set(i2, temp);
    }
    
}
