package com.teya.lemonade

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Records what [lemonadeCardItems] passes into a [LazyListScope], without composing anything.
 */
private sealed interface RecordedCall {
    data class ItemCall(
        val key: Any?,
        val contentType: Any?,
    ) : RecordedCall

    data class ItemsCall(
        val count: Int,
        val key: ((index: Int) -> Any)?,
        val contentType: (index: Int) -> Any?,
    ) : RecordedCall
}

private class FakeLazyListScope : LazyListScope {
    val calls: MutableList<RecordedCall> = mutableListOf()

    override fun item(
        key: Any?,
        contentType: Any?,
        content: @Composable LazyItemScope.() -> Unit,
    ) {
        calls.add(RecordedCall.ItemCall(key = key, contentType = contentType))
    }

    override fun items(
        count: Int,
        key: ((index: Int) -> Any)?,
        contentType: (index: Int) -> Any?,
        itemContent: @Composable LazyItemScope.(index: Int) -> Unit,
    ) {
        calls.add(RecordedCall.ItemsCall(count = count, key = key, contentType = contentType))
    }
}

class CardItemsEmissionTest {
    @Test
    fun `mixed intervals with a header and footer emit header, each interval, then footer in order`() {
        val scope = FakeLazyListScope()
        scope.lemonadeCardItems(
            header = CardHeaderConfig(title = "Header"),
            footerAction = CardFooterActionConfig(label = "See all", onClick = {}),
        ) {
            item { }
            items(count = 3) { _: Int -> }
            item { }
        }

        assertEquals(expected = 5, actual = scope.calls.size)
        assertTrue(scope.calls[0] is RecordedCall.ItemCall)
        assertEquals(expected = 1, actual = (scope.calls[1] as RecordedCall.ItemsCall).count)
        assertEquals(expected = 3, actual = (scope.calls[2] as RecordedCall.ItemsCall).count)
        assertEquals(expected = 1, actual = (scope.calls[3] as RecordedCall.ItemsCall).count)
        assertTrue(scope.calls[4] is RecordedCall.ItemCall)
    }

    @Test
    fun `row contentType lambdas wrap the user content type distinctly from header and footer`() {
        val scope = FakeLazyListScope()
        scope.lemonadeCardItems(
            header = CardHeaderConfig(title = "Header"),
            footerAction = CardFooterActionConfig(label = "See all", onClick = {}),
        ) {
            items(count = 2, contentType = { index: Int -> "row-$index" }) { _: Int -> }
        }

        val headerCall = scope.calls[0] as RecordedCall.ItemCall
        val rowsCall = scope.calls[1] as RecordedCall.ItemsCall
        val footerCall = scope.calls[2] as RecordedCall.ItemCall

        val wrappedRowContentType = rowsCall.contentType(0)
        assertNotNull(actual = wrappedRowContentType)
        assertNotEquals(illegal = headerCall.contentType, actual = wrappedRowContentType)
        assertNotEquals(illegal = footerCall.contentType, actual = wrappedRowContentType)

        val otherScope = FakeLazyListScope()
        otherScope.lemonadeCardItems {
            items(count = 1, contentType = { index: Int -> "row-0" }) { _: Int -> }
        }
        val otherWrappedRowContentType = (otherScope.calls.single() as RecordedCall.ItemsCall).contentType(0)
        assertEquals(expected = wrappedRowContentType, actual = otherWrappedRowContentType)
    }

    @Test
    fun `an items interval forwards the key function unchanged`() {
        val scope = FakeLazyListScope()
        scope.lemonadeCardItems {
            items(count = 3, key = { index: Int -> "key-$index" }) { _: Int -> }
        }

        val key = (scope.calls.single() as RecordedCall.ItemsCall).key
        assertNotNull(actual = key)
        assertEquals(expected = "key-0", actual = key(0))
        assertEquals(expected = "key-2", actual = key(2))
    }

    @Test
    fun `a single item key is wrapped as a constant key lambda`() {
        val scope = FakeLazyListScope()
        scope.lemonadeCardItems {
            item(key = "k") { }
        }

        val call = scope.calls.single() as RecordedCall.ItemsCall
        assertEquals(expected = 1, actual = call.count)
        val key = call.key
        assertNotNull(actual = key)
        assertEquals(expected = "k", actual = key(0))
    }

    @Test
    fun `a single item with no key leaves the key lambda null`() {
        val scope = FakeLazyListScope()
        scope.lemonadeCardItems {
            item { }
        }

        assertNull(actual = (scope.calls.single() as RecordedCall.ItemsCall).key)
    }

    @Test
    fun `an empty card emits nothing`() {
        val scope = FakeLazyListScope()
        scope.lemonadeCardItems { }

        assertTrue(scope.calls.isEmpty())
    }

    @Test
    fun `a card with only rows emits exactly the row items`() {
        val scope = FakeLazyListScope()
        scope.lemonadeCardItems {
            items(count = 4) { _: Int -> }
        }

        assertEquals(expected = 1, actual = scope.calls.size)
        assertEquals(expected = 4, actual = (scope.calls.single() as RecordedCall.ItemsCall).count)
    }
}
