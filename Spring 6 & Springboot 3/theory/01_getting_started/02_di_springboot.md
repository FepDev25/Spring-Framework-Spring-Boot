# Dependency Injection in Spring Boot

## Overview

The fundamental principle of Dependency Injection (DI) in Spring Boot is to delegate object creation and lifecycle management to the Spring IoC container rather than manually instantiating objects within your application code. This approach promotes loose coupling, testability, and maintainability.

## Traditional Object Creation vs. Spring DI

### Traditional Approach (Without Spring)

In traditional Java programming, you would manually create objects using the `new` keyword:

```java
public class Main {
    public static void main(String[] args) {
        Alien alien = new Alien();  // Manual object creation
        alien.code();
    }
}
```

**Problems with this approach:**

- **Tight Coupling**: The calling code is directly coupled to the concrete implementation
- **Hard to Test**: Difficult to replace `Alien` with a mock or test double
- **Lifecycle Management**: You're responsible for managing the object's lifecycle
- **No Centralized Configuration**: Object creation logic is scattered throughout the codebase
- **Resource Management**: Manual handling of object dependencies and cleanup

### Spring Boot Approach (With Dependency Injection)

Spring Boot leverages the IoC container to manage object creation and injection automatically:

```java
@SpringBootApplication
public class P01FirstAppApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(P01FirstAppApplication.class, args);
        Alien alien = context.getBean(Alien.class);
        alien.code();
    }
}
```

## Understanding the ApplicationContext

### What is ApplicationContext?

The `ApplicationContext` is Spring's advanced IoC container that serves as the central registry for all beans in your application. It provides:

- **Bean Lifecycle Management**: Controls creation, initialization, and destruction of beans
- **Dependency Resolution**: Automatically injects dependencies where needed
- **Configuration Loading**: Processes annotations, XML, and Java-based configuration
- **Event Propagation**: Supports application-wide event publishing and listening
- **Resource Loading**: Provides access to files, URLs, and other resources
- **Internationalization**: Built-in support for message bundles

### The SpringApplication.run() Method

```java
ApplicationContext context = SpringApplication.run(P01FirstAppApplication.class, args);
```

**What happens during `SpringApplication.run()`:**

1. **Application Initialization**: Creates a new Spring application instance
2. **Environment Setup**: Configures the application environment (profiles, properties)
3. **Context Creation**: Instantiates a `ConfigurableApplicationContext` (which implements `ApplicationContext`)
4. **Component Scanning**: Scans for classes annotated with Spring stereotypes (`@Component`, `@Service`, `@Repository`, `@Controller`)
5. **Bean Registration**: Registers discovered classes as bean definitions in the container
6. **Dependency Injection**: Resolves and injects dependencies between beans
7. **Auto-Configuration**: Applies Spring Boot's auto-configuration based on classpath dependencies
8. **Application Startup**: Triggers application-ready events and starts embedded servers if present

**Return Type:**

The `run()` method returns a `ConfigurableApplicationContext`, which is a sub-interface of `ApplicationContext` that adds configuration and lifecycle management capabilities:

```text
ConfigurableApplicationContext (extends ApplicationContext)
    ↓
ApplicationContext (extends BeanFactory)
    ↓
BeanFactory (core container interface)
```

## Retrieving Beans from the Container

### The getBean() Method

```java
Alien alien = context.getBean(Alien.class);
```

The `getBean()` method retrieves a managed bean from the IoC container.

**Key Points:**

- **Type-Safe Retrieval**: `getBean(Class<T> type)` returns a bean of the specified class type
- **Singleton Scope**: By default, Spring beans are singletons (one instance per container)
- **Bean Lookup**: The container searches for a bean matching the requested type
- **Exception Handling**: Throws `NoSuchBeanDefinitionException` if no matching bean is found
- **Disambiguation**: If multiple beans of the same type exist, use `@Qualifier` or specify the bean name

### Alternative getBean() Overloads

```java
// Get bean by type
Alien alien = context.getBean(Alien.class);

// Get bean by name
Alien alien = (Alien) context.getBean("alien");

// Get bean by name and type (type-safe)
Alien alien = context.getBean("alien", Alien.class);
```

## Beans in Spring Boot

### What is a Bean?

In Spring terminology, a **bean** is simply an object that is instantiated, managed, and wired by the Spring IoC container. The term "bean" distinguishes Spring-managed objects from regular Java objects (POJOs).

**Bean Characteristics:**

- **Container-Managed**: Created and managed by the Spring IoC container
- **Lifecycle Control**: Spring controls initialization and destruction
- **Dependency Injection**: Can have dependencies automatically injected
- **Scoped**: Has a defined scope (singleton, prototype, request, session, etc.)
- **Configured**: Defined through annotations, XML, or Java configuration

### Registering a Class as a Bean

To make a class eligible for Spring management, you must register it with the IoC container. The most common approach is using **stereotype annotations**.

## The @Component Annotation

### Basic Usage

```java
@Component
public class Alien {
    public void code() {
        System.out.println("Alien is coding...");
    }
}
```

### What @Component Does

When you annotate a class with `@Component`:

1. **Bean Registration**: Spring registers the class as a bean definition during component scanning
2. **Automatic Instantiation**: The container creates an instance of the class
3. **Lifecycle Management**: Spring manages the bean's entire lifecycle
4. **Dependency Injection Enabled**: The bean can be injected into other beans
5. **Default Bean Name**: The bean name defaults to the class name with a lowercase first letter (e.g., `alien`)

### How Spring Discovers @Component

Spring Boot uses **component scanning** to find classes annotated with `@Component` and its specializations:

