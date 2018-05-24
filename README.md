# Flogger
> Whip your logs into shape!

Flogger is an AspectJ library that provides the ability to easily add information to your logging framwork's MDC.

## Table of Contents

- [Install](#install)
- [Usage](#usage)
- [License](#license)

## Install

### Spring Boot

Adding Flogger to a Spring Boot project is as simple as adding a dependency to `flogger-spring-boot`.

```
<dependency>
    <groupId>com.jahnelgroup.flogger</groupId>
    <artifactId>flogger-spring-boot</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Java 8

Flogger can also be used in plain Java projects using the `aspectj-maven-plugin`.

`flogger-sample` is an example project showing how to set it up properly.

## Usage

To add method parameters to the MDC, annotate them with `@BindParam`.

To add the return value of a method to the MDC, annotate the method with `@BindReturn`.

The key, in which values are stored, in the MDC can be customized by the `value` field on both annotations.

## License

[MIT © Richard McRichface.](../LICENSE)
