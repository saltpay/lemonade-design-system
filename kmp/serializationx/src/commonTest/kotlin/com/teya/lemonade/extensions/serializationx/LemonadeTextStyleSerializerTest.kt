package com.teya.lemonade.extensions.serializationx

import com.teya.lemonade.core.LemonadeTextStyle
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class LemonadeTextStyleSerializerTest {
    private val json = Json { prettyPrint = false }

    @Test
    fun serializesTextStyleWithAllFields() {
        val style = LemonadeTextStyle(
            fontSize = 16f,
            lineHeight = 24f,
            fontWeight = 600,
            letterSpacing = 1.5f,
        )
        val serialized = json.encodeToString(LemonadeTextStyleSerializer, style)
        val deserialized = json.decodeFromString(LemonadeTextStyleSerializer, serialized)
        assertEquals(style, deserialized)
    }

    @Test
    fun serializesTextStyleWithNullLetterSpacing() {
        val style = LemonadeTextStyle(
            fontSize = 14f,
            lineHeight = 20f,
            fontWeight = 400,
            letterSpacing = null,
        )
        val serialized = json.encodeToString(LemonadeTextStyleSerializer, style)
        val deserialized = json.decodeFromString(LemonadeTextStyleSerializer, serialized)
        assertEquals(style, deserialized)
    }

    @Test
    fun producesExpectedJsonFormat() {
        val style = LemonadeTextStyle(
            fontSize = 16f,
            lineHeight = 24f,
            fontWeight = 600,
            letterSpacing = 1.5f,
        )
        val serialized = json.encodeToString(LemonadeTextStyleSerializer, style)
        assertEquals(
            """{"fontSize":16.0,"lineHeight":24.0,"fontWeight":600,"letterSpacing":1.5}""",
            serialized,
        )
    }
}
