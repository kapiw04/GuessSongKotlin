package com.example.guesssong.utils

class PriorityList<T>(list: List<T> = emptyList()){
    private data class Node<T>(val value: T, var prev: Node<T>?, var next: Node<T>?) {
        override fun toString(): String {
            return value.toString()
        }
    }

    private var head: Node<T>? = null
    private var lookupMap = mutableMapOf<T, Node<T>>()

    init {
        lookupMap = mutableMapOf()
        list.forEach { this.add(it) }
    }


    fun moveToFront(value: T) {
        val node = lookupMap[value] ?: return
        if (node == head) return
        node.prev?.next = node.next
        node.next?.prev = node.prev
        node.prev = null
        node.next = head
        head?.prev = node
        head = node
    }

    fun add(value: T) {
        if (lookupMap.containsKey(value)) {
            moveToFront(value)
            return
        }
        val node = Node(value, null, head)
        head?.prev = node
        head = node
        lookupMap[value] = node
    }


    fun toList(): List<T> {
        val list = mutableListOf<T>()
        var current = head
        while (current != null) {
            list.add(current.value)
            current = current.next
        }
        return list
    }

}