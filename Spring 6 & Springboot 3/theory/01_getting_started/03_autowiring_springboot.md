# Autowiring in Spring Boot

## Overview

**Autowiring** is Spring's mechanism for automatic dependency injection. It allows the Spring IoC container to automatically resolve and inject collaborating beans into your components without requiring explicit configuration. This promotes loose coupling and reduces boilerplate code.

## The Problem: Managing Dependencies Manually

### Broken Code Without Dependency Injection

Consider the following code that attempts to use a dependency without proper initialization:

```java
@Component
public class Alien {

    Laptop laptop;  // Uninitialized dependency - NullPointerException will occur!

    public void code() {
        System.out.println("Alien is coding...");
        laptop.compilerCode();  // NullPointerException here!
    }
}
```

```java
@Component
public class Laptop {
    public void compilerCode() {
        System.out.println("Compiling code...");
    }
}
```

### Why This Fails

**Problem Analysis:**

1. **Null Reference**: The `laptop` field is declared but never initialized
2. **No Constructor Initialization**: No constructor creates a `Laptop` instance
3. **Spring Doesn't Know**: Although both classes are `@Component` annotated, Spring doesn't know `Alien` needs a `Laptop`
4. **Runtime Exception**: When `alien.code()` is called, `laptop.compilerCode()` throws a `NullPointerException`

**Error Output:**

```text
Exception in thread "main" java.lang.NullPointerException: 
Cannot invoke "com.cultodeportivo.p01_first_app.Laptop.compilerCode()" 
because "this.laptop" is null
```

### Traditional Solutions (Not Recommended)

**Manual Instantiation:**

```java
@Component
public class Alien {
    Laptop laptop = new Laptop();  // Tight coupling, defeats DI purpose
    
    public void code() {
        System.out.println("Alien is coding...");
        laptop.compilerCode();
    }
}
```

**Problems with this approach:**

- **Tight Coupling**: `Alien` is tightly coupled to the concrete `Laptop` class
- **Hard to Test**: Cannot inject a mock `Laptop` for testing
- **Ignores Spring Container**: Bypasses Spring's bean management
- **Multiple Instances**: Creates a new `Laptop` instead of using Spring's singleton
- **No Lifecycle Management**: Spring cannot manage the `Laptop` lifecycle

## The Solution: Autowiring with @Autowired

### Implementing Field Injection with @Autowired

```java
package com.cultodeportivo.p01_first_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Alien {

    @Autowired  // Tells Spring to inject the Laptop bean
    Laptop laptop;

    public void code() {
        System.out.println("Alien is coding...");
        laptop.compilerCode();  // Now works correctly!
    }
}
```

```java
package com.cultodeportivo.p01_first_app;

import org.springframework.stereotype.Component;

@Component
public class Laptop {
    public void compilerCode() {
        System.out.println("Compiling code...");
    }
}
```

### How @Autowired Works

When Spring creates the `Alien` bean, the following process occurs:

1. **Bean Creation**: Spring instantiates the `Alien` class
2. **Dependency Discovery**: Spring detects the `@Autowired` annotation on the `laptop` field
3. **Type Matching**: Spring searches the IoC container for a bean of type `Laptop`
4. **Bean Injection**: Spring injects the `Laptop` bean into the `laptop` field using reflection
5. **Ready for Use**: The `Alien` bean is now fully initialized with its dependencies

**Key Mechanism:**

```text
@Autowired Annotation
    ↓
Spring Container Searches for Matching Bean
    ↓
Finds Laptop Bean (registered via @Component)
    ↓
Injects Laptop Instance into Alien.laptop Field
    ↓
Dependency Resolution Complete
```

## Understanding Autowiring

### What is Autowiring?

**Autowiring** is the process by which Spring automatically:

- **Identifies** dependencies in your beans
- **Locates** matching beans in the IoC container
- **Injects** those beans into the dependent components

This creates a "wiring" or connection between beans, establishing their relationships automatically.

### Autowiring Benefits

1. **Reduced Boilerplate**: No need for explicit setter methods or constructors (though constructors are preferred)
2. **Automatic Resolution**: Spring handles dependency lookup and injection
3. **Loose Coupling**: Dependencies are injected, not hardcoded
4. **Testability**: Easy to inject mock objects during testing
5. **Maintainability**: Centralized dependency management
6. **Flexibility**: Easy to swap implementations without changing dependent code

## Types of Autowiring

Spring supports three main types of autowiring through `@Autowired`:

### 1. Field Injection (Used in Example)

```java
@Component
public class Alien {
    
    @Autowired
    Laptop laptop;  // Spring injects directly into the field
    
    public void code() {
        laptop.compilerCode();
    }
}
```

**Characteristics:**

- **Simple Syntax**: Minimal code required
- **No Constructor/Setter**: Direct field injection
- **Reflection-Based**: Spring uses reflection to set the field value

**Advantages:**

- Concise and readable
- Quick to implement
- Useful for optional dependencies with `@Autowired(required = false)`

**Disadvantages:**

