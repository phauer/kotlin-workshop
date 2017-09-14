package kotlinworkshop.analysis

import org.junit.jupiter.api.Test
import org.springframework.util.StopWatch
import java.time.DayOfWeek
import java.time.Instant

class Test {

    @Test
    fun createManyObjects(){
        val task = {
            (0..49900000).forEach { Instant.now() }
        }
        measureTime(task)
    }

    private fun measureTime(task: () -> Unit){
        val watch = StopWatch()
        watch.start()
        task.invoke()
        watch.stop()
        println("Elapsed time: ${watch.totalTimeMillis} ms")
    }

    @Test
    fun foo(){
        val list = listOf(1,2,3,4)
        val daysList = list.filter { it % 2 == 0 }
                .map { DayOfWeek.of(it) }
        println(daysList) //[TUESDAY, THURSDAY]
    }
}