package com.brunofelixdev.mytodoapp.extension

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringExtensionTest {

    @Test
    fun `test parse string to date when 'stringDate' is valid, return success`() {
        //  given
        val stringDate = "22/03/2021"

        //  when
        val result = stringDate.parseToDate(stringDate)

        //  then
        assertThat(result).isNotNull()
    }

    @Test
    fun `test parse string to date when 'stringDate' is invalid, return null`() {
        //  given
        val stringDate = ""

        //  when
        val result = stringDate.parseToDate(stringDate)

        //  then
        assertThat(result).isNull()
    }

}