- **Hard to Test**: Requires reflection or Spring context for testing
- **Hidden Dependencies**: Not visible in constructors
- **Immutability Issues**: Cannot use `final` fields
- **Not Recommended**: Generally discouraged in favor of constructor injection

### 2. Constructor Injection (Recommended)

```java
@Component
public class Alien {
    
    private final Laptop laptop;  // Can be final for immutability
    
    @Autowired  // Optional in Spring 4.3+ for single constructor
    public Alien(Laptop laptop) {
        this.laptop = laptop;
    }
    
    public void code() {
        laptop.compilerCode();
    }
}
```

**Characteristics:**

- **Constructor-Based**: Dependencies passed as constructor parameters
- **Immutable**: Can use `final` fields
- **Explicit**: Dependencies are clearly visible

**Advantages:**

- **Best Practice**: Recommended by Spring team
- **Immutability**: Supports `final` fields
- **Easy Testing**: Simple to instantiate with mock dependencies
- **Null Safety**: Ensures all required dependencies are provided
- **Clear Contract**: Constructor signature shows all dependencies

**Disadvantages:**

- Slightly more verbose
- Can lead to large constructors if many dependencies exist (may indicate design issues)

### 3. Setter Injection

```java
@Component
public class Alien {
    
    private Laptop laptop;
    
    @Autowired
    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }
    
    public void code() {
        laptop.compilerCode();
    }
}
```

**Characteristics:**

- **Setter-Based**: Dependencies injected via setter methods
- **Optional Dependencies**: Suitable for optional dependencies

**Advantages:**

- Allows optional dependencies
- Can change dependencies at runtime (if needed)
- Partial dependencies (not all required at construction)

**Disadvantages:**

- Verbose compared to field injection
- Objects can be in partially initialized state
- Dependencies can be changed after construction (usually not desired)

**Use Cases:**

- Optional dependencies
- Dependencies that may be reconfigured
- Circular dependency resolution (though circular dependencies should be avoided)

## Autowiring by Type vs. by Name

### Autowiring by Type (Default)

Spring's default autowiring strategy is **by type**:

```java
@Component
public class Alien {
    
    @Autowired
    Laptop laptop;  // Spring searches for a bean of type Laptop
}
```

**Process:**

1. Spring looks for a bean of type `Laptop` in the container
2. If exactly one bean is found, it's injected
3. If no bean is found, an exception is thrown (unless `required = false`)
4. If multiple beans of the same type exist, disambiguation is needed

### Handling Multiple Beans of Same Type

When multiple beans of the same type exist, use `@Qualifier`:

```java
@Component
@Qualifier("gamingLaptop")
public class GamingLaptop extends Laptop {
    // Gaming-specific implementation
}

@Component
@Qualifier("businessLaptop")
public class BusinessLaptop extends Laptop {
    // Business-specific implementation
}

@Component
public class Alien {
    
    @Autowired
    @Qualifier("gamingLaptop")  // Specifies which bean to inject
    Laptop laptop;
}
```

### Autowiring by Name

If no `@Qualifier` is specified and multiple beans exist, Spring falls back to matching by field name:

```java
@Component("gamingLaptop")
public class GamingLaptop extends Laptop { }

@Component
public class Alien {
    
    @Autowired
    Laptop gamingLaptop;  // Field name matches bean name
}
```

## Optional Dependencies

### Making Dependencies Optional

By default, `@Autowired` dependencies are required. If a bean is not found, Spring throws an exception:

```java
@Autowired(required = false)
Laptop laptop;  // Won't throw exception if Laptop bean doesn't exist
```

### Using Optional

```java
@Autowired
Optional<Laptop> laptop;  // Wraps dependency in Optional

public void code() {
    laptop.ifPresent(l -> l.compilerCode());
}
```

### Using @Nullable

```java
@Autowired
public Alien(@Nullable Laptop laptop) {
    this.laptop = laptop;
}
```

## Complete Working Example

### Application Main Class

```java
package com.cultodeportivo.p01_first_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class P01FirstAppApplication {

    public static void main(String[] args) {
        // Bootstrap Spring and get ApplicationContext
        ApplicationContext context = SpringApplication.run(P01FirstAppApplication.class, args);
        
        // Retrieve Alien bean (with autowired Laptop dependency)
        Alien alien = context.getBean(Alien.class);
        alien.code();  // Laptop is already injected, works perfectly!
        
        // Retrieve another instance (same singleton bean)
        Alien alien2 = context.getBean(Alien.class);
        alien2.code();  // Same instance, same injected Laptop
    }
}
```

### Execution Flow

1. **Application Starts**: Spring Boot application initializes
2. **Component Scanning**: Discovers `@Component` classes (`Alien` and `Laptop`)
3. **Bean Registration**: Registers both as bean definitions
4. **Laptop Bean Creation**: Creates `Laptop` singleton instance
5. **Alien Bean Creation**: Creates `Alien` singleton instance
6. **Autowiring**: Detects `@Autowired` on `laptop` field
7. **Dependency Injection**: Injects `Laptop` bean into `Alien.laptop`
8. **Bean Retrieval**: `getBean(Alien.class)` returns fully initialized `Alien`
9. **Method Execution**: `alien.code()` successfully calls `laptop.compilerCode()`

