package ru.etu.common;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class DoubleLinkedList<E> extends AbstractList<E> {

    private int size = 0;

    private Node<E> first;

    private Node<E> last;

    public DoubleLinkedList() {}

    public DoubleLinkedList(Collection<? extends E> items) {
        this();
        this.addAll(items);
    }

    public void swap(int i1, int i2) {
        if (!isValidPosition(i1) || !isValidPosition(i2))
            throw new IndexOutOfBoundsException();

        if (i1 == i2) return;

        final Node<E> node1;
        if (i1 == 0)
            node1 = this.first;
        else if (i1 == (size - 1))
            node1 = this.last;
        else
            node1 = this.getNode(i1);
            
        final Node<E> node2;
        if (i2 == 0)
            node2 = this.first;
        else if (i2 == (size - 1))
            node2 = this.last;
        else
            node2 = this.getNode(i2);

        if (node1.value == node2.value) return;

        E temp = node1.value;
        node1.value = node2.value;
        node2.value = temp;
    }

    @Override
    public boolean add(E e) {
        this.linkLast(e);
        
        return true;
    }

    @Override
    public void add(int index, E element) {
        if (!isValidPosition(index)) throw new IndexOutOfBoundsException("Position " + index
                + " is out of bounds for List with size " + this.size);

        if (index == size)
            this.linkLast(element);
        else
            this.linkBefore(element, this.getNode(index));
    }

    public void addFirst(E e) {
        this.linkFirst(e);
    }

    public void addLast(E e) {
        this.linkLast(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return this.addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if(!isValidPosition(index)) throw new IndexOutOfBoundsException("Position " + index
                + " is out of bounds for List with size " + this.size);

        Object[] array = c.toArray();
        int numNew = array.length;
        if (numNew == 0)
            return false;

        Node<E> prev, next;
        if (index == this.size) {
            next = null;
            prev = last;
        } else {
            next = getNode(index);
            prev = next.prev;
        }

        for (Object obj : array) {
            @SuppressWarnings("unchecked") E e = (E) obj;
            final Node<E> newNode = new Node<>(prev, e, null);
            if (isNull(prev))
                first = newNode;
            else
                prev.next = newNode;
            prev = newNode;
        }

        if (isNull(next)) {
            last = prev;
        } else {
            prev.next = next;
            next.prev = prev;
        }

        size += numNew;

        return true;
    }

    @Override
    public void clear() {
        for (Node<E> node = first; nonNull(node); ) {
            Node<E> next = node.next;
            node.value = null;
            node.next = null;
            node.prev = null;
            node = next;
        }
        first = last = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        return this.indexOf(o) >= 0;
    }

    /**
     * Not implemented for this {@link java.util.List} implementation
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int index) {
        if (!this.isWithinBounds(index)) {
            throw new IndexOutOfBoundsException("Index " + index
                    + " is out of bounds for List with size " + this.size);
        }
        if (index == 0) return this.getFirst();
        if (index == (this.size - 1)) return this.getLast();

        return this.getNode(index).value;
    }

    public E getFirst() {
        if (isNull(this.first)) throw new NoSuchElementException();

        return this.first.value;
    }

    public E getLast() {
        if (isNull(this.last)) throw new NoSuchElementException();

        return this.last.value;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        if (isNull(o)) {
            for (Node<E> node = this.first; nonNull(node); node = node.next) {
                if (isNull(node.value))
                    return index;
                ++index;
            }
        } else {
            for (Node<E> node = this.first; nonNull(node); node = node.next) {
                if (o.equals(node.value))
                    return index;
                ++index;
            }
        }

        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size > 0;
    }

    /**
     * Not implemented for this {@link java.util.List} implementation
     */
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Not implemented for this {@link java.util.List} implementation
     */
    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not implemented for this {@link java.util.List} implementation
     */
    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Not implemented for this {@link java.util.List} implementation
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        if (isNull(o)) {
            for (Node<E> node = first; nonNull(node); node = node.next) {
                if (isNull(node.value)) {
                    this.unlink(node);
                    return true;
                }
            }
        } else {
            for (Node<E> node = first; nonNull(node); node = node.next) {
                if (o.equals(node.value)) {
                    this.unlink(node);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public E remove(int index) {
        if (!isValidPosition(index)) throw new IndexOutOfBoundsException("Position " + index
                + " is out of bounds for List with size " + this.size);

        if (index == size - 1)
            return this.removeLast();
        if (index == 0)
            return this.removeFirst();

        return this.unlink(this.getNode(index));
    }

    public E removeFirst() {
        final Node<E> node = this.first;
        if (isNull(node)) throw new NoSuchElementException();
        return this.unlinkFirst(node);
    }

    public E removeLast() {
        final Node<E> node = this.last;
        if (isNull(node)) throw new NoSuchElementException();

        return this.unlinkLast(node);
    }

    /**
     * Not implemented for this {@link java.util.List} implementation
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not implemented for this {@link java.util.List} implementation
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E set(int index, E element) {
        if (!isWithinBounds(index)) throw new IndexOutOfBoundsException("Index " + index
                + " is out of bounds for List with size " + this.size);
        Node<E> node = getNode(index);
        E oldVal = node.value;
        node.value = element;

        return oldVal;
    }

    @Override
    public int size() {
        return this.size;
    }

    /**
     * Not implemented for this {@link java.util.List} implementation
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = 0;
        for (Node<E> node = this.first; nonNull(node); node = node.next)
            result[index++] = node.value;

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[])java.lang.reflect.Array.newInstance(
                                a.getClass().getComponentType(), size);
        int index = 0;
        Object[] result = a;
        for (Node<E> node = first; nonNull(node); node = node.next)
            result[index++] = node.value;

        if (a.length > size)
            a[size] = null;

        return a;
    }

    private void linkFirst(E value) {
        final Node<E> f = this.first;
        final Node<E> newNode = new Node<>(null, value, f);

        this.first = newNode;
        if (isNull(f))
            this.last = newNode;
        else
            f.prev = newNode;
        size++;
    }

    private void linkLast(E value) {
        final Node<E> l = this.last;
        final Node<E> newNode = new Node<>(l, value, null);

        this.last = newNode;
        if (isNull(l))
            this.first = newNode;
        else
            l.next = newNode;
        size++;
    }

    private void linkBefore(E value, Node<E> node) {
        final Node<E> pred = node.prev;
        final Node<E> newNode = new Node<>(pred, value, node);

        node.prev = newNode;
        if (isNull(pred))
            first = newNode;
        else
            pred.next = newNode;
        size++;
    }

    private E unlinkFirst(Node<E> node) {
        final E element = node.value;
        final Node<E> next = node.next;

        node.value = null;
        node.next = null;
        first = next;
        if (isNull(next))
            last = null;
        else
            next.prev = null;
        size--;
        
        return element;
    }

    private E unlinkLast(Node<E> node) {
        final E element = node.value;
        final Node<E> prev = node.prev;

        node.value = null;
        node.prev = null;
        last = prev;
        if (isNull(prev))
            first = null;
        else
            prev.next = null;
        size--;

        return element;
    }

    private E unlink(Node<E> node) {
        final E element = node.value;
        final Node<E> next = node.next;
        final Node<E> prev = node.prev;

        if (isNull(prev)) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (isNull(next)) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.value = null;
        size--;
        
        return element;
    }

    private boolean isWithinBounds(int index) {
        return (index >= 0 && index < size);
    }

    private boolean isValidPosition(int position) {
        return (position >= 0 && position <= size);
    }

    private Node<E> getNode(int index) {
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; ++i)
                x = x.next;

            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; --i)
                x = x.prev;

            return x;
        }
    }

    static class Node<E> {

        private Node<E> next;

        private Node<E> prev;

        private E value;

        public Node() {}

        public Node(Node<E> prev, E value, Node<E> next) {
            this.prev = prev;
            this.value = value;
            this.next = next;
        }

        public Node<E> getNext() {
            return this.next;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }
        
        public Node<E> getPrev() {
            return this.prev;
        }

        public void setPrev(Node<E> prev) {
            this.prev = prev;
        }

        public E getValue() {
            return this.value;
        }

        public void setValue(E value) {
            this.value = value;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
        
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            if (!super.equals(object)) return false;
            @SuppressWarnings("rawtypes") Node node = (Node) object;

            return Objects.equals(value, node.value);
        }

    }    
    
}
