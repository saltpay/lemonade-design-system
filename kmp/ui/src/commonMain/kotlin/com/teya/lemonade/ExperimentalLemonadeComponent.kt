package com.teya.lemonade

@RequiresOptIn("This component is prone to changes in the future. Minor updates to API might happen.")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
public annotation class ExperimentalLemonadeComponent
