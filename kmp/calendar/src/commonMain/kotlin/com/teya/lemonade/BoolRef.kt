package com.teya.lemonade

/**
 * A simple mutable boolean wrapper used as a `remember`-stable flag inside
 * Composables where [androidx.compose.runtime.MutableState] would cause
 * unwanted recomposition.
 */
internal class BoolRef(var value: Boolean)
