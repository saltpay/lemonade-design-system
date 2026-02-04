---
name: kotlin-conventions
description: Kotlin code conventions for this project. Use when writing, editing, or reviewing Kotlin code (.kt files). Apply these rules for all Kotlin code generation.
---

# Code Conventions

## Explicit API Mode

All modules use Kotlin explicit API mode (`-Xexplicit-api=strict`). This requires:
- All public declarations must have explicit visibility modifiers (`public`, `internal`, `private`)
- All public declarations must have explicit return types

## Formatting Rules

**1. Named Parameters Required**
Always use named parameters when calling functions, class constructors, and enum entries.

```kotlin
// Correct - class constructor
Person(
    name = "Alice",
    age = 30,
)

// Correct - enum entry
enum class Color(val hex: String) {
    Red(hex = "#FF0000"),
    Blue(hex = "#0000FF"),
}

// Wrong
Person("Alice", 30)
Red("#FF0000")
```

**2. One Parameter Per Line**
Each parameter must be on its own line for readability.

```kotlin
// Correct
Address(
    street = "Main St",
    city = "Springfield",
    zipCode = "12345",
)

// Wrong
Address(street = "Main St", city = "Springfield", zipCode = "12345")
```

**3. No Single-Expression Functions**
Always use block body with explicit return statement.

```kotlin
// Correct
public fun getData(): String {
    return "data"
}

// Wrong
public fun getData(): String = "data"
```

**4. Chain Calls on Separate Lines**
When chaining function calls, break the line on each call.

```kotlin
// Correct
listOf(1, 2, 3)
    .filter { it > 1 }
    .map { it * 2 }
    .toSet()

// Wrong
listOf(1, 2, 3).filter { it > 1 }.map { it * 2 }.toSet()
```

**5. Elvis Operator on Separate Line**
The elvis operator (`?:`) must be on a new line.

```kotlin
// Correct
val name = map["key"]
    ?: return null

val value = getValue()
    ?: defaultValue

// Wrong
val name = map["key"] ?: return null
val value = getValue() ?: defaultValue
```

**6. No Implicit `it` in Lambdas**
Always use named parameters in lambdas instead of the implicit `it`.

```kotlin
// Correct
list.filter { item ->
    item.isValid
}
list.associateBy { entry ->
    entry.id
}

// Wrong
list.filter { it.isValid }
list.associateBy { it.id }
```

**7. Separate `when` Subject Declaration**
Always declare the `when` subject variable on a separate line before the `when` expression.

```kotlin
// Correct
val result = getData()
when (result) {
    is Success -> handleSuccess()
    is Error -> handleError()
}

// Wrong
when (val result = getData()) {
    is Success -> handleSuccess()
    is Error -> handleError()
}
```
