package com.brunofelixdev.mytodoapp.extension

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DateExtensionTest {

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