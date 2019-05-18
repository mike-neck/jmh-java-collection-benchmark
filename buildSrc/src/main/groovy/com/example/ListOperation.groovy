/*
 * Copyright 2019 Shinya Mochida
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example

enum ListOperation {

    GET_FIRST('GetFirst', ListOpString.GET_FIRST, ListOpString.NO_OP),
    GET_LAST('GetLast', ListOpString.GET_LAST, ListOpString.NO_OP),
    GET_MID('GetMid', ListOpString.GET_MID, ListOpString.NO_OP),
    ADD_LAST('AddLast', ListOpString.ADD, ListOpString.REMOVE_LAST),
    ADD_FIRST('AddFirst', ListOpString.ADD_FIRST, ListOpString.REMOVE_LAST),
    ADD_MID('AddMid', ListOpString.ADD_MID, ListOpString.REMOVE_LAST),
    REMOVE_FIRST('RemoveFirst', ListOpString.REMOVE_FIRST, ListOpString.ADD),
    REMOVE_LAST('RemoveLast', ListOpString.REMOVE_LAST, ListOpString.ADD),
    REMOVE_MID('RemoveMid', ListOpString.REMOVE_MID, ListOpString.ADD),
    ITERATION('Iteration', ListOpString.ITERATION, ListOpString.NO_OP),
    NO_OP('', ListOpString.NO_OP, ListOpString.NO_OP),
    ;

    final String name
    final String operation
    final String invertOperation

    ListOperation(
            String name,
            String operation,
            String invertOperation) {
        this.name = name
        this.operation = operation
        this.invertOperation = invertOperation
    }

    String invertOperation() {
        invertOperation
    }

    static List<ListOperation> get() {
        values().findAll { it != NO_OP }
    }
}

final class ListOpString {
    static String GET_FIRST = 'list -> list.get(0)'
    static String GET_LAST = 'list -> list.get(list.size() - 1)'
    static String GET_MID = 'list -> list.get(Main.randomIndex(list.size()))'
    static String ADD = 'list -> list.add(Main.randomString())'
    static String ADD_FIRST = 'list -> list.add(0, Main.randomString())'
    static String ADD_MID = 'list -> list.add(Main.randomIndex(list.size()), Main.randomString())'
    static String REMOVE_FIRST = 'list -> list.remove(0)'
    static String REMOVE_LAST = 'list -> list.remove(list.size() - 1)'
    static String REMOVE_MID = 'list -> list.remove(Main.randomIndex(list.size()))'
    static String ITERATION = 'list -> list.forEach(item -> {})'
    static String NO_OP = 'list -> {}'
}
