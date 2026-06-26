package com.teya.lemonade.core

/**
 * Input modes for the PIN code component.
 *
 * @property Numeric Digits only, entered through the component's built-in on-screen numpad.
 * @property Alphanumeric Any character, entered through the device's system keyboard.
 */
public enum class LemonadePinCodeVariant {
    Numeric,
    Alphanumeric,
}
