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
package com.example;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .forks(4)
                .jvmArgs("-Xmx256M", "-Xms256M", "-Dfile.encoding=UTF-8")
                .shouldDoGC(true)
                .measurementTime(TimeValue.seconds(5L))
                .measurementIterations(4)
                .syncIterations(true)
                .forks(4)
                .timeUnit(TimeUnit.MILLISECONDS)
                .timeout(TimeValue.minutes(1L))
                .warmupTime(TimeValue.seconds(4L))
                .warmupIterations(4)
                .build();
        Runner runner = new Runner(options);
        runner.list();
        runner.run();
    }

    public static String randomString() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder builder = new StringBuilder();
        IntStream.generate(() -> random.nextInt(26))
                .limit(random.nextInt(512) + 1)
                .map(ch -> (int) 'A' + ch)
                .forEach(ch -> builder.append((char) ch));
        return builder.toString();
    }

    public static <C extends Collection<String>> C randomCollection(int size, Supplier<? extends C> collectionSupplier) {
        C collection = collectionSupplier.get();
        while (collection.size() < size) {
            collection.add(randomString());
        }
        return collection;
    }
}
