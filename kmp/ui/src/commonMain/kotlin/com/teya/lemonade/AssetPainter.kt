package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageVector
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.rememberResourceEnvironment

private val assetVectorCache = mutableMapOf<DrawableResource, ImageVector>()

/**
 * Loads a vector [DrawableResource] without blocking the main thread.
 *
 * compose-resources' [painterResource] resolves synchronously on non-web targets
 * (`runBlocking` during composition), which can ANR on contended cold starts. This
 * helper decodes on a background dispatcher instead, rendering a transparent painter
 * until the first decode of each resource completes. Decoded vectors are cached for
 * the process lifetime, so every later composition resolves synchronously.
 *
 * [LocalInspectionMode] keeps previews and screenshot tests on the synchronous path.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun rememberAssetPainter(resource: DrawableResource): Painter {
    if (LocalInspectionMode.current) {
        return painterResource(resource = resource)
    }
    val density = LocalDensity.current
    val environment = rememberResourceEnvironment()
    val imageVector by produceState(
        initialValue = assetVectorCache[resource],
        key1 = resource,
    ) {
        if (value == null) {
            value = withContext(context = Dispatchers.Default) {
                getDrawableResourceBytes(
                    environment = environment,
                    resource = resource,
                ).decodeToImageVector(density = density)
            }.also { decodedVector ->
                assetVectorCache[resource] = decodedVector
            }
        }
    }
    val loadedVector = imageVector
    return if (loadedVector != null) {
        rememberVectorPainter(image = loadedVector)
    } else {
        remember {
            ColorPainter(color = Color.Transparent)
        }
    }
}
