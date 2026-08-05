package com.teya.lemonade

import kotlin.test.Test
import kotlin.test.assertEquals

class CardItemPositionTest {
    @Test
    fun `the only slot in a card gets every corner`() {
        assertEquals(
            expected = LemonadeCardItemPosition.Single,
            actual = resolveCardSlotPosition(visualIndex = 0, totalCount = 1),
        )
    }

    @Test
    fun `the first of several slots gets the top corners`() {
        assertEquals(
            expected = LemonadeCardItemPosition.First,
            actual = resolveCardSlotPosition(visualIndex = 0, totalCount = 3),
        )
    }

    @Test
    fun `a slot between others gets no corners`() {
        assertEquals(
            expected = LemonadeCardItemPosition.Middle,
            actual = resolveCardSlotPosition(visualIndex = 1, totalCount = 3),
        )
    }

    @Test
    fun `the final slot gets the bottom corners`() {
        assertEquals(
            expected = LemonadeCardItemPosition.Last,
            actual = resolveCardSlotPosition(visualIndex = 2, totalCount = 3),
        )
    }

    @Test
    fun `a header takes the top corners away from the first row`() {
        val totalCount = cardTotalSlotCount(rowCount = 2, hasHeader = true, hasFooter = false)
        assertEquals(
            expected = LemonadeCardItemPosition.Middle,
            actual = resolveCardSlotPosition(
                visualIndex = cardRowVisualIndex(rowIndex = 0, hasHeader = true),
                totalCount = totalCount,
            ),
        )
    }

    @Test
    fun `a footer takes the bottom corners away from the last row`() {
        val totalCount = cardTotalSlotCount(rowCount = 2, hasHeader = false, hasFooter = true)
        assertEquals(
            expected = LemonadeCardItemPosition.Middle,
            actual = resolveCardSlotPosition(
                visualIndex = cardRowVisualIndex(rowIndex = 1, hasHeader = false),
                totalCount = totalCount,
            ),
        )
    }

    @Test
    fun `a lone row framed by header and footer sits in the middle`() {
        val totalCount = cardTotalSlotCount(rowCount = 1, hasHeader = true, hasFooter = true)
        assertEquals(
            expected = LemonadeCardItemPosition.Middle,
            actual = resolveCardSlotPosition(
                visualIndex = cardRowVisualIndex(rowIndex = 0, hasHeader = true),
                totalCount = totalCount,
            ),
        )
    }

    @Test
    fun `a header with no rows or footer fills the whole card`() {
        val totalCount = cardTotalSlotCount(rowCount = 0, hasHeader = true, hasFooter = false)
        assertEquals(
            expected = LemonadeCardItemPosition.Single,
            actual = resolveCardSlotPosition(visualIndex = 0, totalCount = totalCount),
        )
    }

    @Test
    fun `a footer with no rows or header fills the whole card`() {
        val totalCount = cardTotalSlotCount(rowCount = 0, hasHeader = false, hasFooter = true)
        assertEquals(
            expected = LemonadeCardItemPosition.Single,
            actual = resolveCardSlotPosition(visualIndex = 0, totalCount = totalCount),
        )
    }

    @Test
    fun `an empty card has no slots`() {
        assertEquals(
            expected = 0,
            actual = cardTotalSlotCount(rowCount = 0, hasHeader = false, hasFooter = false),
        )
    }
}