```java
@SpringBootApplication  // Contains @ComponentScan
public class P01FirstAppApplication {
    // Component scanning starts from this package and sub-packages
}
```

The `@SpringBootApplication` annotation is a composite annotation that includes:

- `@Configuration`: Marks the class as a configuration class
- `@EnableAutoConfiguration`: Enables Spring Boot's auto-configuration
- `@ComponentScan`: Enables component scanning from the current package

**Default Scanning Behavior:**

- Scans the package where `@SpringBootApplication` is located
- Scans all sub-packages recursively
- In this case: `com.cultodeportivo.p01_first_app` and all its sub-packages

### Component Scan Customization

You can customize component scanning:

```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.cultodeportivo", "com.other.package"})
public class P01FirstAppApplication {
    // Custom scanning configuration
}
```

## Stereotype Annotations

`@Component` is the generic stereotype. Spring provides specialized stereotypes for different layers:

### @Component

Generic stereotype for any Spring-managed component.

```java
@Component
public class Alien {
    // Generic component
}
```

### @Service

Specialization of `@Component` for service layer classes (business logic).

```java
@Service
public class AlienService {
    public void processAlien() {
        // Business logic
    }
}
```

### @Repository

Specialization of `@Component` for data access layer classes (DAO/Repository pattern).

```java
@Repository
public class AlienRepository {
    public void save(Alien alien) {
        // Database operations
    }
}
```

**Special Feature**: Provides automatic exception translation for persistence exceptions.

### @Controller

Specialization of `@Component` for Spring MVC controllers.

```java
@Controller
public class AlienController {
    @RequestMapping("/alien")
    public String showAlien() {
        return "alien";
    }
}
```

### @RestController

Combination of `@Controller` and `@ResponseBody` for RESTful web services.

```java
@RestController
public class AlienRestController {
    @GetMapping("/api/alien")
    public Alien getAlien() {
        return new Alien();
    }
}
```

## Bean Lifecycle

Understanding the bean lifecycle helps in managing initialization and cleanup:

### Lifecycle Phases

1. **Instantiation**: Spring creates the bean instance
2. **Populate Properties**: Dependencies are injected
3. **Bean Name Awareness**: `setBeanName()` called if implementing `BeanNameAware`
4. **Bean Factory Awareness**: `setBeanFactory()` called if implementing `BeanFactoryAware`
5. **Application Context Awareness**: `setApplicationContext()` called if implementing `ApplicationContextAware`
6. **Pre-Initialization**: `@PostConstruct` methods or `InitializingBean.afterPropertiesSet()` called
7. **Custom Initialization**: Custom init methods defined in configuration
8. **Bean Ready**: Bean is ready for use
9. **Pre-Destruction**: `@PreDestroy` methods or `DisposableBean.destroy()` called
10. **Destruction**: Bean is destroyed when context closes

### Lifecycle Callbacks

```java
@Component
public class Alien {
    
    @PostConstruct
    public void init() {
        System.out.println("Alien bean is initialized");
    }
    
    public void code() {
        System.out.println("Alien is coding...");
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("Alien bean is being destroyed");
    }
}
```

## Complete Example Analysis

### Application Class

```java
package com.cultodeportivo.p01_first_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class P01FirstAppApplication {

    public static void main(String[] args) {
        // 1. Bootstrap the Spring Boot application and get the ApplicationContext
        ApplicationContext context = SpringApplication.run(P01FirstAppApplication.class, args);
        
        // 2. Retrieve the Alien bean from the container
        Alien alien = context.getBean(Alien.class);
        
        // 3. Use the bean
        alien.code();
    }
}
```

### Component Class

```java
package com.cultodeportivo.p01_first_app;

import org.springframework.stereotype.Component;

@Component  // Marks this class as a Spring-managed bean
public class Alien {
    
    // Business logic method
    public void code() {
        System.out.println("Alien is coding...");
    }
}
```

### Execution Flow

1. **Application Starts**: JVM executes the `main()` method
2. **Spring Initialization**: `SpringApplication.run()` creates and configures the ApplicationContext
3. **Component Scanning**: Spring scans `com.cultodeportivo.p01_first_app` package
4. **Bean Discovery**: Finds `Alien` class annotated with `@Component`
5. **Bean Creation**: Spring instantiates `Alien` and stores it in the container
6. **Bean Retrieval**: `context.getBean(Alien.class)` retrieves the managed instance
7. **Method Invocation**: `alien.code()` executes the business logic
8. **Output**: Prints "Alien is coding..." to the console

## Key Takeaways

1. **Inversion of Control**: Spring manages object creation, not your code
2. **ApplicationContext**: The central container that manages all beans
3. **Beans**: Spring-managed objects with lifecycle control
4. **@Component**: Primary annotation to register a class as a bean
5. **Component Scanning**: Automatic discovery of annotated classes
6. **getBean()**: Method to retrieve beans from the container
7. **Stereotype Annotations**: Specialized versions of `@Component` for different layers

## Best Practices

1. **Use Stereotype Annotations Appropriately**: Choose `@Service`, `@Repository`, or `@Controller` over generic `@Component` when applicable
2. **Minimize getBean() Usage**: In production code, prefer dependency injection over manual bean retrieval
3. **Constructor Injection**: Use constructor injection instead of field injection for better testability
4. **Single Responsibility**: Keep beans focused on a single responsibility
5. **Avoid Circular Dependencies**: Design your beans to avoid circular dependency chains

## Next Steps

This example demonstrates basic bean management. In real applications, you would:

- Use **constructor injection** instead of `getBean()`
- Implement **service layers** with `@Service`
- Create **repositories** with `@Repository` or Spring Data
- Build **REST APIs** with `@RestController`
- Configure **application properties** for externalized configuration
