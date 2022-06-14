package io.github.mitsu1119.neoki_de_english.alarm

import java.time.LocalDate
import java.time.LocalTime

class AlarmSet {
    var date = LocalDate.now()
        private set

    var time = LocalTime.now()
        private set
}