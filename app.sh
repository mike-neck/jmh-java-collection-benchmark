#!/usr/bin/env bash

aws s3api get-object --bucket org.mikeneck.test --key jmh-java-collection-benchmark.zip
unzip jmh-java-collection-benchmark.zip

artifact=jmh-java-collection-benchmark-`date "+%Y%m%d%H%M%S"`.txt

jmh-java-collection-benchmark/bin/jmh-java-collection-benchmark > ${artifact}

aws s3api put-object --bucket org.mikeneck.test --key jmh/${artifact} --body ${artifact}