### Console Output

```text
Alien is coding...
Compiling code...
Alien is coding...
Compiling code...
```

**Note**: Both `alien` and `alien2` are the same instance (singleton scope by default), with the same autowired `Laptop` instance.

## Bean Scopes and Autowiring

### Singleton Scope (Default)

```java
@Component  // Singleton by default
public class Laptop {
    public void compilerCode() {
        System.out.println("Compiling code...");
    }
}
```

- **One Instance**: Only one instance per Spring container
- **Shared**: All autowired references point to the same instance
- **Thread Safety**: Must be thread-safe if used concurrently

### Prototype Scope

```java
@Component
@Scope("prototype")  // New instance for each injection
public class Laptop {
    public void compilerCode() {
        System.out.println("Compiling code...");
    }
}
```

- **Multiple Instances**: New instance created for each injection
- **Independent State**: Each dependent bean gets its own instance
- **No Shared State**: No thread-safety concerns

### Other Scopes

- **request**: One instance per HTTP request (web applications)
- **session**: One instance per HTTP session (web applications)
- **application**: One instance per ServletContext (web applications)
- **websocket**: One instance per WebSocket session

## Autowiring Best Practices

### 1. Prefer Constructor Injection

```java
// Recommended
@Component
public class Alien {
    private final Laptop laptop;
    
    public Alien(Laptop laptop) {  // @Autowired optional for single constructor
        this.laptop = laptop;
    }
}
```

### 2. Use Final Fields When Possible

```java
// Immutable dependencies
private final Laptop laptop;
```

### 3. Avoid Circular Dependencies

```java
// Avoid this pattern
@Component
public class A {
    @Autowired B b;
}

@Component
public class B {
    @Autowired A a;  // Circular dependency!
}
```

### 4. Make Dependencies Explicit

```java
// Clear dependencies visible in constructor
public Alien(Laptop laptop, Mouse mouse, Keyboard keyboard) {
    this.laptop = laptop;
    this.mouse = mouse;
    this.keyboard = keyboard;
}
```

### 5. Use @Qualifier for Disambiguation

```java
// Explicitly specify which bean to inject
@Autowired
@Qualifier("highPerformanceLaptop")
Laptop laptop;
```

## Common Issues and Solutions

### Issue 1: NoSuchBeanDefinitionException

**Problem**: No bean of the required type found.

```text
NoSuchBeanDefinitionException: No qualifying bean of type 'Laptop' available
```

**Solutions:**

- Ensure the class is annotated with `@Component` or related stereotype
- Verify component scanning covers the package
- Check for typos in class names or package names

### Issue 2: NoUniqueBeanDefinitionException

**Problem**: Multiple beans of the same type exist.

```text
NoUniqueBeanDefinitionException: No qualifying bean of type 'Laptop' available: 
expected single matching bean but found 2: gamingLaptop, businessLaptop
```

**Solutions:**

- Use `@Qualifier` to specify which bean to inject
- Use `@Primary` to mark a default bean
- Match field name with bean name

### Issue 3: BeanCurrentlyInCreationException

**Problem**: Circular dependency detected.

```text
BeanCurrentlyInCreationException: Error creating bean with name 'alien': 
Requested bean is currently in creation: Is there an unresolvable circular reference?
```

**Solutions:**

- Refactor to eliminate circular dependency
- Use setter injection instead of constructor injection
- Consider redesigning the dependency structure

## Key Takeaways

1. **@Autowired**: Tells Spring to automatically inject dependencies
2. **Dependency Wiring**: Creates connections between beans
3. **Container Lookup**: Spring searches for matching beans in the IoC container
4. **Type-Based Matching**: Default strategy matches by type
5. **Constructor Injection**: Preferred approach for required dependencies
6. **Field Injection**: Simple but has testing drawbacks
7. **Setter Injection**: Suitable for optional dependencies
8. **@Qualifier**: Disambiguates when multiple beans exist
9. **Singleton Scope**: Default scope, one instance shared among all dependents
10. **Immutability**: Use `final` fields with constructor injection for better design

## Migration Path: Improving the Example Code

### Current Implementation (Field Injection)

```java
@Component
public class Alien {
    @Autowired
    Laptop laptop;
    
    public void code() {
        System.out.println("Alien is coding...");
        laptop.compilerCode();
    }
}
```

### Recommended Implementation (Constructor Injection)

```java
@Component
public class Alien {
    
    private final Laptop laptop;
    
    // @Autowired is optional for single constructor in Spring 4.3+
    public Alien(Laptop laptop) {
        this.laptop = laptop;
    }
    
    public void code() {
        System.out.println("Alien is coding...");
        laptop.compilerCode();
    }
}
```

**Benefits of the migration:**

- **Immutability**: `laptop` is now `final`
- **Testability**: Easy to create `Alien` with mock `Laptop` in tests
- **Null Safety**: Compiler ensures `laptop` is initialized
- **Clear Contract**: Constructor shows all dependencies explicitly
