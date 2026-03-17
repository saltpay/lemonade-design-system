package com.teya.lemonade.extensions.serializationx

import com.teya.lemonade.core.LemonadeTextStyle
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Custom [KSerializer] for [LemonadeTextStyle].
 *
 * Allows serializing/deserializing [LemonadeTextStyle] without requiring
 * the `@Serializable` annotation on the core data class.
 */
public object LemonadeTextStyleSerializer : KSerializer<LemonadeTextStyle> {

    @Serializable
    @SerialName("LemonadeTextStyle")
    private data class Surrogate(
        val fontSize: Float,
        val lineHeight: Float,
        val fontWeight: Int,
        val letterSpacing: Float? = null,
    )

    override val descriptor: SerialDescriptor = Surrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: LemonadeTextStyle) {
        val surrogate = Surrogate(
            fontSize = value.fontSize,
            lineHeight = value.lineHeight,
            fontWeight = value.fontWeight,
            letterSpacing = value.letterSpacing,
        )
        encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): LemonadeTextStyle {
        val surrogate = decoder.decodeSerializableValue(Surrogate.serializer())
        return LemonadeTextStyle(
            fontSize = surrogate.fontSize,
            lineHeight = surrogate.lineHeight,
            fontWeight = surrogate.fontWeight,
            letterSpacing = surrogate.letterSpacing,
        )
    }
}
