package com.brunofelixdev.mytodoapp.extension

import com.google.common.truth.Truth.assertThat
import org.joda.time.LocalDateTime
import org.junit.Test

class DateExtensionTest {

    @Test
    fun testConvert() {
        val dueDateFromUser = "26/03/2021 17:40"
        val date = dueDateFromUser.parseToDate(pattern = "dd/MM/yyyy HH:mm")
        val now = LocalDateTime.now()
        val scheduleDate = LocalDateTime(date)

        val result = (scheduleDate.minuteOfHour - now.minuteOfHour)

        assertThat(result).isNotNull()
    }

    @Test
    fun `test parse date to string when 'date' is valid and notNull, return success`() {
        //  given
        val stringDate = "22/03/2021"
        val date = stringDate.parseToDate(stringDate)

        //  when
        val result = date?.parseToString()

        //  then
        assertThat(result).isNotNull()
    }

    @Test
    fun `test parse date to string when 'date' is invalid and null, return null`() {
        //  given
        val stringDate = ""
        val date = stringDate.parseToDate(stringDate)

        //  when
        val result = date?.parseToString()

        //  then
        assertThat(result).isNull()
    }
}