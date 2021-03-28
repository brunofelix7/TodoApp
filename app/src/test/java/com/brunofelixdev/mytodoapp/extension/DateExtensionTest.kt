package com.brunofelixdev.mytodoapp.extension

import com.google.common.truth.Truth.assertThat
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.LocalDateTime
import org.junit.Test

class DateExtensionTest {

    @Test
    fun testConvert() {
        val dueDateFromUser = "28/03/2021 21:00"
        val from = DateTime.now()
        val to = DateTime(dueDateFromUser.parseToDate(pattern = "dd/MM/yyyy HH:mm"))
        val duration = Duration(from, to)
        val result = duration.standardMinutes

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