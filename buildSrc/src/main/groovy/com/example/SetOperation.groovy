package com.example

enum SetOperation {
    ADD("Add", SetOpString.ADD,SetOpString.REMOVE),
    REMOVE("Remove", SetOpString.REMOVE,SetOpString.ADD),
    ITERATION("Iteration", SetOpString.ITERATION,SetOpString.NO_OP);

    final String name
    final String operation
    final String invertOperation

    SetOperation(String name, String operation, String invertOperation) {
        this.name = name
        this.operation = operation
        this.invertOperation = invertOperation
    }

    static List<SetOperation> get() {
        values() as List<SetOperation>
    }
}

@SuppressWarnings("ResultOfMethodCallIgnored")
final class SetOpString {

    //language=java prefix="class Foo { java.util.function.Consumer<Set<String>> f =" suffix=; }
    static final String ADD = 'strings -> strings.add(Main.randomString())'
    //language=java prefix="class Foo { java.util.function.Consumer<Set<String>> f =" suffix=; }
    static final String REMOVE = 'strings -> strings.stream().findFirst().ifPresent(strings::remove)'
    //language=java prefix="class Foo { java.util.function.Consumer<Set<String>> f =" suffix=; }
    static final String ITERATION = 'strings -> strings.forEach(String::isEmpty)'
    //language=java prefix="class Foo { java.util.function.Consumer<Set<String>> f =" suffix=; }
    static final String NO_OP = 'Set::size'
}
