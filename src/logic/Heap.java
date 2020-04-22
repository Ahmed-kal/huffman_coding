package logic;

import java.lang.reflect.Array;

public class Heap<T extends Comparable<T> >{
    private T[] heap;
    private int size;


    public Heap(Class<T> nodeClass, int heapSize) {
        size = 0;
        heap = (T[]) Array.newInstance(nodeClass, heapSize);
    }

    //maintains the heap by sifting down.
    private void siftDown(int i) {
        int leftChild = (2 * i) + 1;
        int rightChild = (2 * i) + 2;
        int minimum = i;
        if(leftChild < size &&  heap[minimum].compareTo(heap[leftChild]) > 0) {
            minimum = leftChild;
        }
        if(rightChild < size && heap[minimum].compareTo(heap[rightChild]) > 0) {
            minimum = rightChild;
        }
        if(minimum != i) {
            swap(i, minimum);
            siftDown(minimum);
        }
    }

    public boolean push(T n) {
        if(size == heap.length) {
            return false;
        }
        heap[size++] = n;
        siftUp(size - 1);
        return true;
    }

    //maintains the heap by sifting up.
    private void siftUp(int i) {
        int parent = (i-1) / 2;
        if(parent >= 0 && heap[i].compareTo(heap[parent]) < 0) {
            swap(i, parent);
            siftUp(parent);
        }
    }

    public T pop() {
        if(size == 0)
            return null;
        T poped = heap[0];
        heap[0] = heap[--size];
        siftDown(0);
        return poped;
    }

    public int getSize() {
        return size;
    }

    //swaps the two given indexes.
    private void swap(int i, int j) {
        T buffer = heap[i];
        heap[i] = heap[j];
        heap[j] = buffer;
    }

}
