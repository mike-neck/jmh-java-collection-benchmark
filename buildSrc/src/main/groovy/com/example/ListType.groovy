package com.example

enum ListType {
    ARRAY_LIST(ArrayList),
    LINKED_LIST(LinkedList),
    ;

    Class klass
    ListType(Class klass) { this.klass = klass }

    String supplier() {
        "${klass.simpleName}::new"
    }

    String asName() {
        klass.simpleName
    }
}
