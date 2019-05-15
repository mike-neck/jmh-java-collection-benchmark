package com.example

enum SetType {
    HASH_SET(HashSet),
    TREE_SET(TreeSet)

    Class klass
    SetType(Class klass) { this.klass = klass }

    String supplier() {
        "${klass.simpleName}::new"
    }

    String asName() {
        klass.simpleName
    }
}