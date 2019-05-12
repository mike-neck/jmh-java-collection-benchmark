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
package com.example.list;

import com.example.Main;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@State(Scope.Group)
public abstract class ListBenchmark {

    private final int size;
    private final Supplier<? extends List<String>> supplier;
    private final Consumer<? super List<String>> operation;
    private final Consumer<? super List<String>> afterHook;

    protected ListBenchmark(
            int size,
            Supplier<? extends List<String>> supplier,
            Consumer<? super List<String>> operation,
            Consumer<? super List<String>> afterHook) {
        this.size = size;
        this.supplier = supplier;
        this.operation = operation;
        this.afterHook = afterHook;
    }

    private List<String> list;

    @Setup(Level.Trial)
    @Group
    public void setup() {
        list = Main.randomCollection(size, supplier);
    }

    @Group
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void benchmark() {
        operation.accept(list);
    }

    @Group
    @TearDown(Level.Invocation)
    public void teardown() {
        afterHook.accept(list);
    }
}
