/*
 * Copyright (C) 2014-2016 Qiujuer <qiujuer@live.cn>
 * WebSite http://www.qiujuer.net
 * Author qiujuer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acorn.myframeapp.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * The queue of fixed storage size
 * Queue length is fixed
 * The tail to add data beyond the specified size to delete header data
 * Head to add data beyond the specified size to delete the tail data
 * Use a method similar to LinkedList
 * Queue operations can be performed, such as: add elements, elements of poll
 */
public class FixedList<E> extends java.util.AbstractSequentialList<E> implements java.util.List<E>, java.util.Deque<E>, java.util.Queue<E>, Cloneable, java.io.Serializable {
    private static final long serialVersionUID = 1L;

    transient int maxSize = Integer.MAX_VALUE;
    transient int size = 0;

    transient Link<E> voidLink;

    private static final class Link<ET> {
        ET data;

        Link<ET> previous, next;

        Link(ET o, Link<ET> p, Link<ET> n) {
            data = o;
            previous = p;
            next = n;
        }
    }

    private static final class LinkIterator<ET> implements ListIterator<ET> {
        int pos, expectedModCount;

        final FixedList<ET> list;

        Link<ET> link, lastLink;

        LinkIterator(FixedList<ET> object, int location) {
            list = object;
            expectedModCount = list.modCount;
            if (location >= 0 && location <= list.size) {
                // pos ends up as -1 if list is empty, it ranges from -1 to
                // list.size - 1
                // if link == voidLink then pos must == -1
                link = list.voidLink;
                if (location < list.size / 2) {
                    for (pos = -1; pos + 1 < location; pos++) {
                        link = link.next;
                    }
                } else {
                    for (pos = list.size; pos >= location; pos--) {
                        link = link.previous;
                    }
                }
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        public void add(ET object) {
            if (expectedModCount == list.modCount) {
                Link<ET> next = link.next;
                Link<ET> newLink = new Link<ET>(object, link, next);
                link.next = newLink;
                next.previous = newLink;
                link = newLink;
                lastLink = null;
                pos++;
                expectedModCount++;
                list.size++;
                list.modCount++;
                // Remove
                if (++list.size > list.maxSize) {
                    list.removeFirstImpl();
                    expectedModCount++;
                }
            } else {
                throw new ConcurrentModificationException();
            }
        }

        public boolean hasNext() {
            return link.next != list.voidLink;
        }

        public boolean hasPrevious() {
            return link != list.voidLink;
        }

        public ET next() {
            if (expectedModCount == list.modCount) {
                Link<ET> next = link.next;
                if (next != list.voidLink) {
                    lastLink = link = next;
                    pos++;
                    return link.data;
                }
                throw new NoSuchElementException();
            }
            throw new ConcurrentModificationException();
        }

        public int nextIndex() {
            return pos + 1;
        }

        public ET previous() {
            if (expectedModCount == list.modCount) {
                if (link != list.voidLink) {
                    lastLink = link;
                    link = link.previous;
                    pos--;
                    return lastLink.data;
                }
                throw new NoSuchElementException();
            }
            throw new ConcurrentModificationException();
        }

        public int previousIndex() {
            return pos;
        }

        public void remove() {
            if (expectedModCount == list.modCount) {
                if (lastLink != null) {
                    Link<ET> next = lastLink.next;
                    Link<ET> previous = lastLink.previous;
                    next.previous = previous;
                    previous.next = next;
                    if (lastLink == link) {
                        pos--;
                    }
                    link = previous;
                    lastLink = null;
                    expectedModCount++;
                    list.size--;
                    list.modCount++;
                } else {
                    throw new IllegalStateException();
                }
            } else {
                throw new ConcurrentModificationException();
            }
        }

        public void set(ET object) {
            if (expectedModCount == list.modCount) {
                if (lastLink != null) {
                    lastLink.data = object;
                } else {
                    throw new IllegalStateException();
                }
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    /*
     * NOTES:descendingIterator is not fail-fast, according to the documentation
     * and test case.
     */
    private class ReverseLinkIterator<ET> implements Iterator<ET> {
        private int expectedModCount;

        private final FixedList<ET> list;

        private Link<ET> link;

        private boolean canRemove;

        ReverseLinkIterator(FixedList<ET> linkedList) {
            list = linkedList;
            expectedModCount = list.modCount;
            link = list.voidLink;
            canRemove = false;
        }

        public boolean hasNext() {
            return link.previous != list.voidLink;
        }

        public ET next() {
            if (expectedModCount == list.modCount) {
                if (hasNext()) {
                    link = link.previous;
                    canRemove = true;
                    return link.data;
                }
                throw new NoSuchElementException();
            }
            throw new ConcurrentModificationException();

        }

        public void remove() {
            if (expectedModCount == list.modCount) {
                if (canRemove) {
                    Link<ET> next = link.previous;
                    Link<ET> previous = link.next;
                    next.next = previous;
                    previous.previous = next;
                    link = previous;
                    list.size--;
                    list.modCount++;
                    expectedModCount++;
                    canRemove = false;
                    return;
                }
                throw new IllegalStateException();
            }
            throw new ConcurrentModificationException();
        }
    }

    /**
     * Constructs a new empty instance of {@code FixedLenList}.
     */
    public FixedList(int maxSize) {
        this.maxSize = maxSize;
        voidLink = new Link<E>(null, null, null);
        voidLink.previous = voidLink;
        voidLink.next = voidLink;
    }

    /**
     * Constructs a new instance of {@code FixedLenList} that holds all of the
     * elements contained in the specified {@code collection}. The order of the
     * elements in this new {@code FixedLenList} will be determined by the
     * iteration order of {@code collection}.
     *
     * @param collection the collection of elements to add.
     */
    public FixedList(Collection<? extends E> collection, int maxSize) {
        this(maxSize);
        addAll(collection);
    }

    /**
     * Inserts the specified object into this {@code FixedLenList} at the
     * specified location. The object is inserted before any previous element at
     * the specified location. If the location is equal to the size of this
     * {@code FixedLenList}, the object is added at the end.
     *
     * @param location the index at which to insert.
     * @param object   the object to add.
     * @throws IndexOutOfBoundsException if {@code location < 0 || location > size()}
     */
    @Override
    public void add(int location, E object) {
        if (location >= 0 && location <= size) {
            Link<E> link = voidLink;
            if (location < (size / 2)) {
                for (int i = 0; i <= location; i++) {
                    link = link.next;
                }
            } else {
                for (int i = size; i > location; i--) {
                    link = link.previous;
                }
            }
            Link<E> previous = link.previous;
            Link<E> newLink = new Link<E>(object, previous, link);
            previous.next = newLink;
            link.previous = newLink;
            if (++size > maxSize)
                removeFirstImpl();
            modCount++;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Adds the specified object at the end of this {@code FixedLenList}.
     *
     * @param object the object to add.
     * @return always true
     */
    @Override
    public boolean add(E object) {
        return addLastImpl(object);
    }

    private boolean addLastImpl(E object) {
        Link<E> oldLast = voidLink.previous;
        Link<E> newLink = new Link<E>(object, oldLast, voidLink);
        voidLink.previous = newLink;
        oldLast.next = newLink;
        if (++size > maxSize)
            removeFirstImpl();
        modCount++;
        return true;
    }

    /**
     * Inserts the objects in the specified collection at the specified location
     * in this {@code FixedLenList}. The objects are added in the order they are
     * returned from the collection's iterator.
     *
     * @param location   the index at which to insert.
     * @param collection the collection of objects
     * @return {@code true} if this {@code FixedLenList} is modified,
     * {@code false} otherwise.
     * @throws ClassCastException        if the class of an object is inappropriate for this list.
     * @throws IllegalArgumentException  if an object cannot be added to this list.
     * @throws IndexOutOfBoundsException if {@code location < 0 || location > size()}
     */
    @Override
    public boolean addAll(int location, Collection<? extends E> collection) {
        if (location < 0 || location > size) {
            throw new IndexOutOfBoundsException();
        }
        int adding = collection.size();
        if (adding == 0) {
            return false;
        }
        Collection<? extends E> elements = (collection == this) ?
                new ArrayList<E>(collection) : collection;

        Link<E> previous = voidLink;
        if (location < (size / 2)) {
            for (int i = 0; i < location; i++) {
                previous = previous.next;
            }
        } else {
            for (int i = size; i >= location; i--) {
                previous = previous.previous;
            }
        }
        Link<E> next = previous.next;
        for (E e : elements) {
            Link<E> newLink = new Link<E>(e, previous, null);
            previous.next = newLink;
            previous = newLink;
        }
        previous.next = next;
        next.previous = previous;
        if ((size += adding) > maxSize)
            removeExcess();
        modCount++;
        return true;
    }

    /**
     * Adds the objects in the specified Collection to this {@code FixedLenList}.
     *
     * @param collection the collection of objects.
     * @return {@code true} if this {@code FixedLenList} is modified,
     * {@code false} otherwise.
     */
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        int adding = collection.size();
        if (adding == 0) {
            return false;
        }
        Collection<? extends E> elements = (collection == this) ?
                new ArrayList<E>(collection) : collection;
        Link<E> previous = voidLink.previous;
        for (E e : elements) {
            Link<E> newLink = new Link<E>(e, previous, null);
            previous.next = newLink;
            previous = newLink;
        }
        previous.next = voidLink;
        voidLink.previous = previous;
        if ((size += adding) > maxSize)
            removeExcess();
        modCount++;
        return true;
    }

    /**
     * Adds the specified object at the beginning of this {@code FixedLenList}.
     *
     * @param object the object to add.
     */
    public void addFirst(E object) {
        addFirstImpl(object);
    }

    private boolean addFirstImpl(E object) {
        Link<E> oldFirst = voidLink.next;
        Link<E> newLink = new Link<E>(object, voidLink, oldFirst);
        voidLink.next = newLink;
        oldFirst.previous = newLink;
        if (++size > maxSize)
            removeLastImpl();
        modCount++;
        return true;
    }

    /**
     * Adds the specified object at the end of this {@code FixedLenList}.
     *
     * @param object the object to add.
     */
    public void addLast(E object) {
        addLastImpl(object);
    }

    /**
     * Removes all elements from this {@code FixedLenList}, leaving it empty.
     *
     * @see #size
     */
    @Override
    public void clear() {
        if (size > 0) {
            size = 0;
            voidLink.next = voidLink;
            voidLink.previous = voidLink;
            modCount++;
        }
    }

    /**
     * Returns a new {@code FixedLenList} with the same elements and size as this
     * {@code FixedLenList}.
     *
     * @return a shallow copy of this {@code FixedLenList}.
     * @see Cloneable
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        try {
            FixedList<E> l = (FixedList<E>) super.clone();
            l.size = 0;
            l.voidLink = new Link<E>(null, null, null);
            l.voidLink.previous = l.voidLink;
            l.voidLink.next = l.voidLink;
            l.addAll(this);
            return l;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Searches this {@code FixedLenList} for the specified object.
     *
     * @param object the object to search for.
     * @return {@code true} if {@code object} is an element of this
     * {@code FixedLenList}, {@code false} otherwise
     */
    @Override
    public boolean contains(Object object) {
        Link<E> link = voidLink.next;
        if (object != null) {
            while (link != voidLink) {
                if (object.equals(link.data)) {
                    return true;
                }
                link = link.next;
            }
        } else {
            while (link != voidLink) {
                if (link.data == null) {
                    return true;
                }
                link = link.next;
            }
        }
        return false;
    }

    @Override
    public E get(int location) {
        if (location >= 0 && location < size) {
            Link<E> link = voidLink;
            if (location < (size / 2)) {
                for (int i = 0; i <= location; i++) {
                    link = link.next;
                }
            } else {
                for (int i = size; i > location; i--) {
                    link = link.previous;
                }
            }
            return link.data;
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Returns the first element in this {@code FixedLenList}.
     *
     * @return the first element.
     * @throws java.util.NoSuchElementException if this {@code FixedLenList} is empty.
     */
    public E getFirst() {
        return getFirstImpl();
    }

    private E getFirstImpl() {
        Link<E> first = voidLink.next;
        if (first != voidLink) {
            return first.data;
        }
        throw new NoSuchElementException();
    }

    /**
     * Returns the last element in this {@code FixedLenList}.
     *
     * @return the last element
     * @throws java.util.NoSuchElementException if this {@code FixedLenList} is empty
     */
    public E getLast() {
        Link<E> last = voidLink.previous;
        if (last != voidLink) {
            return last.data;
        }
        throw new NoSuchElementException();
    }

    @Override
    public int indexOf(Object object) {
        int pos = 0;
        Link<E> link = voidLink.next;
        if (object != null) {
            while (link != voidLink) {
                if (object.equals(link.data)) {
                    return pos;
                }
                link = link.next;
                pos++;
            }
        } else {
            while (link != voidLink) {
                if (link.data == null) {
                    return pos;
                }
                link = link.next;
                pos++;
            }
        }
        return -1;
    }

    /**
     * Searches this {@code FixedLenList} for the specified object and returns the
     * index of the last occurrence.
     *
     * @param object the object to search for
     * @return the index of the last occurrence of the object, or -1 if it was
     * not found.
     */
    @Override
    public int lastIndexOf(Object object) {
        int pos = size;
        Link<E> link = voidLink.previous;
        if (object != null) {
            while (link != voidLink) {
                pos--;
                if (object.equals(link.data)) {
                    return pos;
                }
                link = link.previous;
            }
        } else {
            while (link != voidLink) {
                pos--;
                if (link.data == null) {
                    return pos;
                }
                link = link.previous;
            }
        }
        return -1;
    }

    /**
     * Returns a ListIterator on the elements of this {@code FixedLenList}. The
     * elements are iterated in the same order that they occur in the
     * {@code FixedLenList}. The iteration starts at the specified location.
     *
     * @param location the index at which to start the iteration
     * @return a ListIterator on the elements of this {@code FixedLenList}
     * @throws IndexOutOfBoundsException if {@code location < 0 || location > size()}
     * @see java.util.ListIterator
     */
    @Override
    public ListIterator<E> listIterator(int location) {
        return new LinkIterator<E>(this, location);
    }

    /**
     * Removes the object at the specified location from this {@code FixedLenList}.
     *
     * @param location the index of the object to remove
     * @return the removed object
     * @throws IndexOutOfBoundsException if {@code location < 0 || location >= size()}
     */
    @Override
    public E remove(int location) {
        if (location >= 0 && location < size) {
            Link<E> link = voidLink;
            if (location < (size / 2)) {
                for (int i = 0; i <= location; i++) {
                    link = link.next;
                }
            } else {
                for (int i = size; i > location; i--) {
                    link = link.previous;
                }
            }
            Link<E> previous = link.previous;
            Link<E> next = link.next;
            previous.next = next;
            next.previous = previous;
            size--;
            modCount++;
            return link.data;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean remove(Object object) {
        return removeFirstOccurrenceImpl(object);
    }

    /**
     * Remove excess
     */
    private void removeExcess() {
        int outing = size - maxSize;
        Link<E> previous = voidLink.next;
        for (int i = 0; i < outing; i++) {
            previous = previous.next;
        }
        voidLink.next = previous;
        previous.previous = voidLink;
        size = maxSize;
        modCount++;
    }

    /**
     * Removes the first object from this {@code FixedLenList}.
     *
     * @return the removed object.
     * @throws java.util.NoSuchElementException if this {@code FixedLenList} is empty.
     */
    public E removeFirst() {
        return removeFirstImpl();
    }

    private E removeFirstImpl() {
        Link<E> first = voidLink.next;
        if (first != voidLink) {
            Link<E> next = first.next;
            voidLink.next = next;
            next.previous = voidLink;
            size--;
            modCount++;
            return first.data;
        }
        throw new NoSuchElementException();
    }

    /**
     * Removes the last object from this {@code FixedLenList}.
     *
     * @return the removed object.
     * @throws java.util.NoSuchElementException if this {@code FixedLenList} is empty.
     */
    public E removeLast() {
        return removeLastImpl();
    }

    private E removeLastImpl() {
        Link<E> last = voidLink.previous;
        if (last != voidLink) {
            Link<E> previous = last.previous;
            voidLink.previous = previous;
            previous.next = voidLink;
            size--;
            modCount++;
            return last.data;
        }
        throw new NoSuchElementException();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Deque#descendingIterator()
     * @since 1.6
     */
    public Iterator<E> descendingIterator() {
        return new ReverseLinkIterator<E>(this);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Deque#offerFirst(Object)
     * @since 1.6
     */
    public boolean offerFirst(E e) {
        return addFirstImpl(e);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Deque#offerLast(Object)
     * @since 1.6
     */
    public boolean offerLast(E e) {
        return addLastImpl(e);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Deque#peekFirst()
     * @since 1.6
     */
    public E peekFirst() {
        return peekFirstImpl();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Deque#peekLast()
     * @since 1.6
     */
    public E peekLast() {
        Link<E> last = voidLink.previous;
        return (last == voidLink) ? null : last.data;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Deque#pollFirst()
     * @since 1.6
     */
    public E pollFirst() {
        return (size == 0) ? null : removeFirstImpl();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Deque#pollLast()
     * @since 1.6
     */
    public E pollLast() {
        return (size == 0) ? null : removeLastImpl();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Deque#pop()
     * @since 1.6
     */
    public E pop() {
        return removeFirstImpl();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Deque#push(Object)
     * @since 1.6
     */
    public void push(E e) {
        addFirstImpl(e);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Deque#removeFirstOccurrence(Object)
     * @since 1.6
     */
    public boolean removeFirstOccurrence(Object o) {
        return removeFirstOccurrenceImpl(o);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Deque#removeLastOccurrence(Object)
     * @since 1.6
     */
    public boolean removeLastOccurrence(Object o) {
        Iterator<E> iter = new ReverseLinkIterator<E>(this);
        return removeOneOccurrence(o, iter);
    }

    private boolean removeFirstOccurrenceImpl(Object o) {
        Iterator<E> iter = new LinkIterator<E>(this, 0);
        return removeOneOccurrence(o, iter);
    }

    private boolean removeOneOccurrence(Object o, Iterator<E> iter) {
        while (iter.hasNext()) {
            E element = iter.next();
            if (o == null ? element == null : o.equals(element)) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Replaces the element at the specified location in this {@code FixedLenList}
     * with the specified object.
     *
     * @param location the index at which to put the specified object.
     * @param object   the object to add.
     * @return the previous element at the index.
     * @throws ClassCastException        if the class of an object is inappropriate for this list.
     * @throws IllegalArgumentException  if an object cannot be added to this list.
     * @throws IndexOutOfBoundsException if {@code location < 0 || location >= size()}
     */
    @Override
    public E set(int location, E object) {
        if (location >= 0 && location < size) {
            Link<E> link = voidLink;
            if (location < (size / 2)) {
                for (int i = 0; i <= location; i++) {
                    link = link.next;
                }
            } else {
                for (int i = size; i > location; i--) {
                    link = link.previous;
                }
            }
            E result = link.data;
            link.data = object;
            return result;
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Returns the number of elements in this {@code FixedLenList}.
     *
     * @return the number of elements in this {@code FixedLenList}.
     */
    @Override
    public int size() {
        return size;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        if (size > maxSize)
            removeExcess();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean offer(E o) {
        return addLastImpl(o);
    }

    public E poll() {
        return size == 0 ? null : removeFirst();
    }

    public E remove() {
        return removeFirstImpl();
    }

    public E peek() {
        return peekFirstImpl();
    }

    private E peekFirstImpl() {
        Link<E> first = voidLink.next;
        return first == voidLink ? null : first.data;
    }

    public E element() {
        return getFirstImpl();
    }

    /**
     * Returns a new array containing all elements contained in this
     * {@code FixedLenList}.
     *
     * @return an array of the elements from this {@code FixedLenList}.
     */
    @Override
    public Object[] toArray() {
        int index = 0;
        Object[] contents = new Object[size];
        Link<E> link = voidLink.next;
        while (link != voidLink) {
            contents[index++] = link.data;
            link = link.next;
        }
        return contents;
    }

    /**
     * Returns an array containing all elements contained in this
     * {@code FixedLenList}. If the specified array is large enough to hold the
     * elements, the specified array is used, otherwise an array of the same
     * type is created. If the specified array is used and is larger than this
     * {@code FixedLenList}, the array element following the collection elements
     * is set to null.
     *
     * @param contents the array.
     * @return an array of the elements from this {@code FixedLenList}.
     * @throws ArrayStoreException if the type of an element in this {@code FixedLenList} cannot
     *                             be stored in the type of the specified array.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] contents) {
        int index = 0;
        if (size > contents.length) {
            Class<?> ct = contents.getClass().getComponentType();
            contents = (T[]) Array.newInstance(ct, size);
        }
        Link<E> link = voidLink.next;
        while (link != voidLink) {
            contents[index++] = (T) link.data;
            link = link.next;
        }
        if (index < contents.length) {
            contents[index] = null;
        }
        return contents;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(size);
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            stream.writeObject(it.next());
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream stream) throws IOException,
            ClassNotFoundException {
        stream.defaultReadObject();
        size = stream.readInt();
        voidLink = new Link<E>(null, null, null);
        Link<E> link = voidLink;
        for (int i = size; --i >= 0; ) {
            Link<E> nextLink = new Link<E>((E) stream.readObject(), link, null);
            link.next = nextLink;
            link = nextLink;
        }
        link.next = voidLink;
        voidLink.previous = link;
    }
}