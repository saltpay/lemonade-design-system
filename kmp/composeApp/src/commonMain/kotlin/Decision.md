### Module

- Core (Only contains Kotlin code to share as a sdk to backend to use SDUI): n26 Lego
- UI (Contains Compose components)
- iOS / Web / Android (Show case targets)

```kotlin
// Core
public enum class IconButtonSize {
    xSmall,
    small,
    medium,
}

// Ui

public val IconButtonSize.size: Dp
    get() = when (this) {
        IconButtonSize.xSmall -> 24.dp
        IconButtonSize.small -> 32.dp
        IconButtonSize.medium -> 40.dp
    }
```

### Package

- Just com.teya.lemonade as package

```
data object LemonadeUi

@Composable
fun LemonadeUi.Button(...) {

}

fun LemonadeUi.Text(
    text: String,
    style: TextStyle = LemonadeTheme.typography.body,
) {

}
```

LemonadeUI.Button(
onClick = { /* Do something */ },
size = IconButtonSize.medium,
icon = Icons.Default.Add,
contentDescription = "Add",
modifier = Modifier.padding(8.dp)
)

```
