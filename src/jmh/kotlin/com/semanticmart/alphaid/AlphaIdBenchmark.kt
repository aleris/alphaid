package com.semanticmart.alphaid

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole
import java.util.UUID
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@Fork(value = 2, jvmArgsAppend = [
    "-XX:StartFlightRecording=duration=120s,filename=./build/ProfilingAlphaId.jfr,name=profile,settings=profile"
])
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
open class AlphaIdBenchmark {

    @Benchmark
    fun equivalentUUIDRandomId(blackhole: Blackhole) {
        blackhole.consume(AlphaId.randomId(21))
    }

    @Benchmark
    fun defaultRandomId(blackhole: Blackhole) {
        blackhole.consume(AlphaId.randomId())
    }

    @Benchmark
    fun instanceRandomId(blackhole: Blackhole) {
        val alphaId = AlphaId()
        blackhole.consume(alphaId.next())
    }
}
