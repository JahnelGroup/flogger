# Flogger
[![Build Status](https://travis-ci.org/JahnelGroup/flogger.svg?branch=master)](https://travis-ci.org/JahnelGroup/flogger)
> Whip your logs into shape!

Flogger is an AspectJ library that provides the ability to easily add information to the MDC.

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
    <version>1.3.0</version>
</dependency>
```

### Java 8

Flogger can also be used in plain Java projects using the `aspectj-maven-plugin`.

`flogger-sample` is an example project showing how to set it up.

## Usage

To add method parameters to the MDC, annotate them with `@BindParam`.

To add the return value of a method to the MDC, annotate the method with `@BindReturn`.

The key in the MDC can be customized by the `value` field on both annotations.

### Expansion

You can expand an object before it gets added to the MDC by setting `expand=true` in the annotations.

This will add the object's field values and method values (only methods that take 0 parameters) 
to the MDC intead of calling `.toString()` on the object.

#### Example

```
public class Expanded {
    
    private String field = "field"

    private String method() {
        return "method";    
    }
    
    public String toString() {
        return "expanded.toString()";
    }
}

...

public void method(@BindParam(expand=?) Expanded expanded) {...}
```

When `expand=false` the MDC contains :

```
{expanded=expanded.toString()}
```

When `expand=true` the MDC contains:

```
{field=field, method=method}
```

## License

[MIT © Jahnel Group](LICENSE)
